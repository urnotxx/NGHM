package com.zzx.haoniu.hzcxdj.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        steepStatusBar();
        initView();
    }

    @Override
    public void initParms(Bundle parms) {
        tvTitle.setText("注册");
    }

    @Override
    public int bindLayout() {
        return 0;
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
                        || !AppContext.getInstance().isPhone(edPhoneR.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请输入正确的手机号码!");
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
                        edPwdR.getText().toString().length() < 6 || edPwdR.getText().toString().length() > 13) {
                    ToastUtils.showTextToast(mContext, "请输入6~13位密码");
                    return;
                }
//                else if (rb01.isChecked() == false && rb02.isChecked() == false) {
//                    ToastUtils.showTextToast(mContext, "请选择您要注册的类型!");
//                    return;
//                }
                Bundle bundle = new Bundle();
                bundle.putString("phone", edPhoneR.getText().toString());
                bundle.putString("code", edCodeR.getText().toString());
                bundle.putString("pwd", edPwdR.getText().toString());
                bundle.putString("type", "2");
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
                tvCodeR.setBackgroundResource(R.drawable.shap_gray_10);
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
        tvCodeR.setBackgroundResource(R.drawable.border_orggray10);
        tvCodeR.setText("获取验证码");
        tvCodeR.setClickable(true);
    }
}
