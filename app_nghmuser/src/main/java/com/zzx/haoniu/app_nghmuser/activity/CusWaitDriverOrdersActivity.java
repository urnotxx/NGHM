package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import iosdialog.dialog.listener.OnBtnClickL;
import iosdialog.dialog.widget.NormalDialog;

public class CusWaitDriverOrdersActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.tvTimeCW)
    TextView tvTimeCW;
    @Bind(R.id.tvStartCW)
    TextView tvStartCW;
    @Bind(R.id.tvEndCW)
    TextView tvEndCW;
    @Bind(R.id.tvMoneyCW)
    TextView tvMoneyCW;
    @Bind(R.id.tvPeopleNumCW)
    TextView tvPeopleNumCW;
    @Bind(R.id.rlNoDriverCW)
    RelativeLayout rlNoDriverCW;
    @Bind(R.id.civDriverHeadLW)
    CircleImageView civDriverHeadLW;
    @Bind(R.id.tvDriverNameLW)
    TextView tvDriverNameLW;
    @Bind(R.id.ratingBarLW)
    RatingBar ratingBarLW;
    @Bind(R.id.tvScoreLW)
    TextView tvScoreLW;
    @Bind(R.id.tvTimeLW)
    TextView tvTimeLW;
    @Bind(R.id.tvStartLW)
    TextView tvStartLW;
    @Bind(R.id.tvEndLW)
    TextView tvEndLW;
    @Bind(R.id.rlHaveDriverCW)
    LinearLayout rlHaveDriverCW;
    @Bind(R.id.activity_cus_wait_driver_orders)
    LinearLayout activityCusWaitDriverOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_wait_driver_orders);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityCusWaitDriverOrders);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "下单失败!");
            finish();
            return;
        }
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 顺风车  用户等待司机接单
     */
    private int num = 0;
    private String startPlace;
    private String endPlace;
    private String startTime;

    @Override
    public void initParms(Bundle parms) {
        num = parms.getInt("num", 0);
        startPlace = parms.getString("startPlace");
        endPlace = parms.getString("endPlace");
        startTime = parms.getString("startTime");
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        tvTitle.setText("等待车主接单");
        tvSubmit.setText("取消 ");
        if (startPlace != null && !StringUtils.isEmpty(startPlace)) {
            tvStartCW.setText(startPlace);
        }
        if (endPlace != null && !StringUtils.isEmpty(endPlace)) {
            tvEndCW.setText(endPlace);
        }
        if (startTime != null && !StringUtils.isEmpty(startTime)) {
            tvTimeCW.setText(startTime);
        }
        tvPeopleNumCW.setText(num + "人");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ll_right, R.id.ivCallLw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_right:
                showExitDoalog();
                break;
            case R.id.ivCallLw:

                break;
        }
    }

    private NormalDialog dialog;

    private void showExitDoalog() {
        dialog = new NormalDialog(mContext);
        dialog.content("车主也是上班族，可能还在忙再给他们些时间吧")//
                .isTitleShow(false)
                .show();
        dialog.setLeftText("确定取消");
        dialog.setRightText("确认等待");
        dialog.btnTextColor(getResources().getColor(R.color.colorGrayText48), getResources().getColor(R.color.colorRed));
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        finish();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                });
    }
}
