import datetime
import logging
import pymongo
import requests
import re
import smtplib
from email.mime.text import MIMEText
from email.header import Header

import azure.functions as func

url_search = "https://api.stackexchange.com/2.2/search?pagesize=100&order=desc&sort=activity&intitle=data%20factory&site=stackoverflow&filter=!-*jbN-cLbOj4"
pages = int(200 / 100)
smtp_server = "smtp.office365.com:587"
password = ''
sender = 'helpitweb@outlook.com'
mongo_connectStr = ""


def main(mytimer: func.TimerRequest) -> None:
    utc_timestamp = datetime.datetime.utcnow().replace(
        tzinfo=datetime.timezone.utc).isoformat()

    if mytimer.past_due:
        logging.info('The timer is past due!')

    logging.info('Python timer trigger function ran at %s', utc_timestamp)
    savePosts()


def savePosts():
    smtpObj = smtplib.SMTP()
    smtpObj.connect(smtp_server)
    smtpObj.starttls()
    smtpObj.login(sender, password)

    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    time_record = mydb['time_record']
    post_collection = mydb['post']
    topic_collection = mydb['topic']
    subscription_col = mydb['subscription']
    time_doc = time_record.find_one({"source": "stackoverflow"})
    lastUpdateTime = -1
    if time_doc:
        lastUpdateTime = time_doc['time']

    newDate = None
    for i in range(pages):
        url_tmp = url_search + "&page=" + str(int(i + 1))
        if lastUpdateTime:
            url_tmp += "&fromdate=" + str(lastUpdateTime)

        data = requests.get(url_tmp, headers={"Content-type": "text/json"}).json()
        messages = data['items']
        for j in range(len(messages)):
            m = messages[j]
            threadId = m['question_id']
            title = m['title']
            topics = updateTopics(topic_collection, m['tags'])
            source = "stackoverflow"
            web_url = m['link']
            create_time = datetime.datetime.fromtimestamp(m['creation_date'])
            creator_name = m['owner']['display_name']
            creator_url = m['owner']['link']
            active_long = m['last_activity_date']
            active_time = datetime.datetime.fromtimestamp(active_long)
            comments = None
            latest_comment = None
            if 'comments' in m:
                comments = m['comments']
                if len(comments) > 0:
                    latest_comment = comments[0]

            answers = None
            latest_answer = None
            if 'answers' in m:
                answers = m['answers']
                if len(answers) > 0:
                    latest_answer = answers[0]

            updator_name = ""
            updator_url = ""
            if latest_comment is not None and latest_comment['creation_date'] == active_long:
                updator_name = latest_comment['owner']['display_name']
                updator_url = latest_comment['owner']['link']
            elif latest_answer is not None and latest_answer['creation_date'] == active_long:
                updator_name = latest_answer['owner']['display_name']
                updator_url = latest_answer['owner']['link']
            else:
                updator_name = creator_name
                updator_url = creator_url

            if i == 0 and j == 0:
                newDate = active_long

                if time_doc:
                    time_record.update_one({"source": "stackoverflow"}, {"$set": {"time": newDate}})
                else:
                    time_record.insert_one({"source": "stackoverflow", "time": newDate})

            post_doc = post_collection.find_one({"source": source, "threadId": threadId})
            if post_doc:
                post_collection.update_one({"source": "stackoverflow", "threadId": threadId},
                                           {"$set": {"title": title, "topics": topics, "web_url": web_url,
                                                     "create_time": create_time, "creator_name": creator_name,
                                                     "creator_url": creator_url,
                                                     "active_time": active_time, "updator_name": updator_name,
                                                     "updator_url": updator_url}})
            else:
                post_collection.insert_one(
                    {"threadId": threadId, "title": title, "topics": topics, "source": source, "web_url": web_url,
                     "create_time": create_time, "creator_name": creator_name, "creator_url": creator_url,
                     "active_time": active_time, "updator_name": updator_name, "updator_url": updator_url})

                subscriptions = subscription_col.find({'topics': {'$in': topics}, 'sendEmail': True})
                for s in subscriptions:
                    tmp = [t for t in topics if t in s['topics']]
                    sendEmail(smtpObj, [s['upn']], title, web_url,
                              "There is a new post about " + ','.join(tmp))

    smtpObj.close()


def updateTopics(topic_collection, topics):
    arr = []
    for t in topics:
        t = t.replace('-', ' ')
        tmp = re.escape(t)
        tdoc = topic_collection.find_one({'topic': {'$regex': '^' + tmp + '$', '$options': 'i'}})
        if not tdoc:
            topic_time = datetime.datetime.now()
            topic_collection.insert_one({'topic': t, 'subscribeNum': 0, 'time': topic_time})
            arr.append(t)
        else:
            arr.append(tdoc['topic'])
    return arr


def sendEmail(smtpObj, receivers, title, url, content):
    mail_msg = """
        <p>""" + content + """. Have a look at it.</p>
        <a href='""" + url + """'>""" + title + """</a>
        """
    message = MIMEText(mail_msg, 'html', 'utf-8')
    message['From'] = Header('Helpit<helpitweb@outlook.com>', 'utf-8').encode()
    message['To'] = ";".join(receivers)
    message['Subject'] = Header('Helpit', 'utf-8').encode()

    smtpObj.sendmail(sender, receivers, message.as_string())
