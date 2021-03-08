from .models import Book
from django.shortcuts import render
from django.contrib.flatpages.models import FlatPage
from django.shortcuts import get_object_or_404, get_list_or_404


def results(request, bookID):
    flatPage = get_object_or_404(FlatPage, url='/books/')
    books = get_list_or_404(Book.objects.all())
    template_name = 'flatpages/default.html'
    context = {'flatpage': flatPage, 'books': books}
    return render(request, template_name, context)


def allBooks(request):
    book = Book.objects.get(id=1)
    flatPage = FlatPage.objects.get(url='/books/')
    context = {'flatpage': flatPage}
    return render(request, flatPage.template_name, context)
