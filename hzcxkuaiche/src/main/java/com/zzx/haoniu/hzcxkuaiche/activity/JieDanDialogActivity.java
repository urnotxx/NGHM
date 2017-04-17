package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.recriver.JpushReceiver;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.TTSController_1;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;
import com.zzx.haoniu.hzcxkuaiche.websocket.WebSocketWorker;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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
    @Bind(R.id.tvGrab)
    TextView tvGrab;
    @Bind(R.id.llJiedan)
    LinearLayout llJiedan;
    @Bind(R.id.llJieDanLeft)
    LinearLayout llJieDanLeft;
    @Bind(R.id.activity_jie_dan_dialog)
    RelativeLayout activityJieDanDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_dan_dialog);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityJieDanDialog);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "参数接收有误!");
            finish();
        }
        ttsController_1 = TTSController_1.getInstance(mContext);
        ttsController_1.startSpeck("您有一条新的订单，请注意查看!");
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInKC")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private TTSController_1 ttsController_1;
    private Bundle bundle;
    private OrderInfo orderInfo;
    private double lat;
    private double log;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
        lat = parms.getDouble("lat");
        log = parms.getDouble("log");
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        if (JpushReceiver.orderState == 2) {//第2个订单
            llJieDanLeft.setVisibility(View.VISIBLE);
            tvTimeJD.setVisibility(View.GONE);
            tvDistanceJD.setText("拼单");
            tvGrab.setText("拒接");
        } else if (JpushReceiver.orderState == 1) {
            countDown();
            double distance;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            distance = AMapUtils.calculateLineDistance(new LatLng(lat, log),
                    new LatLng(orderInfo.getTrip().getStartLatitude(), orderInfo.getTrip().getStartLongitude()));
            tvDistanceJD.setText("距您" + (decimalFormat.format(distance / 1000)) + "公里");
        }
        tvStartJD.setText(orderInfo.getReservationAddress());
        tvEndJD.setText(orderInfo.getDestination());
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ivCloseJD, R.id.llJiedan, R.id.llJieDanLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCloseJD:
                changeDriverStatus("取消订单中...");
                break;
            case R.id.llJiedan:
                if (JpushReceiver.orderState == 1) {
                    grabOrder();
                } else if (JpushReceiver.orderState == 2) {
                    finish();
                }
                break;
            case R.id.llJieDanLeft:
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
                if (timer != null)
                    timer.cancel();
                if (JpushReceiver.orderState == 1) {
                    startActivity(MyTripDetialActivity.class, bundle);
                    timer.cancel();
                    ToastUtils.showTextToast(mContext, "接单成功!");
                    JpushReceiver.orderState = 2;
                    finish();
                } else {
                    CommonEventBusEnity enity = new CommonEventBusEnity("secOrderInfo", "");
                    Map<String, Object> map = new HashMap<>();
                    map.put("secOrderInfo", orderInfo);
                    enity.setMap(map);
                    EventBus.getDefault().post(enity);
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                changeDriverStatus("");
                ToastUtils.showTextToast(mContext, msg);
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
                changeDriverStatus("");
            }
        };
        // 调用start方法开始倒计时
        timer.start();
    }

    private void changeDriverStatus(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 3);
        ApiClient.requestNetHandle(mContext, AppConfig.requestDjDriverStatus, msg, map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", json);
                }
                if (timer != null)
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
        changeDriverStatus("");
    }
}
