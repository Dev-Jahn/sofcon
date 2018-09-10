
# coding: utf-8

import zerorpc
import pandas as pd
from gensim.models import Word2Vec
from gensim.models import KeyedVectors 
list_csv = ['data/attraction_places.csv',
            'data/hotel_places.csv',
            'data/restaurant_places.csv']
models = ['model/attraction_tag_e5.model',
          'model/attraction_user.model',
          'model/hotel_tag_e5.model',
          'model/hotel_user.model',
          'model/restaurant_tag_e5.model',
          'model/restaurant_user.model']
class Recomm(object):
    def __init__(self):
        self.attraction_tag  = KeyedVectors.load(models[0])
        self.attraction_user = KeyedVectors.load(models[1])
        self.hotel_tag       = KeyedVectors.load(models[2])
        self.hotel_user      = KeyedVectors.load(models[3])
        self.restaurant_tag  = KeyedVectors.load(models[4])
        self.restaurant_user = KeyedVectors.load(models[5])
    def test(self):
        return 'test string'
    def rank(self, srcwv, entry, positive=[], negative=[]):
        subwv = srcwv[entry]
        newwv = Word2Vec(size=len(subwv[0]))
        # add entries to new vector
        for i in range(len(entry)):
            newwv.wv.add(entry[i], subwv[i])
        # add p, n to new vector
        for w in positive:
            newwv.wv.add(w, srcwv[w])
        for w in negative:
            newwv.wv.add(w, srcwv[w])
        ranked = newwv.most_similar(positive=positive,
                                    negative=negative,
                                    topn=len(entry))
        return ranked
    def drop(self, data):
        df = pd.read_csv(data, converters={'placeId':str})
        cols = df.columns.tolist()
        return df.drop(columns=cols[0:1]+cols[3:])
    def by_score(self, entry):
        df = pd.concat([self.drop(list_csv[i]) for i in range(3)])
        #cols = df.columns.tolist()
        #df = df.drop(columns=cols[0:1]+cols[3:])
        subdf = df[df['placeId'].isin(entry)]
        subdf = subdf.sort_values(by='score', ascending=False)
        ret = []
        for l in subdf.values:
            ret.append(tuple(l))
        return ret
# tag, user 유사도 행렬 합한 wordvector로 rank하도록 수정할 것
    def recommend(self, group, entry, positive=[], negative=[]):
        # 선호 정보가 없을시 별점순으로 정렬
        if len(positive)==0 and len(negative)==0:
            return self.by_score(entry)
        if group == 'attraction':
            return self.rank(self.attraction_tag, entry, positive=positive, negative=negative)
        elif group == 'hotel':
            return self.rank(self.hotel_tag, entry, positive=positive, negative=negative)
        elif group == 'restaurant':
            return self.rank(self.restaurant_tag, entry, positive=positive, negative=negative)
        else:
            raise Exception('Unknown group')

try:
    server = zerorpc.Server(Recomm())
    server.bind("tcp://0.0.0.0:4242")
    server.run()
except Exception as e:
    print('exception caught')

