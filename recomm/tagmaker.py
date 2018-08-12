import numpy as np
import pandas as pd
from pandas import *
import os
import re
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.text import text_to_word_sequence

df = pd.read_csv('data/hotel_review_pp.csv')

#리뷰단위 분할
text = ''
text = text.join([review+'\n' for review in df['review']])
text = re.sub(r'[^ 가-힣0-9\n]',' ',text)
text = re.sub(r' +',' ',text)
array = text.split('\n')[:]
#array

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
