from django.shortcuts import render
from django.http import HttpResponse,JsonResponse
from . import models
from django.core import serializers
import json
from datetime import datetime,date

# Create your views here.
def loginapi(request):
    try:
        print(request.body)
        request_values = json.loads(request.body.decode('utf-8'))
        eid=request_values['userid']
        password=request_values['password']
        data = models.login.objects.all().filter(empid = eid)    
        result=serializers.serialize('json',data)[1:-1]    
        to_Send = {}
        if(data.count()==0):
            to_Send["status"]="0"
            to_Send["error"]="Invalid ID"
        else:
            result=eval(result)            
            pwd = result['fields']['password']
            isfirst = result['fields']['first']
            if password == pwd:
                if isfirst == 0:
                    to_Send["status"] = "2"
                    to_Send['error'] = "None"
                else:
                    to_Send["status"] = "1"
                    to_Send['error'] = "None"
            else:
                to_Send["status"] = "0"
                to_Send['error'] = "INVALID_PASSWORD"
    except Exception as e:
        to_Send["status"]="-1"
        to_Send["error"]=str(e)
    print(to_Send)
    return JsonResponse(to_Send,safe = False)


def activity(request):
    #eid=request.POST.get('empid')
    print(request.body)
    request_values = json.loads(request.body.decode('utf-8'))
    eid=request_values['userid']
    data = models.Activity.objects.order_by('CompletedDate').reverse().values('CompletedDate').distinct().filter(empid=eid)
    result = {}
    i=0
    for e in data:
        result[str(i)] =e
        i+=1
    print(result)
    return JsonResponse(result)

def dateActivity(request):
    #eid=request.POST.get('empid')
    print(request.body)
    request_values = json.loads(request.body.decode('utf-8'))
    eid=request_values['userid']
    date = request_values['Date']
    date = datetime.strptime(date, r"%Y-%m-%d")
    data = models.Activity.objects.all().filter(empid = eid,CompletedDate = date)
    result = {}
    i=0
    for e in data:
        result[str(i)] ={'empid':e.empid,'workid':e.workid,'activity':e.activity,'plannedtime':e.plannedtime,'actualtime':e.actualtime,'remark':e.work_remark,'submission_remark':e.submission_remark,'completedDate':e.CompletedDate,'iscomplete':e.isComplete,'Dependancy':e.dependancy}
        i+=1
    print(result)
    return JsonResponse(result)


def completework(request):    
    request_values = json.loads(request.body.decode('utf-8'))
    print("Completework",request_values)
    eid = request_values['0']['empid']
    for i in request_values:
        if i=='Rating':
            continue
        eid = request_values[i]['empid']
        dependancy= request_values[i]['dependancy']
        actual_time = int(request_values[i]['time'])  
        remark=request_values[i]['remark']
        wid = request_values[i]['WorkID']
        isComplete = request_values[i]['isComplete']
        workevent=models.Activity.objects.get(workid=wid,empid = eid,CompletedDate = date.today())
        workevent.isComplete=isComplete
        workevent.actualtime = actual_time
        workevent.submission_remark = remark
        workevent.dependancy = dependancy
        workevent.save() 
    rating = request_values['Rating']['Rating']
    event=models.EmployeeRating(empid=eid,date = date.today(),rating = int(rating)
    )
    event.save()

    result={'status':"success",'error':"None"}       
    return JsonResponse(result)

def creatework(request):
    request_values = json.loads(request.body.decode('utf-8'))
    print(request_values)
    workid=request_values['workid']
    activity=request_values['name']
    eid=request_values['empid']
    plannedtime=request_values['plannedtime']
    remark=request_values['remarks'] 
    event=models.Activity(empid=eid,
    workid=workid,
    activity=activity,
    plannedtime=plannedtime,
    work_remark=remark,
    CompletedDate = date.today())
    event.save()
    result={'status':"success",'error':"None"}        
    return JsonResponse(result)


def employeedetail(request):
    request_values = json.loads(request.body.decode('utf-8'))
    print(request_values)
    fname=request_values['fname']
    lname=request_values['lname']
    eid=request_values['empid']
    phone=request_values['phone']
    email=request_values['email']
    address=request_values['address']   
    district=request_values['district']
    state=request_values['state']
    qualification=request_values['education']
    date = datetime.strptime(request_values['DOJ'], r"%Y-%m-%d")
    experience=int(request_values['experience'])
    
    event=models.Employee(empid=eid,
    first_name=fname,
    last_name=lname,
    phone_no=phone,
    email=email,
    street=address,
    district = district,
    state = state,
    educational_qualification = qualification,
    date_of_joining = date,
    experience = experience)
    event.save()
    workevent=models.login.objects.get(empid = eid)
    workevent.first = 1
    workevent.save()
    result={'status':"success",'error':"None"}        
    return JsonResponse(result)
def changePass(request):
    request_values = json.loads(request.body.decode('utf-8'))
    print(request_values)
    eid =request_values['empid']
    password = request_values['password']
    event = models.login.objects.get(empid = eid)
    event.password = password
    event.save()
    result={'status':"success",'error':"None"}        
    return JsonResponse(result)

def samplePage(request):
    return render(request,'Sample1.html')

