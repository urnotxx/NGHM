package com.zzx.haoniu.hzcxkuaiche.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.MsgInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MainMsgAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MsgInfo> infos;
    private int tag = 0;  // 1  mianActivity   2  msgActivity

    public MainMsgAdapter(Context mContext, List<MsgInfo> infos, int tag) {
        this.mContext = mContext;
        this.infos = infos;
        this.tag = tag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_msg, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MsgHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MsgHolder tripHolder = (MsgHolder) holder;
        final MsgInfo info = infos.get(position);
        if (info.getContent() != null && !StringUtils.isEmpty(info.getContent()))
            tripHolder.tvContent.setText(info.getContent());
        if (info.getCreate_time() != null && !StringUtils.isEmpty(info.getCreate_time()))
            tripHolder.tvTime.setText(info.getCreate_time());
        if (info.getTitle() != null && !StringUtils.isEmpty(info.getTitle()))
            tripHolder.tvType.setText(info.getTitle());
        if (tag == 1) {
            tripHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new CommonEventBusEnity("delMsg", position + ""));
                }
            });
        } else if (tag == 2) {
            requestDelMsg(position);
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class MsgHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvTime;
        TextView tvContent;
        ImageView ivDel;

        public MsgHolder(View itemView) {
            super(itemView);
            tvType = (TextView) itemView.findViewById(R.id.tvTypeM);
            tvTime = (TextView) itemView.findViewById(R.id.tvTimeM);
            tvContent = (TextView) itemView.findViewById(R.id.tvContentM);
            ivDel = (ImageView) itemView.findViewById(R.id.ivDelM);
        }
    }

    private void requestDelMsg(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("noticeId", infos.get(position).getNotice_id());
        ApiClient.requestNetHandle(mContext, AppConfig.requestDelMsgNotices, "删除中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                infos.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }
}
