
# coding: utf-8

# In[63]:


from gensim.models import Word2Vec
import pandas as pd
import pickle
import time
import logging
import multiprocessing as mp
logging.basicConfig(
	format='%(asctime)s : %(levelname)s : %(message)s',
	level=logging.INFO)

cores = mp.cpu_count()
list_corpus = ['corpus/attraction_tag.list',
               'corpus/hotel_tag.list',
               'corpus/restaurant_tag.list']
list_tag_model = ['model/attraction_tag.model',
                   'model/hotel_tag.model',
                   'model/restaurant_tag.model']
params_tag = [{'size':300, 'window':99999, 'min_count':0,        # Attraction
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,        # Hotel
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,        # Restaurant
               'workers':cores, 'iter':100, 'sg':1, 'sample':1}]


# In[64]:


for c in list_corpus:
    with open(c, 'rb') as f:
        corpus = pickle.load(f)
    corpus = [[str(pid) for pid in line] for line in corpus]
    with open(c,'wb') as f:
        pickle.dump(corpus, f)


# In[65]:


spent = []
for i in range(3):
    with open(list_corpus[i], 'rb') as f:
        corpus = pickle.load(f)
    start = time.time()
    model = Word2Vec(corpus, **params_tag[i])
    spent.append('Elapsed time: '+str(time.time() - start)+' sec'+' ['+list_tag_model[i]+']')
    model.wv.save(list_tag_model[i])


# In[ ]:


print(spent)

