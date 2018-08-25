
# coding: utf-8

# # # 1안
# # 영문: 토큰화, 모든 단어를 기본형으로 변환, 유의미 품사 추출
# # 한글: 형태소 분석, 유의미 형태소 추출
# # 각 단어별로 장소 임베딩
# # # 2안
# # word2vec으로 모든 단어를 임베딩한 후,
# # k-means clustering으로 군집화하여 군집별로 장소 임베딩

# In[6]:


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


# In[7]:


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
                if not df_morpheme['placeId'][i] in places:
                    places.append(df_morpheme['placeId'][i])
        corpus.append(places)
        #print('['+word+']: ',len(places),' places appended to the corpus')
        #sys.stdout.flush()
# # 영문

# In[ ]:


# 현재는 의미있는 품사만 고른후 그대로 corpus 구성
# stem, lemmatize후 구성하도록 수정하고 성능 비교해보기


# In[3]:


import nltk
from nltk.stem import PorterStemmer
from nltk.stem import WordNetLemmatizer
nltk.data.path.append('F:\\소공전프로젝트\\sofcon\\recomm\\nltk_data')

# In[4]:


testnum = 3


# In[5]:


start = time.time()

df = pd.read_csv(list_csv[testnum])
# filter charset exception
df['review'] = df['review'].apply(lambda x: re.sub(r'[^ a-zA-Z0-9.!?\'\n]',' ',x))
# make sentence list
array = df['review'].tolist()
tokens = [nltk.word_tokenize(sentence) for sentence in array]
pos_tagged = [nltk.pos_tag(sentence) for sentence in tokens]
tagpat = r'(JJ[RS]*)|(NN[P]*[S]*)|(RB[RS]*)|(VB[DGNPZ]*)'

print('Elapsed time(tokenize): ', str(time.time() - start), ' secs')


# In[6]:

start = time.time()

lengths = pd.DataFrame([len(sent) for sent in pos_tagged]).sum()
print('before: ',lengths[0])
sub_pos_tagged = []
for sentence in pos_tagged:
    subsentence = [word[0] for word in sentence if not re.fullmatch(tagpat, word[1]) == None]
    sub_pos_tagged.append(subsentence)
lengths = pd.DataFrame([len(sent) for sent in sub_pos_tagged]).sum()
print('after: ',lengths[0])

print('Elapsed time(filter): ', str(time.time() - start), ' secs')

# In[17]:


df_morpheme = pd.DataFrame(columns = ['placeId', 'tags'], dtype='int64')
df_morpheme['placeId'] = df['placeId'].astype('int64')
df_morpheme['tags'] = sub_pos_tagged

# In[ ]:


wordlist = []
for l in df_morpheme['tags']:
    wordlist += l
wordset = orderset(wordlist)
print('In ',list_csv[testnum])
print('단어전체', len(wordlist))
print('단어집합', len(wordset))
# 병렬처리를 위한 데이터 분할 
core_count = mp.cpu_count()
wordsubset = np.array_split(wordset, core_count)
# 멀티프로세스 연산
if __name__ == '__main__':
    start = time.time()

    corpus = []
    pool = Pool(core_count)
    pool.map(mkcorpus, wordsubset)
    pool.close()
    pool.join()
    print('Elapsed time(corpus): ', str(time.time() - start), ' secs')
    # save
    with open(list_corpus[testnum],'wb') as f:
        pickle.dump(corpus, f)

