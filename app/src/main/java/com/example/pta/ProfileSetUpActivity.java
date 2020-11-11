package com.example.pta;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pta.wallet.WithdrawAdapter;
import com.example.pta.wallet.WithdrawClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileSetUpActivity extends AppCompatActivity {

    TextInputLayout nameET,emailET,numberET,referCodeET;
    Button submitBtn;
    CheckBox checkBox;
    SaveUserInfo saveUserInfo;
    String update = null;
    ProgressBar progressBar;
    String currentDateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);


        saveUserInfo= new SaveUserInfo(this);
        submitBtn = findViewById(R.id.profileSubmit_id);
        checkBox = findViewById(R.id.profileCheckBox);
        progressBar= findViewById(R.id.profileSubmitProgressBar);
        progressBar.setVisibility(View.GONE);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
        currentDateAndTime = sdf.format(new Date());

        nameET = findViewById(R.id.profileUserName);
        emailET = findViewById(R.id.profileEmailAddress);
        numberET = findViewById(R.id.profileNumber);
        referCodeET = findViewById(R.id.profileReferCode);
        numberET.getEditText().setText(saveUserInfo.getNumber());
        numberET.setEnabled(false);
        referCodeET.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            update = bundle.getString("status");
            if (update.equals("update")){
                nameET.getEditText().setText(saveUserInfo.getUserName());
                numberET.getEditText().setText(saveUserInfo.getNumber());
                emailET.getEditText().setText(saveUserInfo.getEmail());
                submitBtn.setText("Update");

            }


        }else {
            numberET.getEditText().setText(saveUserInfo.getNumber());
            numberET.setEnabled(true);
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    SubmitProfile();
                }else {

                    Toast.makeText(ProfileSetUpActivity.this, "Please complete check ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SubmitProfile() {

        String name = nameET.getEditText().getText().toString();
        String emailAdd = emailET.getEditText().getText().toString();
        String phone = numberET.getEditText().getText().toString();
        String refer = referCodeET.getEditText().getText().toString().trim();


        if (name.isEmpty()){
            nameET.getEditText().setError("Please Enter Name");
        }else if (emailAdd.isEmpty()){
            emailET.setError("Please Enter Email Address");
        }else if (phone.isEmpty()){
            numberET.setError("Please Enter Number");
        }else {

            if (update.equals("update")){

                progressBar.setVisibility(View.VISIBLE);
                submitBtn.setEnabled(false);
                updateProfile(saveUserInfo.getId(),name,emailAdd,phone);

            }else {
                progressBar.setVisibility(View.VISIBLE);
                checkUser(name,emailAdd,phone,refer,currentDateAndTime);
            }

        }
    }


    private void checkUser(final String userName,final String emailAddress,final String number,final String referCode,final String date_time) {


        String url = getString(R.string.BASS_URL) + "checkUserProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equals("welcome")) {

                        submitBtn.setEnabled(false);
                       insertProfile(userName,emailAddress,number,referCode,date_time);

                    }else if (obj.getString("status").equals("numberAlreadyExits")){

                        Toast.makeText(ProfileSetUpActivity.this, "numberAlreadyExits", Toast.LENGTH_SHORT).show();

                    }
                    else if (obj.getString("status").equals("referCodeIsNotExits")){

                        Toast.makeText(ProfileSetUpActivity.this, "referCode is not Exits", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(ProfileSetUpActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();



                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("number", number);
                Params.put("referCode", referCode);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProfileSetUpActivity.this);
        queue.add(stringRequest);
    }
   private void insertProfile(final String userName,final String emailAddress,final String number,final String referCode,final String date_time) {


        String url = getString(R.string.BASS_URL) + "insertUserProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                        getProfile(number,emailAddress);

                    } else {
                        Toast.makeText(ProfileSetUpActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("userName", userName);
                Params.put("number", number);
                Params.put("emailAddress", emailAddress);
                Params.put("referCode", referCode);
                Params.put("date_time", date_time);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProfileSetUpActivity.this);
        queue.add(stringRequest);
    }



    public void getProfile(final String number, final String email) {
        String url = getString(R.string.BASS_URL) + "getUserProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);

                            String userId = dataobj.getString("id");
                            String userName = dataobj.getString("userName");
                            String emailAddress = dataobj.getString("emailAddress");
                            String number = dataobj.getString("number");
                            String status = dataobj.getString("conditionStatus");
                            String referCode = dataobj.getString("referCode");
                            SaveUserInfo saveUserInfo = new SaveUserInfo(ProfileSetUpActivity.this);
                            saveUserInfo.dataStore(userId,userName,emailAddress,referCode,number,status);

                            if (saveUserInfo.getId() != null){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("number", number);
                Params.put("emailAddress", email);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProfileSetUpActivity.this);
        queue.add(stringRequest);
    }


    private void updateProfile(final String userId, final String userName,final String emailAddress,final String number) {


        String url = getString(R.string.BASS_URL) + "updateProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("userId", userId);
                Params.put("userName", userName);
                Params.put("number", number);
                Params.put("emailAddress", emailAddress);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProfileSetUpActivity.this);
        queue.add(stringRequest);
    }
}
