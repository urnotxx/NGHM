package com.zzx.haoniu.hzcxkuaiche.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.FileInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import iosdialog.dialog.listener.OnBtnClickL;
import iosdialog.dialog.listener.OnOperItemClickL;
import iosdialog.dialog.widget.ActionSheetDialog;
import iosdialog.dialog.widget.NormalDialog;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.utils.MD5Encrypt;
import self.androidbase.views.SelfLinearLayout;

public class RegisterNextActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edNameRN)
    EditText edNameRN;
    @Bind(R.id.edCarPlatRN)
    EditText edCarPlatRN;
    @Bind(R.id.tvRegisterRN)
    TextView tvRegisterRN;
    @Bind(R.id.tvSexUD)
    TextView tvSexUD;
    @Bind(R.id.llSexUD)
    LinearLayout llSexUD;
    @Bind(R.id.ivJiaShiRN)
    ImageView ivJiaShiRN;
    @Bind(R.id.ivXingShiRN)
    ImageView ivXingShiRN;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.llTop)
    RelativeLayout llTop;
    @Bind(R.id.edIdenCodeRN)
    EditText edIdenCodeRN;
    @Bind(R.id.edCarColorRN)
    EditText edCarColorRN;
    @Bind(R.id.edCarMileageRN)
    EditText edCarMileageRN;
    @Bind(R.id.tvCarBrandRN)
    TextView tvCarBrandRN;
    @Bind(R.id.rlBrandRN)
    LinearLayout rlBrandRN;
    @Bind(R.id.tvCarCityRN)
    TextView tvCarCityRN;
    @Bind(R.id.rlCityRN)
    LinearLayout rlCityRN;
    @Bind(R.id.ivCarPhoneRN)
    ImageView ivCarPhoneRN;
    @Bind(R.id.activity_register_next)
    RelativeLayout activityRegisterNext;
    @Bind(R.id.edCardNumRN)
    EditText edCardNumRN;


    private final static int REQUEST_CITY = 6;//城市返回结果码
    private final static int REQUEST_CAR = 5;//车辆返回结果码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityRegisterNext);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
        initView();
    }

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
    }

    private Bundle bundle;

    @Override
    public int bindLayout() {
        return R.layout.activity_register_next;
    }

    @Override
    public void initView() {
        tvTitle.setText("个人信息");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.tvRegisterRN,
            R.id.llSexUD, R.id.ivJiaShiRN, R.id.ivXingShiRN, R.id.rlBrandRN, R.id.rlCityRN, R.id.ivCarPhoneRN})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                showExitDoalog();
                break;
            case R.id.tvRegisterRN:
                if (edNameRN.getText().toString() == null || StringUtils.isEmpty(edNameRN.getText().toString()) ||
                        !AppContext.getInstance().isName(edNameRN.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请输入正确的真实姓名!");
                    return;
                } else if (edCardNumRN.getText().toString() == null || StringUtils.isEmpty(edCardNumRN.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请填写本人身份证号码");
                    return;
                } else if (jiaShiPath == null || StringUtils.isEmpty(jiaShiPath)) {
                    ToastUtils.showTextToast(mContext, "请上传驾驶证照片");
                    return;
                } else if (xingShiPath == null || StringUtils.isEmpty(xingShiPath)) {
                    ToastUtils.showTextToast(mContext, "请上传行驶证照片");
                    return;
                } else if (xingShiPath == null || StringUtils.isEmpty(xingShiPath)) {
                    ToastUtils.showTextToast(mContext, "请上传行驶证照片");
                    return;
                }
                register();
                break;
            case R.id.llSexUD:
                showSex();
                break;
            case R.id.ivJiaShiRN:
                tag = 1;
                showPhotoDialog();
                break;
            case R.id.ivXingShiRN:
                tag = 2;
                showPhotoDialog();
                break;
            case R.id.ivCarPhoneRN:
                tag = 3;
                showPhotoDialog();
                break;
            case R.id.rlBrandRN:
                startActivityForResult(new Intent(mContext, CarBrandActivity.class)
                        , REQUEST_CAR);
                break;
            case R.id.rlCityRN:
                startActivityForResult(new Intent(mContext, CityActivity.class)
                        , REQUEST_CITY);
                break;
        }
    }

    final String[] stringItems = {"相册", "相机"};
    private ExpandableListView elv;
    private View decorView;
    private ActionSheetDialog photoDialog;
    private String path;
    private File imgFile;
    private int tag = 0;
    private ActionSheetDialog sexDialog;
    private String jiaShiPath, xingShiPath, carPath;

    private String proCode, cityCode, areaCode;
    private String proName, cityName, areaName;
    private String brandId, modleId;
    private String brandName, modleName;

    private void register() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", bundle.getString("phone"));
        map.put("smsCode", bundle.getString("code"));
        map.put("type", "4");
        map.put("password", MD5Encrypt.MD5(bundle.getString("pwd")));
        map.put("name", edNameRN.getText().toString());
        map.put("drivingLicense", xingShiPath);
        map.put("driverLicense", jiaShiPath);
        map.put("picture", carPath);
        map.put("distance", edCarMileageRN.getText().toString());
        map.put("county", areaCode);
        map.put("province", proCode);
        map.put("city", cityCode);
        map.put("plate_no", edCarPlatRN.getText().toString());
        map.put("color", edCarColorRN.getText().toString());
        map.put("vin", edIdenCodeRN.getText().toString());
        map.put("idCard", edCardNumRN.getText().toString());
        map.put("brand", brandId);
        map.put("model_type", modleId);
        if (tvSexUD.getText().toString().equals("男")) {
            map.put("gender", 1);
        } else {
            map.put("gender", 0);
        }
        ApiClient.requestNetHandle(mContext, AppConfig.requestRegister, "注册帐号中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "司机注册:" + json);
                }
                startActivity(RegisterSuccessActivity.class);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void showSex() {
        final String[] stringItems = {"男", "女"};
        if (sexDialog == null)
            sexDialog = new ActionSheetDialog(mContext, stringItems, null);
        sexDialog.title("性别选择")//
                .titleTextSize_SP(13.5f)//
                .show();
        sexDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvSexUD.setText(stringItems[position]);
                sexDialog.dismiss();
            }
        });
    }

    private void showPhotoDialog() {
        if (photoDialog == null) {
            photoDialog = new ActionSheetDialog(mContext, stringItems, elv);
        }
        photoDialog.isTitleShow(false);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + +System.currentTimeMillis()
                + ".jpg";
        photoDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    photoDialog.dismiss();
                    selectPhoto();
                } else {
                    photoDialog.dismiss();
                    takePhoto();
                }
            }
        });
        photoDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CITY) {
            if (resultCode == CityActivity.RESUALT_CITY) {
                proCode = data.getStringExtra("proCode");
                cityCode = data.getStringExtra("cityCode");
                areaCode = data.getStringExtra("areaCode");
                proName = data.getStringExtra("proName");
                cityName = data.getStringExtra("cityName");
                areaName = data.getStringExtra("areaName");
                tvCarCityRN.setText(proName + cityName + areaName);
            }
        } else if (requestCode == REQUEST_CAR) {
            if (resultCode == CarBrandActivity.RESUALT_CAR) {
                brandId = data.getStringExtra("brandId");
                modleId = data.getStringExtra("modleId");
                brandName = data.getStringExtra("brandName");
                modleName = data.getStringExtra("modleName");
                tvCarBrandRN.setText(brandName + modleName);
            }
        }
    }

    private void uploadFile() {
        Map<String, Object> map = new HashMap<>();
        map.put("file", imgFile);
        ApiClient.requestNetHandle(mContext, AppConfig.updateFile, "上传图片中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "上传图片的接口:" + json);
                    FileInfo info = JSON.parseObject(json, FileInfo.class);
                    if (info != null && info.getPath() != null && !StringUtils.isEmpty(info.getPath())) {
                        if (tag == 1) {
                            jiaShiPath = info.getPath();
                            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + jiaShiPath, ivJiaShiRN);
                        } else if (tag == 2) {
                            xingShiPath = info.getPath();
                            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + xingShiPath, ivXingShiRN);
                        } else if (tag == 3) {
                            carPath = info.getPath();
                            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + carPath, ivCarPhoneRN);
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }


    private void selectPhoto() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showTextToast(mContext, "请打开相册权限");
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return;
        }
        getTakePhoto().onPickFromDocuments();
    }

    private void takePhoto() {
        File file = new File(path);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showTextToast(mContext, "请打开相机权限");
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }
        getTakePhoto().onPickFromCapture(imageUri);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result.getImage() != null) {
            path = result.getImage().getOriginalPath();
            L.d("TAG", "LUJING:" + path);
            imgFile = new File(path);
            uploadFile();
        }
    }

    private NormalDialog exitDdialog;

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
                        exitDdialog.dismiss();
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
    public void onBackPressed() {
        showExitDoalog();
    }


}
