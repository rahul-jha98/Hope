from django.shortcuts import render
from .serializers import QuerySerializer, ChatRoomSerializer
from .models import ChatRoom
from rest_framework.views import APIView
from rest_framework.response import Response
import pandas as pd
import pickle
import os
import sklearn
from sklearn.preprocessing import LabelEncoder
import nltk, string, numpy
from sklearn.feature_extraction.text import TfidfVectorizer


class QueryAPIView(APIView):
    serializer_class = QuerySerializer

    def post(self, request, format = 'json'):
        serializer = QuerySerializer(data = request.data)
        if serializer.is_valid():
            data = serializer.data
            df = pd.DataFrame(data = {
                'gender' : data['gender'],
                'sexuallity' : data['sexuality'],
                'age' : data['age'],
                'income' : data['income'],
                'race' : data['race'],
                'bodyweight' : data['bodyweight'],
                'virgin' : data['virgin'],
                'friends' : data['friends'],
                'social_fear' : data['social_fear'],
                'depressed' : data['depressed'],
                'employment' : data['employment'],
                'edu_level' : data['edu_level'],
            }, index = [0])
            # print(os.listdir())

            data = pd.read_csv('Model/foreveralone.csv')
            target = data.pop('attempt_suicide')
            categorical = ['gender', 'sexuallity', 'income', 'race', 'bodyweight', 'virgin', 'social_fear', 'depressed', 'employment', 'edu_level']
            data = data.drop('what_help_from_others', axis = 1)
            data = data.drop('improve_yourself_how', axis = 1)
            data = data.drop(['prostitution_legal', 'pay_for_sex'], axis = 1)
            data['job_title'] = data['job_title'].fillna(method = 'bfill')
            data = data.drop('time', axis = 1)
            data = data.drop('job_title', axis = 1)
            # print(data.values.shape)
            # print(df.values.shape)
            data_train = data
            data = data.append(df)
            data = pd.get_dummies(data = data, columns = categorical)
            data_train = pd.get_dummies(data = data_train, columns = categorical)
            # print(data.shape)

            lb = LabelEncoder()
            target_arr = lb.fit_transform(target)
            # from sklearn.ensemble import RandomForestRegressor
            # regressor = RandomForestRegressor(n_estimators = 100)
            # regressor.fit(data_train, target_arr)


            pr_data = data.iloc[-1, :].values
            pr_data = pr_data.reshape(1, -1)
            loaded_model = pickle.load(open('Model/my_model', 'rb'))
            predicted_perc = loaded_model.predict(pr_data)
            json = {
                'predicted' : predicted_perc,
            }

            return Response(json)


class ChatRoomAPIView(APIView):
    serializer_class = ChatRoomSerializer

    def post(self, request, format = 'json'):
        serializer = ChatRoomSerializer(data = request.data)
        if serializer.is_valid():
            
            # d1 = "I'm sad because my grandpa died and now I feel so lonely. I loved him so much."
            # d2 = "Sad, Death, Lonely, Died, Alone, Crying, Don't know what to do, Loved one, Love, Close"
            # d3 = "every now and then a movie comes along from a suspect studio , with every indication that it will be a stinker , and to everybody's surprise ( perhaps even the studio ) the film becomes a critical darling . "
            # d4 = "films adapted from comic books have had plenty of success , whether they're about superheroes ( batman , superman , spawn ) , or geared toward kids ( casper ) or the arthouse crowd ( ghost world ) , but there's never really been a comic book like from hell before . "
            # documents = [d1, d2, d3, d4]
            documents = [serializer.data['text']]
            obj = ChatRoom.objects.all()
            for i in obj:
                documents.append(i.keyword)
            print(documents)

            lemmer = nltk.stem.WordNetLemmatizer()
            def LemTokens(tokens):
                return [lemmer.lemmatize(token) for token in tokens]
            remove_punct_dict = dict((ord(punct), None) for punct in string.punctuation)
            def LemNormalize(text):
                return LemTokens(nltk.word_tokenize(text.lower().translate(remove_punct_dict)))

            # from sklearn.feature_extraction.text import CountVectorizer
            # LemVectorizer = CountVectorizer(tokenizer=LemNormalize, stop_words='english')
            # LemVectorizer.fit_transform(documents)



            # tf_matrix = LemVectorizer.transform(documents).toarray()
            # print(tf_matrix)


            # from sklearn.feature_extraction.text import TfidfTransformer
            # tfidfTran = TfidfTransformer(norm="l2")
            # tfidfTran.fit(tf_matrix)
            # print(tfidfTran.idf_)


            TfidfVec = TfidfVectorizer(tokenizer=LemNormalize, stop_words='english')
            def cos_similarity(textlist):
                tfidf = TfidfVec.fit_transform(textlist)
                return (tfidf * tfidf.T).toarray()
            similarity = cos_similarity(documents)

            suggestions = similarity[0][:]
            # print(suggestions)

            maxi = 0
            for index, i in enumerate(suggestions[1:]):
                if i > maxi:
                    maxi = i
                    max_index = index
            ret = obj[max_index]
            json = {
                'id' : ret.id,
                'name' : ret.name,
            }

                


            # data = serializer.data
            # documents = [data['text']]
            # obj = Keyword.objects.all()
            # for i in obj:
            #     documents.append(i.keyword)
            # print(cos_similarity(documents))
            return Response(json)