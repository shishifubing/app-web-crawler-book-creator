from django.db import models

# Create your models here.


class Book(models.Model):
    author = models.CharField(verbose_name='book author', max_length=100)
    title = models.CharField(verbose_name='book title', max_length=200)
    link = models.CharField(verbose_name='chapter link', max_length=200)


class Chapter(models.Model):
    book = models.ForeignKey(Book, on_delete=models.CASCADE)
    number = models.IntegerField(verbose_name='chapter number', null=False)
    text = models.TextField(verbose_name='chapter text')
    link = models.CharField(verbose_name='chapter link', max_length=200)
