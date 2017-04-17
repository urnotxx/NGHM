package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialog.listener.OnBtnClickL;
import iosdialog.dialog.widget.NormalDialog;
import self.androidbase.activity.ImagePagerActivity;
import self.androidbase.views.SelfLinearLayout;

public class OwnerAuthenticationActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.llModifyCarPlateOA)
    LinearLayout llModifyCarPlateOA;
    @Bind(R.id.tvNameOA)
    TextView tvNameOA;
    @Bind(R.id.tvCarLicenseOA)
    TextView tvCarLicenseOA;
    @Bind(R.id.tvPicCarPlate)
    TextView tvPicCarPlate;
    @Bind(R.id.llModifyTravelCardOA)
    LinearLayout llModifyTravelCardOA;
    @Bind(R.id.tvCarNumOA)
    TextView tvCarNumOA;
    @Bind(R.id.tvCarBelongOA)
    TextView tvCarBelongOA;
    @Bind(R.id.tvCarBlandOA)
    TextView tvCarBlandOA;
    @Bind(R.id.tvLoginOA)
    TextView tvLoginOA;
    @Bind(R.id.tvPicTravelOA)
    TextView tvPicTravelOA;
    @Bind(R.id.tvAgreementOA)
    TextView tvAgreementOA;
    @Bind(R.id.tvSubmitOA)
    TextView tvSubmitOA;

    /***
     * 提交审核页面
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_authentication);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误，请重新填写信息!");
            return;
        }
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("ownerFinish")) {
            finish();
        } else if (event.getSendCode().equals("authentication")) {
            finish();
        }else   if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    private Bundle bundle;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_owner_authentication;
    }

    private String driverName, driverLicense, driverPlatPhoto, driverPlatNum, driverBelong, carDetial, driverLogin, travelCardPhoto;
    private String travelCardPhotoNet, driverPlatPhotoNet;
    private String urls[];

    @Override
    public void initView() {
        tvTitle.setText("车主认证");
        if (bundle != null) {
            driverName = bundle.getString("driverName");
            driverLicense = bundle.getString("driverLicense");
            driverPlatPhoto = bundle.getString("driverPlatPhoto");
            travelCardPhoto = bundle.getString("travelCardPhoto");
            driverPlatNum = bundle.getString("driverPlatNum");
            driverBelong = bundle.getString("driverBelong");
            carDetial = bundle.getString("carDetial");
            driverLogin = bundle.getString("driverLogin");
            String travelCardPhotoNet = bundle.getString("travelCardPhotoNet");
            String driverPlatPhotoNet = bundle.getString("driverPlatPhotoNet");
            if (driverName != null) {
                tvNameOA.setText(driverName);
            }
            if (driverLicense != null) {
                tvCarLicenseOA.setText(driverLicense);
            }
            if (driverPlatPhotoNet != null && travelCardPhotoNet != null) {
                urls = new String[2];
                this.travelCardPhotoNet = travelCardPhotoNet;
                this.travelCardPhotoNet = travelCardPhotoNet;
                this.driverPlatPhotoNet = driverPlatPhotoNet;
                urls[0] = AppConfig.mainPicUrl + driverPlatPhotoNet;
                urls[1] = AppConfig.mainPicUrl + travelCardPhotoNet;
            }
            if (driverPlatNum != null) {
                tvCarNumOA.setText(driverPlatNum);
            }
            if (driverBelong != null) {
                tvCarBelongOA.setText(driverBelong);
            }
            if (carDetial != null) {
                tvCarBlandOA.setText(carDetial);
            }
            if (driverLogin != null) {
                tvLoginOA.setText(driverLogin);
            }
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.llModifyCarPlateOA, R.id.tvPicCarPlate, R.id.llModifyTravelCardOA, R.id.tvPicTravelOA, R.id.tvAgreementOA, R.id.tvSubmitOA})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                showExitDoalog();
                break;
            case R.id.llModifyCarPlateOA:
                startActivity(CarAuthenticationActivity.class, bundle);
                break;
            case R.id.tvPicCarPlate:
                startActivity(new Intent(mContext, ImagePagerActivity.class)
                        .putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0).putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls));
                break;
            case R.id.llModifyTravelCardOA:
                startActivity(TravelCardAuthenticationActivity.class, bundle);
                break;
            case R.id.tvPicTravelOA:
                startActivity(new Intent(mContext, ImagePagerActivity.class)
                        .putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1).putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls));
                break;
            case R.id.tvAgreementOA:
                break;
            case R.id.tvSubmitOA:
                requestCarownerAuthentication();
                break;
        }
    }

    private void requestCarownerAuthentication() {
        Map<String, Object> map = new HashMap<>();
        map.put("realname", driverName);
        map.put("driverpicdir", driverPlatPhotoNet);
        map.put("drivercarnumber", driverLicense);
        map.put("carnumber", driverPlatNum);
        map.put("carowner", driverBelong);
        map.put("varbrand", carDetial);
        map.put("licregisteddate", driverLogin);
        map.put("licpicdir", travelCardPhotoNet);
        ApiClient.requestNetHandle(mContext, AppConfig.updateCarownerAuthentication, "提交信息中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "车主认证：" + json);
                    File file = new File(driverPlatPhoto);
                    File file1 = new File(travelCardPhoto);
                    file.delete();
                    file1.delete();
                    UserInfo userInfo = AppContext.getInstance().getUserInfo();
                    userInfo.setIs_ensure_idcard("1");
                    ToastUtils.showTextToast(mContext, "提交信息成功!");
                    finish();
                    userInfo.setIs_ensure_carowner("1");
                    AppContext.getInstance().saveUserInfo(userInfo);
                    EventBus.getDefault().post(new CommonEventBusEnity("authentication", null));
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }


    private NormalDialog dialog;

    private void showExitDoalog() {
        dialog = new NormalDialog(mContext);
        dialog.content("确定放弃提交吗？")//
                .isTitleShow(false)
                .show();
        dialog.setLeftText("确定放弃");
        dialog.setRightText("取消放弃");
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        EventBus.getDefault().post(new CommonEventBusEnity("authentication", null));
                        finish();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showExitDoalog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
