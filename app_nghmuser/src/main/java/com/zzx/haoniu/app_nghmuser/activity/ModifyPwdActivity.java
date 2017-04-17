package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ModifyPwdActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.edPwd1MP)
    EditText edPwd1MP;
    @Bind(R.id.edPwd2MP)
    EditText edPwd2MP;
    @Bind(R.id.edPwd3MP)
    EditText edPwd3MP;
    @Bind(R.id.tvUpdateMP)
    TextView tvUpdateMP;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.activity_modify_pwd)
    LinearLayout activityModifyPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        steepStatusBar();
        tvTitle.setText("修改密码");
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

    @OnClick({R.id.ll_back, R.id.tvUpdateMP})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tvUpdateMP:
                if (edPwd1MP.getText().toString() == null || StringUtils.isEmpty(edPwd1MP.getText().toString())
                        || (edPwd1MP.getText().toString().length() < 6 || edPwd1MP.getText().toString().length() > 16)) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位原密码");
                    return;
                } else if (edPwd2MP.getText().toString() == null || StringUtils.isEmpty(edPwd2MP.getText().toString())
                        || edPwd2MP.getText().toString().length() < 6 || edPwd2MP.getText().toString().length() > 16) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位新密码");
                    return;
                } else if (edPwd3MP.getText().toString() == null || StringUtils.isEmpty(edPwd3MP.getText().toString())
                        || edPwd3MP.getText().toString().length() < 6 || edPwd3MP.getText().toString().length() > 16) {
                    ToastUtils.showTextToast(mContext, "请输入6~16位确认密码");
                    return;
                } else if (!edPwd2MP.getText().toString().equals(edPwd3MP.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "两次密码不一致，请重新输入!");
                    return;
                }
                updatePwd();
                break;
        }
    }

    private void updatePwd() {
        Map<String, Object> map = new HashMap<>();
        map.put("password", MD5Encrypt.MD5(edPwd1MP.getText().toString()));
        map.put("newPassword", MD5Encrypt.MD5(edPwd2MP.getText().toString()));
        map.put("rePassword", MD5Encrypt.MD5(edPwd2MP.getText().toString()));
        ApiClient.requestNetHandle(mContext, AppConfig.modifyPwd, "修改密码中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "修改密码成功:" + json);
                }
                ToastUtils.showTextToast(mContext, "密码修改成功!");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }
}
