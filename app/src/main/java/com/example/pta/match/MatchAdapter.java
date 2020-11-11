package com.example.pta.match;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private Context context;
    private List<MatchClass>matchClassList;
    private MatchClass matchClass;
    private int showCount;
    private SaveUserInfo saveUserInfo;

    int left;

    public MatchAdapter(Context context, List<MatchClass> matchClassList) {
        this.context = context;
        this.matchClassList = matchClassList;
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


        matchClass = matchClassList.get(position);

        holder.name.setText(matchClass.getName());
        holder.date.setText(matchClass.getDate_time());

        holder.type.setText(matchClass.getType());
        holder.entryFee.setText(matchClass.getEntryFee());
        holder.syllabus.setText(matchClass.getSyllabus());
        holder.routine.setText(matchClass.getRoutine());
        holder.totalTime.setText(matchClass.getTotal_time());
        holder.winnerPrize.setText("winner -"+matchClass.getWinnerPrice()+" Taka");
        checkJointOrNot(matchClass.getId(),saveUserInfo.getId(),holder);
        getTotalJoint(matchClass.getId(), holder, matchClass.getCandidateNo());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                matchClass = matchClassList.get(position);
                Intent intent = new Intent(context, MatchDetailsActivity.class);
                intent.putExtra("name",matchClass.getName());
                intent.putExtra("jointStatus",holder.jointOrNot.getText().toString());
                intent.putExtra("matchId",matchClass.getId());
                intent.putExtra("entryFee",matchClass.getEntryFee());
                intent.putExtra("syllabus",matchClass.getSyllabus());
                intent.putExtra("routine",matchClass.getRoutine());
                intent.putExtra("category",matchClass.getCategory());
                intent.putExtra("date_time",matchClass.getDate_time());
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
                    intent.putExtra("entryFee",matchClass.getEntryFee());
                    intent.putExtra("category",matchClass.getCategory());
                    intent.putExtra("date_time",matchClass.getDate_time());
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
                ,syllabus,routine,winnerPrize, totalTime;

        TextView jointUserLeft, joinUserCounter;
        Button jointOrNot;
        ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.resultMatchName);
            date= itemView.findViewById(R.id.resultMatchTime);

            entryFee= itemView.findViewById(R.id.resultEntryFee);
            type= itemView.findViewById(R.id.resultType);
            syllabus= itemView.findViewById(R.id.resultVersion);
            routine= itemView.findViewById(R.id.resultMap);
            totalTime= itemView.findViewById(R.id.resultPerKill);

            winnerPrize= itemView.findViewById(R.id.resultWinnerPrize);
            joinUserCounter= itemView.findViewById(R.id.matchModelJoinUserCounter);
            jointUserLeft= itemView.findViewById(R.id.matchModelUserLeft);
            progressBar= itemView.findViewById(R.id.matchModelProgressBar);
            jointOrNot= itemView.findViewById(R.id.resultModelUserJointOrNot);


        }

    }
}
