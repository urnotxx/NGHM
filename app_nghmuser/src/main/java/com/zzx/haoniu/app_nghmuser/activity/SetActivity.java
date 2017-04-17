package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialogsamples.extra.CustomBaseDialog;
import self.androidbase.views.SelfLinearLayout;

public class SetActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rlAddressS)
    RelativeLayout rlAddressS;
    @Bind(R.id.rlGuideS)
    RelativeLayout rlGuideS;
    @Bind(R.id.rlUpdateS)
    RelativeLayout rlUpdateS;
    @Bind(R.id.rlAboutS)
    RelativeLayout rlAboutS;
    @Bind(R.id.rlLawS)
    RelativeLayout rlLawS;
    @Bind(R.id.tvExitS)
    TextView tvExitS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
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

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        tvTitle.setText("我的设置");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.rlAddressS, R.id.rlGuideS, R.id.rlUpdateS, R.id.rlAboutS, R.id.rlLawS, R.id.tvExitS, R.id.rlModifyPwdS})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rlModifyPwdS:
                startActivity(ModifyPwdActivity.class);
                break;
            case R.id.rlAddressS:
                startActivity(CommeonAddressActivity.class);
                break;
            case R.id.rlGuideS:
                startActivity(UserGuildActivity.class);
                break;
            case R.id.rlUpdateS:
                AppConfig.checkVersion(this, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            ToastUtils.showTextToast(mContext, "已是最新版本!");
                        }
                    }
                }, 1, 0);
                break;
            case R.id.rlAboutS:
                startActivity(AboutActivity.class);
                break;
            case R.id.rlLawS:
                startActivity(LawActivity.class);
                break;
            case R.id.tvExitS:
                showExitDialog();
                break;
        }
    }

    private CustomBaseDialog dialog;

    private void showExitDialog() {
        if (dialog == null)
            dialog = new CustomBaseDialog(this);
        dialog.show();
        dialog.tv_content.setText("确定要退出么(●—●)");
        dialog.tv_cancel.setText("取消");
        dialog.tv_exit.setText("确定");
        dialog.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void logOut() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestLogout, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "登出:" + json);
                }
                EventBus.getDefault().post(new CommonEventBusEnity("exit", null));
                dialog.dismiss();
                AppContext.getInstance().cleanLoginCallback();
                startActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dialog.dismiss();
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }
}
