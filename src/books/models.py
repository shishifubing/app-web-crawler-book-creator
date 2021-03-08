import os
import requests
import re
from django.db import models
from bs4 import BeautifulSoup, SoupStrainer


# Create your models here.


class Book(models.Model):
    author = models.CharField(verbose_name='book author', max_length=100)
    title = models.CharField(verbose_name='book title', max_length=200)
    link = models.CharField(verbose_name='chapter link', max_length=200)

    @staticmethod
    def getLinksFromSitemaps():

        sitemapURL = 'https://www.wuxiaworld.com/sitemap.xml'
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0'}
        response = requests.get(sitemapURL, headers=headers)
        if (response.status_code == 200):
            try:
                sitemap = BeautifulSoup(
                    response.content, 'lxml-xml').findAll('loc')
            except IndexError:
                print('no results')
            else:
                links = {'links': ['https://www.wuxiaworld.com']}
                for linkNode in sitemap:
                    novelLink = linkNode.string.strip()
                    links.update({novelLink: ['null']})
        else:
            return response.status_code

            # for linkNode in BeautifulSoup(
            #        chapters, 'lxml-xml', parse_only=onlyLinks):
            #    chapterLink = linkNode.string.strip()
            #    novelLink = links['links'][0] + \
            #        '/novel/' + chapterLink.split('/')[4]
            #    if novelLink in links:
            #        links[novelLink].append(chapterLink)
            #    else:
            #        links.update({novelLink: list()})


class Chapter(models.Model):
    book = models.ForeignKey(Book, on_delete=models.CASCADE)
    number = models.IntegerField(verbose_name='chapter number', null=False)
    text = models.TextField(verbose_name='chapter text')
    link = models.CharField(verbose_name='chapter link', max_length=200)
