package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialog.listener.OnBtnClickL;
import iosdialog.dialog.widget.NormalDialog;
import iosdialog.dialogsamples.utils.L;
import self.androidbase.views.SelfLinearLayout;

public class NameAuthenticationActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edNameNA)
    EditText edNameNA;
    @Bind(R.id.edCardNumNA)
    EditText edCardNumNA;
    @Bind(R.id.tvExamineNA)
    TextView tvExamineNA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_authentication);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            tag = getIntent().getIntExtra("tag", 0);
        }
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
    }

    @OnClick({R.id.ll_back, R.id.tvExamineNA})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                showExitDoalog();
                break;
            case R.id.tvExamineNA:
                showPromptDialog();
                break;
        }
    }

    private NormalDialog promptDialo;

    private void showPromptDialog() {
        if (promptDialo == null)
            promptDialo = new NormalDialog(mContext);
        promptDialo.content("实名信息认证通过后将无法修改，冒用他人信息会导致账号被封禁。是否提交？")//
                .isTitleShow(false)
                .show();
        promptDialo.setLeftText("修改信息");
        promptDialo.setRightText("确认提交");
        promptDialo.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        promptDialo.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        updateRealNameAuthentication();
                    }
                });
    }

    private NormalDialog exitDdialog;

    @Override
    public void onBackPressed() {
        showExitDoalog();
    }

    private void showExitDoalog() {
        if (exitDdialog == null)
            exitDdialog = new NormalDialog(mContext);
        exitDdialog.content("确定放弃编辑吗？")//
                .isTitleShow(false)
                .show();
        exitDdialog.setLeftText("确定放弃");
        exitDdialog.setRightText("取消放弃");
        exitDdialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        finish();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        exitDdialog.dismiss();
                    }
                });
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_name_authentication;
    }

    @Override
    public void initView() {
        tvTitle.setText("实名认证");
        tvExamineNA.setClickable(false);
        edCardNumNA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edNameNA.getText().toString() != null && !StringUtils.isEmpty(edNameNA.getText().toString())) {
                    if (AppContext.getInstance().isName(edNameNA.getText().toString())) {
                        if (editable.toString() != null) {
                            if (AppContext.getInstance().personIdValidation(editable.toString())) {
                                tvExamineNA.setClickable(true);
                                tvExamineNA.setBackgroundResource(R.drawable.border_gray5);
                            } else {
                                tvExamineNA.setClickable(false);
                                tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                            }
                        } else {
                            tvExamineNA.setClickable(false);
                            tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                        }
                    } else {
                        tvExamineNA.setClickable(false);
                        tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                    }
                } else {
                    tvExamineNA.setClickable(false);
                    tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                }
            }
        });
        edNameNA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edCardNumNA.getText().toString() != null && !StringUtils.isEmpty(edCardNumNA.getText().toString())) {
                    if (AppContext.getInstance().personIdValidation(edCardNumNA.getText().toString())) {
                        if (editable.toString() != null) {
                            if (AppContext.getInstance().isName(editable.toString())) {
                                tvExamineNA.setClickable(true);
                                tvExamineNA.setBackgroundResource(R.drawable.border_gray5);
                            } else {
                                tvExamineNA.setClickable(false);
                                tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                            }
                        } else {
                            tvExamineNA.setClickable(false);
                            tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                        }
                    } else {
                        tvExamineNA.setClickable(false);
                        tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                    }
                } else {
                    tvExamineNA.setClickable(false);
                    tvExamineNA.setBackgroundResource(R.drawable.shap_darkgray_5);
                }
            }
        });
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private void updateRealNameAuthentication() {
        Map<String, Object> map = new HashMap<>();
        map.put("realname", edNameNA.getText().toString());
        map.put("idCard", edCardNumNA.getText().toString());
        ApiClient.requestNetHandle(mContext, AppConfig.updateRealNameAuthentication, "上传信息中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                promptDialo.dismiss();
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "实名认证:" + json);
                    UserInfo userInfo = AppContext.getInstance().getUserInfo();
                    userInfo.setIs_ensure_idcard("1");
                    AppContext.getInstance().saveUserInfo(userInfo);
                    if (tag == 0) {
                        ToastUtils.showTextToast(mContext, "提交信息成功!");
                    } else {
                        startActivity(CarAuthenticationActivity.class);
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                promptDialo.dismiss();
            }
        });
    }
}
