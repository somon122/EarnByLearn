package com.example.pta.result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pta.topPlayerList.TopPlayerAdapter;
import com.example.pta.topPlayerList.TopPlayerClass;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResultFragment extends Fragment {


    RecyclerView recyclerView;
    List<ResultClass> resultClassList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_result, container, false);

        recyclerView = root.findViewById(R.id.resultRecyclerView);

        ShimmerFrameLayout container3 =
                (ShimmerFrameLayout) root.findViewById(R.id.result_shimmer_view_container);
        container3.startShimmer(); // If auto-start is set to false

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        resultClassList = new ArrayList<>();
        getResultData();

        return root;
    }

    public void getResultData() {
        String url = getString(R.string.BASS_URL) + "getResult";
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
                            String matchId = dataobj.getString("matchId");
                            String category = dataobj.getString("category");
                            String date_time = dataobj.getString("date_time");
                            String winnerPrize = dataobj.getString("winnerPrize");
                            String total_time = dataobj.getString("total_time");
                            String entryFee = dataobj.getString("enteryFee");
                            String type = dataobj.getString("type");
                            String routine = dataobj.getString("routine");
                            String syllabus = dataobj.getString("syllabus");

                            ResultClass resultClass = new ResultClass(rId,matchName,matchId,category,date_time,winnerPrize,total_time,
                                    entryFee,type,routine,syllabus,winnerPrize);
                            resultClassList.add(resultClass);
                        }

                        ResultAdapter resultAdapter = new ResultAdapter(getContext(),resultClassList);
                        recyclerView.setAdapter(resultAdapter);
                        resultAdapter.notifyDataSetChanged();

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

        });
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        queue.add(stringRequest);
    }



}