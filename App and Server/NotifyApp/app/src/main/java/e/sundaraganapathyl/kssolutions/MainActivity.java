package e.sundaraganapathyl.kssolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    //You need to type ur IP address Here
    String BASE_URL = "http://"/*IP ADDRESS*/+"kathi/mobile/";

    public interface VolleyCallback{
        void onSuccessResponse(JSONObject result) throws JSONException;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = username.getText().toString();
                String pass = password.getText().toString();
                onResume(user_id,pass);
            }
        });
    }

    private void onResume(final String user_id, final String pass) {
        super.onResume();
        loginAPI(user_id, pass, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) throws JSONException {

                String Result = result.getString("error");
                if (Result.equals("None")) {
                    if (result.getString("status").equals("2")) {
                        Intent myIntent = new Intent(MainActivity.this,registeration.class);
                        myIntent.putExtra("ID", user_id);
                        startActivity(myIntent);
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent myIntent = new Intent(MainActivity.this,DateWise_Activity.class);
                        myIntent.putExtra("ID", user_id);
                        myIntent.putExtra("pass",pass);
                        startActivity(myIntent);
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, Result, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginAPI(String user_id, String pass, final VolleyCallback callback) {
        String url = BASE_URL+"loginapi";
        final HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("userid", user_id);
        postParams.put("password", pass);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
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
