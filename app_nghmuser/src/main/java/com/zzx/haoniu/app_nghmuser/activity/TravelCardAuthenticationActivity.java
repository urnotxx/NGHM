package com.zzx.haoniu.app_nghmuser.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.FileInfo;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.utils.AppUtils;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialog.listener.OnBtnClickL;
import iosdialog.dialog.listener.OnOperItemClickL;
import iosdialog.dialog.widget.ActionSheetDialog;
import iosdialog.dialog.widget.NormalDialog;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.utils.ImageUtils;
import self.androidbase.views.SelfLinearLayout;

public class TravelCardAuthenticationActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edPlatNumTC)
    EditText edPlatNumTC;
    @Bind(R.id.edCarBelongToTC)
    EditText edCarBelongToTC;
    @Bind(R.id.edCarBrandTC)
    EditText edCarBrandTC;
    @Bind(R.id.tvTravelCarTC)
    TextView tvTravelCarTC;
    @Bind(R.id.ivPlatTC)
    ImageView ivPlatTC;
    @Bind(R.id.tvNextTC)
    TextView tvNextTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_card_authentication);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误，请重新填写信息");
            finish();
        }
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
        tvTitle.setText("车主认证");
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("ownerFinish")) {
            finish();
        } else if (event.getSendCode().equals("authentication")) {
            finish();
        } else if (event.getSendCode().equals("unLogInUser")) {
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
        return R.layout.activity_travel_card_authentication;
    }

    @Override
    public void initView() {
        if (bundle != null) {
            if (bundle.getString("driverPlatNum") != null) {
                String driverPlatNum = bundle.getString("driverPlatNum");
                String driverBelong = bundle.getString("driverBelong");
                String carDetial = bundle.getString("carDetial");
                String driverLogin = bundle.getString("driverLogin");
                String travelCardPhoto = bundle.getString("travelCardPhoto");
                String travelCardPhotoNet = bundle.getString("travelCardPhotoNet");
                edPlatNumTC.setText(driverPlatNum);
                if (driverBelong != null)
                    edCarBelongToTC.setText(driverBelong);
                if (carDetial != null)
                    edCarBrandTC.setText(carDetial);
                if (driverLogin != null)
                    tvTravelCarTC.setText(driverLogin);
                if (travelCardPhoto != null) {
                    path = travelCardPhoto;
                    ivPlatTC.setImageBitmap(BitmapFactory.decodeFile(path));
                    imgFile = new File(path);
                }
                if (travelCardPhotoNet != null) {
                    networkPath = travelCardPhotoNet;
                }
                tvNextTC.setText("修改完成");
            }
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ivPlatTC, R.id.tvNextTC, R.id.tvTravelCarTC})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                showExitDoalog();
                break;
            case R.id.ivPlatTC:
                showPhotoDialog();
                break;
            case R.id.tvNextTC:
                if (edPlatNumTC.getText().toString() == null || StringUtils.isEmpty(edPlatNumTC.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "车牌号码不能为空!");
                    return;
                } else if (edCarBelongToTC.getText().toString() == null || StringUtils.isEmpty(edCarBelongToTC.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "车辆所有人不能为空!");
                    return;
                } else if (edCarBrandTC.getText().toString() == null || StringUtils.isEmpty(edCarBrandTC.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "车辆品牌不能为空");
                    return;
                } else if (tvTravelCarTC.getText().toString() == null || StringUtils.isEmpty(tvTravelCarTC.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "注册日期不能为空!");
                    return;
                } else if (imgFile == null) {
                    ToastUtils.showTextToast(mContext, "行驶证照片不能为空!");
                    return;
                }
                EventBus.getDefault().post(new CommonEventBusEnity("ownerFinish", null));
                bundle.putString("driverPlatNum", edPlatNumTC.getText().toString());
                bundle.putString("driverBelong", edCarBelongToTC.getText().toString());
                bundle.putString("carDetial", edCarBrandTC.getText().toString());
                bundle.putString("driverLogin", tvTravelCarTC.getText().toString());
                bundle.putString("travelCardPhoto", path);
                bundle.putString("travelCardPhotoNet", networkPath);
                startActivity(OwnerAuthenticationActivity.class, bundle);
                finish();
                break;
            case R.id.tvTravelCarTC:
                showDate();
                break;
        }
    }

    final String[] stringItems = {"相册", "相机"};
    private ExpandableListView elv;
    private View decorView;
    private ActionSheetDialog photoDialog;
    private String path;
    private File imgFile;

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
                        EventBus.getDefault().post(new CommonEventBusEnity("authentication", null));
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

    private DatePickerDialog datePickerDialog;

    private void showDate() {
        // 获取日历对象
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        // 获取当前对应的年、月、日的信息
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year,
                                      int monthOfYear, int dayOfMonth) {
                    tvTravelCarTC.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                }
            }, year, month + 1, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        datePickerDialog.show();
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

    private void selectPhoto() {
        Intent picture = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, AppUtils.StartImages);
    }

    private void takePhoto() {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + +System.currentTimeMillis()
                + ".jpg";
        final Uri uri = Uri.fromFile(new File(path));
        try {
            Intent intent = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, AppUtils.StartCamera);
        } catch (Exception E) {
            ToastUtils.showTextToast(mContext, "没有拍照权限");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == -1) {
                if (requestCode == AppUtils.StartImages
                        || requestCode == AppUtils.StartCamera
                        || requestCode == AppUtils.StartCamera2) {
                    if (requestCode == AppUtils.StartImages) {
                        Uri selectedImage = data.getData();
                        if (selectedImage.toString().startsWith("content")) {
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = mContext.getContentResolver().query(
                                    selectedImage, filePathColumns, null, null,
                                    null);
                            c.moveToFirst();
                            int columnIndex = c
                                    .getColumnIndex(filePathColumns[0]);
                            String currImagePath = c.getString(columnIndex);
                            Bitmap bit = ImageUtils.getSmallBitmap(currImagePath);
                            AppUtils.saveBitmapToPath(bit, path);
                            c.close();
                        } else if (selectedImage.toString().startsWith("file")) {
                            String currImagePath = selectedImage.getPath();
                            Bitmap bit = ImageUtils.getSmallBitmap(currImagePath);
                            AppUtils.saveBitmapToPath(bit, path);
                        }
                    } else if (requestCode == AppUtils.StartCamera) {
                        Bitmap bit = ImageUtils.getSmallBitmap(path);
                        AppUtils.saveBitmapToPath(bit, path);
                    }
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 2);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 600);
                    intent.putExtra("outputY", 300);
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
                    intent.putExtra("return-data", false);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true); // no face detection
                    startActivityForResult(intent, AppUtils.StartCutPicture);
                } else if (requestCode == AppUtils.StartCutPicture) {
                    long fileSize = AppContext.getInstance().getFileSize(new File(path));
                    if (fileSize != 0 && fileSize > 1024 * 500) {
                        ImageUtils.doCompressImage(path, path);
                        long fileSize1 = AppContext.getInstance().getFileSize(new File(path));
                        L.d("TAG", "000000+" + AppContext.getInstance().formetFileSize(fileSize1));
                    }
                    imgFile = new File(path);
                    ivPlatTC.setImageBitmap(BitmapFactory.decodeFile(path));
                    uploadFile();
                }
            }
        } catch (Error e) {
            Toast.makeText(mContext,
                    "系统Error：" + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Toast.makeText(mContext,
                    "系统Exception：" + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    private String networkPath;

    private void uploadFile() {
        Map<String, Object> map = new HashMap<>();
        map.put("file", imgFile);
        ApiClient.requestNetHandle(mContext, AppConfig.updateFile, "图片上传中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "上传图片的接口:" + json);
                    FileInfo info = JSON.parseObject(json, FileInfo.class);
                    if (info != null && info.getPath() != null && !StringUtils.isEmpty(info.getPath())) {
                        networkPath = info.getPath();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
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
