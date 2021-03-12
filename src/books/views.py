import requests
from bs4 import BeautifulSoup
from .models import Book, Sitemap, Chapter
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
    else:
        print('\n\n FUCKED UP \n\n')


def results(request, bookID):
    context = {'books': Book.objects.all()[:5]}
    return render(request, DEFAULT_TEMPLATE, context)


def chapters(request, bookURL, chapterNumber):
    novelsUrl = 'https://www.wuxiaworld.com/sitemap/novels'
    novelUrl = 'https://www.wuxiaworld.com/novel/i-shall-seal-the-heavens'
    #novelsSitemap = Sitemap.objects.get(sourceUrl=novelsUrl)
    #content = ''
    amount = 0
    for chapter in Chapter.objects.all():
        chapter.delete()
    book = Book.objects.get(sourceUrl=novelUrl)
    for chapterUrl in Book.getChapterUrls(getPage(novelUrl)):
        number = Chapter.objects.filter(book=book).count()+1
        title = book.title + ' - Chapter ' + str(number)
        Chapter.objects.create(book=book, chapterNumber=number, title=title,
                               content='content', sourceUrl=chapterUrl)
        amount += 1
    # book.chapters.set(chaptersList)
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
