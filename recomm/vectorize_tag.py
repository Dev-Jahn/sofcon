
# coding: utf-8

# In[7]:


from gensim.models import Word2Vec
import pandas as pd
import pickle
import time
import logging
import multiprocessing as mp
logging.basicConfig(
	format='%(asctime)s : %(levelname)s : %(message)s',
	level=logging.INFO)
corpus_path = 'corpus/'
cores = mp.cpu_count()
name_corpus = ['attraction_tag.list',
               'hotel_tag.list',
               'restaurant_tag.list']

name_model = ['model/attraction_tag.model',
              'model/hotel_tag.model',
              'model/restaurant_tag.model']

params_tag = [{'size':300, 'window':99999, 'min_count':0,        # Attraction
               'workers':cores, 'iter':100, 'sg':1, 'sample':1e-5},
              {'size':300, 'window':99999, 'min_count':0,        # Hotel
               'workers':cores, 'iter':100, 'sg':1, 'sample':1e-5},
              {'size':300, 'window':99999, 'min_count':0,        # Restaurant
               'workers':cores, 'iter':100, 'sg':1, 'sample':1e-5}]


# In[9]:


# int to string
for c in name_corpus:
    with open(corpus_path+'kor_'+c, 'rb') as f:
        corpus = pickle.load(f)
    corpus = [[str(pid) for pid in line] for line in corpus]
    with open(corpus_path+'kor_'+c,'wb') as f:
        pickle.dump(corpus, f)
    with open(corpus_path+'eng_'+c, 'rb') as f:
        corpus = pickle.load(f)
    corpus = [[str(pid) for pid in line] for line in corpus]
    with open(corpus_path+'eng_'+c,'wb') as f:
        pickle.dump(corpus, f)


# In[11]:


# 한글&영문 corpus 병합
corpora = []
for c in name_corpus:
    with open(corpus_path+'kor_'+c, 'rb') as f:
        kor = pickle.load(f)
    with open(corpus_path+'eng_'+c, 'rb') as f:
        eng = pickle.load(f)
    merged = kor+eng
    with open(corpus_path+c, 'wb') as f:
        pickle.dump(merged, f)
    corpora.append(merged)


# In[16]:


#test
corpus = corpora[0]
print('corpus 길이', len(corpus))
total = pd.DataFrame([len(sent) for sent in corpus]).sum()
length = 0
for line in corpus:
    length = max(length, len(line))
print('corpus내 최대 길이', length)
print('corpus내 모든 장소 합', total)


# In[21]:


spent = []
for i in range(2,3):
    start = time.time()
    model = Word2Vec(corpora[i], **params_tag[i])
    spent.append('Elapsed time: '+str(time.time() - start)+' sec'+' ['+name_model[i]+']')
    model.wv.save(name_model[i])
print(spent)

