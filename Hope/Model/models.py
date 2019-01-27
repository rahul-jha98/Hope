from django.db import models

class ChatRoom(models.Model):
    name = models.CharField(max_length = 100)
    keyword = models.CharField(max_length = 1000, default = None)

    def __str__(self):
        return self.name
