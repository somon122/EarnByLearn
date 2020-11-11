package com.example.pta.LiveDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DB_LiveManager {
    DB_LiveHelper db_liveHelper;
    SQLiteDatabase db;
    String links;

    public DB_LiveManager(Context context) {
        db_liveHelper = new DB_LiveHelper(context);

    }

    public Boolean save_Data(LiveDBClass liveDBClass) {
        db = db_liveHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_LiveHelper.Q_ID, liveDBClass.getQId());
        contentValues.put(DB_LiveHelper.QUESTION, liveDBClass.getQuestion());
        contentValues.put(DB_LiveHelper.OPTION_1, liveDBClass.getOption1());
        contentValues.put(DB_LiveHelper.OPTION_2, liveDBClass.getOption2());
        contentValues.put(DB_LiveHelper.OPTION_3, liveDBClass.getOption3());
        contentValues.put(DB_LiveHelper.OPTION_4, liveDBClass.getOption4());
        contentValues.put(DB_LiveHelper.ANS, liveDBClass.getAns());
        contentValues.put(DB_LiveHelper.USER_ANS, liveDBClass.getUserAns());
        long isInsert = db.insert(DB_LiveHelper.LIVE_EXAM_TABLE, null, contentValues);
        db.close();
        if (isInsert > 0) {
            return true;
        } else {
            return false;
        }
    }
   public Boolean update_Data(LiveDBClass liveDBClass) {
        db = db_liveHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DB_LiveHelper.Q_ID, liveDBClass.getQId());
        contentValues.put(DB_LiveHelper.QUESTION, liveDBClass.getQuestion());
        contentValues.put(DB_LiveHelper.OPTION_1, liveDBClass.getOption1());
        contentValues.put(DB_LiveHelper.OPTION_2, liveDBClass.getOption2());
        contentValues.put(DB_LiveHelper.OPTION_3, liveDBClass.getOption3());
        contentValues.put(DB_LiveHelper.OPTION_4, liveDBClass.getOption4());
        contentValues.put(DB_LiveHelper.ANS, liveDBClass.getAns());
        contentValues.put(DB_LiveHelper.USER_ANS, liveDBClass.getUserAns());

        long isInsert = db.update(DB_LiveHelper.LIVE_EXAM_TABLE,contentValues,"qId="+liveDBClass.getQId(),null);
        db.close();
        if (isInsert > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<LiveDBClass> getData(String qId) {
        List<LiveDBClass> liveDBClassList = new ArrayList<>();
        db = db_liveHelper.getReadableDatabase();
        String Query = "Select * from " + DB_LiveHelper.LIVE_EXAM_TABLE + " where " + DB_LiveHelper.Q_ID + " = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{qId});
        if (cursor.moveToFirst()) {
            do {
                String questionId = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.Q_ID));
                String question = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.QUESTION));
                String option1 = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.OPTION_1));
                String option2 = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.OPTION_2));
                String option3 = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.OPTION_3));
                String option4 = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.OPTION_4));
                String ans = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.ANS));
                String userAns = cursor.getString(cursor.getColumnIndex(DB_LiveHelper.USER_ANS));
                LiveDBClass liveDBClass = new LiveDBClass(questionId,question,option1,option2,option3,option4,ans,userAns);
                liveDBClassList.add(liveDBClass);
            } while (cursor.moveToNext());
            db.close();
        }
        return liveDBClassList;
    }

    public boolean removeAll() {
        db = db_liveHelper.getWritableDatabase();
        int d = db.delete(DB_LiveHelper.LIVE_EXAM_TABLE, null, null);
        if (d > 0) {
            return true;
        } else {
            return false;
        }

    }
}
