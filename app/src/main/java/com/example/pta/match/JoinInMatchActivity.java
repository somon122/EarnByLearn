package com.example.pta.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pta.MainActivity;
import com.example.pta.R;
import com.example.pta.SaveUserInfo;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class JoinInMatchActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    String matchId,category,start_date_time,end_date_time,entryFee,matchName, spotsLeft, jointStatus, type;
    int balance;
    int cost = 0;
    TextView matchNameTV, entryFeeTV,balanceTV,spotsLeftTV, balanceStatus;
    SaveUserInfo saveUserInfo;

    ProgressBar progressBar;
    Button userJoinSubmitBtn;
    String userNumber;
    String currentDateAndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_in_match);

        Toolbar toolbar = findViewById(R.id.joinInMatchToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveUserInfo = new SaveUserInfo(this);
        userNumber = saveUserInfo.getNumber();
        progressBar= findViewById(R.id.joinProgressBar);
        progressBar.setVisibility(View.GONE);
        userJoinSubmitBtn = findViewById(R.id.userJoinSubmitBtn);

        matchNameTV = findViewById(R.id.joinMatchName);
        entryFeeTV = findViewById(R.id.joinMatchEntryFee);
        balanceTV = findViewById(R.id.joinBalanceStatus);
        balanceStatus = findViewById(R.id.balanceStatus);
        spotsLeftTV = findViewById(R.id.joinMatchSportLeft);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
        currentDateAndTime = sdf.format(new Date());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            matchId = bundle.getString("matchId");
            matchName = bundle.getString("matchName");
            type = bundle.getString("type");
            spotsLeft = bundle.getString("spotsLeft");
            entryFee = bundle.getString("entryFee");
            category = bundle.getString("category");
            start_date_time = bundle.getString("start_date_time");
            end_date_time = bundle.getString("end_date_time");
            entryFeeTV.setText("Entry fee: "+entryFee+"TK");
            matchNameTV.setText(matchName);
            getUserBalanceData(saveUserInfo.getNumber());
            cost = Integer.parseInt(entryFee);
        }
    }


    public void joinSubmit(View view) {
        confirmAlert();
    }


    private void confirmAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(JoinInMatchActivity.this);
        builder.setTitle("Confirm Alert")
                .setMessage("Are you 100% Sure to Join?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (userNumber != null){
                            join();
                        }else {
                            Toast.makeText(JoinInMatchActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void join() {

             if (balance >= cost){
                 int lastBalance = balance-cost;
                 if (cost <=0){
                     progressBar.setVisibility(View.VISIBLE);
                     userJoinSubmitBtn.setEnabled(false);
                     submitJoinData( userNumber);
                 }else {
                     progressBar.setVisibility(View.VISIBLE);
                     userJoinSubmitBtn.setEnabled(false);
                     joinFee( String.valueOf(lastBalance), userNumber);
                 }

             }else {
                 Toast.makeText(this, "You have not enough Balance", Toast.LENGTH_SHORT).show();
             }
         }


    public void getUserBalanceData(final String number) {
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
                            String totalBalance = dataobj.getString("totalBalance");
                            balanceTV.setText("Your balance available: "+totalBalance+"TK");
                            balance = Integer.parseInt(totalBalance);
                            if (Integer.parseInt(entryFee) > balance){
                                balanceStatus.setVisibility(View.VISIBLE);
                            }

                        }

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
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(JoinInMatchActivity.this);
        queue.add(stringRequest);
    }


    public void joinFee (final String lastBalance,final String number) {
        String url = getString(R.string.BASS_URL) + "joinFee";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                        submitJoinData(number);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(JoinInMatchActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinInMatchActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(JoinInMatchActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("number", number);
                Params.put("totalBalance", lastBalance);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(JoinInMatchActivity.this);
        queue.add(stringRequest);
    }


    public void submitJoinData (final String number) {
        String url = getString(R.string.BASS_URL) + "joinUser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                            Toasty.success(JoinInMatchActivity.this, "Join is successfully", Toast.LENGTH_SHORT, true).show();
                            startActivity(new Intent(JoinInMatchActivity.this, MainActivity.class));
                            finish();
                    } else {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(JoinInMatchActivity.this, "Sorry Net Problem.", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinInMatchActivity.this, "Sorry Net Problem.", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(JoinInMatchActivity.this, "Sorry Net Problem.", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("matchId", matchId);
                Params.put("userId", saveUserInfo.getId());
                Params.put("playerName", saveUserInfo.getUserName());
                Params.put("number", number);
                Params.put("date_time", currentDateAndTime);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(JoinInMatchActivity.this);
        queue.add(stringRequest);
    }

}