package com.example.pta.match;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pta.R;
import com.google.android.gms.ads.AdView;


import java.util.List;
import java.util.Objects;

public class ParticipateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object>participateClassList;
    ParticipateClass participateClass;
    private static final int P_USER_LIST=0;
    private static final int ITEM_BANNER_ADS=1;

    public ParticipateAdapter(Context context, List<Object> participateClassList) {
        this.context = context;
        this.participateClassList = participateClassList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case P_USER_LIST:
                View view = LayoutInflater.from(context).inflate(R.layout.participated_view_model,parent,false);
                return  new ViewHolder(view);
            default:
            case ITEM_BANNER_ADS:
                View banner_ads = LayoutInflater.from(context).inflate(R.layout.ads_container_model,parent,false);
                return new BannerViewHolder(banner_ads);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case P_USER_LIST:
                if (participateClassList.get(position) instanceof ParticipateClass){
                    ViewHolder viewHolder = (ViewHolder)holder;
                    participateClass = (ParticipateClass) participateClassList.get(position);
                    viewHolder.serialNo.setText(""+participateClass.getSerialNo());
                    viewHolder.pName.setText(participateClass.getpName());
                }

            case ITEM_BANNER_ADS:
                if (participateClassList.get(position) instanceof AdView){
                    BannerViewHolder bannerViewHolder = (BannerViewHolder)holder;
                    AdView adView = (AdView) participateClassList.get(position);
                    ViewGroup adsContainer = (ViewGroup) bannerViewHolder.itemView;
                    if (adsContainer.getChildCount() >0){
                        adsContainer.removeAllViews();
                    }
                    if (adsContainer.getParent() != null){
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }
                    adsContainer.addView(adView);

                }

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (position%MatchDetailsActivity.ITEM_PER_AD == 0)
            return ITEM_BANNER_ADS;
        else
            return P_USER_LIST;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return participateClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serialNo, pName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serialNo = itemView.findViewById(R.id.participatedModelSerialNo);
            pName = itemView.findViewById(R.id.participatedModelPlayerName);

        }
    }


public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
