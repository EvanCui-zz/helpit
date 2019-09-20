import datetime
import time
import logging
import yampy
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
    user_collection = mydb['account']
    topic_collection = mydb['topic']
    analysis_collection = mydb['analysis']
    user_accounts = user_collection.find({'source': 'yammer'})
    today = datetime.date.today()
    lastweek = today - datetime.timedelta(days=7)
    start = datetime.datetime(lastweek.year, lastweek.month, lastweek.day, 0, 0, 0)
    yammer = yampy.Yammer(access_token='107-IEsv2JUsPZRwWvUQ3t1EaA')
    oldID = None

    for account in user_accounts:
        if 'account' in account.keys():
            accountid = int(account['account'])
            upn = account['upn']
            isDone = False
            while True:
                data = yammer.messages.from_user(user_id=accountid, older_than=oldID)
                messages = data['messages']
                if not messages:
                    break
                mlen = len(messages)

                if mlen == 0:
                    break
                for j in range(mlen):
                    m = messages[j]
                    mid = m['id']
                    if j == mlen - 1:
                        oldID = mid

                    post_time = datetime.datetime.strptime(m['created_at'][:-6], '%Y/%m/%d %H:%M:%S')
                    if post_time < start:
                        isDone = True
                        break

                    if m['replied_to_id'] is None or m['replied_to_id'] == "":
                        continue

                    content = m['body']['parsed']
                    topics = getTopics(topic_collection, content)
                    if len(topics) == 0:
                        continue

                    score = 0.5
                    comment_count = 0
                    threadId = m['thread_id']
                    oldID_thread = None
                    isDone_thread = False

                    while True:
                        tdata = yammer.messages.in_thread(thread_id=threadId, older_than=oldID_thread)
                        tmessages = tdata['messages']
                        tlen = len(tmessages)

                        if tlen == 0:
                            break
                        for k in range(tlen):
                            if k == tlen - 1:
                                oldID_thread = tmessages[k]['id']

                            ptime_thread = datetime.datetime.strptime(tmessages[k]['created_at'][:-6],
                                                                      '%Y/%m/%d %H:%M:%S')
                            if ptime_thread < start:
                                isDone_thread = True
                                break

                            if tmessages[k]['replied_to_id'] == mid:
                                comment_count += 1
                                score += getSentiment(tmessages[k]['body']['parsed'])

                        if isDone_thread:
                            break

                    analysis_collection.insert_one(
                        {'userId': accountid, 'upn': upn, 'source': 'yammer', 'post_time': post_time,
                         'comment_count': comment_count, 'score': score, 'topic': topics})
                if isDone:
                    break

    report_col = mydb['report']
    stime = datetime.datetime(lastweek.year, lastweek.month, lastweek.day, 0, 0, 0)
    today_time = datetime.datetime(today.year, today.month, today.day, 0, 0, 0)
    while stime < today_time:
        stime += datetime.timedelta(days=1)
        dtmp = stime + datetime.timedelta(days=1)
        adocs = analysis_collection.aggregate(
            [{"$match": {'post_time': {'$lt': dtmp, '$gte': stime}, 'source': 'yammer'}},
             {"$group": {'_id': '$upn', 'score': {'$sum': '$score'}, 'count': {'$sum': 1}}}])

        for a in adocs:
            report_col.insert_one(
                {'upn': a['_id'], 'source': 'yammer', 'count': a['count'], 'score': a['score'],
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
