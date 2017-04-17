package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.utils.L;
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.adapter.MainMsgAdapter;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.MsgInfo;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class MyMsgInfoActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.llTop)
    RelativeLayout llTop;
    @Bind(R.id.activity_my_msg_info)
    LinearLayout activityMyMsgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_msg_info);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityMyMsgInfo);
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<MsgInfo> msgInfos;
    private MainMsgAdapter msgAdapter;

    @Override
    public void initView() {
        tvTitle.setText("我的消息");
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        msgInfos = new ArrayList<>();
        msgAdapter = new MainMsgAdapter(mContext, msgInfos, 2);
        recyclerView.setAdapter(msgAdapter);
        msgAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        msgInfos.clear();
        requestNotices();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    private void requestNotices() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestNoticesListAll, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "获取消息:" + json);
                    List<MsgInfo> msgInfoList = JSON.parseArray(json, MsgInfo.class);
                    if (msgInfoList != null && msgInfoList.size() > 0) {
                        msgInfos.addAll(msgInfoList);
                    }
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                msgAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
