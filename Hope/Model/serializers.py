from rest_framework import serializers


class QuerySerializer(serializers.Serializer):
    gender = serializers.CharField(max_length = 50)
    sexuality = serializers.CharField(max_length = 50)
    age = serializers.IntegerField()
    income = serializers.CharField(max_length = 50)
    race = serializers.CharField(max_length = 50)
    bodyweight = serializers.CharField(max_length = 50)
    virgin = serializers.CharField(max_length = 50)
    friends = serializers.IntegerField()
    social_fear = serializers.CharField(max_length = 50)
    depressed = serializers.CharField(max_length = 50)
    employment = serializers.CharField(max_length = 50)
    edu_level = serializers.CharField(max_length = 50)


class ChatRoomSerializer(serializers.Serializer):
    text = serializers.CharField(max_length = 1000)
