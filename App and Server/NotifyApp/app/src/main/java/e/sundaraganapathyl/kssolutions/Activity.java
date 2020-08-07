package e.sundaraganapathyl.kssolutions;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity {
    public String mWork,mWorkID;
    public int isComplete;
    public Activity(String work,String workid,int complete ) {
        mWork = work;
        mWorkID = workid;
        isComplete = complete;
    }
    public String getWork(){
        return mWork;
    }
    public String getWorkID(){
        return mWorkID;
    }
    public int getIsComplete(){
        return isComplete;
    }

    public static ArrayList<Activity> DaywiseWork(JSONObject activity) throws JSONException {
        ArrayList<Activity> works = new ArrayList<Activity>();
        try {
            int len = activity.length();
            for (int i = 0; i < len; i++) {
                String ind = Integer.toString(i);
                works.add(new Activity(activity.getJSONObject(ind).getString("activity"),activity.getJSONObject(ind).getString("workid"),activity.getJSONObject(ind).getInt("iscomplete")));
            }
            Log.i("works",works.toString());
        }
        catch (Exception e)
        {
            Log.d("ERROR", e.toString());
        }
        return works;
    }
}
