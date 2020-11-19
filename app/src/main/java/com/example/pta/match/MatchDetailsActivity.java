package com.example.pta.match;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codesgood.views.JustifiedTextView;
import com.example.pta.R;
import com.example.pta.RulesClass;
import com.example.pta.SaveUserInfo;
import com.example.pta.liveExam.ExamActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class MatchDetailsActivity extends AppCompatActivity {

    String matchName, matchId,category,total_time,start_date_time, end_date_time, entryFee,
            type,routine,syllabus, jointStatus;
    JustifiedTextView instruction1;
    RecyclerView recyclerView;
    List<Object>participatedList;
    Button jointButton, participateShowButton, syllabusOpenBtn;
    TextView matchNameTV,dateTV,totalPrizeTV,total_timeTV,entryFeeTV,  typeTV, routineTV;
    ImageView imageView;

    SaveUserInfo saveUserInfo;
    public static final int ITEM_PER_AD = 4;
    private static final String BANNER_ID = "ca-app-pub-3940256099942544/6300978111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        initBannerAds();

        instruction1 = findViewById(R.id.instruction1);
        instruction1.setText(RulesClass.rules1+"\n\n"+ RulesClass.rules2+"\n\n"+ RulesClass.rules3);

        imageView = findViewById(R.id.matchDetailsImage);
        matchNameTV = findViewById(R.id.matchDetailsMatchName);
        dateTV = findViewById(R.id.matchDetailsDate);
        totalPrizeTV = findViewById(R.id.matchDetailsTotalPrize);
        total_timeTV = findViewById(R.id.matchDetailsPerKill);
        entryFeeTV = findViewById(R.id.matchDetailsEntryFee);
        typeTV = findViewById(R.id.matchDetailsType);
        routineTV = findViewById(R.id.matchDetailsRoutine);
        syllabusOpenBtn = findViewById(R.id.matchDetailsSyllabus);
        saveUserInfo = new SaveUserInfo(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            matchName = bundle.getString("name");
            matchId = bundle.getString("matchId");
            jointStatus = bundle.getString("jointStatus");
            category = bundle.getString("category");
            start_date_time = bundle.getString("start_date_time");
            end_date_time = bundle.getString("end_date_time");
            total_time = bundle.getString("total_time");
            entryFee = bundle.getString("entryFee");
            type = bundle.getString("type");
            syllabus = bundle.getString("syllabus");
            matchNameTV.setText(matchName);
            dateTV.setText("Match Schedule on "+start_date_time);

            total_timeTV.setText(total_time+" Minute");
            entryFeeTV.setText(entryFee+" TK");
            typeTV.setText(type);
            routineTV.setText(routine);
            imageShow(type);
           getRoomIdData(matchId,saveUserInfo.getId());

        }

        syllabusOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = "https://game.earnbylearn.club/public/files/pdf_file/"+syllabus;

                if (syllabus.contains(".")){
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
                    }
                }else {
                    Toasty.error(MatchDetailsActivity.this,"Empty File",Toasty.LENGTH_SHORT).show();
                }

            }
        });

        participatedList = new ArrayList<>();

        getBannerAd();
        loadBannerAds();

        recyclerView = findViewById(R.id.participatedRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setVisibility(View.GONE);
        jointButton = findViewById(R.id.matchDetailsJointNow);
        participateShowButton = findViewById(R.id.participatedListShowBtn);

        if (jointStatus.equals("JOINED")){
            jointButton.setText("JOINED");
        }

        jointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (jointStatus.equals("JOINED")){
                    Toast.makeText(MatchDetailsActivity.this, "You are already Joined", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), JoinInMatchActivity.class);
                    intent.putExtra("matchId",matchId);
                    intent.putExtra("matchName",matchName);
                    intent.putExtra("type",type);
                    intent.putExtra("spotsLeft","");
                    intent.putExtra("entryFee",entryFee);
                    intent.putExtra("category",category);
                    intent.putExtra("start_date_time",start_date_time);
                    intent.putExtra("end_date_time",end_date_time);
                    startActivity(intent);
                }

            }
        });

        participateShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                participateShowButton.setEnabled(false);
                recyclerView.setVisibility(View.VISIBLE);
                getParticipatedData(matchId);

            }
        });

    }

    private void getBannerAd(){
        for (int i = ITEM_PER_AD; i <= participatedList.size(); i+=ITEM_PER_AD) {
            final AdView adView = new AdView(MatchDetailsActivity.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(BANNER_ID);
            participatedList.add(i,adView);
        }
    }
    private void loadBannerAds(){

        loadBannerAd(ITEM_PER_AD);

    }

    private void loadBannerAd(final int index){

        if (ITEM_PER_AD >=participatedList.size()){
            return;
        }
        Object items = participatedList.get(index);

        if (items instanceof AdView){
           throw new ClassCastException("error 123");
        }

        AdView adView = (AdView) items;
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                loadBannerAd(index+ITEM_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                loadBannerAd(index+ITEM_PER_AD);
            }
        });
        adView.loadAd(new AdRequest.Builder().build());


    }

    @Override
    protected void onResume() {
        for(Object items: participatedList){
            if(items instanceof AdView){
                AdView adView = (AdView) items;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        for(Object items: participatedList){
            if(items instanceof AdView){
                AdView adView = (AdView) items;
                adView.pause();
            }
        }
        super.onPause();
    }


    @Override
    protected void onDestroy() {

        for(Object items: participatedList){
            if(items instanceof AdView){
                AdView adView = (AdView) items;
                adView.destroy();
            }
        }

        super.onDestroy();
    }

    private void initBannerAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {



            }
        });
    }

    private void imageShow(String type) {
        if (type.equals("Weekly_Match")){
            Picasso.get().load("https://i.ytimg.com/vi/6b4hSESA33A/maxresdefault.jpg").fit().into(imageView);
        }else if (type.equals("Daily_Match")){
            Picasso.get().load("https://data-flair.training/blogs/wp-content/uploads/sites/2/2018/10/QlikView-Quiz-Questions-3.jpg").fit().into(imageView);

        }
    }

    public void getParticipatedData(final String matchId) {
        String url = getString(R.string.BASS_URL) + "getUserTotalJoint";
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
                            String name = dataobj.getString("playerName");
                            participatedList.add(new ParticipateClass(serialNo,name));
                        }

                        ParticipateAdapter participateAdapter = new ParticipateAdapter(MatchDetailsActivity.this,participatedList);
                        recyclerView.setAdapter(participateAdapter);
                        participateAdapter.notifyDataSetChanged();

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
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(MatchDetailsActivity.this));
        queue.add(stringRequest);
    }


    public void getRoomIdData(final String matchId , final String userId) {
        String url = getString(R.string.BASS_URL) + "getRoomId";
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
                            String status = dataobj.getString("idStatus");
                            String matchName = dataobj.getString("matchName");
                            if (status.equals("Open")){
                               idCopyAlert(matchName);
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
                Params.put("matchId", matchId);
                Params.put("userId", userId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(MatchDetailsActivity.this));
        queue.add(stringRequest);
    }


    private void idCopyAlert(final String mName ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchDetailsActivity.this);
        View view1 = LayoutInflater.from(MatchDetailsActivity.this).inflate(R.layout.room_id_model,null);

        TextView matchNameTV = view1.findViewById(R.id.roomIdMatchName);
        Button enterExam = view1.findViewById(R.id.modelEnterExam);
        matchNameTV.setText(mName);
        enterExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MatchDetailsActivity.this, ExamActivity.class);
                intent.putExtra("matchId",matchId);
                intent.putExtra("type",type);
                intent.putExtra("total_time",total_time);
                intent.putExtra("start_date_time",start_date_time);
                intent.putExtra("end_date_time",end_date_time);
                startActivity(intent);

            }
        });


        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.show();


    }


}