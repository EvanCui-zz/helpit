import datetime
import logging
import pymongo
import requests
import re
import smtplib
from email.mime.text import MIMEText
from email.header import Header

import azure.functions as func

urlstr_group = "https://www.yammer.com/api/v1/messages/in_group/5038059.json"
token = "107-IEsv2JUsPZRwWvUQ3t1EaA"
times = int(100 / 20)
stopwords = ['film', 'TV', 'band', 'song', 'Thank', 'economic', 'album', 'comics', '!']
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

    # f = open("stopwords.txt", "r")
    # for x in f:
    #     stopwords.append(x.strip())
    # f.close()

    savePosts()


def savePosts():
    smtpObj = smtplib.SMTP()
    smtpObj.connect(smtp_server)
    smtpObj.starttls()
    smtpObj.login(sender, password)

    myClient = pymongo.MongoClient(
        "mongodb://helpit:hZEqE98WCQgLv0xD3PrbBlAXUSjnV4Lr7wVpL3ErD9xrlboTZ0jLVmHNrNoHYDjAMjKt0HBp8ZBXk2h1Mwb21w==@helpit.documents.azure.com:10255/?ssl=true&replicaSet=globaldb")
    mydb = myClient['helpit']
    time_record = mydb['time_record']
    post_collection = mydb['post']
    topic_collection = mydb['topic']
    subscription_col = mydb['subscription']
    doc = time_record.find_one({"source": "yammer"})
    lastUpdateId = -1
    if doc:
        lastUpdateId = doc['time']

    oldId = None
    newId = None

    isDone = False
    for i in range(times):
        url_str = urlstr_group + "?threaded=true"
        if oldId:
            url_str += "&older_than=" + str(oldId)
        data = requests.get(url_str,
                            headers={"Authorization": "Bearer " + token, "Content-type": "application/json"}).json()
        messages = data['messages']
        mlen = len(messages)
        if mlen == 0:
            break

        for j in range(mlen):
            m = messages[j]
            threadId = m['thread_id']
            thread_latest = getLatestInThread(threadId)
            latestId_thread = thread_latest['id']

            if latestId_thread <= lastUpdateId:
                isDone = True
                break

            if i == 0 and j == 0:
                newId = latestId_thread

                if doc:
                    time_record.update_one({"source": "yammer"}, {"$set": {"time": newId}})
                else:
                    time_record.insert_one({"source": "yammer", "time": newId})

            if j == mlen - 1:
                oldId = latestId_thread

            web_url = m['web_url']
            content = m['body']['plain']
            create_time = datetime.datetime.strptime(m['created_at'][:-6], '%Y/%m/%d %H:%M:%S')

            creatorId = m["sender_id"]
            userInfo = getUserInfo(creatorId)
            creator_name = userInfo['full_name']
            creator_url = userInfo['web_url']

            updatorId = thread_latest["sender_id"]
            updatorInfo = getUserInfo(updatorId)
            updator_name = updatorInfo['full_name']
            updator_url = updatorInfo['web_url']
            update_time = datetime.datetime.strptime(thread_latest['created_at'][:-6], '%Y/%m/%d %H:%M:%S')

            post_doc = post_collection.find_one({"source": "yammer", "threadId": threadId})

            if post_doc:
                post_collection.update_one({"source": "yammer", "threadId": threadId},
                                           {"$set": {"active_time": update_time, "updator_name": updator_name,
                                                     "updator_url": updator_url}})
            else:
                title = content
                # if len(content) > 200:
                #     title = content[:200] + "..."
                # else:
                #     title = content[:]
                topics = getTopics(topic_collection, content)
                # updateTopics(topic_collection, topics)
                post_collection.insert_one(
                    {"threadId": threadId, "title": title, "topics": topics, "source": "yammer", "web_url": web_url,
                     "create_time": create_time, "creator_name": creator_name, "creator_url": creator_url,
                     "active_time": update_time, "updator_name": updator_name, "updator_url": updator_url})

                subscriptions = subscription_col.find({'topics': {'$in': topics}, 'sendEmail': True})
                for s in subscriptions:
                    tmp = [t for t in topics if t in s['topics']]
                    sendEmail(smtpObj, [s['upn']], title, web_url,
                              "There is a new post about " + ','.join(tmp))

        if isDone:
            break

    smtpObj.close()


def getLatestInThread(tid):
    data = requests.get("https://www.yammer.com/api/v1/messages/in_thread/" + str(tid) + ".json",
                        headers={"Authorization": "Bearer " + token, "Content-type": "application/json"}).json()
    messages = data['messages']
    res = None
    if len(messages) > 0:
        res = messages[0]
    return res


def getUserInfo(uid):
    data = requests.get("https://www.yammer.com/api/v1/users/" + str(uid) + ".json",
                        headers={"Authorization": "Bearer " + token, "Content-type": "application/json"}).json()
    return data


def getTopics(topic_collection, text):
    headers = {"Ocp-Apim-Subscription-Key": "42002033e8fa47e9bcb6acd4222bf4cc"}
    documents = {"documents": [
        {"id": "1",
         "text": text}
    ]}
    response = requests.post("https://eastasia.api.cognitive.microsoft.com/text/analytics/v2.1/entities",
                             headers=headers, json=documents)
    key_phrases = response.json()
    arr = []
    data = key_phrases['documents'][0]['entities']
    for d in data:
        if d['type'] == 'Other' or d['type'] == 'Organization':
            t = d['name']
            if t.lower() in text.lower() and checkTopic(t):
                tmp = re.escape(t)
                tdoc = topic_collection.find_one({'topic': {'$regex': '^' + tmp + '$', '$options': 'i'}})
                if not tdoc:
                    topic_time = datetime.datetime.now()
                    topic_collection.insert_one({'topic': t, 'subscribeNum': 0, 'time': topic_time})
                    arr.append(t)
                else:
                    arr.append(tdoc['topic'])

    return arr


def checkTopic(topic):
    for w in stopwords:
        if w in topic:
            return False
    return True


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
