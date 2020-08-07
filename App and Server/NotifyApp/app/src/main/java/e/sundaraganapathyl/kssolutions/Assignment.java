package e.sundaraganapathyl.kssolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

import java.util.HashMap;

public class Assignment extends AppCompatActivity {
    //You need to type ur IP address Here
    String BASE_URL = "http://"/*IP ADDRESS*/+"kathi/mobile/";
    String empid,pass;
    String CDate;

    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result) throws JSONException;
    }

    public void onResume(String workID,String name, String plannedTime,String remarks){
        super.onResume();
        try {
            Assignment(workID,name,plannedTime,remarks,new VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) throws JSONException {
                    Toast.makeText(Assignment.this,"ADDED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                    Intent myIntent  =new Intent(Assignment.this,DateWise_Activity.class);
                    myIntent.putExtra("ID", empid);
                    myIntent.putExtra("pass",pass);
                    startActivity(myIntent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        final EditText name,plannedTime,remarks,work_id;
        empid = getIntent().getStringExtra("ID");
        pass= getIntent().getStringExtra("pass");
        Button submit;
        work_id = findViewById(R.id.workid);
        name = findViewById(R.id.newWork);
        plannedTime = findViewById(R.id.newTime);
        remarks = findViewById(R.id.newRemarks);
        submit = findViewById(R.id.Assign);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume(work_id.getText().toString(),name.getText().toString(),plannedTime.getText().toString(),remarks.getText().toString());

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.refresh:
                Intent intent = new Intent(Assignment.this,Assignment.class);
                startActivity(intent);
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.logout:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Assignment.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent myIntent = new Intent(Assignment.this,MainActivity.class);
                startActivity(myIntent);
                break;
            default:
                break;
        }

        return true;
    }

    void Assignment(String workID,String name,String PlannedTime,String remarks,final VolleyCallback callback) throws JSONException {
        String url = BASE_URL+"creatework";
        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("empid",empid);
        postParams.put("workid", workID);
        postParams.put("name", name);
        postParams.put("plannedtime",PlannedTime);
        postParams.put("remarks",remarks);
        RequestQueue queue = Volley.newRequestQueue(Assignment.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccessResponse(response);
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + t + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
                Log.i("WORKING", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}
