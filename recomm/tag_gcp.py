import numpy as np
import pandas as pd
from pandas import *
import os
import re
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.text import text_to_word_sequence

#전처리
df_raw = pd.read_csv('hotel_review.csv', names=['placeId','userId','score','title','review'],encoding='ms949')
df_raw = df_raw.drop(df_raw.columns[5:],axis=1)
df_raw = df_raw.dropna()
df_raw['score'] = df_raw['score']/10
df_raw['score'] = df_raw['score'].apply(int)
df_raw['review'] = df_raw['review'].apply(lambda x: re.sub('<br/>',' ',x))
df_raw = df_raw.reset_index(drop=True)
df_raw.to_csv('hotel_review_pp.csv',mode='w')
df = df_raw
del df_raw
pd.DataFrame.head(df)
pd.DataFrame.tail(df)

#리뷰단위 분할
text = ''
text = text.join([review+'\n' for review in df['review']])
text = re.sub(r'[^ 가-힣0-9\n]',' ',text)
text = re.sub(r' +',' ',text)
array = text.split('\n')[:]
array

#형태소 분리
from konlpy.tag import Mecab
mecab = Mecab()
tagged = [mecab.pos(sentence) for sentence in array]

#태그로 사용할 형태소만 추출
pattern = re.compile('MM|NNG|VA[+].*|VV[+].*|XR')
df_tags = pd.DataFrame(columns = ['score','tags'], dtype = 'int64')
taglist = []
for place in tagged:
    tag = np.array(place)
    npbool = []
    for t in tag:
        npbool.append(re.fullmatch(pattern,t[1])!=None)
    tag = tag[npbool].tolist()
    taglist.append(tag)
df_tags['tags'] = taglist
df_tags['score'] = df['score'].astype('int64')
print(df_tags['tags'][0])

corpus = [mecab.morphs(sentence) for sentence in array]
import logging
logging.basicConfig(
	format='%(asctime)s : %(levelname)s : %(message)s',
	level=logging.INFO)
from gensim.models import Word2Vec
import time
start = time.time()
print("train start")
model = Word2Vec(corpus, size=300, window=50, min_count=10, workers=8, iter=10, sg=1)#, sample=1e-3)
model.save("hotel2.model")
print("train end")
print("Elapsed time: %s sec" % (time.time() - start))
