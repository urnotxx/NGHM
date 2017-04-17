package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.entity.CarInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import self.androidbase.views.SelfLinearLayout;

public class MyCarActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tvPlatMC)
    TextView tvPlatMC;
    @Bind(R.id.tvCarModelMC)
    TextView tvCarModelMC;
    @Bind(R.id.tvBelongMC)
    TextView tvBelongMC;
    @Bind(R.id.tvRegisterDateMC)
    TextView tvRegisterDateMC;
    @Bind(R.id.tvNameMC)
    TextView tvNameMC;
    @Bind(R.id.llDateMC)
    LinearLayout llDateMC;
    @Bind(R.id.activity_my_car)
    LinearLayout activityMyCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityMyCar);
        tvTitle.setText("我的车辆");
        requestCars();
    }

    @Override
    public void initParms(Bundle parms) {

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

    private void requestCars() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestCars, "获取车辆信息中...", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "获取车辆信息:" + json);
                    List<CarInfo> infos = JSON.parseArray(json, CarInfo.class);
                    if (infos != null && infos.size() > 0) {
                        L.d("TAG", "我的车辆信息:" + infos.size());
                        CarInfo info = infos.get(0);
                        if (info.getPlate_no() != null && !StringUtils.isEmpty(info.getPlate_no())) {
                            tvPlatMC.setText(info.getPlate_no());
                        }
                        if (info.getModel() != null && !StringUtils.isEmpty(info.getModel())) {
                            tvCarModelMC.setText(info.getModel());
                        }
                        if (info.getReal_name() != null && !StringUtils.isEmpty(info.getReal_name())) {
                            tvBelongMC.setText(info.getReal_name());
                            tvNameMC.setText(info.getReal_name());
                        }
                        llDateMC.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
