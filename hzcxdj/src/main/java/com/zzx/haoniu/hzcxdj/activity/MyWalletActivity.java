package com.zzx.haoniu.hzcxdj.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.adapter.MingXiAdapter;
import com.zzx.haoniu.hzcxdj.entity.MingXiInfo;
import com.zzx.haoniu.hzcxdj.entity.WalletInfo;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class MyWalletActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.tvMoneyMW)
    TextView tvMoneyMW;
    @Bind(R.id.rlBackMW)
    RelativeLayout rlBackMW;
    @Bind(R.id.rlRightMW)
    RelativeLayout rlRightMW;
    @Bind(R.id.activity_my_wallet)
    RelativeLayout activityMyWallet;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        steepStatusBar();
        initView();
        requestAmount();
    }

    private MingXiAdapter adapter;
    private List<MingXiInfo> mingXiInfos;

    @Override
    public void initView() {
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mingXiInfos = new ArrayList<>();
        adapter = new MingXiAdapter(mContext, mingXiInfos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @OnClick(R.id.rlBackMW)
    public void onClick() {
        finish();
    }

    private void requestAmount() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestAmount, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "余额信息：" + json);
                    WalletInfo info = JSON.parseObject(json, WalletInfo.class);
                    tvMoneyMW.setText(df.format(info.getYe()) + "");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * 账户明细
     */
    private void requestAccount() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestAccount, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "明细：" + json);
                    List<MingXiInfo> infos = JSON.parseArray(json, MingXiInfo.class);
                    if (infos != null && infos.size() > 0) {
                        mingXiInfos.addAll(infos);
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mingXiInfos.clear();
        requestAccount();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
