import datetime
import logging
from gensim.models import word2vec
import nltk
import pymongo
import re

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
    # generateCorpus(mydb)
    nltk.download('averaged_perceptron_tagger')
    computeRelevance(mydb)


def computeRelevance(mydb):
    post_collection = mydb['post']
    pdocs = post_collection.find()
    arr = []
    for p in pdocs:
        arr.append(p['title'].lower())
    sentences = [s.split() for s in arr]
    # sentences = word2vec.Text8Corpus("data.txt")  # 加载语料
    n_dim = 200
    model = word2vec.Word2Vec(sentences, size=n_dim, min_count=0, sg=1)  # train skip-gram model; window=5

    topic_collection = mydb['topic']
    topic_relevance_col = mydb['topic_relevance']

    tdocs = topic_collection.find()
    for td in tdocs:
        topic = td['topic']
        if topic in model:
            topic_relevance_col.delete_many({'topic1': topic})

            y = model.most_similar(topic, topn=20)
            for item in y:
                suggestion = item[0]
                wordtype = nltk.pos_tag([suggestion])[0][1]
                if wordtype == 'NN' or wordtype == 'NNS':
                    relevance = item[1]
                    tmp = topic_collection.find(
                        {'topic': {'$regex': '^' + re.escape(suggestion) + '$', '$options': 'i'}})
                    for t in tmp:
                        topic_relevance_col.insert_one({'topic1': topic, 'topic2': t['topic'], 'relevance': relevance})
