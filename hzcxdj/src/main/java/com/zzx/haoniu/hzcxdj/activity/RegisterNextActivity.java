package com.zzx.haoniu.hzcxdj.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.entity.FileInfo;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.utils.AppUtils;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;

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
import self.androidbase.utils.ImageUtils;
import self.androidbase.utils.MD5Encrypt;
import self.androidbase.views.SelfLinearLayout;

public class RegisterNextActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ivUserHeadRN)
    ImageView ivUserHeadRN;
    @Bind(R.id.edNameRN)
    EditText edNameRN;
    @Bind(R.id.edCardNumRN)
    EditText edCardNumRN;
    @Bind(R.id.edBankRN)
    EditText edBankRN;
    @Bind(R.id.tvCarBelongRN)
    TextView tvCarBelongRN;
    @Bind(R.id.tvCarCompanyRN)
    TextView tvCarCompanyRN;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误，请重新进入!");
            finish();
            return;
        }
        steepStatusBar();
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
        initView();
    }

    final String[] stringItems = {"相册", "相机"};
    private ExpandableListView elv;
    private View decorView;
    private ActionSheetDialog photoDialog;
    private String path;
    private File imgFile;
    private int tag = 0;
    private ActionSheetDialog sexDialog;
    private String jiaShiPath, xingShiPath;
    private Bundle bundle;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        tvTitle.setText("个人信息");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ivUserHeadRN, R.id.tvCarBelongRN, R.id.tvCarCompanyRN, R.id.tvRegisterRN, R.id.llSexUD, R.id.ivJiaShiRN, R.id.ivXingShiRN})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                showExitDoalog();
                break;
            case R.id.ivUserHeadRN:
                showPhotoDialog();
                break;
            case R.id.tvCarBelongRN:
                break;
            case R.id.tvCarCompanyRN:
                break;
            case R.id.tvRegisterRN:
                if (edNameRN.getText().toString() == null || StringUtils.isEmpty(edNameRN.getText().toString()) ||
                        !AppContext.getInstance().isName(edNameRN.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "请输入正确的真实姓名!");
                    return;
                } else if (jiaShiPath == null || StringUtils.isEmpty(jiaShiPath)) {
                    ToastUtils.showTextToast(mContext, "请上传驾驶证照片");
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
        }
    }

    private void register() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", bundle.getString("phone"));
        map.put("smsCode", bundle.getString("code"));
        map.put("type", "2");
        map.put("password", MD5Encrypt.MD5(bundle.getString("pwd")));
        map.put("name", edNameRN.getText().toString());
        map.put("drivingLicense", xingShiPath);
        map.put("driverLicense", jiaShiPath);
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
                ToastUtils.showTextToast(mContext, "报名成功");
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
                    long fileSize = AppContext.getInstance().getFileSize(new File(path));
                    if (fileSize != 0 && fileSize > 1024 * 500) {
                        ImageUtils.doCompressImage(path, path);
                    }
                    imgFile = new File(path);
                    uploadFile();
                    
//                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
//                    intent.putExtra("crop", "true");
//                    intent.putExtra("aspectX", 1);
//                    intent.putExtra("aspectY", 2);
//                    intent.putExtra("outputX", 300);
//                    intent.putExtra("outputY", 600);
//                    intent.putExtra("scale", true);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
//                    intent.putExtra("return-data", false);
//                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                    intent.putExtra("noFaceDetection", true); // no face detection
//                    startActivityForResult(intent, AppUtils.StartCutPicture);
                } else if (requestCode == AppUtils.StartCutPicture) {
                    long fileSize = AppContext.getInstance().getFileSize(new File(path));
                    if (fileSize != 0 && fileSize > 1024 * 500) {
                        ImageUtils.doCompressImage(path, path);
                        long fileSize1 = AppContext.getInstance().getFileSize(new File(path));
                        L.d("TAG", "000000+" + AppContext.getInstance().formetFileSize(fileSize1));
                    }
                    imgFile = new File(path);
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

    private void uploadFile() {
        Map<String, Object> map = new HashMap<>();
        map.put("file", imgFile);
        ApiClient.requestNetHandle(mContext, AppConfig.updateFile, "", map, new ResultListener() {
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
                        }
                    }
                    imgFile.delete();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }


    private void selectPhoto() {
        Intent picture = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, AppUtils.StartImages);
    }

    private void takePhoto() {
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
