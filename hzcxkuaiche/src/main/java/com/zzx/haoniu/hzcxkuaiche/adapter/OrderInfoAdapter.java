package com.zzx.haoniu.hzcxkuaiche.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.entity.TripInfo;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class OrderInfoAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<TripInfo> infos;

    public OrderInfoAdapter(Context mContext, List<TripInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_orderinfo, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new OrderInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OrderInfoHolder tripHolder = (OrderInfoHolder) holder;
        final TripInfo info = infos.get(position);
        if (info.getCreate_time() != null && !StringUtils.isEmpty(info.getCreate_time())) {
            tripHolder.tvTime.setText(info.getCreate_time());
        }
        if (info.getReservation_address() != null && !StringUtils.isEmpty(info.getReservation_address())) {
            tripHolder.tvStart.setText(info.getReservation_address());
        }
        if (info.getDestination() != null && !StringUtils.isEmpty(info.getDestination())) {
            tripHolder.tvEnd.setText(info.getDestination());
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class OrderInfoHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvStart;
        TextView tvEnd;

        public OrderInfoHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTimeAOI);
            tvStart = (TextView) itemView.findViewById(R.id.tvStartAOI);
            tvEnd = (TextView) itemView.findViewById(R.id.tvEndAOI);
        }
    }
}
