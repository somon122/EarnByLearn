package com.example.pta.LiveDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DB_LiveHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DB_NAME = "LiveExam_db";
    public static final int VERSION = 1;
    public static final String LIVE_EXAM_TABLE = "Exam_Details";
    public static final String KEY_ID = "id";
    public static final String Q_ID = "qId";
    public static final String QUESTION = "question";
    public static final String OPTION_1 = "option1";
    public static final String OPTION_2 = "option2";
    public static final String OPTION_3 = "option3";
    public static final String OPTION_4 = "option4";

    public static final String ANS = "ans";
    public static final String USER_ANS = "userAns";



    private static final String Create_Table = "CREATE TABLE " + LIVE_EXAM_TABLE + "( " + KEY_ID + " INTEGER  PRIMARY KEY ," + Q_ID + " TEXT ," + QUESTION + " TEXT ,"
            + OPTION_1 + " TEXT ," + OPTION_2 + " TEXT ," + OPTION_3 + " TEXT ," + OPTION_4 + " TEXT ," + ANS + " TEXT ," + USER_ANS + " TEXT)";


    public DB_LiveHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Create_Table);
            Toast.makeText(context, "onCreate is called", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LIVE_EXAM_TABLE);
        onCreate(db);

    }
}
