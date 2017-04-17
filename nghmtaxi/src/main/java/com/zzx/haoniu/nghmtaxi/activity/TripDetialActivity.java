package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.app.AppContext;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.OrderInfo;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import iosdialog.animation.SlideExit.dialogsamples.utils.L;
import self.androidbase.views.SelfLinearLayout;

public class TripDetialActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.civHeadTD)
    CircleImageView civHeadTD;
    @Bind(R.id.tvPhoneTD)
    TextView tvPhoneTD;
    @Bind(R.id.ivCallTD)
    ImageView ivCallTD;
    @Bind(R.id.tvStartTD)
    TextView tvStartTD;
    @Bind(R.id.tvEndTD)
    TextView tvEndTD;
    @Bind(R.id.tvCancleTD)
    TextView tvCancleTD;
    @Bind(R.id.activity_trip_detial)
    LinearLayout activityTripDetial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detial);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityTripDetial);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误!");
            finish();
        }
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("userCancleOrderInfo")) {
            ToastUtils.showTextToast(mContext, "订单已被乘客取消!");
            finish();
        } else if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        }
    }

    private OrderInfo orderInfo;

    @Override
    public void initView() {
        tvTitle.setText("行程详情");
        if (orderInfo.getPhone() != null && !StringUtils.isEmpty(orderInfo.getPhone())) {
            String phone = orderInfo.getPhone().substring(7);
            tvPhoneTD.setText("尾号" + phone);
        }
        if (orderInfo.getReservationAddress() != null && !StringUtils.isEmpty(orderInfo.getReservationAddress())) {
            tvStartTD.setText(orderInfo.getReservationAddress());
        }
        if (orderInfo.getDestination() != null && !StringUtils.isEmpty(orderInfo.getDestination())) {
            tvEndTD.setText(orderInfo.getDestination());
        }
        if (orderInfo.getHead_portrait() != null && !StringUtils.isEmpty(orderInfo.getHead_portrait())) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo.getHead_portrait(), civHeadTD);
        } else {
            civHeadTD.setImageResource(R.mipmap.img_head);
        }
    }

    @Override
    public void initParms(Bundle parms) {
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ivCallTD, R.id.tvCancleTD})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ivCallTD:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
                break;
            case R.id.tvCancleTD:
                cancleOrder();
                break;
        }
    }

    /**
     * 取消订单
     */
    private void cancleOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestCancleOrder, "取消订单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "取消订单:" + json);
                }
                CommonEventBusEnity enity = new CommonEventBusEnity("cancleOrder", "");
                EventBus.getDefault().post(enity);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
