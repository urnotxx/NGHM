package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.app.AppContext;
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
import self.androidbase.views.SelfLinearLayout;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edPhoneR)
    EditText edPhoneR;
    @Bind(R.id.tvCodeR)
    TextView tvCodeR;
    @Bind(R.id.edCodeR)
    EditText edCodeR;
    @Bind(R.id.tvNextR)
    TextView tvNextR;
    @Bind(R.id.edPwdR)
    EditText edPwdR;
    @Bind(R.id.activity_register)
    LinearLayout activityRegister;
    @Bind(R.id.edConfirmPwdR)
    EditText edConfirmPwdR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityRegister);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "接收数据有误,请重新接收!");
            finish();
        }
        initView();
    }


    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        tvTitle.setText("注册");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.tvCodeR, R.id.tvNextR})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tvCodeR:
                if (edPhoneR.getText().toString() == null || StringUtils.isEmpty(edPhoneR.getText().toString())
                        || edPhoneR.getText().toString().length() != 11) {
                    ToastUtils.showTextToast(mContext, "请输入十一位手机号!");
                    return;
                }
                getCode();
                break;
            case R.id.tvNextR:
                if (edPhoneR.getText().toString() == null || StringUtils.isEmpty(edPhoneR.getText().toString())
                        || !AppContext.getInstance().isPhone(edPhoneR.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请输入正确的手机号码!");
                    return;
                } else if (edCodeR.getText().toString() == null || StringUtils.isEmpty(edCodeR.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "验证码不能为空!");
                    return;
                } else if (edPwdR.getText().toString() == null || StringUtils.isEmpty(edPwdR.getText().toString()) ||
                        edPwdR.getText().toString().length() < 6 || edPwdR.getText().toString().length() > 16) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位密码");
                    return;
                } else if (edConfirmPwdR.getText().toString() == null || StringUtils.isEmpty(edConfirmPwdR.getText().toString()) ||
                        !edPwdR.getText().toString().equals(edConfirmPwdR.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "两次密码不一致");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("phone", edPhoneR.getText().toString());
                bundle.putString("code", edCodeR.getText().toString());
                bundle.putString("pwd", edPwdR.getText().toString());
                bundle.putString("type", "3");
                startActivity(RegisterNextActivity.class, bundle);
                finish();
                break;
        }
    }

    private void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", edPhoneR.getText().toString());
        map.put("type", 1);
        ApiClient.requestNetHandle(mContext, AppConfig.requestGetCode, "获取短信验证码...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                tvCodeR.setClickable(false);
                tvCodeR.setBackgroundResource(R.drawable.shap_gray_5);
                tvCodeR.setTextColor(getResources().getColor(R.color.colorRed));
                countDown();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private CountDownTimer timer;

    private void countDown() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCodeR.setText(millisUntilFinished / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                clickCode();
            }
        };
        // 调用start方法开始倒计时
        timer.start();
    }

    private void clickCode() {
        tvCodeR.setBackgroundResource(R.drawable.border_redgray5);
        tvCodeR.setTextColor(getResources().getColor(R.color.colorWhite));
        tvCodeR.setText("获取验证码");
        tvCodeR.setClickable(true);
    }
}
