package com.zzx.haoniu.app_nghmuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2016/12/1.
 */
public class PayDialog extends Dialog implements View.OnClickListener {
    public PayDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.layout_driverbottom);
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
        mContext = context;
        initView();
        initEvent();
    }

    private void initView() {
        civDriverHeadB = (CircleImageView) findViewById(R.id.civDriverHeadB);
        tvDriverNameB = (TextView) findViewById(R.id.tvDriverNameB);
        tvPlateB = (TextView) findViewById(R.id.tvPlateB);
        tvPriceB = (TextView) findViewById(R.id.tvPriceB);
        tvPay = (TextView) findViewById(R.id.tvPayB);
        tvMingXi = (TextView) findViewById(R.id.tvMingxiB);
        ivCall = (ImageView) findViewById(R.id.ivCallB);
        llPayB = (LinearLayout) findViewById(R.id.llPayB);
        llPayB.setVisibility(View.VISIBLE);
    }

    private Context mContext;
    private TextView tvPay, tvMingXi;
    private ImageView ivCall;
    private OnClickListener clickPay, clickMingXi, clickCall;

    public CircleImageView civDriverHeadB;
    public TextView tvDriverNameB;
    public TextView tvPlateB;
    public TextView tvPriceB;

    private LinearLayout llPayB;

    public void setPayClick(OnClickListener clickPay, OnClickListener clickMingXi, OnClickListener clickCall) {
        this.clickPay = clickPay;
        this.clickMingXi = clickMingXi;
        this.clickCall = clickCall;
    }

    private void initEvent() {
        tvPay.setOnClickListener(this);
        tvMingXi.setOnClickListener(this);
        ivCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvPayB:
                if (clickPay != null) {
                    clickPay.onClick(this, 0);
                }
                break;
            case R.id.tvMingxiB:
                if (clickMingXi != null) {
                    clickMingXi.onClick(this, 0);
                }
                break;
            case R.id.ivCallB:
                if (clickCall != null) {
                    clickCall.onClick(this, 0);
                }
                break;
        }
    }
}
