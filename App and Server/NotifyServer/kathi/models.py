from django.db import models
import datetime
# Create your models here.
class login(models.Model):
    id = models.AutoField(primary_key=True)
    empid=models.CharField(max_length=50)
    password=models.CharField(max_length=50)
    first=models.IntegerField(default=0)


class Activity(models.Model):
    id = models.AutoField(primary_key = True)
    empid = models.CharField(max_length = 10)
    activity=models.CharField(max_length=500)
    workid=models.CharField(max_length=10)
    CompletedDate = models.DateField(auto_now=True)
    plannedtime=models.IntegerField(default=0)
    actualtime=models.IntegerField(default=0)
    work_remark = models.CharField(max_length=500)
    submission_remark = models.CharField(max_length=500)
    isComplete = models.IntegerField(default=0)
    dependancy = models.CharField(max_length = 500)
    

class Employee(models.Model):
    empid=models.CharField(max_length=10)
    first_name = models.CharField(max_length=50)
    last_name = models.CharField(max_length=50)
    phone_no = models.CharField(max_length=20)
    email = models.EmailField()
    street = models.CharField(max_length=50)
    district = models.CharField(max_length=50)
    state = models.CharField(max_length=50)
    educational_qualification = models.CharField(max_length=50)
    date_of_joining = models.DateField()
    experience = models.IntegerField()

class EmployeeRating(models.Model):
    id=models.AutoField(primary_key=True)
    empid=models.CharField(max_length=10)
    date=models.DateField(default=datetime.datetime(2000, 1, 1))
    rating=models.IntegerField(default=0)
    
class ManagerRating(models.Model):
    id=models.AutoField(primary_key=True)
    empid=models.CharField(max_length=10)
    workid=models.CharField(max_length=10)
    date=models.DateField()
    rating=models.IntegerField(default=0)

class Projects(models.Model):
    id=models.AutoField(primary_key=True)
    empid=models.CharField(max_length=10)
    workid=models.CharField(max_length=10)
    workordertype=models.CharField(max_length=100)
    status=models.CharField(max_length=100)
    iscomplete=models.IntegerField(default=0)
    workordernumber=models.IntegerField(default=0)
    productname=models.CharField(max_length=100)
    expecteddate=models.DateField()
    woissuedate=models.DateField()
    acceptedby=models.CharField(max_length=100)
    customername=models.CharField(max_length=100)
    remarks=models.CharField(max_length=500)
    attachments=models.CharField(max_length=100)
    customercontact=models.CharField(max_length=10)
    cusromeremail=models.CharField(max_length=100)
    amountspent=models.IntegerField(default=0)
    isdone=models.IntegerField(default=0)