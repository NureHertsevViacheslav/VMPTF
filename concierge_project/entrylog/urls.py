from django.urls import path
from . import views
from django.contrib.auth import views as auth_views

urlpatterns = [
    path('register/', views.register, name='register'),
    path('login/', auth_views.LoginView.as_view(template_name='login.html'), name='login'),
    path('logout/', auth_views.LogoutView.as_view(next_page='login'), name='logout'),

    path('', views.entry_list, name='entry_list'),       
    path('add/', views.add_entry, name='add_entry'),       
    path('exit/<int:pk>/', views.mark_exit, name='mark_exit'),
]
