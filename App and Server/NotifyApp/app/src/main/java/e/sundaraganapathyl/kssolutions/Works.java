package e.sundaraganapathyl.kssolutions;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Works {
    public String mdate;
    public Works(String completedDate) {
        mdate = completedDate;
    }
    public String getDate(){
        return mdate;
    }

    public static ArrayList<Works> DatewiseWork(JSONObject activity) throws JSONException {
        ArrayList<Works> works = new ArrayList<Works>();
        try {
            int len = activity.length();
            for (int i = 0; i < len; i++) {
                String ind = Integer.toString(i);
                works.add(new Works(activity.getJSONObject(ind).getString("CompletedDate")));
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
