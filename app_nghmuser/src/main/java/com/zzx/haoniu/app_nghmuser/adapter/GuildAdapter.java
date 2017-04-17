package com.zzx.haoniu.app_nghmuser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.MsgInfo;
import com.zzx.haoniu.app_nghmuser.entity.UserGuild;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/22.
 */
public class GuildAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserGuild> infos;

    public GuildAdapter(Context mContext, List<UserGuild> infos ) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_guild, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MsgHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MsgHolder tripHolder = (MsgHolder) holder;
        final UserGuild info = infos.get(position);
        if (info.getContent() != null && !StringUtils.isEmpty(info.getContent()))
            tripHolder.tvContent.setText(info.getContent());
        if (info.getNum() != null && !StringUtils.isEmpty(info.getNum()))
            tripHolder.tvNum.setText(info.getNum());
        if (info.getTitle() != null && !StringUtils.isEmpty(info.getTitle()))
            tripHolder.tvTitle.setText(info.getTitle());
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class MsgHolder extends RecyclerView.ViewHolder {
        TextView tvNum;
        TextView tvTitle;
        TextView tvContent;

        public MsgHolder(View itemView) {
            super(itemView);
            tvNum = (TextView) itemView.findViewById(R.id.tvNumG);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitleG);
            tvContent = (TextView) itemView.findViewById(R.id.tvContentG);
        }
    }
}
