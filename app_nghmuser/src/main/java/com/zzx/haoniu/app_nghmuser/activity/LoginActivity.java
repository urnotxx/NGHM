package com.zzx.haoniu.app_nghmuser.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.LoginCallback;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import iosdialog.dialogsamples.utils.L;
import self.androidbase.utils.MD5Encrypt;

public class LoginActivity extends BaseActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

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
    @Bind(R.id.tvRegisterL)
    TextView tvRegisterL;
    @Bind(R.id.checkboxL)
    CheckBox checkboxL;
    @Bind(R.id.activity_login)
    LinearLayout activityLogin;

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

    @OnClick({R.id.tvForPwdL, R.id.tvLoginL, R.id.tvAgreementL, R.id.tvRegisterL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvForPwdL:
                Bundle bundle = new Bundle();
                bundle.putInt("tag", 2);
                startActivity(ForgetPwdActivity.class, bundle);
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
                } else if (!checkboxL.isChecked()) {
                    ToastUtils.showTextToast(mContext, "请您同意用户协议");
                    return;
                }
                showDiolog();
                login();
                break;
            case R.id.tvAgreementL:
                break;
            case R.id.tvRegisterL:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("tag", 1);
                startActivity(ForgetPwdActivity.class, bundle1);
                break;
        }
    }

    private ProgressDialog progressDialog;

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
                        AppContext.getInstance().saveUserInfo(userInfo);
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        ToastUtils.showTextToast(mContext, "登陆成功!");
                        startActivity(MainActivity.class);
                        finish();
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

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.READ_PHONE_STATE
    };
//    /**
//     * 需要进行检测的权限数组
//     */
//    protected String[] needPermissions = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
//    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    /**
     *
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n请点击\"设置\"---\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
