from django.urls import path

from . import views

urlpatterns = [
    path('<int:bookID>/', views.results),
]
