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
    addTopics()


def addTopics():
    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    post_collection = mydb['post']
    topic_collection = mydb['topic']
    topicAdd_collection = mydb['topic_add']
    tdocs = topicAdd_collection.find()

    for t in tdocs:
        topic = t['topic']
        tmp = topic_collection.find({'topic': topic})
        if tmp.count() < 1:
            topic_time = datetime.datetime.now()
            topic_collection.insert_one({'topic': topic, 'subscribeNum': 0, 'time': topic_time})
            matchPosts(post_collection, topic)
        topicAdd_collection.delete_many({'topic': topic})


def matchPosts(post_collection, topic):
    pdocs = post_collection.find()
    for p in pdocs:
        title = p['title'].lower()
        if ' ' + topic.lower() + ' ' in title or ' ' + topic.lower() in title or topic.lower() + ' ' in title:
            _id = p['_id']
            tarr = p['topics']
            tarr.append(topic)
            post_collection.update_one({'_id': _id}, {"$set": {'topics': tarr}})
