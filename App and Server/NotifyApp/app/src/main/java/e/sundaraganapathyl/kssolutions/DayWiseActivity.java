package e.sundaraganapathyl.kssolutions;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class DayWiseActivity extends AppCompatActivity {

    String empid,cDate,pass;
    //You need to type ur IP address Here
    String BASE_URL = "http://"/*IP ADDRESS*/+"kathi/mobile/";
    Button submit;
    ArrayList<Activity> activity;
    RecyclerView rvWorks;
    View buttonL;
    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result) throws JSONException;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        setContentView(R.layout.activity_day_wise);
        submit = findViewById(R.id.submission);
        buttonL = findViewById(R.id.buttonLayout);
        empid = getIntent().getStringExtra("ID");
        pass= getIntent().getStringExtra("pass");
        cDate = getIntent().getStringExtra("date");
        if (dtf.format(now).equals(cDate) == false) {
            buttonL.setVisibility(View.GONE);
        }
        onResume(empid,cDate);

    }
    public  void onResume(final String empid,final String date) {
        super.onResume();
        Assignedwork(empid,date,new DayWiseActivity.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) throws JSONException {
                rvWorks = findViewById(R.id.activities);
                activity = Activity.DaywiseWork(result);
                final DayWorkDetails adapter = new DayWorkDetails(empid,activity,result,cDate);
                rvWorks.setAdapter(adapter);
                rvWorks.setLayoutManager(new LinearLayoutManager(DayWiseActivity.this));
                Button btn = (Button) findViewById(R.id.submission);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(DayWiseActivity.this);
                        View mView = LayoutInflater.from(DayWiseActivity.this).inflate(R.layout.submitdialog, null);
                        final EditText Rating = mView.findViewById(R.id.rating_work);
                        Button btn_cancel = mView.findViewById(R.id.btn_cancel);
                        Button btn_okay = mView.findViewById(R.id.btn_okay);
                        alert.setView(mView);
                        final AlertDialog alertDialog = alert.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        btn_okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String rating = Rating.getText().toString();
                                if (!(Integer.parseInt(rating) >= 0 && Integer.parseInt(rating) <= 100)) {
                                    Rating.requestFocus();
                                    Rating.setError("Rating should be in the range(0-100)");
                                }
                                else {
                                    int len = adapter.getList().size();
                                    int i = 0;
                                    String urrll = BASE_URL + "completework";
                                    HashMap<String, HashMap> twoD = new HashMap<String, HashMap>();
                                    for (StoreData ch : adapter.getList()) {
                                        HashMap<String, String> Data = new HashMap<String, String>();
                                        Data.put("empid", ch.getEmpID());
                                        Data.put("WorkID", ch.getWorkID());
                                        Data.put("time", ch.gettime());
                                        Data.put("dependancy", ch.getDependancy());
                                        Data.put("remark", ch.getWRemark());
                                        Data.put("isComplete", ch.getIsComplete());
                                        twoD.put(String.valueOf(i), Data);
                                        Log.d("Data", twoD.toString());
                                        i += 1;
                                    }
                                    HashMap<String, String> Data = new HashMap<String, String>();
                                    Data.put("Rating", rating);
                                    twoD.put("Rating", Data);


                                    RequestQueue queue = Volley.newRequestQueue(DayWiseActivity.this);
                                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, urrll, new JSONObject(twoD),
                                            new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        new VolleyCallback() {
                                                            @Override
                                                            public void onSuccessResponse(JSONObject result) throws JSONException {
                                                                Intent in = new Intent(DayWiseActivity.this, DateWise_Activity.class);
                                                                in.putExtra("ID", empid);
                                                                in.putExtra("pass", pass);
                                                                startActivity(in);
                                                                Toast.makeText(DayWiseActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }.onSuccessResponse(response);
                                                        Log.i("Response before", response.toString());

                                                        Log.i("Response parse", "onResponse: " + response.getJSONObject("0").getJSONObject("fields").getString("planneddate"));
                                                    } catch (Throwable t) {
                                                        Log.e("My App", "Could not parse malformed JSON: \"" + t + "\"");
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.i("WORKING", error.toString());
                                        }
                                    });
                                    queue.add(stringRequest);
                                    Log.d("Hashmap ", twoD.toString());
                                    alertDialog.dismiss();
                                }
                            }
                        });

                        alertDialog.show();
                    }
                });

            }
        });
    }
    void Assignedwork(String id,String date,final DayWiseActivity.VolleyCallback callback) {
        String url = BASE_URL+"dateActivity";
        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("userid",id);
        postParams.put("Date",date);
        RequestQueue queue = Volley.newRequestQueue(DayWiseActivity.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccessResponse(response);


                        } catch (Throwable t) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
            }
        });
        queue.add(stringRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addworks, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addwork:
                Intent myIntent = new Intent(DayWiseActivity.this,Assignment.class);
                myIntent.putExtra("ID",empid);
                myIntent.putExtra("pass",pass);
                startActivity(myIntent);
                break;

            case R.id.changepass:
                Intent intent = new Intent(DayWiseActivity.this,Authentication.class);
                intent.putExtra("id",empid);
                intent.putExtra("pass",pass);
                startActivity(intent);
                break;


            case R.id.refresh:
                Intent in = new Intent(DayWiseActivity.this,DayWiseActivity.class);
                in.putExtra("ID",empid);
                in.putExtra("date",cDate);
                in.putExtra("pass",pass);
                startActivity(in);
                break;
            // action with ID action_settings was selected
            case R.id.logout:

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DayWiseActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(DayWiseActivity.this,MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }

}
