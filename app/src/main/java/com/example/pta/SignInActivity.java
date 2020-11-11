package com.example.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.idp.SingleSignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        if (saveUserInfo.checkUser()){
            logoutAlert2();
        }
    }
    @Override
    public void onBackPressed() {
    }

    private static final int RC_SIGN_IN = 100;
    TextInputLayout username, password;
    List<AuthUI.IdpConfig> providers;
    Button login;

    ProgressBar progressBar;
    SaveUserInfo saveUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);


                providers = Arrays.asList(
                        new AuthUI.IdpConfig.PhoneBuilder().build());

                login = findViewById(R.id.login_id);
                progressBar = findViewById(R.id.signProgressBar);
                progressBar.setVisibility(View.GONE);
         saveUserInfo = new SaveUserInfo(SignInActivity.this);

    }

    public void singUp(View view) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                login.setVisibility(View.GONE);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String phoneNumber = null;
                if (user != null) {
                    phoneNumber = user.getPhoneNumber();
                }
                if (phoneNumber != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    phoneNumber = phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
                    checkUser(phoneNumber);
                }
                }else {
                    logoutAlert();
                }
            } else {
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show();

            }
        }


    public void checkUser(final String number) {
        String url = getString(R.string.BASS_URL) + "testUser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                      getProfile(number,"hello");

                    } else {
                        SaveUserInfo saveUserInfo = new SaveUserInfo(SignInActivity.this);
                        saveUserInfo.numberStore(number);
                       Intent intent = new Intent(SignInActivity.this,ProfileSetUpActivity.class);
                       intent.putExtra("status","new");
                       startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("number", number);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        queue.add(stringRequest);
    }

    private void logoutAlert() {


        AuthUI.getInstance()
                .signOut(SignInActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                        login.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, SignInActivity.class));

                    }
                });

    }


    private void logoutAlert2() {


        AuthUI.getInstance()
                .signOut(SignInActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        login.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                });

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
                            SaveUserInfo saveUserInfo = new SaveUserInfo(SignInActivity.this);
                            saveUserInfo.dataStore(userId,userName,emailAddress,referCode,number,status);

                            if (saveUserInfo.getId() != null){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }else {

                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.GONE);
                                logoutAlert();
                            }
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        logoutAlert();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    logoutAlert();
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                logoutAlert();
                progressBar.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);

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
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        queue.add(stringRequest);
    }
}
