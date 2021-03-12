import os
import requests
import re
from django.db import models
from bs4 import BeautifulSoup, SoupStrainer


# Create your models here.


class Book(models.Model):
    author = models.CharField(
        verbose_name='author', max_length=200, default='no author')
    title = models.CharField(verbose_name='title',
                             max_length=200, default='no title')
    sourceUrl = models.CharField(
        verbose_name='Source', max_length=200, default='no source link')
    siteUrl = models.CharField(verbose_name='Site url', default='/no-url/',
                               max_length=200)

    def __str__(self):
        return self.title

    @staticmethod
    def getChapterUrls(pageString):
        links = []
        try:
            for url in BeautifulSoup(
                    pageString, 'lxml', parse_only=SoupStrainer('li')).select('.chapter-item > a'):
                links.append('https://www.wuxiaworld.com' + url['href'])
        except IndexError:
            print('\n\nNO RESULTS\n\n')
        else:
            return links

    @staticmethod
    def getChapter(domainURL, chapter, number):
        headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
                   'Accept': 'text/html'}
        chapter.number = number
        while True:
            response = requests.get(chapter.link, headers=headers)
            if (response.status_code == 200):
                cssSelectorChapterText = '#chapter-content'
                cssSelectorRemoveElements = 'script, style, .chapter-nav'
                try:
                    chapterDocument = BeautifulSoup(
                        response.content, 'html.parser')
                    chapterText = chapterDocument.select(
                        cssSelectorChapterText, limit=1)[0]
                except IndexError:
                    print('no results')
                else:
                    numberString = str(number)
                    placeholder = '000000'
                    numberString = placeholder[0:(
                        6 - len(numberString) - 1)] + numberString
                    chapter.title = numberString
                    chapterText['id'] = 'chapter-'+numberString
                    chapterText['class'] = 'chapter-wrapper'
                    for uselessElement in chapterText.select(cssSelectorRemoveElements):
                        uselessElement.decompose()
                    for relativeLink in chapterText(href=re.compile('^/')):
                        relativeLink['href'] = domainURL + relativeLink['href']
                    chapterText.smooth()
                    chapterString = f'<div id="chapter-{numberString}" class=chapter-wrapper>\n    '
                    for node in chapterText.findAll(['p', 'a']):
                        chapterString += node.prettify(
                            formatter='html').replace('\n', '\n    ')
                    chapter.text = chapterString+'\n</div>'
                    chapter.save()
                break
            else:
                print(chapter.link, response.status_code)


class Chapter(models.Model):
    book = models.ForeignKey(
        Book, related_name='chapters', on_delete=models.CASCADE)
    chapterNumber = models.IntegerField(
        verbose_name='chapter number', null=False)
    content = models.TextField(verbose_name='chapter text')
    title = models.CharField(verbose_name='chapter title',
                             max_length=200, default="title")
    sourceUrl = models.CharField(verbose_name='chapter link', max_length=200)

    def __str__(self):
        return self.title


class Sitemap(models.Model):
    sourceUrl = models.CharField(verbose_name='chapter link',
                                 max_length=200, default='no-url')
    title = models.CharField(verbose_name='chapter title',
                             max_length=200, default='no-title')
    content = models.TextField(verbose_name='chapter text', default="empty")

    def __str__(self):
        return self.title

    def getUrls(self):
        links = []
        linksList = BeautifulSoup(
            self.content, 'lxml-xml').findAll('loc')
        for link in linksList:
            links.append(link.string)
        return links
