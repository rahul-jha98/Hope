import nltk, string, numpy

d1 = "plot: two teen couples go to a church party, drink and then drive."
d2 = "films adapted from comic books have had plenty of success , whether they're about superheroes ( batman , superman , spawn ) , or geared toward kids ( casper ) or the arthouse crowd ( ghost world ) , but there's never really been a comic book like from hell before . "
d3 = "every now and then a movie comes along from a suspect studio , with every indication that it will be a stinker , and to everybody's surprise ( perhaps even the studio ) the film becomes a critical darling . "
d4 = "films adapted from comic books have had plenty of success , whether they're about superheroes ( batman , superman , spawn ) , or geared toward kids ( casper ) or the arthouse crowd ( ghost world ) , but there's never really been a comic book like from hell before . "
documents = [d1, d2, d3, d4]

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


from sklearn.feature_extraction.text import TfidfVectorizer
TfidfVec = TfidfVectorizer(tokenizer=LemNormalize, stop_words='english')
def cos_similarity(textlist):
    tfidf = TfidfVec.fit_transform(textlist)
    return (tfidf * tfidf.T).toarray()
print(cos_similarity(documents))