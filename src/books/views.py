from .models import Book, Chapter, Sitemap
from django.shortcuts import render
from django.contrib.flatpages.models import FlatPage
from django.shortcuts import get_object_or_404, get_list_or_404

DEFAULT_TEMPLATE = 'flatpages/default.html'


def results(request, bookID):
    context = {'books': Book.objects.all()[:5]}
    return render(request, DEFAULT_TEMPLATE, context)


def chapters(request, bookURL, chapterNumber):
    url1 = 'https://wuxiaworld.com/sitemap/chapters/1'
    url2 = 'https://wuxiaworld.com/sitemap/chapters/2'
    chapters1 = Sitemap(title='wuxiaworld.com chapters 1',
                        url=url1, text=Sitemap.getNewSitemap(url1))
    chapters1.save()
    chapters2 = Sitemap(title='wuxiaworld.com chapters 2',
                        url=url2, text=Sitemap.getNewSitemap(url2))
    chapters2.save()
    links = Book.getLinksFromSitemaps()
    for novelURL in links:
        book = Book.objects.get(link=novelURL)
        count = 0
        for chapterLink in Chapter.objects.all():
            # chapter = Chapter(number=count, text='no-text',
            #                  title = novelURL + str(count), link = chapterLink)
            chapterLink.delete()
            # book.chapters.add()
            # book.save()
            # count += 1
    context = {'content': Sitemap.get(url=url1)}
    template_name = ''
    return render(request, DEFAULT_TEMPLATE, context)


def allBooks(request):
    book = Book.objects.get(id=1)
    flatPage = FlatPage.objects.get(url='/books/')
    context = {'flatpage': flatPage}
    return render(request, flatPage.template_name, context)
