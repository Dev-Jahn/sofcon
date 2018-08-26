import numpy as np
import pandas as pd
import re
import os
import platform
import multiprocessing as mp
from multiprocessing import Pool
import sys
import time
import pickle
import konlpy
from konlpy.tag import Mecab

list_csv = ['data/kor/attraction_review_tag.csv',
            'data/kor/hotel_review_tag.csv',
            'data/kor/restaurant_review_tag.csv',
            'data/eng/eng_attraction_review_tag.csv',
            'data/eng/eng_hotel_review_tag.csv',
            'data/eng/eng_restaurant_review_tag.csv']
list_corpus = ['corpus/attraction_tag.list',
               'corpus/hotel_tag.list',
               'corpus/restaurant_tag.list',
               'corpus/eng_attraction_tag.list',
               'corpus/eng_hotel_tag.list',
               'corpus/eng_restaurant_tag.list']
try:
    os.stat('corpus')
except:
    os.mkdir('corpus')
# 순서유지 집합리스트화
def orderset(seq):
    seen = set()
    seen_add = seen.add
    return [x for x in seq if not (x in seen or seen_add(x))]
# corpus 생성함수
def mkcorpus(ws):
    for word in ws :
        places = []
        for i in range(len(df_morpheme)):
            if word in df_morpheme['tags'][i]:
                #태그문장 내 동일 장소 중복 방지
                #if not df_morpheme['placeId'][i] in places:
                    places.append(df_morpheme['placeId'][i])
        corpus.append(places)
import nltk
from nltk.stem import PorterStemmer
from nltk.stem import WordNetLemmatizer
nltk.data.path.append('F:\\소공전프로젝트\\sofcon\\recomm\\nltk_data')

for csv in range(4,5):
    start = time.time()
    df = pd.read_csv(list_csv[csv])
    # filter charset exception
    df['review'] = df['review'].apply(lambda x: re.sub(r'[^ a-zA-Z0-9.!?\'\n]',' ',x))
    # make sentence list
    array = df['review'].tolist()
    tokens = [nltk.word_tokenize(sentence) for sentence in array]
    pos_tagged = [nltk.pos_tag(sentence) for sentence in tokens]
    tagpat = r'(JJ[RS]*)|(NN[P]*[S]*)|(RB[RS]*)|(VB[DGNPZ]*)'
    print('Elapsed time(tokenize): ', str(time.time() - start), ' secs')

    start = time.time()
    sub_pos_tagged = []
    for sentence in pos_tagged:
        subsentence = [word[0] for word in sentence if not re.fullmatch(tagpat, word[1]) == None]
        sub_pos_tagged.append(subsentence)
    print('Elapsed time(filter): ', str(time.time() - start), ' secs')

    df_morpheme = pd.DataFrame(columns = ['placeId', 'tags'], dtype='int64')
    df_morpheme['placeId'] = df['placeId'].astype('int64')
    df_morpheme['tags'] = sub_pos_tagged

    wordlist = []
    for l in df_morpheme['tags']:
        wordlist += l
    wordset = orderset(wordlist)
    print('In ',list_csv[csv])
    print('단어전체', len(wordlist))
    print('단어집합', len(wordset))
    # 병렬처리를 위한 데이터 분할 
    core_count = mp.cpu_count()
    wordsubset = np.array_split(wordset, core_count)
    
smallset = wordset[0:4]
smallsub = np.array_split(smallset, 4)
if __name__ == '__main__':
    corpus = []
    pool = Pool(4)
    pool.map(mkcorpus, smallsub)
    pool.close()
    pool.join()
    print('Elapsed time(corpus): ', str(time.time() - start), ' secs')
    # save
    with open('test.corpus','wb') as f:
        pickle.dump(corpus, f)
print(corpus)