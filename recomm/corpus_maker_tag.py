
# coding: utf-8

# # # 1안
# # 영문: 토큰화, 모든 단어를 기본형으로 변환, 유의미 품사 추출
# # 한글: 형태소 분석, 유의미 형태소 추출
# # 각 단어별로 장소 임베딩
# # # 2안
# # word2vec으로 모든 단어를 임베딩한 후,
# # k-means clustering으로 군집화하여 군집별로 장소 임베딩

# In[28]:


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
try:
    os.stat('corpus/token')
except:
    os.mkdir('corpus/token')


# In[28]:


# 순서유지 집합리스트화
def orderset(seq):
    seen = set()
    seen_add = seen.add
    return [x for x in seq if not (x in seen or seen_add(x))]
# corpus 생성함수
def mkcorpus(ws):
    subcorpus = []
    for word in ws :
        places = []
        for i in range(len(df_morpheme)):
            if word in df_morpheme['tags'][i]:
                #태그문장 내 동일 장소 중복 방지
                #if not df_morpheme['placeId'][i] in places:
                    places.append(df_morpheme['placeId'][i])
        subcorpus.append(places)
    return subcorpus 


# # 한글

# In[8]:


if platform.system() == 'Linux':
    mecab = Mecab()
elif platform.system() == 'Windows':
    mecab = Mecab(dicpath="C:\\mecab\\mecab-ko-dic")


# In[5]:


for csv in range(3):
    df = pd.read_csv(list_csv[csv])
    # filter charset exception
    df['review'] = df['review'].apply(lambda x: re.sub(r'[^ 가-힣0-9.!?\n]',' ',x))
    # make sentence list
    array = df['review'].tolist()
    # 한글형태소 분리
    list_pos = [mecab.pos(sentence) for sentence in array]
    # 형태소 리스트화
    morpheme = [mecab.morphs(sentence) for sentence in array]

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
    # 멀티프로세스 연산
    if __name__ == '__main__':
        start = time.time()

        pool = Pool(core_count)
        subcorpora = pool.map(mkcorpus, wordsubset)
        pool.close()
        pool.join()
        
        corpus = []
        for i in subcorpora:
            corpus += i
        print('Elapsed time(corpus): ', str(time.time() - start), ' secs')
        # save
        # 개수가 단어집합과 동일해야함
        print('Length of corpus: ', len(corpus))
        # save
        with open(list_corpus[csv],'wb') as f:
            pickle.dump(corpus, f)


# # 영문

# In[ ]:


# 현재는 의미있는 품사만 고른후 그대로 corpus 구성
# stem, lemmatize후 구성하도록 수정하고 성능 비교해보기


# In[2]:


import nltk
from nltk.stem import PorterStemmer
from nltk.stem import WordNetLemmatizer
nltk.data.path.append('F:\\소공전프로젝트\\sofcon\\recomm\\nltk_data')
list_tokens = ['corpus/token/eng_attraction.token',
               'corpus/token/eng_hotel.token',
               'corpus/token/eng_restaurant.token']


# In[7]:


for csv in range(3,6):
    df = pd.read_csv(list_csv[csv])
    # filter charset exception
    df['review'] = df['review'].apply(lambda x: re.sub(r'[^ a-zA-Z0-9.!?\'\n]',' ',x))
    # make sentence list
    array = df['review'].tolist()
    # Load if token file exists, tokenize if not.
    try:
        os.stat(list_tokens[csv-3])
        with open(list_tokens[csv-3],'rb') as f:
            pickle.dump(tokens, f)
        print('Token loaded from ', list_tokens[csv-3])
    except:
        start = time.time()
        tokens = [nltk.word_tokenize(sentence) for sentence in array]
        print('Elapsed time(tokenize): ', str(time.time() - start), ' secs')
    
    start = time.time()
    pos_tagged = [nltk.pos_tag(sentence) for sentence in tokens]
    print('Elapsed time(pos-tag): ', str(time.time() - start), ' secs')
    tagpat = r'(JJ[RS]*)|(NN[P]*[S]*)|(RB[RS]*)|(VB[DGNPZ]*)'

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
    # 멀티프로세스 연산
    if __name__ == '__main__':
        start = time.time()
        pool = Pool(core_count)
        subcorpora = pool.map(mkcorpus, wordsubset)
        pool.close()
        pool.join()
        corpus = []
        for i in subcorpora:
            corpus += i
        print('Elapsed time(corpus): ', str(time.time() - start), ' secs')
        # save
        # 개수가 단어집합과 동일해야함
        print('Length of corpus: ', len(corpus))
        with open(list_corpus[csv],'wb') as f:
            pickle.dump(corpus, f)


# In[90]:


import pickle
with open('corpus/token/eng_attraction.token', 'rb') as f:
    tokens = pickle.load(f)
pos_tagged = [nltk.pos_tag(sentence) for sentence in tokens]
tagpat = r'(JJ[RS]*)|(NN[P]*[S]*)|(RB[RS]*)|(VB[DGNPZ]*)'

sub_pos_tagged = []
for sentence in pos_tagged:
    subsentence = [word for word in sentence if not re.fullmatch(tagpat, word[1]) == None]
    sub_pos_tagged.append(subsentence)


# In[89]:


sub_pos_tagged


# In[ ]:


from nltk.corpus import wordnet
def posclass(tag):
    if tag in ['JJ', 'JJR', 'JJS']
        return wordnet.ADJ 
    elif tag in ['NN', 'NNP', 'NNS', 'NNPS']
        return wordnet.NOUN
    elif tag in ['RB', 'RBR', 'RBS']
        return wordnet.ADV
    elif tag in ['VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ']
        return wordnet.VERB


# In[80]:


st = PorterStemmer()
lm = WordNetLemmatizer()
start = time.time()
for sentence in sub_pos_tagged:
    for word in range(len(sentence)):
        sentence[word] = st.stem(sentence[word])
        sentence[word] = lm.lemmatize(sentence[word], pos='v')
print('Elapsed time(lemmatize): ', str(time.time() - start), ' secs')


# In[72]:


sub_pos_tagged

