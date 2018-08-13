import pandas as pd
import re

####호텔####
df = pd.read_csv('data/kor/hotel_review_pp.csv')
#리뷰단위 분할
text = ''
text = text.join([review+'\n' for review in df['review']])
text = re.sub(r'[^ 가-힣0-9\n]',' ',text)
text = re.sub(r' +',' ',text)
array = text.split('\n')[:]
#형태소 분리
from konlpy.tag import Mecab
mecab = Mecab()
tagged = [mecab.pos(sentence) for sentence in array]
#corpus 저장
import pickle
corpus = [mecab.morphs(sentence) for sentence in array]
with open('corpus/tag-based_hotel.list','wb') as f:
    pickle.dump(corpus, f)

####명소####
df = pd.read_csv('data/kor/attraction_review_pp.csv')
text = ''
text = text.join([review+'\n' for review in df['review']])
text = re.sub(r'[^ 가-힣0-9\n]',' ',text)
text = re.sub(r' +',' ',text)
array = text.split('\n')[:]
from konlpy.tag import Mecab
mecab = Mecab()
tagged = [mecab.pos(sentence) for sentence in array]
import pickle
corpus = [mecab.morphs(sentence) for sentence in array]
with open('corpus/tag-based_attraction.list','wb') as f:
    pickle.dump(corpus, f)

####식당####
df = pd.read_csv('data/kor/restaurant_review_pp.csv')
text = ''
text = text.join([review+'\n' for review in df['review']])
text = re.sub(r'[^ 가-힣0-9\n]',' ',text)
text = re.sub(r' +',' ',text)
array = text.split('\n')[:]
from konlpy.tag import Mecab
mecab = Mecab()
tagged = [mecab.pos(sentence) for sentence in array]
import pickle
corpus = [mecab.morphs(sentence) for sentence in array]
with open('corpus/tag-based_restaurant.list','wb') as f:
    pickle.dump(corpus, f)

#태그로 사용할 형태소 분류만 추출
'''pattern = re.compile('MM|NNG|VA[+].*|VV[+].*|XR')
df_tags = pd.DataFrame(columns = ['score','tags'], dtype = 'int64')
taglist = []
for place in tagged:
    tag = np.array(place)
    npbool = []
    for t in tag:
        npbool.append(re.fullmatch(pattern,t[1])!=None)
    tag = tag[npbool].tolist()
    taglist.append(tag)
df_tags['tags'] = taglist
df_tags['score'] = df['score'].astype('int64')
print(df_tags['tags'][0])'''
