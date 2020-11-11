package com.example.pta;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

public class UpdateStatusClass {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public UpdateStatusClass(@NotNull Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("updateStatus", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setDate (String date){
        editor.putString("date",date);
        editor.commit();
    }
    public String getDate (){

        String date = sharedPreferences.getString("date","");
        return date;
    }

    public void delete (){

        editor.clear();
        editor.commit();

    }

}
