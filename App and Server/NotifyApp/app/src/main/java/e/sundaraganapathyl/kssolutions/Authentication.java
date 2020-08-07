package e.sundaraganapathyl.kssolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
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

public class Authentication extends AppCompatActivity {

    //You need to type ur IP address Here
    String BASE_URL = "http://"/*IP ADDRESS*/+"kathi/mobile/";
    String empid,pass;
    public interface VolleyCallback{
        void onSuccessResponse(JSONObject result) throws JSONException;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        pass = getIntent().getStringExtra("pass");
        Log.i("pass",pass);
        empid = getIntent().getStringExtra("id");
        final Button submit = (Button) findViewById(R.id.summit);
        final EditText old = (EditText) findViewById(R.id.oldpass);
        final EditText new1 = (EditText) findViewById(R.id.newpass1);
        final EditText new2 = (EditText) findViewById(R.id.newpass2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("oldpass",old.getText().toString());
                if(old.getText().toString().equals(pass))
                {
                    if(new1.getText().toString().equals(new2.getText().toString())){
                        onResume(new1.getText().toString());
                    }
                    else{
                        new2.requestFocus();
                        new2.setError("New password and Confirm New password should be same");
                        //Toast.makeText(Authentication.this,"New password and confirm new password should be same",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    old.requestFocus();
                    old.setError("Old password is mismatched");
                }
            }
        });
    }
    public  void onResume(final String newPass) {
        super.onResume();
        Authenticate(empid,newPass,new Authentication.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) throws JSONException {
                Toast.makeText(Authentication.this, "Successfully changed", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(Authentication.this,DateWise_Activity.class);
                in.putExtra("ID",empid);
                in.putExtra("pass",newPass);
                startActivity(in);
            }
        });
    }
    private void Authenticate(String user_id,String newpass, final Authentication.VolleyCallback callback) {
        String url = BASE_URL+"changepass";
        final HashMap<String, String> postParams = new HashMap<String, String>();
        Log.d("empid",empid);
        postParams.put("empid", user_id);
        postParams.put("password", newpass);
        RequestQueue queue = Volley.newRequestQueue(Authentication.this);
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
