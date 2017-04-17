package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.app.AppContext;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.OrderInfo;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;
import com.zzx.haoniu.nghmtaxi.view.SlidingButton;
import com.zzx.haoniu.nghmtaxi.websocket.WebSocketWorker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import self.androidbase.views.SelfLinearLayout;

public class ConfirmFinishActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.civHeadCF)
    CircleImageView civHeadCF;
    @Bind(R.id.tvStartCF)
    TextView tvStartCF;
    @Bind(R.id.tvEndCF)
    TextView tvEndCF;
    @Bind(R.id.ivCallCF)
    ImageView ivCallCF;
    @Bind(R.id.tvPayStatue)
    TextView tvPayStatue;
    @Bind(R.id.llPayStatuCF)
    LinearLayout llPayStatuCF;
    @Bind(R.id.activity_confirm_finish)
    LinearLayout activity_confirm_finish;
    @Bind(R.id.tvPriceCF)
    TextView tvPriceCF;
    @Bind(R.id.slidingButtonJieDanCF)
    SlidingButton slidingButtonJieDanCF;
//    @Bind(R.id.slidingButtonGoHomeCF)
//    SlidingButton slidingButtonGoHomeCF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_finish);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activity_confirm_finish);
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
        if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private Bundle bundle;
    private OrderInfo orderInfo;
    private double totleFee;

    @Override
    public void initView() {
        tvTitle.setText("订单详情");
        tvPriceCF.setText(totleFee + "");
        llBack.setVisibility(View.INVISIBLE);
        tvStartCF.setText(orderInfo.getReservationAddress());
        tvEndCF.setText(orderInfo.getDestination());
    }

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        totleFee = parms.getDouble("totleFee");
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ivCallCF, R.id.tvShoucheCF})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCallCF:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
                break;
            case R.id.tvShoucheCF:
                AppContext.getInstance().onLine(mContext, 0, "收车中...");
                WebSocketWorker.jiedanState = 0;
                finish();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slidingButtonJieDanCF.handleActivityEvent(event)) {
            AppContext.getInstance().onLine(mContext, 1, "");
            WebSocketWorker.jiedanState = 0;
            finish();
        }
        return super.onTouchEvent(event);
    }
}
