
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

cores = 4

list_places = ['data/kor/attraction_places.csv',
               'data/kor/hotel_places.csv',
               'data/kor/restaurant_places.csv']
list_corpus = ['corpus/user-score-based_attraction.list',
               'corpus/user-score-based_hotel.list',
               'corpus/user-score-based_restaurant.list']
list_user_model = ['model/attraction_user.model',
                   'model/hotel_user.model',
                   'model/restaurant_user.model']
params_user = [{'size':300, 'window':99999, 'min_count':0,        # Attraction
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,        # Hotel
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,        # Restaurant
               'workers':cores, 'iter':100, 'sg':1, 'sample':1}]


# In[21]:


spent = []


# In[ ]:


for i in range(3):
    df_place = pd.read_csv(list_places[i],
                           names=['placeId', 'name', 'location', 'class'],
                           encoding='ms949')
    with open(list_corpus[i], 'rb') as f:
        corpus = pickle.load(f)
    start = time.time()
    model = Word2Vec(corpus, **params_user[i])
    spent.append("Elapsed time: %s sec" % (time.time() - start),' [',list_user_model[i],']')
    model.wv.save(list_user_model[i])


# In[ ]:


for string in spent:
    print(string)

