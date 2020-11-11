package com.example.pta.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResultDetailsActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    String matchName, matchId,category,date_time,total_time,entryFee,
            type,version,map,winnerPrice;

    TextView matchNameTV,dateTV,totalPrizeTV,perKillTV,entryFeeTV;

    RecyclerView recyclerView;
    List<ResultDetailsClass>resultClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        Toolbar toolbar = findViewById(R.id.resultDetailsToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Results Details");

        matchNameTV = findViewById(R.id.resultsDetailsMatchName);
        dateTV = findViewById(R.id.resultsDetailsDate);
        totalPrizeTV = findViewById(R.id.resultDetailsTotalPrize);
        perKillTV = findViewById(R.id.resultDetailsPerKill);
        entryFeeTV = findViewById(R.id.resultDetailsEntryFee);

        recyclerView = findViewById(R.id.resultsFullRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        resultClassList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            matchName = bundle.getString("name");
            matchId = bundle.getString("matchId");
            category = bundle.getString("category");
            date_time = bundle.getString("date_time");
            winnerPrice = bundle.getString("winner_prize");
            total_time = bundle.getString("total_time");
            entryFee = bundle.getString("entryFee");
            type = bundle.getString("type");
            version = bundle.getString("routine");
            map = bundle.getString("syllabus");


          matchNameTV.setText(matchName);
          dateTV.setText("organize on "+date_time);
          totalPrizeTV.setText(winnerPrice+" TK");
          perKillTV.setText(total_time+" minute");
          entryFeeTV.setText(entryFee+"TK");

          getResultData(matchId);

        }


    }

    public void getResultData(final String matchId) {
        String url = getString(R.string.BASS_URL) + "getResultSummary";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            int serialNo = i+1;
                            String name = dataobj.getString("name");
                            String time = dataobj.getString("time");
                            String number = dataobj.getString("number");
                            String prizeMoney = dataobj.getString("prizeMoney");

                            ResultDetailsClass resultDetailsClass = new ResultDetailsClass(serialNo,name,number,time,prizeMoney);
                            resultClassList.add(resultDetailsClass);
                        }

                        ResultsDetailsAdapter resultsDetailsAdapter = new ResultsDetailsAdapter(ResultDetailsActivity.this,resultClassList);
                        recyclerView.setAdapter(resultsDetailsAdapter);
                        resultsDetailsAdapter.notifyDataSetChanged();

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
                Params.put("matchId", matchId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(ResultDetailsActivity.this));
        queue.add(stringRequest);
    }





}