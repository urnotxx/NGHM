package com.zzx.haoniu.app_nghmuser.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Main3WindCarView extends FrameLayout implements View.OnClickListener {

    TextView tvMain3MarkTripC;
    TextView tvAddAddressC;
    TextView tvMain3Certified;
    TextView tvMain3OrderInfo;
    TextView tvMain3MarkTripD;
    TextView tvAddAddressD;
    TextView tvMain3Driver;
    TextView tvMain3Custom;
    ImageView ivMain3Driver;
    ImageView ivMain3Custonm;
    LinearLayout llMain3Level1;
    LinearLayout llMain3Level2;
    LinearLayout llMain3Level3;
    LinearLayout llMain3Custom;
    LinearLayout llMain3Driver;
    ScrollView scrollViewM3;

    private View contentView;

    public Main3WindCarView(Context context) {
        super(context);
        if (contentView == null)
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_main3, this);
        initView();
        initEvent();
    }

    private void initView() {
        tvMain3MarkTripC = (TextView) contentView.findViewById(R.id.tvMain3MarkTripC);
        tvAddAddressC = (TextView) contentView.findViewById(R.id.tvAddAddressC);
        tvMain3Certified = (TextView) contentView.findViewById(R.id.tvMain3Certified);
        tvMain3OrderInfo = (TextView) contentView.findViewById(R.id.tvMain3OrderInfo);
        tvMain3MarkTripD = (TextView) contentView.findViewById(R.id.tvMain3MarkTripD);
        tvAddAddressD = (TextView) contentView.findViewById(R.id.tvAddAddressD);
        tvMain3Driver = (TextView) contentView.findViewById(R.id.tvMain3Driver);
        tvMain3Custom = (TextView) contentView.findViewById(R.id.tvMain3Custom);

        ivMain3Driver = (ImageView) contentView.findViewById(R.id.ivMain3Driver);
        ivMain3Custonm = (ImageView) contentView.findViewById(R.id.ivMain3Custonm);

        llMain3Level1 = (LinearLayout) contentView.findViewById(R.id.llMain3Level1);
        llMain3Level2 = (LinearLayout) contentView.findViewById(R.id.llMain3Level2);
        scrollViewM3 = (ScrollView) contentView.findViewById(R.id.scrollViewM3);
        llMain3Level3 = (LinearLayout) contentView.findViewById(R.id.llMain3Level3);
        llMain3Custom = (LinearLayout) contentView.findViewById(R.id.llMain3Custom);
        llMain3Driver = (LinearLayout) contentView.findViewById(R.id.llMain3Driver);
    }

    private void initEvent() {
        findViewById(R.id.tvMain3MarkTripC).setOnClickListener(this);
        findViewById(R.id.tvAddAddressC).setOnClickListener(this);
        findViewById(R.id.tvMain3Certified).setOnClickListener(this);
        findViewById(R.id.tvMain3OrderInfo).setOnClickListener(this);
        findViewById(R.id.tvMain3MarkTripD).setOnClickListener(this);
        findViewById(R.id.tvAddAddressD).setOnClickListener(this);
        findViewById(R.id.llMain3Custom).setOnClickListener(this);
        findViewById(R.id.llMain3Driver).setOnClickListener(this);
    }

    private OnClickListener cusListener, driListener;
    private OnClickListener cusMarkListener, driMarkListener;
    private OnClickListener addAddressCListener, addAddressDListener;
    private OnClickListener certifiedListener;
    private OnClickListener orderInfoListener;

    public void setCus(boolean isCus) {
        if (isCus) {
            ivMain3Custonm.setBackgroundResource(R.mipmap.img_chengkec);
            tvMain3Custom.setTextColor(getResources().getColor(R.color.colorRed));
            ivMain3Driver.setBackgroundResource(R.mipmap.img_chezhu);
            tvMain3Driver.setTextColor(getResources().getColor(R.color.colorGrayText68));
            llMain3Level1.setVisibility(VISIBLE);
            scrollViewM3.setVisibility(GONE);
            llMain3Level3.setVisibility(GONE);
        } else {
            ivMain3Custonm.setBackgroundResource(R.mipmap.img_chengke);
            tvMain3Custom.setTextColor(getResources().getColor(R.color.colorGrayText68));
            ivMain3Driver.setBackgroundResource(R.mipmap.img_chezhuc);
            tvMain3Driver.setTextColor(getResources().getColor(R.color.colorRed));

            llMain3Level1.setVisibility(GONE);
            UserInfo userInfo = AppContext.getInstance().getUserInfo();
            if (userInfo.getIs_ensure_carowner() != null
                    && !StringUtils.isEmpty(userInfo.getIs_ensure_carowner())
                    && userInfo.getIs_ensure_carowner().equals("1")) {//已认证
                scrollViewM3.setVisibility(GONE);
                llMain3Level3.setVisibility(VISIBLE);
            } else {
                scrollViewM3.setVisibility(VISIBLE);
                llMain3Level3.setVisibility(GONE);
            }
        }
    }


    public void setBottomListener(OnClickListener cusListener, OnClickListener driListener) {
        this.cusListener = cusListener;
        this.driListener = driListener;
    }

    public void setLevel1Listener(OnClickListener cusMarkListener, OnClickListener addAddressCListener) {
        this.cusMarkListener = cusMarkListener;
        this.addAddressCListener = addAddressCListener;
    }

    public void setLevel2Listener(OnClickListener certifiedListener) {
        this.certifiedListener = certifiedListener;
    }

    public void setLevel3Listener(OnClickListener driMarkListener, OnClickListener addAddressDListener, OnClickListener orderInfoListener) {
        this.driMarkListener = driMarkListener;
        this.addAddressDListener = addAddressDListener;
        this.orderInfoListener = orderInfoListener;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMain3MarkTripC:
                if (cusMarkListener != null)
                    cusMarkListener.onClick(this);
                break;
            case R.id.tvAddAddressC:
                if (addAddressCListener != null)
                    addAddressCListener.onClick(this);
                break;
            case R.id.tvMain3Certified:
                if (certifiedListener != null)
                    certifiedListener.onClick(this);
                break;
            case R.id.tvMain3OrderInfo:
                if (orderInfoListener != null)
                    orderInfoListener.onClick(this);
                break;
            case R.id.tvMain3MarkTripD:
                if (driMarkListener != null)
                    driMarkListener.onClick(this);
                break;
            case R.id.tvAddAddressD:
                if (addAddressDListener != null)
                    addAddressDListener.onClick(this);
                break;
            case R.id.llMain3Custom:
                if (cusListener != null)
                    cusListener.onClick(this);
                break;
            case R.id.llMain3Driver:
                if (driListener != null)
                    driListener.onClick(this);
                break;
        }
    }
}
