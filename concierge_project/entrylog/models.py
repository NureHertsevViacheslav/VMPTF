from django.contrib.auth.models import AbstractUser
from django.db import models
from django.utils import timezone

class CustomUser(AbstractUser):
    ROLE_CHOICES = [
        ('concierge', 'Concierge'),
        ('admin', 'Administrator'),
    ]
    role = models.CharField(max_length=20, choices=ROLE_CHOICES, default='concierge')

class VisitorEntry(models.Model):
    full_name = models.CharField("Повне ім'я", max_length=100)
    entered_at = models.DateTimeField("Час входу", auto_now_add=True)
    exited_at = models.DateTimeField("Час виходу", null=True, blank=True)
    added_by = models.ForeignKey(
        CustomUser,
        verbose_name="Додав користувач",
        on_delete=models.CASCADE
    )

    def mark_exit(self):
        self.exited_at = timezone.now()
        self.save()

    def __str__(self):
        return f"{self.full_name} (in: {self.entered_at}, out: {self.exited_at})"
