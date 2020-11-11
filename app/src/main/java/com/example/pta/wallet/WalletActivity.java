package com.example.pta.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
import com.example.pta.SaveUserInfo;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    SaveUserInfo saveUserInfo;
    TextView balance,depositBalance;
    UserWalletInfo userWalletInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Toolbar toolbar = findViewById(R.id.walletToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Wallet");


        userWalletInfo = new UserWalletInfo(this);
        balance = findViewById(R.id.walletWinAmount);
        depositBalance = findViewById(R.id.walletDepositAmount);
        saveUserInfo = new SaveUserInfo(this);
        getUserProfileData(saveUserInfo.getNumber());


        tabLayout = findViewById(R.id.walletTabLayout);
        viewPager = findViewById(R.id.walletViewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Deposit"));
        tabLayout.addTab(tabLayout.newTab().setText("Withdraw"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        WalletAdapter walletAdapter = new WalletAdapter(WalletActivity.this.getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(walletAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void getUserProfileData(final String number) {
        String url = getString(R.string.BASS_URL) + "getUserProfile";
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
                            String depositAmount = dataobj.getString("depositAmount");
                            String winAmount = dataobj.getString("winAmount");
                            String totalBalance = dataobj.getString("totalBalance");
                            balance.setText(winAmount+"TK");
                            depositBalance.setText(depositAmount+"TK");
                            userWalletInfo.setBalance(winAmount);
                            userWalletInfo.setTotalBalance(totalBalance);

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
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(WalletActivity.this);
        queue.add(stringRequest);
    }


}
