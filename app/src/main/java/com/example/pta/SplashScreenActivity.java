package com.example.pta;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1500;


    //varriable
    Animation top,bottom;
    ImageView imageView;
    TextView textView;

    int versionCode;
    String versionName;

    UpdateStatusClass updateStatusClass;
    String currentDateAndTime;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        updateStatusClass = new UpdateStatusClass(this);
        //Hooks
        imageView = findViewById(R.id.image1);
        textView = findViewById(R.id.text1);

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;

        imageView.setAnimation(top);
        textView.setAnimation(bottom);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        currentDateAndTime = sdf.format(new Date());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
        if (haveNetwork()){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                if (updateStatusClass.getDate().equals(currentDateAndTime)){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    checkUpdate(String.valueOf(versionCode));

                }

            } else {
                Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                intent.putExtra("status","new");
                startActivity(intent);
                finish();
            }
        }else {
            netAlert();
        }
            }
        },SPLASH_SCREEN);

    }
    public void checkUpdate(final String versionCode) {
        String url = getString(R.string.BASS_URL) + "getVersionCode";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);

                            String title = dataobj.getString("title");
                            String versionC = dataobj.getString("versionCode");

                            if (!versionC.equals("")){

                                if (versionC.equals(versionCode)){

                                    updateStatusClass.setDate(currentDateAndTime);
                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {

                                    updateAlert();

                                }
                            }
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("id", "1");
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SplashScreenActivity.this);
        queue.add(stringRequest);
    }

    private void updateAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        builder.setTitle("Update Alert")
                .setMessage("Please Update your App")
                .setCancelable(false)
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://battlegamingbd.com/getApp")));
                        } catch (ActivityNotFoundException e) {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://battlegamingbd.com/getApp")));

                        }

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        builder.setTitle("Alert")
                .setMessage("Please Connect your internet first")
                .setCancelable(false)
                .setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(getApplicationContext(),SplashScreenActivity.class));
                        finish();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean haveNetwork ()
    {
        boolean have_WiFi = false;
        boolean have_Mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfo){

            if (info.getTypeName().equalsIgnoreCase("WIFI"))
            {
                if (info.isConnected())
                {
                    have_WiFi = true;
                }
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))

            {
                if (info.isConnected())
                {
                    have_Mobile = true;
                }
            }

        }
        return have_WiFi || have_Mobile;

    }


}
