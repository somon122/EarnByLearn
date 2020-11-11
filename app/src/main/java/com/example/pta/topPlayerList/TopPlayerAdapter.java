package com.example.pta.topPlayerList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pta.R;

import java.util.List;

public class TopPlayerAdapter extends RecyclerView.Adapter<TopPlayerAdapter.ViewHolder> {

    private Context context;
    private List<TopPlayerClass>topPlayerClassList;
    private TopPlayerClass topPlayerClass;

    public TopPlayerAdapter(Context context, List<TopPlayerClass> topPlayerClassList) {
        this.context = context;
        this.topPlayerClassList = topPlayerClassList;
    }

    @NonNull
    @Override
    public TopPlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.top_player_model,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TopPlayerAdapter.ViewHolder holder, int position) {


        topPlayerClass = topPlayerClassList.get(position);

        holder.totalWin.setText(topPlayerClass.getTotalWin());
        holder.name.setText(topPlayerClass.getUserName());
        holder.serialNo.setText("No."+topPlayerClass.getSerialNo());

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
        return topPlayerClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, serialNo, totalWin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.topModelUserName);
            serialNo = itemView.findViewById(R.id.topModelSerialNo);
            totalWin = itemView.findViewById(R.id.topModelTotalWin);


        }
    }
}
