package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.MyTripAdapter;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.TripInfo;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class MyTripActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);
//        View view = LayoutInflater.from(mContext).inflate(bindLayout(), null);
        ButterKnife.bind(this);
        initView();
        doBusiness(mContext);
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private MyTripAdapter adapter;
    private List<TripInfo> infos;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_my_trip;
    }

    @Override
    public void initView() {
        tvTitle.setText("我的行程");
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        infos.clear();
        requestOrderList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        return false;
    }

    @Override
    public void doBusiness(Context mContext) {
        infos = new ArrayList<>();
//        infos.add(new MyTripInfo("顺风车", "2016-3-10", "蜀山区", "包河区", false));
//        infos.add(new MyTripInfo("顺风车", "2016-3-11", "蜀山区", "包河区", true));
//        infos.add(new MyTripInfo("顺风车", "2016-3-12", "蜀山区", "包河区", false));
        adapter = new MyTripAdapter(mContext, infos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    private void requestOrderList() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestOrderList, "加载数据中...", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "订单列表:" + json);
                    List<TripInfo> myTripInfos = JSON.parseArray(json, TripInfo.class);
                    if (myTripInfos != null && myTripInfos.size() > 0) {
                        infos.addAll(myTripInfos);
                    }
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                adapter.notifyDataSetChanged();
            }
        });
    }

}
