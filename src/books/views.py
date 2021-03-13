import requests
from bs4 import BeautifulSoup, SoupStrainer
from .models import Book, Sitemap, Chapter
from django.shortcuts import render
from django.contrib.flatpages.models import FlatPage
from django.shortcuts import get_object_or_404, get_list_or_404
from selenium import webdriver

DEFAULT_TEMPLATE = 'flatpages/default.html'


def results(request, bookID):
    context = {'books': Book.objects.all()[:5]}
    return render(request, DEFAULT_TEMPLATE, context)


def chapters(request, bookURL, chapterNumber):
    novelsUrl = 'https://readnovelfull.com/novel_sitemap.xml'
    novelUrl = 'https://readnovelfull.com/renegade-immortal.html'
    # novelsSitemap = Sitemap.objects.get(sourceUrl=novelsUrl)
    # content = ''
    readNovelFullSitemapNovels = Sitemap.objects.get(sourceUrl=novelsUrl)
    readNovelFullSitemapNovels.content = Book.getPage(novelsUrl)
    readNovelFullSitemapNovels.save()
    amount = 0
    book = Book.objects.get(sourceUrl=novelUrl)
    try:
        fireFoxOptions = webdriver.FirefoxOptions()
        fireFoxOptions.headless = True
        fireFoxOptions.add_argument('--ignore-certificate-errors')
        fireFoxOptions.set_preference('javascript.enabled', True)
        fireFoxOptions.set_preference(
            'general.useragent.override', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0')
        browser = webdriver.Firefox(firefox_options=fireFoxOptions)
        browser.get(
            'https://www.readnovelfull.com/renegade-immortal.html#tab-chapters-title')
        novelPage = browser.page_source
    finally:
        try:
            browser.close()
        except:
            pass

    chapterList = BeautifulSoup(
        novelPage, 'lxml', parse_only=SoupStrainer(id='tab-chapters')).find_all('li')

    # for chapter in chapterList:
    # chapterNumber = Chapter.objects.filter(book=book).count()+1
    # Chapter.objects.create(book=book, content='content', title=chapter.a.string,
    #                       sourceUrl='https://readnovelfull.com' + chapter.a['href'], chapterNumber=chapterNumber)
    #    amount += 1

    # book = Book.objects.get(sourceUrl=novelUrl)
    # for chapterUrl in Book.getChapterUrls(Book.getPage(novelUrl)):
    #    number = Chapter.objects.filter(book=book).count()+1
    #    title = book.title + ' - Chapter ' + str(number)
    #    Chapter.objects.create(book=book, chapterNumber=number, title=title,
    #                           content='content', sourceUrl=chapterUrl)
    #    amount += 1
    context = {'content': chapterList}
    template_name = ''
    return render(request, DEFAULT_TEMPLATE, context)


def allBooks(request):
    book = Book.objects.get(id=1)
    flatPage = FlatPage.objects.get(url='/books/')
    context = {'flatpage': flatPage}
    return render(request, flatPage.template_name, context)
