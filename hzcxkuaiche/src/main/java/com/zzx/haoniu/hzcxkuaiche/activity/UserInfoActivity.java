package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.FuwuInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.civHeadUI)
    CircleImageView civHeadUI;
    @Bind(R.id.tvNameUI)
    TextView tvNameUI;
    @Bind(R.id.tvServiceUI)
    TextView tvServiceUI;
    @Bind(R.id.tvEvaluateUI)
    TextView tvEvaluateUI;
    @Bind(R.id.tvPreUI)
    TextView tvPreUI;
    @Bind(R.id.llTripUI)
    LinearLayout llTripUI;
    @Bind(R.id.llCarUI)
    LinearLayout llCarUI;
    @Bind(R.id.llWalletUI)
    LinearLayout llWalletUI;
    @Bind(R.id.llMsgUI)
    LinearLayout llMsgUI;
    @Bind(R.id.llSetUI)
    LinearLayout llSetUI;
    @Bind(R.id.tvShoucheUI)
    TextView tvShoucheUI;
    @Bind(R.id.ivQuanUI)
    ImageView ivQuanUI;
    @Bind(R.id.tvQuanUI)
    TextView tvQuanUI;
    @Bind(R.id.activity_user_info)
    RelativeLayout activityUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityUserInfo);
        initView();
        requesrMsg();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("shouche")) {
            stopJieDan();
        } else if (event.getSendCode().equals("jiedan")) {
            startJieDan();
        } else if (event.getSendCode().equals("exit")) {
            finish();
        } else if (event.getSendCode().equals("unLogInKC")) {
            finish();
        }
    }

    private boolean orderState = true;//是否接单

    @Override
    public void initParms(Bundle parms) {
        orderState = parms.getBoolean("orderState", true);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        if (orderState) {
            startAnim();
        } else {
            stopJieDan();
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.civHeadUI, R.id.llTripUI, R.id.llCarUI, R.id.rlRightUI, R.id.llWalletUI, R.id.llMsgUI, R.id.llSetUI, R.id.tvShoucheUI, R.id.ivQuanUI})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlRightUI:
//                startActivity(JieDanDialogActivity.class);
                finish();
                break;
            case R.id.civHeadUI:
//                startActivity(JieDanDialogActivity.class);
                break;
            case R.id.llTripUI:
                Bundle bundle = new Bundle();
                bundle.putBoolean("orderState", orderState);
                startActivity(OrderInfoActivity.class, bundle);
                break;
            case R.id.llCarUI:
                startActivity(MyCarActivity.class);
                break;
            case R.id.llWalletUI:
                startActivity(MyWalletActivity.class);
                break;
            case R.id.llMsgUI:
                startActivity(MyMsgInfoActivity.class);
                break;
            case R.id.llSetUI:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("orderState", orderState);
                startActivity(UserDetialActivity.class, bundle1);
                break;
            case R.id.tvShoucheUI:
                AppContext.getInstance().onLine(mContext, 0, "收车中...");
                break;
            case R.id.ivQuanUI:
                AppContext.getInstance().onLine(mContext, 1, "上线中...");
                break;
        }
    }

    private void stopJieDan() {
        orderState = false;
        tvShoucheUI.setVisibility(View.GONE);
        tvQuanUI.setText("出车");
        tvQuanUI.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));//红色
        stopAnim();
    }

    private void startJieDan() {
        if (!orderState) {
            orderState = true;
            tvShoucheUI.setVisibility(View.VISIBLE);
            tvQuanUI.setText("接单中");
            tvQuanUI.setTextColor(Color.rgb(0x09, 0xa6, 0xed));//蓝色09A6ED
            startAnim();
        }
    }

    private Animation operatingAnim;

    public void startAnim() {
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_quan);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (null != operatingAnim) {
            ivQuanUI.startAnimation(operatingAnim);
        }
    }

    public void stopAnim() {
        if (operatingAnim != null) {
            ivQuanUI.clearAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppContext.getInstance().getUserInfo().getNick_name() != null) {
            tvNameUI.setText(AppContext.getInstance().getUserInfo().getNick_name());
        }
        if (AppContext.getInstance().getUserInfo().getHead_portrait() != null
                && !StringUtils.isEmpty(AppContext.getInstance().getUserInfo().getHead_portrait())) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + AppContext.getInstance().getUserInfo().getHead_portrait(), civHeadUI);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requesrMsg() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestMsg, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "服务信息:" + json);
                    FuwuInfo info = JSON.parseObject(json, FuwuInfo.class);
                    if (info.getCloseRate() != null) {
                        tvPreUI.setText(info.getCloseRate());
                    }
                    if (info.getDriverxj() == null) {
                        tvEvaluateUI.setText("5.0");
                    } else {
                        tvEvaluateUI.setText(info.getDriverxj() + "");
                    }
                    if (info.getDriverfwf() == null) {
                        tvServiceUI.setText("100");
                    } else {
                        tvServiceUI.setText(info.getDriverfwf() + "");
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }
}
