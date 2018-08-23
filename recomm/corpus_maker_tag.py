
# coding: utf-8

# # # 1안
# # 영문: 토큰화, 모든 단어를 기본형으로 변환, 유의미 품사 추출
# # 한글: 형태소 분석, 유의미 형태소 추출
# # 각 단어별로 장소 임베딩
# # # 2안
# # word2vec으로 모든 단어를 임베딩한 후,
# # k-means clustering으로 군집화하여 군집별로 장소 임베딩

# In[106]:


import numpy as np
import pandas as pd
import re
import os
import time
import pickle
import nltk
from nltk.stem import PorterStemmer
from nltk.stem import WordNetLemmatizer
import konlpy
from konlpy.tag import Mecab

nltk.data.path.append('F:\\소공전프로젝트\\sofcon\\recomm\\nltk_data')

list_csv = ['data/kor/attraction_review_tag.csv',
            'data/kor/hotel_review_tag.csv',
            'data/kor/restaurant_review_tag.csv',
            'data/eng/eng_attraction_review_tag.csv',
            'data/eng/eng_hotel_review_tag.csv',
            'data/eng/eng_restaurant_review_tag.csv']
list_corpus = ['corpus/attraction_tag.list',
               'corpus/hotel_tag.list',
               'corpus/restaurant_tag.list']
try:
    os.stat('corpus')
except:
    os.mkdir('corpus')


# In[79]:


def orderset(seq):
    seen = set()
    seen_add = seen.add
    return [x for x in seq if not (x in seen or seen_add(x))]


# # 한글

# In[40]:


df = pd.read_csv(list_csv[0])
# filter charset exception
df['review'] = df['review'].apply(lambda x: re.sub(r'[^ 가-힣0-9.!?\n]',' ',x))
# make sentence list
array = df['review'].tolist()
# 한글형태소 분리
mecab = Mecab()
list_pos = [mecab.pos(sentence) for sentence in array]
# 형태소 리스트화
morpheme = [mecab.morphs(sentence) for sentence in array]


# In[57]:


# 의미를 가지는 형태소만 추출
pattern = re.compile('MM|NNG|VA[+].*|VV[+].*|XR')
df_morpheme = pd.DataFrame(columns = ['placeId','tags'], dtype = 'int64')
taglist = []
for i in range(len(list_pos)):
    pairs = np.array(list_pos[i])
    tags = np.array(morpheme[i])
    npbool = []
    for pair in pairs:
        npbool.append(re.fullmatch(pattern,pair[1])!=None)
    tags = tags[npbool]
    taglist.append(tags.tolist())
df_morpheme['tags'] = taglist
df_morpheme['placeId'] = df['placeId'].astype('int64')


# In[94]:


wordlist = []
for l in df_morpheme['tags']:
    wordlist += l
wordset = orderset(wordlist)
print('전체', len(wordlist))
print('집합', len(wordset))


# In[104]:


start = time.time()
corpus = []
for word in wordset:
    places = []
    for i in range(len(df_morpheme)):
        if word in df_morpheme['tags'][i]:
           places.append(df_morpheme['placeId'][i])
    corpus.append(places)
print(corpus[1])
print('Elapsed time: ', str(time.time() - start))
