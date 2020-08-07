package e.sundaraganapathyl.kssolutions;

public class StoreData {

    String empID,A_time,WRemark,Dependancy,WorkID,isComplete;
    public StoreData(String EmpId,String workId,String Actual,String remark,String Dependancy,String complete){
        this.empID = EmpId;
        this.WorkID = workId;
        this.A_time = Actual;
        this.WRemark = remark;
        this.Dependancy = Dependancy;
        this.isComplete = complete;
    }
    public String getEmpID(){
        return empID;
    }
    public String gettime(){
        return A_time;
    }
    public String getWRemark(){
        return WRemark;
    }
    public String getDependancy(){
        return Dependancy;
    }
    public String getWorkID(){
        return WorkID;
    }
    public String getIsComplete(){return isComplete;}
}
