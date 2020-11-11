package com.example.pta.liveExam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ExamActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<LiveExamClass> liveExamList;
    private int mQuestionsLength;
    private int qCount=0;
    private int scoreCount=0;
    private RadioButton optionButtonNo1, optionButtonNo2, optionButtonNo3, optionButtonNo4;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView questionTV,numberTV, questionCountTV, timeShowTV;
    private Button subjectiveP_NextBtn;
    private String answer;

    private long START_TIME_IN_MILLIS = 500000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    int tTest=0;
    AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Toolbar toolbar = findViewById(R.id.subjectPracticeShowToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Live Exam");
        liveExamList = new ArrayList<>();
        radioGroup = findViewById(R.id.liveExamOptionGroup);
        optionButtonNo1 = findViewById(R.id.subjectOptionNo1_id);
        optionButtonNo2 = findViewById(R.id.subjectOptionNo2_id);
        optionButtonNo3 = findViewById(R.id.subjectOptionNo3_id);
        optionButtonNo4 = findViewById(R.id.subjectOptionNo4_id);
        questionTV = findViewById(R.id.subjectQuestion_id);
        numberTV = findViewById(R.id.subjectNumber_id);

        questionCountTV = findViewById(R.id.examQuestionCount);
        timeShowTV = findViewById(R.id.examTimeShow);
        subjectiveP_NextBtn = findViewById(R.id.subjectiveP_NextBtn);
        examQList("2020-08-30","DailyTest","General_Knowledge");

        subjectiveP_NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (radioGroup.getCheckedRadioButtonId() == -1){

                    Toast.makeText(ExamActivity.this, "Please select option", Toast.LENGTH_SHORT).show();
                }else {
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    String selectOption = radioButton.getText().toString();

                    if (selectOption.equals(answer)){

                        nextQuestion();
                        
                    }else {
                        START_TIME_IN_MILLIS = mTimeLeftInMillis-10000;
                        pauseTimer();
                        nextQuestion();
                    }

              /*  if (tTest==0){
                    startTimer();
                    tTest++;
                }else {
                   START_TIME_IN_MILLIS = mTimeLeftInMillis-10000;
                  pauseTimer();
                }*/

                }
            }
        });
    }

    ///// Start time coding//////

    private void nextQuestion(){
        if (scoreCount < mQuestionsLength){
            qCount = qCount+1;
            updateData(qCount);
            unCheckOption();
        }else {
            popUp();
        }

    }

    private void unCheckOption() {
        radioGroup.clearCheck();

        /*optionButtonNo1.setChecked(false);
        optionButtonNo2.setChecked(false);
        optionButtonNo3.setChecked(false);
        optionButtonNo4.setChecked(false);*/

    }

    private void startExamAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        View view = LayoutInflater.from(ExamActivity.this).inflate(R.layout.ads_click_model,null);

        Button clickBtn = view.findViewById(R.id.modelClimAdsNow);
        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                popUp();

            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        resetTimer();
        startTimer();
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }
    private void updateCountDownText() {
        int hours = (int) ((mTimeLeftInMillis / 1000) / 60) / 60;
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours,minutes, seconds);
        timeShowTV.setText("Time left: "+timeLeftFormatted);
    }
    /// time section  ///////

    @Override
    public void onBackPressed() {
        if (mTimerRunning){
            pauseTimer();
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (mTimerRunning){
            pauseTimer();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (mTimerRunning){
            pauseTimer();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mTimerRunning){
            pauseTimer();
        }
        super.onDestroy();
    }


    void popUp(){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ExamActivity.this,
                R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.category_popup_model,
                (LinearLayout)findViewById(R.id.pendingAlertPopUp_id));
        TextView userName = bottomSheetView.findViewById(R.id.payAlertUserName);
        TextView number = bottomSheetView.findViewById(R.id.payAlertNumber);
        TextView referCode = bottomSheetView.findViewById(R.id.payAlertReferCode);
        Button payNow = bottomSheetView.findViewById(R.id.payAlertPayNowBtn);
        Button payLater = bottomSheetView.findViewById(R.id.payAlertPayLaterBtn);

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        payLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }


    private void updateData(int qCount) {

        if (!liveExamList.isEmpty()){
            LiveExamClass liveExamClass = liveExamList.get(qCount);
            scoreCount = scoreCount+1;
            numberTV.setText(""+scoreCount+") ");
            questionCountTV.setText(""+scoreCount+"/"+mQuestionsLength);
            questionTV.setText(liveExamClass.getQuestion());
            optionButtonNo1.setText(liveExamClass.getOption1());
            optionButtonNo2.setText(liveExamClass.getOption2());
            optionButtonNo3.setText(liveExamClass.getOption3());
            optionButtonNo4.setText(liveExamClass.getOption4());
            answer = liveExamClass.getAnswer();

            if (scoreCount==1){
                startExamAlert();
            }
            if (scoreCount==mQuestionsLength){
                subjectiveP_NextBtn.setText("Submit");
            }

        }else {
            Toast.makeText(this, "Data is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void examQList(final String date_time, final String category , final String subject) {

        String url = /*getString(R.string.BASS_URL)*/ "https://apronseekers.xyz/api/"+ "liveExam";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        //progressBar.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            LiveExamClass liveExamClass = new LiveExamClass();
                            liveExamClass.setId(i + 1);
                            liveExamClass.setQuestion(dataobj.getString("question"));
                            liveExamClass.setOption1(dataobj.getString("option1"));
                            liveExamClass.setOption2(dataobj.getString("option2"));
                            liveExamClass.setOption3(dataobj.getString("option3"));
                            liveExamClass.setOption4(dataobj.getString("option4"));
                            liveExamClass.setAnswer(dataobj.getString("answer"));
                            liveExamClass.setDateTime(dataobj.getString("date_time"));
                            liveExamList.add(liveExamClass);
                        }
                        mQuestionsLength = liveExamList.size();
                        updateData(qCount);
                    } else {

                        noDataAlert();
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
                Params.put("date_time", date_time);
                Params.put("examCategory", category);
                Params.put("subject", subject);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ExamActivity.this);
        queue.add(stringRequest);
    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setTitle("Alert!")
                .setMessage("No data found")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setTitle("Alert!")
                .setMessage("Please make sure your net connection")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}