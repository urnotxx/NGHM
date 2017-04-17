package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.utils.L;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import self.androidbase.utils.MD5Encrypt;

public class ForgetPwdActivity extends BaseActivity {

    @Bind(R.id.edPhoneFP)
    EditText edPhoneFP;
    @Bind(R.id.edCodeFP)
    EditText edCodeFP;
    @Bind(R.id.tvCodeFP)
    TextView tvCodeFP;
    @Bind(R.id.edPwdFP)
    EditText edPwdFP;
    @Bind(R.id.tvSubmitFP)
    TextView tvSubmitFP;
    @Bind(R.id.activity_forget_pwd)
    LinearLayout activity_forget_pwd;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activity_forget_pwd);
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

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initView() {
        tvTitle.setText("忘记密码");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.tvCodeFP, R.id.tvSubmitFP})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tvCodeFP:
                if (edPhoneFP.getText().toString() == null || StringUtils.isEmpty(edPhoneFP.getText().toString())
                        || 11 != edPhoneFP.getText().toString().length()) {
                    ToastUtils.showTextToast(mContext, "请输入正确的手机号码!");
                    return;
                }
                getCode();
                break;
            case R.id.tvSubmitFP:
                if (edPhoneFP.getText().toString() == null || StringUtils.isEmpty(edPhoneFP.getText().toString())
                        || 11 != edPhoneFP.getText().toString().length()) {
                    ToastUtils.showTextToast(mContext, "请输入正确的手机号码!");
                    return;
                } else if (edCodeFP.getText().toString() == null || StringUtils.isEmpty(edCodeFP.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请输入验证码!");
                    return;
                } else if (edPwdFP.getText().toString() == null || StringUtils.isEmpty(edPwdFP.getText().toString())
                        || edPwdFP.getText().toString().length() < 6 || edPwdFP.getText().toString().length() > 12) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位密码");
                    return;
                }
                updatePwd();
                break;
        }
    }

    private void updatePwd() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", edPhoneFP.getText().toString());
        map.put("code", edCodeFP.getText().toString());
        map.put("rePassword", MD5Encrypt.MD5(edPwdFP.getText().toString()));
        map.put("password", MD5Encrypt.MD5(edPwdFP.getText().toString()));
        ApiClient.requestNetHandle(mContext, AppConfig.modifyPwdByFor, "修改密码中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "密码：" + json);
                }
                ToastUtils.showTextToast(mContext, "修改成功");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", edPhoneFP.getText().toString());
        map.put("type", 2);
        ApiClient.requestNetHandle(mContext, AppConfig.requestGetCode, "获取短信验证码...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                tvCodeFP.setClickable(false);
                tvCodeFP.setBackgroundResource(R.drawable.shap_gray_10);
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
                tvCodeFP.setText(millisUntilFinished / 1000 + "s后重新获取");
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
        tvCodeFP.setBackgroundResource(R.drawable.border_orggray10);
        tvCodeFP.setText("获取验证码");
        tvCodeFP.setClickable(true);
    }
}
