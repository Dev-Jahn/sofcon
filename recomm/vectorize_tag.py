import numpy as np
from pandas import *
import os
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.text import text_to_word_sequence


import logging
logging.basicConfig(
	format='%(asctime)s : %(levelname)s : %(message)s',
	level=logging.INFO)
from gensim.models import Word2Vec
import time
start = time.time()
print("train start")
model = Word2Vec(corpus, size=300, window=10, min_count=10, workers=8, iter=100, sg=1)#, sample=1e-3)
model.save("model/restaurent.model")
print("train end")
print("Elapsed time: %s sec" % (time.time() - start))