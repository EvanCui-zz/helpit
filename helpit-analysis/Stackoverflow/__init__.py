import datetime
import time
import logging
import pymongo
import requests
import re

import azure.functions as func

stopwords = ['film', 'TV', 'band', 'song', 'Thank', 'economic', 'album', 'comics', '!']
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

    getAnswers()


def getAnswers():
    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    user_collection = mydb['user']
    topic_collection = mydb['topics']
    analysis_collection = mydb['analysis']
    user_accounts = user_collection.find({'source': 'stackoverflow'})
    today = datetime.date.today()
    lastweek = today - datetime.timedelta(days=7)
    start = str(int(datetime.datetime(lastweek.year, lastweek.month, lastweek.day, 0, 0, 0).timestamp()))

    for account in user_accounts:
        if 'account' in account.keys():
            accountid = int(account['account'])
            upn = account['upn']
            url_str = "https://api.stackexchange.com/2.2/users/" + str(
                accountid) + "/answers?order=desc&sort=activity&site=stackoverflow&filter=!b1MMEr*sm*wys1&pagesize=100&fromdate=" + start
            pageIndex = 1
            while True:
                data = requests.get(url_str + "&page=" + str(pageIndex), headers={"Content-type": "text/json"}).json()
                messages = data['items']
                if messages is None or len(messages) == 0:
                    break
                pageIndex += 1

                for j in range(len(messages)):
                    post_time = datetime.datetime.fromtimestamp(messages[j]['last_activity_date'])
                    is_accepted = messages[j]['is_accepted']
                    comments = []
                    comment_count = messages[j]['comment_count']
                    topics = getTopics(topic_collection, messages[j]['body'])
                    score = 0.0
                    if comment_count > 0:
                        comments = messages[j]['comments']
                    for t in range(comment_count):
                        score += getSentiment(comments[t]['body'])
                    if is_accepted:
                        score = score * 1.5
                    score += 0.5

                    analysis_collection.insert_one(
                        {'userId': accountid, 'upn': upn, 'source': 'stackoverflow', 'post_time': post_time,
                         'is_accepted': is_accepted, 'comment_count': comment_count, 'score': score, 'topic': topics})

    report_col = mydb['report']
    stime = datetime.datetime(lastweek.year, lastweek.month, lastweek.day, 0, 0, 0)
    today_time = datetime.datetime(today.year, today.month, today.day, 0, 0, 0)
    while stime < today_time:
        stime += datetime.timedelta(days=1)
        dtmp = stime + datetime.timedelta(days=1)
        adocs = analysis_collection.aggregate(
            [{"$match": {'post_time': {'$lt': dtmp, '$gte': stime}, 'source': 'stackoverflow'}},
             {"$group": {'_id': '$upn', 'score': {'$sum': '$score'}, 'count': {'$sum': 1}}}])

        for a in adocs:
            report_col.insert_one(
                {'upn': a['_id'], 'source': 'stackoverflow', 'count': a['count'], 'score': a['score'],
                 'date': time.mktime(stime.timetuple()) * 1000})


def getTopics(topic_collection, text):
    headers = {"Ocp-Apim-Subscription-Key": ""}
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
                # if not tdoc:
                #     topic_time = datetime.datetime.now()
                #     topic_collection.insert_one({'topic': t, 'subscribeNum': 0, 'time': topic_time})
                #     arr.append(t)
                # else:
                if tdoc:
                    arr.append(tdoc['topic'])

    return arr


def getSentiment(text):
    headers = {"Ocp-Apim-Subscription-Key": ""}
    documents = {"documents": [
        {"id": "1",
         "text": text}
    ]}
    response = requests.post("https://eastasia.api.cognitive.microsoft.com/text/analytics/v2.1/sentiment",
                             headers=headers, json=documents)
    sentiments = response.json()
    data = sentiments['documents'][0]
    return data['score']


def checkTopic(topic):
    for w in stopwords:
        if w in topic:
            return False
    return True
