
# coding: utf-8

# In[1]:


import numpy as np
import pandas as pd
from pandas import *
import re
import pickle
import os


# In[2]:


list_csv = ['data/kor/attraction_review_user.csv',
            'data/kor/hotel_review_user.csv',
            'data/kor/restaurant_review_user.csv']
list_corpus = ['corpus/user-score-based_attraction.list',
               'corpus/user-score-based_hotel.list',
               'corpus/user-score-based_restaurant.list']
try:
    os.stat('corpus')
except:
    os.mkdir('corpus')


# In[3]:


for csv in list_csv:
    df_review = pd.read_csv(csv)
    userlist = list(set(df_review.userId.tolist()))
    userlist.sort()
    corpus = []
    for name in userlist:
        for i in range(1, 6):
            user_score = [str(place) for place in
                           df_review[(df_review['userId'] == name) &
                                     (df_review['score'] == i)].placeId]
            if len(user_score)!=0:
                corpus.append(user_score)

    with open(list_corpus[list_csv.index(csv)],'wb') as f:
        pickle.dump(corpus, f)
    print('corpus created for ', csv)
    count = 0
    for user in corpus:
        count = max(count,len(user))
    print('longest word count in corpus: ', count)

