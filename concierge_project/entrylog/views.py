from django.utils import timezone
from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.views import LoginView, LogoutView
from .forms import CustomUserCreationForm
from .models import VisitorEntry
from .forms import RegisterForm
from django.contrib.auth.decorators import login_required

def register(request):
    if request.method == 'POST':
        form = CustomUserCreationForm(request.POST)
        if form.is_valid():
            user = form.save()
            login(request, user)
            return redirect('entry_list')
    else:
        form = CustomUserCreationForm()
    return render(request, 'register.html', {'form': form})

@login_required
def entry_list(request):
    entries = VisitorEntry.objects.all()
    return render(request, 'entry_list.html', {'entries': entries})

@login_required
def add_entry(request):
    if request.method == 'POST':
        full_name = request.POST.get('full_name')
        VisitorEntry.objects.create(full_name=full_name, added_by=request.user)
        return redirect('entry_list')
    return render(request, 'add_entry.html')

@login_required
def mark_exit(request, pk):
    entry = get_object_or_404(VisitorEntry, pk=pk)
    entry.exited_at = timezone.now()
    entry.save()
    return redirect('entry_list')

def register_view(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            user = form.save()
            login(request, user) 
            return redirect('entry_list') 
    else:
        form = RegisterForm()
    return render(request, 'register.html', {'form': form})