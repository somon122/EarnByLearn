package com.example.pta.topPlayerList;

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
import com.example.pta.wallet.WithdrawAdapter;
import com.example.pta.wallet.WithdrawClass;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopPlayerListFragment extends Fragment {

    RecyclerView recyclerView;
    List<TopPlayerClass> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_top_player_list, container, false);

        recyclerView = root.findViewById(R.id.topPlayerRecyclerView);

        ShimmerFrameLayout container3 =
                (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_container3);
        container3.startShimmer(); // If auto-start is set to false

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        list = new ArrayList<>();
        getUserProfileData();


        return root;
    }

    public void getUserProfileData() {
        String url = getString(R.string.BASS_URL) + "getTopUser";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            int no = i+1;
                            String userName = dataobj.getString("userName");
                            String winMatch = dataobj.getString("totalWin");
                            TopPlayerClass topPlayerClass = new TopPlayerClass();
                            topPlayerClass.setSerialNo(String.valueOf(no));
                            topPlayerClass.setUserName("Name: "+userName);
                            topPlayerClass.setTotalWin("Win Match: "+winMatch);
                            list.add(topPlayerClass);
                        }

                        TopPlayerAdapter topPlayerAdapter = new TopPlayerAdapter(getContext(),list);
                        recyclerView.setAdapter(topPlayerAdapter);
                        topPlayerAdapter.notifyDataSetChanged();

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }

        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }



}