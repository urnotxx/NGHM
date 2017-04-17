package com.zzx.haoniu.app_nghmuser.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import self.androidbase.views.SelfLinearLayout;

public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.civHeadUI)
    CircleImageView civHeadUI;
    @Bind(R.id.tvNameUI)
    TextView tvNameUI;
    @Bind(R.id.tvOccupation)
    TextView tvOccupation;
    @Bind(R.id.tvNameCertified)
    TextView tvNameCertified;
    @Bind(R.id.llNameRZ)
    LinearLayout llNameRZ;
    @Bind(R.id.tvDriverCertified)
    TextView tvDriverCertified;
    @Bind(R.id.llDriverRZ)
    LinearLayout llDriverRZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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

    @OnClick({R.id.ll_back, R.id.ll_right, R.id.llNameRZ, R.id.llDriverRZ})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_right:
                startActivity(new Intent(mContext, UserDetialActivity.class));
//                showDate();
                break;
            case R.id.llNameRZ:
                startActivity(new Intent(mContext, NameAuthenticationActivity.class));
                break;
            case R.id.llDriverRZ:
                UserInfo userInfo = AppContext.getInstance().getUserInfo();
                if (userInfo.getIs_ensure_idcard() == null
                        || StringUtils.isEmpty(userInfo.getIs_ensure_idcard())
                        || !userInfo.getIs_ensure_idcard().equals("1")) {
                    startActivity(new Intent(mContext, NameAuthenticationActivity.class));
                } else {
                    startActivity(new Intent(mContext, CarAuthenticationActivity.class));
                }
                break;
        }
    }

    private DatePickerDialog datePickerDialog;

    private void showDate() {
        // 获取日历对象
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        L.d("TAG", "year:" + year + "---month:" + month + "---day:" + day);
        // 获取当前对应的年、月、日的信息
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year,
                                      int monthOfYear, int dayOfMonth) {
                    tvSubmit.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }, year, month + 1, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        datePickerDialog.show();
    }


    @Override
    public int bindLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        tvTitle.setText("");
        tvSubmit.setText("编辑资料");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo = AppContext.getInstance().getUserInfo();
        tvNameUI.setText(userInfo.getNick_name());
        if (userInfo.getHead_portrait() != null) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + userInfo.getHead_portrait(), civHeadUI);
        }
        if (userInfo.getIs_ensure_idcard() != null
                && !StringUtils.isEmpty(userInfo.getIs_ensure_idcard())
                && userInfo.getIs_ensure_idcard().equals("1")) {
            tvNameCertified.setText("已认证");
            llNameRZ.setClickable(false);
        }
        if (userInfo.getIs_ensure_carowner() != null
                && !StringUtils.isEmpty(userInfo.getIs_ensure_carowner())
                && userInfo.getIs_ensure_carowner().equals("1")) {
            tvDriverCertified.setText("已认证");
            llDriverRZ.setClickable(false);
        }
    }
}
