package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.iflytek.cloud.thirdparty.S;
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.adapter.MingXiAdapter;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.MingXiInfo;
import com.zzx.haoniu.nghmtaxi.entity.WalletInfo;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.utils.L;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;

public class MyWalletActivity extends BaseActivity {
    @Bind(R.id.tvMoneyMW)
    TextView tvMoneyMW;
    @Bind(R.id.rlBackMW)
    RelativeLayout rlBackMW;
    @Bind(R.id.rlRightMW)
    RelativeLayout rlRightMW;
    @Bind(R.id.activity_my_wallet)
    LinearLayout activityMyWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityMyWallet);
        initView();
        requestAmount();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        } else if (event.getSendCode().equals("withDrawalSuccess")) {
            requestAmount();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.rlBackMW, R.id.rlRightMW, R.id.llWithDrawalMW})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBackMW:
                finish();
                break;
            case R.id.rlRightMW:
                startActivity(MingXiWalletActivity.class);
                break;
            case R.id.llWithDrawalMW:
                startActivity(WithdrawalsActivity.class);
                break;
        }
    }

    private void requestAmount() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestAmount, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "余额信息：" + json);
                    JSONObject obj;
                    try {
                        obj = new JSONObject(json);
                        double ye = obj.getDouble("ye");
                        tvMoneyMW.setText(ye + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
//    private void requestAccount() {
//        ApiClient.requestNetHandle(mContext, AppConfig.requestAccount, "", null, new ResultListener() {
//            @Override
//            public void onSuccess(String json) {
//                if (json != null && !StringUtils.isEmpty(json)) {
//                    L.d("TAG", "明细：" + json);
//                    List<MingXiInfo> infos = JSON.parseArray(json, MingXiInfo.class);
//                    if (infos != null && infos.size() > 0) {
//                        mingXiInfos.addAll(infos);
//                    }
//                }
//                mRefreshLayout.endRefreshing();
//                mRefreshLayout.endLoadingMore();
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                ToastUtils.showTextToast(mContext, msg);
//                mRefreshLayout.endRefreshing();
//                mRefreshLayout.endLoadingMore();
//                adapter.notifyDataSetChanged();
//            }
//        });
//    }

}
