import numpy as np
import pandas as pd
from pandas import *
import os
import re
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.text import text_to_word_sequence

#전처리
df =pd.read_csv('hotel_review.csv',encoding='ms949')
df = df.drop(df.columns[5:],axis=1)
df = df.dropna()
df['score'] = df['score']/10
df['score'] = df['score'].apply(int)
df['review'] = df['review'].apply(lambda x: re.sub('<br/>',' ',x))
df.to_csv('hotel_review_modified.csv',mode='w')
df = df.reset_index(drop=True)
pd.DataFrame.tail(df)

#리뷰단위 분할
text = ''
text = text.join([review+'\n' for review in df['review']])
text = re.sub(r'[^ 가-힣0-9\n]',' ',text)
text = re.sub(r' +',' ',text)
array = text.split('\n')[:9692]
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

corpus = [mecab.morphs(sentence) for sentence in array]
from gensim.models import Word3Vec
model = Word2Vec(corpus, size=100, window=99999, min_count=10, workers=4, iter=100, sg=1)
