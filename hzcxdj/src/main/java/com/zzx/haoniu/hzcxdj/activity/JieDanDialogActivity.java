package com.zzx.haoniu.hzcxdj.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.entity.OrderInfo;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;
import com.zzx.haoniu.hzcxdj.websocket.WebSocketWorker;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JieDanDialogActivity extends BaseActivity {

    @Bind(R.id.tvStartJD)
    TextView tvStartJD;
    @Bind(R.id.tvEndJD)
    TextView tvEndJD;
    @Bind(R.id.ivPromptJD)
    ImageView ivPromptJD;
    @Bind(R.id.tvPromptJD)
    TextView tvPromptJD;
    @Bind(R.id.tvDistanceJD)
    TextView tvDistanceJD;
    @Bind(R.id.ivCloseJD)
    ImageView ivCloseJD;
    @Bind(R.id.tvTimeJD)
    TextView tvTimeJD;
    @Bind(R.id.llJiedan)
    LinearLayout llJiedan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_dan_dialog);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "参数接收有误!");
            finish();
        }
        tvDistanceJD.setText(orderInfo.getRealDistance() + "米");
        tvStartJD.setText(orderInfo.getReservationAddress());
        tvEndJD.setText(orderInfo.getDestination());
    }

    private Bundle bundle;
    private OrderInfo orderInfo;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
//        if (WebSocketWorker.jiedanState == 0) {
        countDown();
//        }
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ivCloseJD, R.id.llJiedan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCloseJD:
                changeDriverStatus();
                break;
            case R.id.llJiedan:
                timer.cancel();
                grabOrder();
                break;
        }
    }

    public void grabOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestGrabOrder, "接单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "接单:" + json);
                }
                ToastUtils.showTextToast(mContext, "接单成功!");
                startActivity(TripInterfaceActivity.class, bundle);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                finish();
            }
        });
    }

    private CountDownTimer timer;

    private void countDown() {
        timer = new CountDownTimer(15 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimeJD.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                changeDriverStatus();
            }
        };
        // 调用start方法开始倒计时
        timer.start();
    }

    private void changeDriverStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 3);
        ApiClient.requestNetHandle(mContext, AppConfig.requestDjDriverStatus, "取消订单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", json);
                }
                timer.cancel();
                finish();
            }

            @Override
            public void onFailure(String msg) {
                L.d("TAG", msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        changeDriverStatus();
    }
}
