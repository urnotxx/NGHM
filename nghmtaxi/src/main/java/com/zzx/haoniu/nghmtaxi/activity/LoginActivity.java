package com.zzx.haoniu.nghmtaxi.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.app.AppContext;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.LoginCallback;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.http.UserInfo;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.TTSController_1;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import iosdialog.dialogsamples.utils.L;
import self.androidbase.utils.MD5Encrypt;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.edPhoneL)
    EditText edPhoneL;
    @Bind(R.id.edPwdL)
    EditText edPwdL;
    @Bind(R.id.tvForPwdL)
    TextView tvForPwdL;
    @Bind(R.id.tvLoginL)
    TextView tvLoginL;
    @Bind(R.id.tvAgreementL)
    TextView tvAgreementL;
    @Bind(R.id.tvRegisterTaxiL)
    TextView tvRegisterTaxiL;
    @Bind(R.id.activity_login)
    LinearLayout activityLogin;
    @Bind(R.id.checkbox)
    CheckBox checkbox;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityLogin);
        boolean isLog, isUser;
        isLog = AppContext.getInstance().checkCallBackUser();
        isUser = AppContext.getInstance().checkUser();
        if (isLog && isUser) {
            startActivity(MainActivity.class);
            finish();
        } else if (!isLog && isUser) {//用户信息被清
            edPhoneL.setText(AppContext.getInstance().getUserInfo().getPhone());
        }
        initView();
    }


    @OnClick({R.id.tvForPwdL, R.id.tvLoginL, R.id.tvAgreementL, R.id.tvRegisterTaxiL, R.id.tvRechargL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvForPwdL:
                startActivity(ForgetPwdActivity.class);
                break;
            case R.id.tvLoginL:
                if (edPhoneL.getText().toString() == null || StringUtils.isEmpty(edPhoneL.getText().toString())
                        || !AppContext.getInstance().isPhone(edPhoneL.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请输入正确的手机号码!");
                    return;
                } else if (edPwdL.getText().toString() == null || StringUtils.isEmpty(edPwdL.getText().toString())
                        || edPwdL.getText().toString().length() < 6 || edPwdL.getText().toString().length() > 16) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位密码");
                    return;
                } else if (!checkbox.isChecked()) {
                    ToastUtils.showTextToast(mContext, "请同意用户协议!");
                    return;
                }
                showDiolog();
                login();
                break;
            case R.id.tvAgreementL:
                break;
            case R.id.tvRegisterTaxiL:
                Bundle bundle = new Bundle();
                startActivity(RegisterActivity.class, bundle);
                break;
            case R.id.tvRechargL:
                startActivity(RechargeActivity.class);
                break;
        }
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private void showDiolog() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("登陆中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void login() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", edPhoneL.getText().toString());
        String pwd = MD5Encrypt.MD5(edPwdL.getText().toString());
        map.put("password", pwd);
        map.put("registrationId", AppContext.getInstance().getRegistrationId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestLogin, "", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "登录回调:" + json);
                    LoginCallback callback = JSON.parseObject(json, LoginCallback.class);
                    if (callback.getLoginId() != null) {
                        AppContext.getInstance().saveCallBack(callback);
                        JPushInterface.setAlias(getApplicationContext(), callback.getLoginId(), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                            }
                        });
                        JPushInterface.resumePush(getApplicationContext());
                        requestUserInfo();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                progressDialog.dismiss();
            }
        });
    }

    private void requestUserInfo() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestUserInfo, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "获取个人信息:" + json);
                    UserInfo userInfo = JSON.parseObject(json, UserInfo.class);
                    if (userInfo != null) {
                        if (userInfo.getType().equals("3")) {
                            AppContext.getInstance().saveUserInfo(userInfo);
                            progressDialog.dismiss();
                            startActivity(MainActivity.class);
                            finish();
                        } else {
                            ToastUtils.showTextToast(mContext, "请用正确的出租车账号登陆!");
                            AppContext.getInstance().cleanLoginCallback();
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                progressDialog.dismiss();
            }
        });
    }

//    private void requestCars(String msg) {
//        ApiClient.requestNetHandle(mContext, AppConfig.requestCars, msg, null, new ResultListener() {
//            @Override
//            public void onSuccess(String json) {
//                if (json != null && !StringUtils.isEmpty(json)) {
//                    L.d("TAG", "获取车辆信息:" + json);
//                    List<CarInfo> infos = JSON.parseArray(json, CarInfo.class);
//                    if (infos != null && infos.size() > 0) {
//                        LoginCallback loginCallback = AppContext.getInstance().getLoginCallback();
//                        loginCallback.setCarId(infos.get(0).getId());
//                        AppContext.getInstance().saveCallBack(loginCallback);
//                        startActivity(MainActivity.class);
//                        finish();
//                    }
//                }
//                if (progressDialog != null && progressDialog.isShowing())
//                    progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                if (progressDialog != null && progressDialog.isShowing())
//                    progressDialog.dismiss();
//                ToastUtils.showTextToast(mContext,msg);
//            }
//        });
//    }
}
