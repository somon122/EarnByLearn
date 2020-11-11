package com.example.pta.wallet;

import android.content.Context;
import android.content.SharedPreferences;

public class UserWalletInfo {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public UserWalletInfo(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences("UserWalletInfo",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

    }
    public void setBalance (String balance){
        editor.putString("balance",balance);
        editor.commit();
    }

    public String getBalance (){

        String balance = sharedPreferences.getString("balance","0");
        return balance;
    }
public void setTotalBalance (String balance){
        editor.putString("totalBalance",balance);
        editor.commit();
    }

    public String getTotalBalance (){

        String balance = sharedPreferences.getString("totalBalance","0");
        return balance;
    }


}
