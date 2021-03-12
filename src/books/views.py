import requests
from bs4 import BeautifulSoup
from .models import Book, Chapter, Sitemap
from django.shortcuts import render
from django.contrib.flatpages.models import FlatPage
from django.shortcuts import get_object_or_404, get_list_or_404

DEFAULT_TEMPLATE = 'flatpages/default.html'


def getPage(url):
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
               'Accept': 'text/html'}
    response = requests.get(url, headers=headers)
    if (response.status_code == 200):
        return response.content


def results(request, bookID):
    context = {'books': Book.objects.all()[:5]}
    return render(request, DEFAULT_TEMPLATE, context)


def chapters(request, bookURL, chapterNumber):
    novelsUrl = 'https://www.wuxiaworld.com/sitemap/novels'
    chaptersUrl1 = 'https://wuxiaworld.com/sitemap/chapters/1'
    chaptersUrl2 = 'https://wuxiaworld.com/sitemap/chapters/2'
    novelsSitemap = Sitemap.objects.get(sourceUrl=novelsUrl)
    chaptersSitemap1 = Sitemap.objects.get(sourceUrl=chaptersUrl1)
    chaptersSitemap1.content = getPage(chaptersUrl1)
    chaptersSitemap1.save()
    chaptersSitemap2 = Sitemap.objects.get(sourceUrl=chaptersUrl2)
    chaptersSitemap2.content = getPage(chaptersUrl2)
    chaptersSitemap2.save()
    content = ''
    amount = 0
    for chapter in Chapter.objects.all():
        chapter.delete()
    for chapterUrl in Sitemap.getUrls(chaptersSitemap1):
        book = Book.objects.get(
            sourceUrl='https://www.wuxiaworld.com/novel/' + chapterUrl.split('/')[4])
        if (book.siteUrl.replace('/', '') == chapterUrl.split('/')[4]):
            chapters = book.chapters
            number = chapters.all().count()+1
            title = book.title + ' - Chapter ' + str(number)
            chapter = chapters.create(chapterNumber=number, title=title,
                                      content='content', sourceUrl=chapterUrl)
            chapters.add(chapter)
            amount += 1
            if (amount == 200):
                break
    # links = Book.getLinksFromSitemaps()
    # for novelURL in links:
    #    book = Book.objects.get(link=novelURL)
    #    count = 0
    #    for chapterLink in Chapter.objects.all():
    # chapter = Chapter(number=count, text='no-text',
    #                  title = novelURL + str(count), link = chapterLink)
    #        chapterLink.delete()
    # book.chapters.add()
    # book.save()
    # count += 1
    context = {'content': str(amount)}
    template_name = ''
    return render(request, DEFAULT_TEMPLATE, context)


def allBooks(request):
    book = Book.objects.get(id=1)
    flatPage = FlatPage.objects.get(url='/books/')
    context = {'flatpage': flatPage}
    return render(request, flatPage.template_name, context)
