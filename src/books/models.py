import os
import requests
import re
from django.db import models
from bs4 import BeautifulSoup, SoupStrainer


# Create your models here.


class Chapter(models.Model):
    number = models.IntegerField(verbose_name='chapter number', null=False)
    text = models.TextField(verbose_name='chapter text')
    link = models.CharField(verbose_name='chapter link', max_length=200)

    def __str__(self):
        return self.link


class Book(models.Model):
    author = models.CharField(verbose_name='book author', max_length=100)
    title = models.CharField(verbose_name='book title', max_length=200)
    link = models.CharField(verbose_name='chapter link', max_length=200)
    chapters = models.ManyToManyField(Chapter)

    def __str__(self):
        return self.link

    @staticmethod
    def getLinksFromSitemaps():

        def updateLinks(links, sitemapURL, headers, isChapter=True):
            response = requests.get(sitemapURL, headers=headers)
            if (response.status_code == 200):
                try:
                    sitemap = BeautifulSoup(
                        response.content, 'lxml-xml', parse_only=SoupStrainer('loc')).findAll('loc')
                except IndexError:
                    print('no results')
                else:
                    if (not isChapter):
                        for linkNode in sitemap:
                            novelLink = linkNode.get_text().strip()
                            links.update({novelLink: list()})
                    else:
                        for linkNode in sitemap:
                            chapterLink = linkNode.get_text().strip()
                            novelLink = links['links'][0] + \
                                '/novel/' + chapterLink.split('/')[4]
                            if novelLink in links:
                                links[novelLink].append(chapterLink)
                            else:
                                links.update({novelLink: list()})
            else:
                return response.status_code

        sitemapURL = 'https://www.wuxiaworld.com/sitemap/novels'
        chaptersURL1 = 'https://www.wuxiaworld.com/sitemap/chapters/1'
        chaptersURL2 = 'https://www.wuxiaworld.com/sitemap/chapters/2'
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0'}

        links = {'links': ['https://www.wuxiaworld.com']}

        updateLinks(links, sitemapURL, headers, False)
        updateLinks(links, chaptersURL1, headers)
        updateLinks(links, chaptersURL2, headers)
        return links
