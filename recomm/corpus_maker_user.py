
# coding: utf-8

# In[70]:


import numpy as np
import pandas as pd
from pandas import *
import re
import pickle
import os


# In[71]:


list_csv = ['data/kor/attraction_review_user.csv',
            'data/kor/hotel_review_user.csv',
            'data/kor/restaurant_review_user.csv',
            'data/eng/eng_attraction_review_user.csv',
            'data/eng/eng_hotel_review_user.csv',
            'data/eng/eng_restaurant_review_user.csv']
list_corpus = ['corpus/kor_attraction_user.list',
               'corpus/kor_hotel_user.list',
               'corpus/kor_restaurant_user.list',
               'corpus/eng_attraction_user.list',
               'corpus/eng_hotel_user.list',
               'corpus/eng_restaurant_user.list']
try:
    os.stat('corpus')
except:
    os.mkdir('corpus')


# In[65]:


# user cooccurrence in kor-eng reviews
for i in range(3):
    df1 = pd.read_csv(list_csv[i])
    df2 = pd.read_csv(list_csv[i+3])
    print(len(set(df1.userId.tolist())
              .intersection(df2.userId.tolist())))


# In[ ]:


for i in range(3):
    df_kor = pd.read_csv(list_csv[i])
    df_eng = pd.read_csv(list_csv[i+3])
    df = df_kor.append(df_eng)
    userlist = list(set(df.userId.tolist()))
    userlist.sort()
    corpus = []
    for name in userlist:
        for i in range(1, 6):
            user_score = [str(place) for place in
                           df[(df['userId'] == name) &
                                     (df['score'] == i)].placeId]
            if len(user_score)!=0:
                corpus.append(user_score)

    with open(list_corpus[list_csv.index(csv)],'wb') as f:
        pickle.dump(corpus, f)
    print('corpus created for ', csv)
    count = 0
    for user in corpus:
        count = max(count,len(user))
    print('longest word count in corpus: ', count)

