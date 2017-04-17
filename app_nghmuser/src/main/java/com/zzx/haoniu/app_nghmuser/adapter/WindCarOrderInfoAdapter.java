package com.zzx.haoniu.app_nghmuser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.TripInfo;
import com.zzx.haoniu.app_nghmuser.entity.WindCarInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/11/22.
 */
public class WindCarOrderInfoAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<WindCarInfo> infos;

    public WindCarOrderInfoAdapter(Context mContext, List<WindCarInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_windcarorderinfo, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TripHolder tripHolder = (TripHolder) holder;
        final WindCarInfo info = infos.get(position);
        if (info.getName() != null && !StringUtils.isEmpty(info.getName())) {
            tripHolder.tvDriverName.setText(info.getName());
        }
        if (info.getTime() != null && !StringUtils.isEmpty(info.getTime())) {
            tripHolder.tvTime.setText(info.getTime());
        }
        if (info.getEndPlace() != null && !StringUtils.isEmpty(info.getEndPlace())) {
            tripHolder.tvEnd.setText(info.getEndPlace());
        }
        if (info.getStartPlace() != null && !StringUtils.isEmpty(info.getStartPlace())) {
            tripHolder.tvStart.setText(info.getStartPlace());
        }
        tripHolder.ratingBar.setRating(info.getScore());
        tripHolder.tvScore.setText(info.getScore() + "");
        tripHolder.tvMoney.setText(info.getMoney() + "");
        if (info.getState() == 1) {
            tripHolder.tvState.setText("未开始");
            tripHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        } else if (info.getState() == 2) {
            tripHolder.tvState.setText("行驶中");
            tripHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.colorBlueBg));
        } else if (info.getState() == 3) {
            tripHolder.tvState.setText("已完成");
            tripHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.colorGrayText68));
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class TripHolder extends RecyclerView.ViewHolder {
        CircleImageView civDriverHead;
        TextView tvDriverName;
        RatingBar ratingBar;
        TextView tvScore;
        TextView tvState;
        TextView tvTime;
        TextView tvStart;
        TextView tvEnd;
        TextView tvMoney;

        public TripHolder(View itemView) {
            super(itemView);
            civDriverHead = (CircleImageView) itemView.findViewById(R.id.civDriverHeadAW);
            tvDriverName = (TextView) itemView.findViewById(R.id.tvDriverNameAW);
            tvScore = (TextView) itemView.findViewById(R.id.tvScoreAW);
            tvState = (TextView) itemView.findViewById(R.id.tvStateAW);
            tvTime = (TextView) itemView.findViewById(R.id.tvTimeAW);
            tvStart = (TextView) itemView.findViewById(R.id.tvStartAW);
            tvEnd = (TextView) itemView.findViewById(R.id.tvEndAW);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoneyAW);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarAW);
        }
    }
}
