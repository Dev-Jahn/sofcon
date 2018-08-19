
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
#              size window min  workers iter sg sample
params_user = [{'size':300, 'window':99999, 'min_count':0,
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,
               'workers':cores, 'iter':100, 'sg':1, 'sample':1},
               {'size':300, 'window':99999, 'min_count':0,
               'workers':cores, 'iter':100, 'sg':1, 'sample':1}]


# In[ ]:


# Attraction
df_place = pd.read_csv(list_places[0], names=['placeId', 'name', 'location', 'class'], encoding='ms949')
with open(list_corpus[0], 'rb') as f:
    corpus = pickle.load(f)
start = time.time()
model = Word2Vec(corpus, **params_user[0])
print("Elapsed time: %s sec" % (time.time() - start),' [',place,']')
model.wv.save(list_user_model[0])


# In[ ]:


# Hotel
df_place = pd.read_csv(list_places[1], names=['placeId', 'name', 'location', 'class'], encoding='ms949')
with open(list_corpus[1], 'rb') as f:
    corpus = pickle.load(f)
start = time.time()
model = Word2Vec(corpus, *(params_user[1]))
print("Elapsed time: %s sec" % (time.time() - start),' [',place,']')
model.wv.save(list_user_model[1])


# In[ ]:


# Restaurant
df_place = pd.read_csv(list_places[2], names=['placeId', 'name', 'location', 'class'], encoding='ms949')
with open(list_corpus[2], 'rb') as f:
    corpus = pickle.load(f)
start = time.time()
model = Word2Vec(corpus, *(params_user[2]))
print("Elapsed time: %s sec" % (time.time() - start),' [',place,']')
model.wv.save(list_user_model[2])


# In[83]:


top10 = model_attr.wv.most_similar(positive=['320359','324887'], negative=['1958940'])
for place in top10:
    print(df_places[df_places['placeId'] == int(place[0])].name)

