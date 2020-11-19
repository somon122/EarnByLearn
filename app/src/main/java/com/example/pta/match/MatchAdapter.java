package com.example.pta.match;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pta.R;
import com.example.pta.SaveUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private Context context;
    private List<MatchClass>matchClassList;
    private MatchClass matchClass;
    private int showCount;
    private SaveUserInfo saveUserInfo;
    private long currentMilliseconds;
    private long startMilliseconds;
    private long endMilliseconds;

    int left;

    public MatchAdapter(Context context, List<MatchClass> matchClassList , long currentMilliseconds) {
        this.context = context;
        this.matchClassList = matchClassList;
        this.currentMilliseconds = currentMilliseconds;
    }
    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_view_model,parent,false);
        saveUserInfo = new SaveUserInfo(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchAdapter.ViewHolder holder, final int position) {

   /*     @SuppressLint("SimpleDateFormat")
        SimpleDateFormat start_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start_Date = start_sdf.parse(matchClass.getStart_date_time());
            startMilliseconds = start_Date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat end_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date end_Date = end_sdf.parse(matchClass.getEnd_date_time());
            endMilliseconds = end_Date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

            holder.matchModelBox.setVisibility(View.VISIBLE);
            matchClass = matchClassList.get(position);
            holder.name.setText(matchClass.getName());
            holder.date.setText(matchClass.getStart_date_time());
            holder.type.setText(matchClass.getType());
            holder.entryFee.setText(matchClass.getEntryFee());
            holder.totalTime.setText(matchClass.getTotal_time());
            holder.winnerPrize.setText(matchClass.getWinnerPrice()+" Taka");
            checkJointOrNot(matchClass.getId(),saveUserInfo.getId(),holder);
            getTotalJoint(matchClass.getId(), holder, matchClass.getCandidateNo());

        holder.syllabusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchClass = matchClassList.get(position);
                String imageUrl = "https://game.earnbylearn.club/public/files/pdf_file/"+matchClass.getSyllabus();

                if (matchClass.getSyllabus().contains(".")){
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
                    } catch (ActivityNotFoundException e) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl)));
                    }
                }else {
                    Toasty.error(context,"Empty File",Toasty.LENGTH_SHORT).show();
                }
            }
        });


        holder.showDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchClass = matchClassList.get(position);
                Intent intent = new Intent(context, MatchDetailsActivity.class);
                intent.putExtra("name",matchClass.getName());
                intent.putExtra("jointStatus",holder.jointOrNot.getText().toString());
                intent.putExtra("matchId",matchClass.getId());
                intent.putExtra("entryFee",matchClass.getEntryFee());
                intent.putExtra("syllabus",matchClass.getSyllabus());
                intent.putExtra("type",matchClass.getType());
                intent.putExtra("category",matchClass.getCategory());
                intent.putExtra("start_date_time",matchClass.getStart_date_time());
                intent.putExtra("end_date_time",matchClass.getEnd_date_time());
                intent.putExtra("total_time",matchClass.getTotal_time());
                intent.putExtra("winnerPrice",matchClass.getWinnerPrice());
                context.startActivity(intent);
            }
        });

        holder.jointOrNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.jointOrNot.getText().toString().equals("JOIN NOW")){

                    matchClass = matchClassList.get(position);
                    Intent intent = new Intent(context, JoinInMatchActivity.class);
                    intent.putExtra("matchId",matchClass.getId());
                    intent.putExtra("matchName",matchClass.getName());
                    intent.putExtra("spotsLeft",left);
                    intent.putExtra("type",matchClass.getType());
                    intent.putExtra("entryFee",matchClass.getEntryFee());
                    intent.putExtra("category",matchClass.getCategory());
                    intent.putExtra("start_date_time",matchClass.getStart_date_time());
                    intent.putExtra("end_date_time",matchClass.getEnd_date_time());
                    context.startActivity(intent);

                }else {
                    Toast.makeText(context, "You are already joined", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void checkJointOrNot(final String matchId, final String userId, final MatchAdapter.ViewHolder holder) {
        String url = context.getString(R.string.BASS_URL) + "checkAlreadyJoint";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        holder.jointOrNot.setText("JOINED");

                    } else {
                        holder.jointOrNot.setText("JOIN NOW");
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
                Params.put("matchId", matchId);
                Params.put("userId", userId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }


    public void getTotalJoint(final String matchId, final MatchAdapter.ViewHolder holder,final String cNo) {

        String url = context.getString(R.string.BASS_URL) + "getUserTotalJoint";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        int num = jsonArray.length();
                        holder.progressBar.setMax(Integer.parseInt(cNo));
                        holder.progressBar.setProgress(num);
                        holder.joinUserCounter.setText(num+"/"+cNo);
                        int tUser= Integer.parseInt(cNo);
                        left = tUser-num;
                        holder.jointUserLeft.setText("only "+left+ "spots left");
                        if (tUser==num){
                            holder.jointOrNot.setText("CLOSED");
                        }

                    } else {
                        Toast.makeText(context, "Helllllllll", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    holder.jointOrNot.setEnabled(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                holder.jointOrNot.setEnabled(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("matchId", matchId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return matchClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        TextView name,date,entryFee,type
                ,winnerPrize, totalTime;

        TextView jointUserLeft, joinUserCounter;
        Button jointOrNot, syllabusBtn, showDetailsBtn;
        ProgressBar progressBar;
        LinearLayout matchModelBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.resultMatchName);
            date= itemView.findViewById(R.id.resultMatchTime);
            matchModelBox= itemView.findViewById(R.id.matchModelBox);

            entryFee= itemView.findViewById(R.id.resultEntryFee);
            type= itemView.findViewById(R.id.resultType);
            totalTime= itemView.findViewById(R.id.resultPerKill);

            syllabusBtn= itemView.findViewById(R.id.resultSyllabus);
            showDetailsBtn= itemView.findViewById(R.id.showDetailsBtn);

            winnerPrize= itemView.findViewById(R.id.resultWinnerPrize);
            joinUserCounter= itemView.findViewById(R.id.matchModelJoinUserCounter);
            jointUserLeft= itemView.findViewById(R.id.matchModelUserLeft);
            progressBar= itemView.findViewById(R.id.matchModelProgressBar);
            jointOrNot= itemView.findViewById(R.id.resultModelUserJointOrNot);


        }

    }
}
