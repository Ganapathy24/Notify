from django.urls import path

from . import views
from django.views.decorators.csrf import csrf_exempt 

urlpatterns = [
    path('mobile/loginapi', csrf_exempt(views.loginapi), name='loginapi'),
    path('mobile/creatework', csrf_exempt(views.creatework), name='creatework'),
    path('mobile/completework', csrf_exempt(views.completework), name='completework'),
    path('mobile/activity', csrf_exempt(views.activity), name='activity'),
    path('mobile/dateActivity', csrf_exempt(views.dateActivity), name='dateActivity'),
    path('mobile/employeedetail', csrf_exempt(views.employeedetail), name='employeedetail'),
    path('mobile/samplePage', csrf_exempt(views.samplePage), name='samplePage'),
    path('mobile/changepass', csrf_exempt(views.changePass), name='changepass'),
]

#assignedwork  creatework