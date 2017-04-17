package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.MingXiAdapter;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.MingXiInfo;
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

public class MingXiWalletActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;
    @Bind(R.id.activity_ming_xi_walate)
    LinearLayout activityMingXiWalate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ming_xi_walate);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityMingXiWalate);
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

    private MingXiAdapter adapter;
    private List<MingXiInfo> infos;
    private int pageIndex = 0;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_ming_xi_walate;
    }

    @Override
    public void initView() {
        tvTitle.setText("明细");
        mRefreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        infos = new ArrayList<>();
        adapter = new MingXiAdapter(mContext, infos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        mRefreshLayout.beginRefreshing();
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        infos.clear();
        requestAccount();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    private void requestAccount() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        ApiClient.requestNetHandle(mContext, AppConfig.requestAccount, "", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "账户明细:" + json);
                    List<MingXiInfo> mingXiInfos = JSON.parseArray(json, MingXiInfo.class);
                    if (mingXiInfos != null && mingXiInfos.size() > 0) {
                        infos.addAll(mingXiInfos);
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
