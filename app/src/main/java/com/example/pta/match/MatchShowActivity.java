package com.example.pta.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pta.R;
import com.example.pta.result.ResultAdapter;
import com.example.pta.result.ResultClass;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MatchShowActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    RecyclerView recyclerView;
    List<MatchClass> matchClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_show);

        Toolbar toolbar = findViewById(R.id.matchShowToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.matchShowRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        matchClassList = new ArrayList<>();
        Bundle bundle =getIntent().getExtras();
        if (bundle != null){
            String category = bundle.getString("category");
            setTitle(category);
            getMatchData(category);
        }

    }

    public void getMatchData(final String category) {
        String url = getString(R.string.BASS_URL) + "getMatch";
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

                            int id = dataobj.getInt("id");
                            String rId = String.valueOf(id);
                            String matchName = dataobj.getString("name");
                            String category = dataobj.getString("category");
                            String candidateNo = dataobj.getString("candidateNo");
                            String date_time = dataobj.getString("date_time");
                            String total_time = dataobj.getString("total_time");
                            String entryFee = dataobj.getString("enteryFee");
                            String type = dataobj.getString("type");
                            String syllabus = dataobj.getString("syllabus");
                            String routine = dataobj.getString("routine");
                            String winnerPrice = dataobj.getString("winnerPrize");


                            MatchClass matchClass = new MatchClass(rId,matchName,category,candidateNo,date_time,
                                    total_time,entryFee,type,routine,syllabus,winnerPrice);
                            matchClassList.add(matchClass);
                        }

                        MatchAdapter matchAdapter = new MatchAdapter(MatchShowActivity.this,matchClassList);
                        recyclerView.setAdapter(matchAdapter);
                        matchAdapter.notifyDataSetChanged();

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
                Params.put("category", category);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(MatchShowActivity.this));
        queue.add(stringRequest);
    }



}