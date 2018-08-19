
# coding: utf-8

# In[6]:


from gensim.models import Word2Vec
import pandas as pd
import pickle
import time
import logging
logging.basicConfig(
	format='%(asctime)s : %(levelname)s : %(message)s',
	level=logging.INFO)

workers = 4

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
params_user = [(300, 99999,  0, workers, 100, 1,     1),
               (300, 99999,  0, workers, 100, 1,     1),
               (300, 99999,  0, workers, 100, 1,     1)]


# In[8]:


for i in range(3):
    df_place = pd.read_csv(list_places[i], names=['placeId', 'name', 'location', 'class'], encoding='ms949')
    with open(list_corpus[i], 'rb') as f:
        corpus = pickle.load(f)
    start = time.time()
    model = Word2Vec(corpus, *(params_users[i]))
    print("Elapsed time: %s sec" % (time.time() - start),' [',place,']')
    model.wv.save(list_user_model[i])


# In[83]:


top10 = model_attr.wv.most_similar(positive=['320359','324887'], negative=['1958940'])
for place in top10:
    print(df_places[df_places['placeId'] == int(place[0])].name)

