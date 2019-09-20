import datetime
import logging
import pymongo
import requests
# import textdistance

import azure.functions as func

token = "107-IEsv2JUsPZRwWvUQ3t1EaA"
url_str = "https://www.yammer.com/api/v1/users/in_group/5038059.json"
mongo_connectStr = ""


def main(mytimer: func.TimerRequest) -> None:
    utc_timestamp = datetime.datetime.utcnow().replace(
        tzinfo=datetime.timezone.utc).isoformat()

    if mytimer.past_due:
        logging.info('The timer is past due!')

    logging.info('Python timer trigger function ran at %s', utc_timestamp)

    suggestAll()


def suggestAll():
    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    suggestion_collection = mydb['account_suggestion']
    user_collection = mydb['account']
    users = user_collection.aggregate([{"$group": {'_id': {'upn': "$upn", 'name': '$name'}}}])
    for u in users:
        upn = u['_id']['upn']
        name = u['_id']['name']
        udoc = user_collection.find_one({'upn': upn, 'source': 'yammer'})
        if udoc and 'account' in udoc and udoc['account'] is not None:
            continue

        data = requests.get(url_str + upn,
                            headers={"Authorization": "Bearer " + token, "Content-type": "application/json"})

        if data:
            suggestion_collection.delete_many({'upn': upn, 'source': 'yammer'})
            for s in data.json():
                suggestion_collection.insert_one(
                    {'upn': upn, 'name': name, 'account': s['id'], 'source': 'yammer',
                     'display_name': s['full_name'], 'relevance': 1})
