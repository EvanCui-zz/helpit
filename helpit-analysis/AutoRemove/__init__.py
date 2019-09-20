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
    analysis_col = mydb['analysis']
    now = datetime.datetime.now()
    last_year = now - datetime.timedelta(days=365)
    analysis_docs = analysis_col.find()
    for a in analysis_docs:
        if a['post_time'] < last_year:
            analysis_col.delete_one({'_id': a['_id']})
