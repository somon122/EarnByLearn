package com.example.pta.wallet;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DepositFragment extends Fragment {

    Button submitBtn;
    TextInputLayout amountET, transactionET, numberET;
    RadioGroup radioGroup;
    View root;
    RadioButton method;
    String value;
    SaveUserInfo saveUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         root =  inflater.inflate(R.layout.fragment_deposit, container, false);


        radioGroup = root.findViewById(R.id.selectDepositRadioGroup);
        saveUserInfo = new SaveUserInfo(getContext());
        numberET = root.findViewById(R.id.depositNumber);
        submitBtn = root.findViewById(R.id.depositSubmitButton);
        amountET = root.findViewById(R.id.depositAmount);
        transactionET = root.findViewById(R.id.depositTransaction);
        saveUserInfo = new SaveUserInfo(getContext());


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                method = root.findViewById(checkedId);
                value =method.getText().toString();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (value.equals("")){
                    Toast.makeText(getContext(), "Please Select any one from Bkash, Rocket, or Nagad", Toast.LENGTH_SHORT).show();
                }else {
                    submitInfo();
                }
            }
        });


        return root;
    }

    private void submitInfo() {
        String num = numberET.getEditText().getText().toString().trim();
        String tk = amountET.getEditText().getText().toString().trim();
        String tId = transactionET.getEditText().getText().toString().trim();

        if (num.isEmpty()){

            numberET.getEditText().setError("Please enter Number");

        }else if (tk.isEmpty()){
            amountET.getEditText().setError("Please enter Amount");

        }else if (tId.isEmpty()){
            transactionET.getEditText().setError("Please enter Amount");

        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            String currentDateAndTime = sdf.format(new Date());

            int dTK = Integer.parseInt(tk);

            if (dTK >=20){
                confirmAlert(saveUserInfo.getId(),saveUserInfo.getUserName(),tk,num,tId,currentDateAndTime,value,"Pending");

            }else {
                Toast.makeText(getContext(), "Minimum Deposit 20TK", Toast.LENGTH_SHORT).show();
            }


        }

    }


    private void confirmAlert(final String id, final String userName,final String amount,final String number,final String transactionId,final String date_time,
                              final String method, final String pending) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Alert")
                .setMessage("Are you 100% Sure?\n\nThis Number is correct"+number)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        submitBtn.setEnabled(false);
                        insertDeposit(id,userName,amount,number,transactionId,date_time,method,pending);
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


    private void insertDeposit(final String id, final String userName,final String amount,final String number,final String transactionId,final String date_time,
                                final String method, final String pending) {


        String url = getString(R.string.BASS_URL) + "insertDeposit";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {

                        startActivity(new Intent(getContext(),WalletActivity.class));
                        Toasty.success(getContext(), "Info Submit Success!", Toast.LENGTH_SHORT, true).show();
                        getActivity().finish();

                    } else {
                        Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
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
                Params.put("userId", id);
                Params.put("userName", userName);
                Params.put("number", number);
                Params.put("amount", amount);
                Params.put("method", method);
                Params.put("transactionId", transactionId);
                Params.put("status", pending);
                Params.put("date_time", date_time);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }




}