package com.zzx.haoniu.nghmtaxi.activity;

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
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.OrderInfo;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.utils.L;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.TTSController_1;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;
import com.zzx.haoniu.nghmtaxi.websocket.WebSocketWorker;

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
    @Bind(R.id.llJiedan)
    LinearLayout llJiedan;
    @Bind(R.id.activity_jie_dan_dialog)
    RelativeLayout activityJieDanDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_dan_dialog);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityJieDanDialog);
        WebSocketWorker.dialogShow = 1;
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "参数接收有误!");
            finish();
        }
        ttsController_1 = TTSController_1.getInstance(mContext);
        ttsController_1.startSpeck("您有一条新的订单，请注意查看!");
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        }
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
        double distance;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        distance = AMapUtils.calculateLineDistance(new LatLng(lat, log),
                new LatLng(orderInfo.getTrip().getStartLatitude(), orderInfo.getTrip().getStartLongitude()));
        tvDistanceJD.setText("距您" + (decimalFormat.format(distance / 1000)) + "公里");
        tvStartJD.setText(orderInfo.getReservationAddress());
        tvEndJD.setText(orderInfo.getDestination());
        if (WebSocketWorker.jiedanState == 0) {
            countDown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsController_1.destroy();
        EventBus.getDefault().unregister(this);
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
                if (WebSocketWorker.jiedanState == 0) {
                    grabOrder();
                }
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
                WebSocketWorker.jiedanState = 1;
                startActivity(TripInterfaceActivity.class, bundle);
                WebSocketWorker.dialogShow = 2;
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
                WebSocketWorker.dialogShow = 2;
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
