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

    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    post_col = mydb['post']
    now = datetime.datetime.now()
    last_year = now - datetime.timedelta(days=365)
    post_docs = post_col.find()
    for p in post_docs:
        if p['active_time'] < last_year:
            post_col.delete_one({'_id': p['_id']})
