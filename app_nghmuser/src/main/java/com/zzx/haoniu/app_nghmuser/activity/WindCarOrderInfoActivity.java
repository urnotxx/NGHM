package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.WindCarOrderInfoAdapter;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.WindCarInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class WindCarOrderInfoActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tvAllW)
    TextView tvAllW;
    @Bind(R.id.tvNotStartW)
    TextView tvNotStartW;
    @Bind(R.id.tvOrdeingW)
    TextView tvOrdeingW;
    @Bind(R.id.tvFinW)
    TextView tvFinW;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.activity_wind_car_order_info)
    LinearLayout activityWindCarOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind_car_order_info);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityWindCarOrderInfo);
        initView();
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

    private WindCarOrderInfoAdapter orderInfoAdapter;
    private List<WindCarInfo> infos;
    private List<WindCarInfo> allInfos;

    private int selectIndex;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        tvTitle.setText("我的订单");
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        infos = new ArrayList<>();
        allInfos = new ArrayList<>();
        allInfos.add(new WindCarInfo("程先生", "今天 19:00", "西湖国际广场", "之心城", 4, 1, 13));
        allInfos.add(new WindCarInfo("张女士", "昨天 19:00", "西湖国际广场", "之心城", 5, 2, 12));
        allInfos.add(new WindCarInfo("程先生", "前天 19:00", "西湖国际广场", "之心城", 3, 3, 11));
        orderInfoAdapter = new WindCarOrderInfoAdapter(mContext, infos);
        recyclerView.setAdapter(orderInfoAdapter);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.tvAllW, R.id.tvNotStartW, R.id.tvOrdeingW, R.id.tvFinW})
    public void onClick(View view) {
        resetText();
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                return;
            case R.id.tvAllW:
                selectIndex = 0;
                tvAllW.setTextColor(getResources().getColor(R.color.colorRed));
                break;
            case R.id.tvNotStartW:
                selectIndex = 1;
                tvNotStartW.setTextColor(getResources().getColor(R.color.colorRed));
                break;
            case R.id.tvOrdeingW:
                selectIndex = 2;
                tvOrdeingW.setTextColor(getResources().getColor(R.color.colorRed));
                resetInfo();
                break;
            case R.id.tvFinW:
                selectIndex = 3;
                tvFinW.setTextColor(getResources().getColor(R.color.colorRed));
                break;
        }
        resetInfo();
    }

    private void resetText() {
        tvAllW.setTextColor(getResources().getColor(R.color.colorGrayText68));
        tvNotStartW.setTextColor(getResources().getColor(R.color.colorGrayText68));
        tvOrdeingW.setTextColor(getResources().getColor(R.color.colorGrayText68));
        tvFinW.setTextColor(getResources().getColor(R.color.colorGrayText68));
    }

    //  1  未开始  行驶中  已完成
    private void resetInfo() {
        infos.clear();
        if (selectIndex == 0) {
            infos.addAll(allInfos);
        } else if (selectIndex == 1) {
            for (int i = 0; i < allInfos.size(); i++) {
                if (allInfos.get(i).getState() == 1) {
                    infos.add(allInfos.get(i));
                }
            }
        } else if (selectIndex == 2) {
            for (int i = 0; i < allInfos.size(); i++) {
                if (allInfos.get(i).getState() == 2) {
                    infos.add(allInfos.get(i));
                }
            }
        } else if (selectIndex == 3) {
            for (int i = 0; i < allInfos.size(); i++) {
                if (allInfos.get(i).getState() == 3) {
                    infos.add(allInfos.get(i));
                }
            }
        }
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
        orderInfoAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        infos.clear();
        resetInfo();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
