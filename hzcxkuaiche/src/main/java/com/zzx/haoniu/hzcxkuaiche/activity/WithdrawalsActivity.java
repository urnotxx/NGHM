package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.TiXianInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import self.androidbase.utils.MD5Encrypt;

public class WithdrawalsActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edMoneyWD)
    EditText edMoneyWD;
    @Bind(R.id.edPwdWD)
    EditText edPwdWD;
    @Bind(R.id.activity_withdrawals)
    LinearLayout activityWithdrawals;
    @Bind(R.id.edNumWD)
    EditText edNumWD;
    @Bind(R.id.rb_01T)
    RadioButton rb01T;
    @Bind(R.id.rb_02T)
    RadioButton rb02T;
    @Bind(R.id.tvYueWD)
    TextView tvYueWD;
    @Bind(R.id.tvCanWD)
    TextView tvCanWD;
    @Bind(R.id.tvHeadWD)
    TextView tvHeadWD;
    @Bind(R.id.tvSXWD)
    TextView tvSXWD;

    private double money = 50.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityWithdrawals);
        requestWithDrawData();
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

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        tvTitle.setText("提现");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ll_right, R.id.tvWithDrawals})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_right:
                break;
            case R.id.tvWithDrawals:
                if (edMoneyWD.getText().toString() == null || StringUtils.isEmpty(edMoneyWD.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "提现金额不能为空!");
                    return;
                } else if (Double.parseDouble(edMoneyWD.getText().toString()) == 0) {
                    ToastUtils.showTextToast(mContext, "提现金额不能为零!");
                    return;
                } else if (edNumWD.getText().toString() == null || StringUtils.isEmpty(edNumWD.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "提现账户不能为空!");
                    return;
                } else if (edPwdWD.getText().toString() == null || StringUtils.isEmpty(edPwdWD.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "登录密码不能为空!");
                    return;
                } else if (!rb01T.isChecked() && !rb02T.isChecked()) {
                    ToastUtils.showTextToast(mContext, "请选择提现方式!");
                    return;
                }
                requestWithDraw();
                break;
        }
    }

    private TiXianInfo info;

    private void requestWithDrawData() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestTiXianJine, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("可提现金额:" + json);
                    info = JSON.parseObject(json, TiXianInfo.class);
                    tvHeadWD.setText(info.getAreadyTx() + "");
                    tvYueWD.setText(info.getZhyue() + "");
                    tvSXWD.setText(info.getTxSangxian() + "");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * TxSangxian : 1000
     * areadyTx : 0
     * yajin : 100
     * zhyue : 500.0
     */
    private void requestWithDraw() {
        Map<String, Object> map = new HashMap<>();
        //1 支付宝 2 微信
        if (rb01T.isChecked()) {
            map.put("type", 10001);
        } else if (rb02T.isChecked()) {
            map.put("type", 10002);
        }
        map.put("txcount", edNumWD.getText().toString());
        map.put("amountStr", edMoneyWD.getText().toString());
        map.put("password", MD5Encrypt.MD5(edPwdWD.getText().toString()));
        map.put("TxSangxian", info.getTxSangxian());
        map.put("areadyTx", info.getAreadyTx());
        map.put("yajin", info.getYajin());
        map.put("zhyue", info.getZhyue());
        ApiClient.requestNetHandle(mContext, AppConfig.requestTiXian, "提现申请中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                EventBus.getDefault().post(new CommonEventBusEnity("withDrawalSuccess", null));
                ToastUtils.showTextToast(mContext, "申请成功！请耐心等待审核完成！");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

}
