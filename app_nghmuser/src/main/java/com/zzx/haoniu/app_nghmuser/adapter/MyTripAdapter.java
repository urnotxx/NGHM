package com.zzx.haoniu.app_nghmuser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.TripInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MyTripAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<TripInfo> infos;

    public MyTripAdapter(Context mContext, List<TripInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trip, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TripHolder tripHolder = (TripHolder) holder;
        final TripInfo info = infos.get(position);
        if (info.getReservation_address() != null && !StringUtils.isEmpty(info.getReservation_address())) {
            tripHolder.tvStart.setText(info.getReservation_address());
        }
        if (info.getDestination() != null && !StringUtils.isEmpty(info.getDestination())) {
            tripHolder.tvEnd.setText(info.getDestination());
        }
        if (info.getCreate_time() != null && !StringUtils.isEmpty(info.getCreate_time())) {
            tripHolder.tvTime.setText(info.getCreate_time());
        }
        if (info.getType() != 0) {
            if (info.getType() == 42) {
                tripHolder.tvType.setText("出租车");
            } else if (info.getType() == 44) {
                tripHolder.tvType.setText("快车");
            }
        }
        if (info.getYg_amount() != 0) {
            tripHolder.tvMoney.setText("￥" + info.getYg_amount());
        }
        if (info.isPd_flag()) {
            tripHolder.tvPin.setVisibility(View.VISIBLE);
        } else {
            tripHolder.tvPin.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class TripHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvTime;
        TextView tvStart;
        TextView tvEnd;
        TextView tvPin;
        TextView tvMoney;

        public TripHolder(View itemView) {
            super(itemView);
            tvType = (TextView) itemView.findViewById(R.id.tvTypeT);
            tvTime = (TextView) itemView.findViewById(R.id.tvTimeT);
            tvStart = (TextView) itemView.findViewById(R.id.tvStartT);
            tvEnd = (TextView) itemView.findViewById(R.id.tvEndT);
            tvPin = (TextView) itemView.findViewById(R.id.tvPinT);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoneyT);
        }
    }
}
