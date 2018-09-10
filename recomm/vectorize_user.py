
# coding: utf-8

# In[11]:


from gensim.models import Word2Vec
import pandas as pd
import pickle
import time
import logging
logging.basicConfig(
	format='%(asctime)s : %(levelname)s : %(message)s',
	level=logging.INFO)

cores = 8
list_corpus = ['corpus/attraction_user.list',
               'corpus/hotel_user.list',
               'corpus/restaurant_user.list']
list_user_model = ['model/attraction_user.model',
                   'model/hotel_user.model',
                   'model/restaurant_user.model']
params_user = [{'size':300, 'window':99999, 'min_count':0,        # Attraction
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,        # Hotel
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,        # Restaurant
               'workers':cores, 'iter':100, 'sg':1, 'sample':1}]


# In[ ]:


spent = []
for i in range(3):
    with open(list_corpus[i], 'rb') as f:
        corpus = pickle.load(f)
    start = time.time()
    model = Word2Vec(corpus, **params_user[i])
    spent.append('Elapsed time: '+str(time.time() - start)+' sec'+' ['+list_user_model[i]+']')
    model.wv.save(list_user_model[i])


# In[ ]:


for string in spent:
    print(string)

