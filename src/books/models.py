from django.db import models

# Create your models here.


class Books(models.Model):
    author = models.CharField(verbose_name='book author', max_length=100)
    title = models.CharField(verbose_name='book title', max_length=200)


class Chapters(models.Model):
    book = models.ForeignKey(Books, on_delete=models.CASCADE)
    number = models.IntegerField(verbose_name='chapter number', null=False)
    text = models.TextField(verbose_name='chapter text')
