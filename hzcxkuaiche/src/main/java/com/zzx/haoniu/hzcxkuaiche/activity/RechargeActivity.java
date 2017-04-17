package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import ali.PayResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edPhoneR)
    EditText edPhoneR;
    @Bind(R.id.llPhoneR)
    LinearLayout llPhoneR;
    @Bind(R.id.edCodeR)
    EditText edCodeR;
    @Bind(R.id.tvCodeR)
    TextView tvCodeR;
    @Bind(R.id.llCodeR)
    LinearLayout llCodeR;
    @Bind(R.id.edRechargeR)
    EditText edRechargeR;
    @Bind(R.id.activity_recharge)
    LinearLayout activityRecharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityRecharge);
        initView();
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
        tvTitle.setText("充值");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.tvRechargeR, R.id.tvCodeR})
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
            case R.id.tvRechargeR:
                if (edPhoneR.getText().toString() == null || StringUtils.isEmpty(edPhoneR.getText().toString())
                        || edPhoneR.getText().toString().length() != 11) {
                    ToastUtils.showTextToast(mContext, "请输入十一位手机号!");
                    return;
                } else if (edCodeR.getText().toString() == null || StringUtils.isEmpty(edCodeR.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "验证码不能为空!");
                    return;
                } else if (edRechargeR.getText().toString() == null || StringUtils.isEmpty(edRechargeR.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "充值金额不能为空!");
                    return;
                } else if (Double.parseDouble(edRechargeR.getText().toString()) == 0) {
                    ToastUtils.showTextToast(mContext, "充值金额不能为零!");
                    return;
                }
                requestRecharge();
                break;
        }
    }

    private void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", edPhoneR.getText().toString());
        map.put("type", 8);
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

    private void requestRecharge() {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", edRechargeR.getText().toString());
        map.put("phone", edPhoneR.getText().toString());
        map.put("cmscode", edCodeR.getText().toString());
        map.put("apptype_type", "4");
        ApiClient.requestNetHandle(mContext, AppConfig.requestRecharge, "正在获取支付信息...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    pay(json);
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private static final int PAYFLAG = 310;

    private void pay(final String msg) {
        new Thread() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this); // 构造PayTask 对象
                String result = alipay.pay(msg, true); // 调用支付接口，获取支付结果
                Message msg = new Message();
                msg.what = PAYFLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 支付宝支付异步通知
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == PAYFLAG) {
                String resultStatus = new PayResult((String) msg.obj).getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if ("9000".equals(resultStatus)) {
                    Toast.makeText(mContext, "支付成功!", Toast.LENGTH_LONG).show();
                    //后续业务处理
                    finish();
                } else if ("8000".equals(resultStatus)) { //"8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    Toast.makeText(mContext, "支付结果确认中!", Toast.LENGTH_LONG).show();
                } else { //其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    Toast.makeText(mContext, "支付失败!", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
