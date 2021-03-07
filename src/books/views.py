from .models import Books
from django.shortcuts import render
from django.contrib.flatpages.models import FlatPage
from django.shortcuts import get_object_or_404


def results(request, bookID):
    flatPage = get_object_or_404(FlatPage, url='/books/')
    context = {'flatpage': flatPage, 'books': Books.objects.all()}
    flatPage.content = '<p>test</p>'
    return render(request, 'flatpages/default.html', context)


def allBooks(request):
    book = Books.objects.get(id=1)
    flatPage = FlatPage.objects.get(url='/books/')
    context = {'flatpage': flatPage}
    return render(request, flatPage.template_name, context)
