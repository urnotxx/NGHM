package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import self.androidbase.views.SelfLinearLayout;

public class MingXiTaxiActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tvPriceMX)
    TextView tvPriceMX;
    @Bind(R.id.tvKMMX)
    TextView tvKMMX;
    @Bind(R.id.tvPriceKMMX)
    TextView tvPriceKMMX;
    @Bind(R.id.tvTimeMX)
    TextView tvTimeMX;
    @Bind(R.id.tvPriceTimeMX)
    TextView tvPriceTimeMX;
    @Bind(R.id.tvFeeRuleMX)
    TextView tvFeeRuleMX;
    @Bind(R.id.activity_ming_xi_taxi)
    LinearLayout activityMingXiTaxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ming_xi_taxi);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityMingXiTaxi);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
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

    @OnClick({R.id.ll_back, R.id.tvFeeRuleMX})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tvFeeRuleMX:
                ToastUtils.showTextToast(mContext, "计费规则");
                break;
        }
    }

    private Bundle bundle;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_ming_xi_taxi;
    }

    @Override
    public void initView() {
        tvTitle.setText("查看明细");
        tvPriceMX.setText(bundle.getDouble("totalFee") + "");
        tvPriceKMMX.setText(bundle.getDouble("ygFee") + "元");
        tvPriceTimeMX.setText(bundle.getFloat("otherFee") + "元");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

}
