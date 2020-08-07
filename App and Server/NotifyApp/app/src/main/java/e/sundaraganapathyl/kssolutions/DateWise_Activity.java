package e.sundaraganapathyl.kssolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DateWise_Activity extends AppCompatActivity {

    ArrayList<Works> works;
    //You need to type ur IP address Here
    String BASE_URL = "http://"/*IP ADDRESS*/+"kathi/mobile/";
    String empid,pass;
    RecyclerView rvWorks;
    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result) throws JSONException;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empid = getIntent().getStringExtra("ID");
        pass= getIntent().getStringExtra("pass");
        setContentView(R.layout.activity_date_wise_);
        onResume(empid);
    }
    public  void onResume(final String empid) {
        super.onResume();
        Assignedwork(empid,new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) throws JSONException {
                rvWorks = findViewById(R.id.works);
                works = Works.DatewiseWork(result);
                WorksDetail adapter = new WorksDetail(works,result,empid,pass);
                rvWorks.setAdapter(adapter);
                rvWorks.setLayoutManager(new LinearLayoutManager(DateWise_Activity.this));

            }
        });
    }
    void Assignedwork(String id,final DateWise_Activity.VolleyCallback callback) {
        String url = BASE_URL+"activity";
        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("userid",id);
        JSONObject data= new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(DateWise_Activity.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccessResponse(response);
                            Log.i("AssignedWork before", response.toString());

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
                Intent myIntent = new Intent(DateWise_Activity.this,Assignment.class);
                myIntent.putExtra("ID",empid);
                myIntent.putExtra("pass",pass);
                startActivity(myIntent);
                break;

            case R.id.refresh:
                Intent in = new Intent(DateWise_Activity.this,DateWise_Activity.class);
                in.putExtra("ID", empid);
                in.putExtra("pass",pass);
                startActivity(in);
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;

            case R.id.changepass:
                Intent intent = new Intent(DateWise_Activity.this,Authentication.class);
                intent.putExtra("id",empid);
                intent.putExtra("pass",pass);
                startActivity(intent);
                break;
            // action with ID action_settings was selected
            case R.id.logout:

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DateWise_Activity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(DateWise_Activity.this,MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }
}
