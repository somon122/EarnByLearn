package com.example.pta;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

public class SaveUserInfo {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SaveUserInfo(@NotNull Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void dataStore (String id,String userName, String email, String referCode, String number , String conditionStatus){

        editor.putString("id",id);
        editor.putString("number",number);
        editor.putString("email",email);
        editor.putString("userName",userName);
        editor.putString("referCode",referCode);
        editor.putString("conditionStatus",conditionStatus);
        editor.commit();

    }
  public void numberStore (String number){
        editor.putString("number",number);
        editor.commit();
    }

    public String getId (){

        String number = sharedPreferences.getString("id","");
        return number;
    }

    public String getReferCode (){

        String userName = sharedPreferences.getString("referCode","");
        return userName;
    }
    public String getConditionStatus (){

        String password = sharedPreferences.getString("conditionStatus","");
        return password;
    }
   public String getNumber (){

        String number = sharedPreferences.getString("number","");
        return number;
    }

    public String getUserName (){

        String userName = sharedPreferences.getString("userName","");
        return userName;
    }
    public String getEmail (){

        String password = sharedPreferences.getString("email","");
        return password;
    }




    public boolean checkUser(){
        boolean id = sharedPreferences.getString("id","").isEmpty();
        boolean userName = sharedPreferences.getString("userName","").isEmpty();
        return id || userName;
    }

    public void delete (){

        editor.clear();
        editor.commit();

    }

}
