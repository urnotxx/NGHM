package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import self.androidbase.utils.MD5Encrypt;
import self.androidbase.views.SelfLinearLayout;

public class ForgetPwdActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edPhoneFP)
    EditText edPhoneFP;
    @Bind(R.id.edCodeFP)
    EditText edCodeFP;
    @Bind(R.id.tvCodeFP)
    TextView tvCodeFP;
    @Bind(R.id.edPwdFP)
    EditText edPwdFP;
    @Bind(R.id.edCPwdFP)
    EditText edCPwdFP;
    @Bind(R.id.tvSubmitFP)
    TextView tvSubmitFP;
    @Bind(R.id.rb_01F)
    RadioButton rb01F;
    @Bind(R.id.rb_02F)
    RadioButton rb02F;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.activity_forget_pwd)
    LinearLayout activityForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        if (getIntent().getExtras() != null)
            initParms(getIntent().getExtras());
        else {
            ToastUtils.showTextToast(mContext, "数据接收有误，请重新进入");
            finish();
        }
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityForgetPwd);
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

    private int tag = 0;

    @Override
    public void initParms(Bundle parms) {
        tag = parms.getInt("tag", 0);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initView() {
        if (tag == 1) {
            tvTitle.setText("注册");
            radioGroup.setVisibility(View.VISIBLE);
        } else if (tag == 2) {
            tvTitle.setText("找回密码");
            radioGroup.setVisibility(View.INVISIBLE);
        }
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
                } else if (edPwdFP.getText().toString() == null || StringUtils.isEmpty(edCPwdFP.getText().toString())
                        || edCPwdFP.getText().toString().length() < 6 || edCPwdFP.getText().toString().length() > 12) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位确认密码密码");
                    return;
                } else if (!edPwdFP.getText().toString().equals(edCPwdFP.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "两次密码不一致，请重新输入！");
                    edCPwdFP.setText("");
                    return;
                }
                if (tag == 1) {
                    if (!rb01F.isChecked() && !rb02F.isChecked()) {
                        ToastUtils.showTextToast(mContext, "请选择性别!");
                        return;
                    }
                    register();
                } else {
                    updatePwd();
                }
                break;
        }
    }

    private void register() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", edPhoneFP.getText().toString());
        map.put("code", edCodeFP.getText().toString());
        map.put("rePassword", MD5Encrypt.MD5(edPwdFP.getText().toString()));
        map.put("password", MD5Encrypt.MD5(edPwdFP.getText().toString()));
        if (rb01F.isChecked()) {
            map.put("gender", 1);
        } else if (rb02F.isChecked()) {
            map.put("gender", 0);
        }
        ApiClient.requestNetHandle(mContext, AppConfig.requestRegister, "注册中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "注册：" + json);
                }
                ToastUtils.showTextToast(mContext, "注册成功");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
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
        map.put("type", tag);
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
