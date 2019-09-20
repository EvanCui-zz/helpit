import datetime
import logging
import pymongo
import requests
import textdistance

import azure.functions as func

mongo_connectStr = ""


def main(mytimer: func.TimerRequest) -> None:
    utc_timestamp = datetime.datetime.utcnow().replace(
        tzinfo=datetime.timezone.utc).isoformat()

    if mytimer.past_due:
        logging.info('The timer is past due!')

    logging.info('Python timer trigger function ran at %s', utc_timestamp)
    suggestAll()


def getSuggestion(name):
    candidate = []
    url_str = 'https://api.stackexchange.com/2.2/users?pagesize=100&order=desc&sort=reputation&site=stackoverflow&filter=!9Z(-x-Q)8&page='
    inname = name.split(' ')[0]
    page = 1
    while True:
        data = requests.get(url_str + str(page) + "&inname=" + inname, headers={"Content-type": "text/json"}).json()

        if 'items' not in data:
            return 'error'
        if data['total'] > 100:
            inname = name
            continue

        items = data['items']
        if not items or len(items) == 0:
            break

        for item in items:
            sy = textdistance.levenshtein.normalized_similarity(item['display_name'].strip(' \t\n\r').lower(),
                                                            name.strip(' \t\n\r').lower())
            if sy > 0.75:
                candidate.append({'id': item['account_id'], 'display_name': item['display_name'], 'relevance': sy})
        page += 1

    if len(candidate) > 0:
        candidate.sort(key=lambda e: e.__getitem__('relevance'), reverse=True)
        num = len(candidate)
        if num > 4:
            num = 4
        return candidate[:num]
    else:
        return None


def suggestAll():
    myClient = pymongo.MongoClient(mongo_connectStr)
    mydb = myClient['helpit']
    suggestion_collection = mydb['account_suggestion']
    user_collection = mydb['account']
    users = user_collection.aggregate([{"$group": {'_id': {'upn': "$upn", 'name': '$name'}}}])
    for u in users:
        upn = u['_id']['upn']
        name = u['_id']['name']
        udoc = user_collection.find_one({'upn': upn, 'source': 'stackoverflow'})
        if udoc and 'account' in udoc and udoc['account'] is not None:
            continue

        sarr = getSuggestion(name)

        if sarr is not None:
            if sarr is not 'error':
                suggestion_collection.delete_many({'upn': upn, 'source': 'stackoverflow'})
                for s in sarr:
                    suggestion_collection.insert_one(
                        {'upn': upn, 'name': name, 'account': s['id'], 'source': 'stackoverflow',
                         'display_name': s['display_name'], 'relevance': s['relevance']})
