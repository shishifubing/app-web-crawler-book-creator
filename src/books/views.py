from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse


def results(request, bookID):
    print('here')
    response = f"id of the book {bookID}."
    return HttpResponse(response)
