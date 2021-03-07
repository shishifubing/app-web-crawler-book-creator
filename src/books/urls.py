from django.urls import path
from . import views

urlpatterns = [
    path('0/', views.allBooks),
    path('<int:bookID>/', views.results),
]
