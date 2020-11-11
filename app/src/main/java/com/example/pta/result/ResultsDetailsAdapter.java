package com.example.pta.result;

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

public class ResultsDetailsAdapter extends RecyclerView.Adapter<ResultsDetailsAdapter.ViewHolder> {

    private Context context;
    private List<ResultDetailsClass>detailsClassList;
    private ResultDetailsClass resultDetailsClass;

    public ResultsDetailsAdapter(Context context, List<ResultDetailsClass> detailsClassList) {
        this.context = context;
        this.detailsClassList = detailsClassList;
    }

    @NonNull
    @Override
    public ResultsDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.result_details_view_model,parent,false);
       return  new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResultsDetailsAdapter.ViewHolder holder, final int position) {

        resultDetailsClass = detailsClassList.get(position);
        holder.serialNo.setText(""+resultDetailsClass.getSerialNo());
        holder.playerName.setText(resultDetailsClass.getPlayerName());
        holder.number.setText(resultDetailsClass.getNumber());
        holder.timeTV.setText(resultDetailsClass.getTime());
        holder.winAmount.setText(resultDetailsClass.getWinAmount());

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
        return detailsClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView serialNo, playerName,number,timeTV,winAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serialNo = itemView.findViewById(R.id.resultsModelSerialNo);
            playerName = itemView.findViewById(R.id.resultsModelPlayerName);
            number = itemView.findViewById(R.id.resultsModelNumber);
            timeTV = itemView.findViewById(R.id.resultsModelTime);
            winAmount = itemView.findViewById(R.id.resultsModelWinningAmount);

        }
    }
}
