package com.example.pta.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pta.ProfileSetUpActivity;
import com.example.pta.R;
import com.example.pta.SaveUserInfo;
import com.example.pta.SignInActivity;
import com.example.pta.SplashScreenActivity;
import com.example.pta.wallet.WalletActivity;
import com.example.pta.wallet.WithdrawAdapter;
import com.example.pta.wallet.WithdrawClass;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    CardView logout,profileWallet,updateProfile, share,howToPlay,aboutUs;
    TextView userNameTV, userNumberTV, totalWinTV, balanceTV, totalPlayedMatch;
    ImageView userImage;
    SaveUserInfo saveUserInfo;
    int jointC;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);

        logout = root.findViewById(R.id.logout_id);
        profileWallet = root.findViewById(R.id.profileWallet);

        saveUserInfo = new SaveUserInfo(getContext());

        userNameTV = root.findViewById(R.id.profileUserName);
        userNumberTV = root.findViewById(R.id.profileShowNumber);
        balanceTV = root.findViewById(R.id.profileShowBalance);

        totalPlayedMatch = root.findViewById(R.id.profileTotalPlayedMatch);

        totalWinTV = root.findViewById(R.id.profileShowTotalWin);
        userImage = root.findViewById(R.id.profileUserImage);
        updateProfile = root.findViewById(R.id.profileUpdate);
        share = root.findViewById(R.id.profileShare);
        howToPlay = root.findViewById(R.id.profileHowToPlay);
        aboutUs = root.findViewById(R.id.profileAboutUs);

        getUserProfileData(saveUserInfo.getNumber());


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logOutAlert();
            }
        });

        profileWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getContext(), WalletActivity.class));
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getContext(),ProfileSetUpActivity.class);
              intent.putExtra("status","update");
              startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Site link: https://battlegamingbd.com/tournamentApp";
                String shareSub = "App Link";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Tournament App"));

            }
        });
        howToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));

                }

            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));

                }

            }
        });

        return root;
    }

    private void logOutAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout Alert")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmLogoutAlert();

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

    private void confirmLogoutAlert() {

        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                        saveUserInfo.delete();
                        Toast.makeText(getContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), SignInActivity.class));
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
                            String userName = dataobj.getString("userName");
                            String number = dataobj.getString("number");
                            String totalWin = dataobj.getString("totalWin");
                            String totalBalance = dataobj.getString("totalBalance");
                            userNameTV.setText(userName);
                            userNumberTV.setText("+"+number);
                            totalWinTV.setText(totalWin);
                            balanceTV.setText(totalBalance+"TK");
                            getTotalJoint(number);

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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

   public void getTotalJoint(final String number) {
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
                        totalPlayedMatch.setText(""+jsonArray.length());

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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }




}