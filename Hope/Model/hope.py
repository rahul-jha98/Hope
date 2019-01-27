import pandas as pd
from sklearn.preprocessing import LabelEncoder
import pickle

data = pd.read_csv('foreveralone.csv')
target = data.pop('attempt_suicide')
categorical = ['gender', 'sexuallity', 'income', 'race', 'bodyweight', 'virgin', 'social_fear', 'depressed', 'employment', 'edu_level']
data = data.drop('what_help_from_others', axis = 1)
data = data.drop('improve_yourself_how', axis = 1)
data = data.drop(['prostitution_legal', 'pay_for_sex'], axis = 1)
data['job_title'] = data['job_title'].fillna(method = 'bfill')
data = data.drop('time', axis = 1)
data = data.drop('job_title', axis = 1)
data = pd.get_dummies(data = data, columns = categorical)
data_arr = data.values

lb = LabelEncoder()
target_arr = lb.fit_transform(target)

from sklearn.ensemble import RandomForestRegressor
regressor = RandomForestRegressor(n_estimators = 100)
regressor.fit(data_arr, target_arr)

s = pickle.dump(regressor, open('my_model', 'wb'))

acc = regressor.score(data_arr, target_arr)
print(acc)