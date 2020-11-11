package com.example.pta.wallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pta.R;
import com.example.pta.SaveUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryFragment extends Fragment {


    RecyclerView recyclerView;
    List<WithdrawClass> list;
    Button withdraw, deposit;
    SaveUserInfo saveUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_history, container, false);


        recyclerView = root.findViewById(R.id.withDrawHistoryRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        list = new ArrayList<>();

        saveUserInfo = new SaveUserInfo(getContext());

        withdraw = root.findViewById(R.id.withdrawHistory_id);
        deposit = root.findViewById(R.id.depositHistory_id);

        getDepositData(saveUserInfo.getId());
        withdraw.setTextColor(getResources().getColor(R.color.Azure));
        deposit.setTextColor(getResources().getColor(R.color.Chartreuse));

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWithdrawData(saveUserInfo.getId());
                withdraw.setTextColor(getResources().getColor(R.color.Chartreuse));
                deposit.setTextColor(getResources().getColor(R.color.Azure));
            }
        });

       deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDepositData(saveUserInfo.getId());
                withdraw.setTextColor(getResources().getColor(R.color.Azure));
                deposit.setTextColor(getResources().getColor(R.color.Chartreuse));
            }
        });

        return root;
    }



    public void getWithdrawData(final String userId) {
        String url = getString(R.string.BASS_URL) + "getWithdraw";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                list.clear();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            String name = dataobj.getString("userName");
                            String type = dataobj.getString("method");
                            String amount = dataobj.getString("amount");
                            String status = dataobj.getString("status");
                            String number = dataobj.getString("number");
                            WithdrawClass withdrawClass = new WithdrawClass();
                            withdrawClass.setUserName(name);
                            withdrawClass.setType(type);
                            withdrawClass.setAmount(amount);
                            withdrawClass.setStatus(status);
                            withdrawClass.setNumber(number);
                            list.add(withdrawClass);
                        }
                        WithdrawAdapter withdrawAdapter = new WithdrawAdapter(getContext(),list);
                        recyclerView.setAdapter(withdrawAdapter);
                        withdrawAdapter.notifyDataSetChanged();

                    } else {
                        //noDataAlert();
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
                Params.put("userId", userId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    public void getDepositData(final String userId) {
        String url = getString(R.string.BASS_URL) + "getDeposit";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                list.clear();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            String name = dataobj.getString("userName");
                            String type = dataobj.getString("method");
                            String amount = dataobj.getString("amount");
                            String status = dataobj.getString("status");
                            String number = dataobj.getString("number");
                            WithdrawClass withdrawClass = new WithdrawClass();
                            withdrawClass.setUserName(name);
                            withdrawClass.setType(type);
                            withdrawClass.setAmount(amount);
                            withdrawClass.setStatus(status);
                            withdrawClass.setNumber(number);
                            list.add(withdrawClass);
                        }
                        WithdrawAdapter withdrawAdapter = new WithdrawAdapter(getContext(),list);
                        recyclerView.setAdapter(withdrawAdapter);
                        withdrawAdapter.notifyDataSetChanged();

                    } else {
                        //noDataAlert();
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
                Params.put("userId", userId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

}