import datetime
import logging
import pymongo

import azure.functions as func

mongo_connectStr = ""


def main(mytimer: func.TimerRequest) -> None:
    utc_timestamp = datetime.datetime.utcnow().replace(
        tzinfo=datetime.timezone.utc).isoformat()

    if mytimer.past_due:
        logging.info('The timer is past due!')

    logging.info('Python timer trigger function ran at %s', utc_timestamp)
    deleteTopics()


def deleteTopics():
    upperBound = 10
    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    topicDelete_collection = mydb['topic_delete']
    post_collection = mydb['post']
    analysis_collection = mydb['analysis']
    topic_collection = mydb['topic']
    topic_relevance_col = mydb['topic_relevance']
    subscription_collection = mydb['subscription']
    tdocs = topicDelete_collection.aggregate([{"$group": {'_id': "$topic", 'count': {'$sum': 1}}}])

    for t in tdocs:
        if t['count'] > upperBound:
            topic = t['_id']
            topic_collection.delete_many({'topic': topic})
            topic_relevance_col.delete_many({'topic1': topic})
            topic_relevance_col.delete_many({'topic2': topic})

            pdocs = post_collection.find({'topics': {'$in': [topic]}})
            adocs = analysis_collection.find({'topic': {'$in': [topic]}})
            sdocs = subscription_collection.find({'topics': {'$in': [topic]}})

            for s in sdocs:
                _id = s['_id']
                tarr = s['topics']
                tarr.remove(topic)
                subscription_collection.update_one({'_id': _id}, {"$set": {'topics': tarr}})

            for p in pdocs:
                _id = p['_id']
                tarr = p['topics']
                tarr.remove(topic)
                post_collection.update_one({'_id': _id}, {"$set": {'topics': tarr}})

            for a in adocs:
                _id = a['_id']
                tarr = a['topic']
                tarr.remove(topic)
                analysis_collection.update_one({'_id': _id}, {"$set": {'topic': tarr}})
            topicDelete_collection.delete_many({'topic': topic})
