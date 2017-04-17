package com.zzx.haoniu.app_nghmuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.MingXiInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MingXiAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MingXiInfo> infos;

    public MingXiAdapter(Context mContext, List<MingXiInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mingxi, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MingxiHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MingxiHolder mingxiHolder = (MingxiHolder) holder;
        final MingXiInfo info = infos.get(position);
        if (info.getOperaterTypeRemark() != null && !StringUtils.isEmpty(info.getOperaterTypeRemark())) {
            mingxiHolder.tvType.setText(info.getOperaterTypeRemark());
        }
        if (info.getCreate_time() != null && !StringUtils.isEmpty(info.getCreate_time())) {
            mingxiHolder.tvDate.setText(info.getCreate_time());
        }
        if (info.getAmount() != 0) {
            if (info.getAmount() > 0) {
                mingxiHolder.tvMoney.setText("+" + info.getAmount() + "元");
                mingxiHolder.tvYue.setText("余额:+" + info.getAmount() + "元");
            } else {
                mingxiHolder.tvMoney.setText("" + info.getAmount() + "元");
                mingxiHolder.tvYue.setText("余额:" + info.getAmount() + "元");
            }
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class MingxiHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvYue;
        TextView tvMoney;
        TextView tvDate;

        public MingxiHolder(View itemView) {
            super(itemView);
            tvType = (TextView) itemView.findViewById(R.id.tvTypeMX);
            tvYue = (TextView) itemView.findViewById(R.id.tvYueMX);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoneyMX);
            tvDate = (TextView) itemView.findViewById(R.id.tvTimeMX);
        }
    }
}
