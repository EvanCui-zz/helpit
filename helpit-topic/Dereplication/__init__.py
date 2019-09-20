import datetime
import logging
import pymongo
import textdistance

import azure.functions as func

mongo_connectStr = ""


def main(mytimer: func.TimerRequest) -> None:
    utc_timestamp = datetime.datetime.utcnow().replace(
        tzinfo=datetime.timezone.utc).isoformat()

    if mytimer.past_due:
        logging.info('The timer is past due!')

    logging.info('Python timer trigger function ran at %s', utc_timestamp)
    removeDuplication()


def removeDuplication():
    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    topic_collection = mydb['topic']
    topic_relevance_col = mydb['topic_relevance']
    post_collection = mydb['post']
    subscription_collection = mydb['subscription']
    analysis_collection = mydb['analysis']
    tdocs = topic_collection.find()
    tarr = []
    for doc in tdocs:
        tarr.append(doc['topic'])

    for i in range(len(tarr) - 1):
        for j in range(i + 1, len(tarr)):
            sy = textdistance.hamming.normalized_similarity(tarr[i].lower(), tarr[j].lower())
            if sy >= 0.9:
                topic = tarr[i]
                other = tarr[j]
                if len(tarr[i]) > len(tarr[j]):
                    topic = tarr[j]
                    other = tarr[i]

                sdocs = subscription_collection.find({'topics': {'$in': [other]}})
                pdocs = post_collection.find({'topics': {'$in': [other]}})
                adocs = analysis_collection.find({'topic': {'$in': [other]}})

                topic_collection.delete_one({'topic': other})
                topic_relevance_col.delete_many({'topic1': other})
                topic_relevance_col.delete_many({'topic2': other})

                for s in sdocs:
                    _id = s['_id']
                    tmp = s['topics']
                    if tarr[i] in tmp:
                        tmp.remove(tarr[i])
                    if tarr[j] in tmp:
                        tmp.remove(tarr[j])
                    tmp.append(topic)
                    subscription_collection.update_one({'_id': _id}, {"$set": {'topics': tmp}})

                for p in pdocs:
                    _id = p['_id']
                    tmp = p['topics']
                    if tarr[i] in tmp:
                        tmp.remove(tarr[i])
                    if tarr[j] in tmp:
                        tmp.remove(tarr[j])
                    tmp.append(topic)
                    post_collection.update_one({'_id': _id}, {"$set": {'topics': tmp}})

                for a in adocs:
                    _id = a['_id']
                    tmp = a['topic']
                    if tarr[i] in tmp:
                        tmp.remove(tarr[i])
                    if tarr[j] in tmp:
                        tmp.remove(tarr[j])
                    tmp.append(topic)
                    analysis_collection.update_one({'_id': _id}, {"$set": {'topic': tmp}})
