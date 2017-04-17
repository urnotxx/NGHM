package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.adapter.OrderInfoAdapter;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.TripInfo;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.utils.L;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class OrderInfoActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.tvShoucheOI)
    TextView tvShoucheOI;
    @Bind(R.id.ivQuanOI)
    ImageView ivQuanOI;
    @Bind(R.id.tvQuanOI)
    TextView tvQuanOI;
    @Bind(R.id.activity_order_info)
    RelativeLayout activityOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityOrderInfo);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        initView();
        doBusiness(mContext);
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("shouche")) {
            stopJieDan();
        } else if (event.getSendCode().equals("jiedan")) {
            startJieDan();
        }else    if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        }
    }

    @Override
    public void initParms(Bundle parms) {
        orderState = parms.getBoolean("orderState", true);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_order_info;
    }

    private List<TripInfo> tripInfos;
    private OrderInfoAdapter orderInfoAdapter;

    @Override
    public void initView() {
        if (orderState) {
            startAnim();
        } else {
            stopJieDan();
        }
        tvTitle.setText("我的行程");
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void doBusiness(Context mContext) {
        tripInfos = new ArrayList<>();
        orderInfoAdapter = new OrderInfoAdapter(mContext, tripInfos);
        recyclerView.setAdapter(orderInfoAdapter);
        orderInfoAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @OnClick({R.id.ll_back, R.id.tvShoucheOI, R.id.ivQuanOI})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tvShoucheOI:
                stopJieDan();
                break;
            case R.id.ivQuanOI:
                startJieDan();
                break;
        }
    }

    private boolean orderState;

    private void stopJieDan() {
        orderState = false;
        tvShoucheOI.setVisibility(View.GONE);
        tvQuanOI.setText("出车");
        tvQuanOI.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));//红色
        stopAnim();
    }

    private void startJieDan() {
        if (!orderState) {
            orderState = true;
            tvShoucheOI.setVisibility(View.VISIBLE);
            tvQuanOI.setText("接单中");
            tvQuanOI.setTextColor(Color.rgb(0x09, 0xa6, 0xed));//蓝色09A6ED
            startAnim();
        }
    }

    private Animation operatingAnim;

    public void startAnim() {
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_quan);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (null != operatingAnim) {
            ivQuanOI.startAnimation(operatingAnim);
        }
    }

    public void stopAnim() {
        if (operatingAnim != null) {
            ivQuanOI.clearAnimation();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        tripInfos.clear();
        requestOrderList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requestOrderList() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestOrderList, "加载数据中...", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "订单列表:" + json);
                    List<TripInfo> myTripInfos = JSON.parseArray(json, TripInfo.class);
                    if (myTripInfos != null && myTripInfos.size() > 0) {
                        tripInfos.addAll(myTripInfos);
                    }
                }
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                orderInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                orderInfoAdapter.notifyDataSetChanged();
            }
        });
    }
}
