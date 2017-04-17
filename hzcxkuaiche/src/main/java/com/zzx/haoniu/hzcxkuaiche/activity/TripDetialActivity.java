package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detial);
        ButterKnife.bind(this);
        steepStatusBar();
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
        if (event.getSendCode().equals("unLogInKC")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @OnClick({R.id.ll_back, R.id.ivCallTD})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ivCallTD:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
                break;
        }
    }
}
