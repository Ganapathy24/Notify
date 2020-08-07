package e.sundaraganapathyl.kssolutions;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;

public class registeration extends AppCompatActivity  {
    String empid;
    String DOJ;
    private AwesomeValidation awesomeValidation;
    //You need to type ur IP address Here
    String BASE_URL = "http://"/*IP ADDRESS*/+"kathi/mobile/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        final TextView date;
        final EditText fName,lName,phone,email,address,district,state,education,experience;
        final Button submit;
        final ImageButton doj;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        fName = findViewById(R.id.fname);
        lName = findViewById(R.id.lname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        district = findViewById(R.id.district);
        state = findViewById(R.id.state);
        date = findViewById(R.id.date);
        education = findViewById(R.id.qualification);
        doj =  findViewById(R.id.doj);
        experience = findViewById(R.id.exp);
        submit = findViewById(R.id.register);
        empid = getIntent().getStringExtra("ID");


        doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(registeration.this);
                View mView = LayoutInflater.from(registeration.this).inflate(R.layout.datepicker, null);
                final DatePicker datePicker = mView.findViewById(R.id.picker);
                Button submit = mView.findViewById(R.id.submit);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                DOJ = ""+datePicker.getYear() + "-" + (datePicker.getMonth()+1)+"-" + datePicker.getDayOfMonth();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DOJ = ""+datePicker.getYear() + "-" + (datePicker.getMonth()+1)+"-" + datePicker.getDayOfMonth();
                        date.setText(DOJ);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.phone, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    onResume(fName.getText().toString(), lName.getText().toString(), phone.getText().toString(), email.getText().toString(), address.getText().toString(), district.getText().toString(), state.getText().toString(), education.getText().toString(), DOJ, experience.getText().toString());
                }
            }
        });
    }


    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result) throws JSONException;
    }

    public void onResume(String fName,String lName,String phone,String email,String address,String district,String state,String education,String doj,String experience){
        super.onResume();
        try {
            EmployeeDetails(fName,lName,phone,email,address,district,state,education,doj,experience,new VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) throws JSONException {
                    Toast.makeText(registeration.this,"ADDED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                    Intent myIntent  =new Intent(registeration.this,DateWise_Activity.class);
                    myIntent.putExtra("ID", empid);
                    startActivity(myIntent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void EmployeeDetails(String fName,String lName,String phone,String email,String address,String district,String state,String education,String doj,String experience,final VolleyCallback callback) throws JSONException {
        String url = BASE_URL+"employeedetail";
        final HashMap<String, String> postParams = new HashMap<String, String>();
        Log.i("EMMM",empid);
        postParams.put("empid",empid);
        postParams.put("fname",fName);
        postParams.put("lname", lName);
        postParams.put("phone", phone);
        postParams.put("email",email);
        postParams.put("address",address);
        postParams.put("district",district);
        postParams.put("state",state);
        postParams.put("education",education);
        postParams.put("DOJ",doj);
        postParams.put("experience",experience);
        Log.d("DATA", String.valueOf(postParams));
        RequestQueue queue = Volley.newRequestQueue(registeration.this);
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
