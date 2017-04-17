package com.zzx.haoniu.app_nghmuser.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.dialog.PhotoPromptDialog;
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
import java.util.HashMap;
import java.util.List;
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

public class CarAuthenticationActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edNameCA)
    EditText edNameCA;
    @Bind(R.id.edCardNumCA)
    EditText edCardNumCA;
    @Bind(R.id.tvNextCA)
    TextView tvNextCA;
    @Bind(R.id.ivPlatCA)
    ImageView ivPlatCA;

    /**
     * 车主认证
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_authentication);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
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

    final String[] stringItems = {"相册", "相机"};
    private ExpandableListView elv;
    private View decorView;
    private ActionSheetDialog dialog;

    @OnClick({R.id.ll_back, R.id.tvNextCA, R.id.ivPlatCA})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                showExitDoalog();
                break;
            case R.id.ivPlatCA:
                showPhotoPrompt();
                break;
            case R.id.tvNextCA:
                if (edNameCA.getText().toString() == null || StringUtils.isEmpty(edNameCA.getText().toString())
                        || !AppContext.getInstance().isName(edNameCA.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "真实姓名不符规范，请重新填写！");
                    edNameCA.setText("");
                    return;
                } else if (edCardNumCA.getText().toString() == null || StringUtils.isEmpty(edCardNumCA.getText().toString())
                        || !AppContext.getInstance().personIdValidation(edCardNumCA.getText().toString())) {
                    ToastUtils.showTextToast(mContext, "身份证号码不符规范，请重新填写！");
                    edCardNumCA.setText("");
                    return;
                } else if (imgFile == null) {
                    ToastUtils.showTextToast(mContext, "请选择上传驾驶证图片!");
                    return;
                }
                if (bundle != null) {
                    EventBus.getDefault().post(new CommonEventBusEnity("ownerFinish", null));
                    bundle.putString("driverName", edNameCA.getText().toString());
                    bundle.putString("driverLicense", edCardNumCA.getText().toString());
                    bundle.putString("driverPlatPhoto", path);
                    bundle.putString("driverPlatPhotoNet", networkPath);
                    startActivity(OwnerAuthenticationActivity.class, bundle);
                    finish();
                } else {
                    bundle = new Bundle();
                    bundle.putString("driverName", edNameCA.getText().toString());
                    bundle.putString("driverLicense", edCardNumCA.getText().toString());
                    bundle.putString("driverPlatPhoto", path);
                    bundle.putString("driverPlatPhotoNet", networkPath);
                    startActivity(TravelCardAuthenticationActivity.class, bundle);
                    finish();
                }
                break;
        }
    }

    private NormalDialog exitDdialog;


    private String path;
    private File imgFile;


    private Bundle bundle;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_car_authentication;
    }

    @Override
    public void initView() {//border_redgray0
        tvTitle.setText("车主认证");
        if (bundle != null) {
            String driverName = bundle.getString("driverName");
            String driverLicense = bundle.getString("driverLicense");
            String driverPlatPhoto = bundle.getString("driverPlatPhoto");
            String driverPlatPhotoNet = bundle.getString("driverPlatPhotoNet");
            if (driverName != null)
                edNameCA.setText(driverName);
            if (driverLicense != null)
                edCardNumCA.setText(driverLicense);
            if (driverPlatPhoto != null) {
                path = driverPlatPhoto;
                ivPlatCA.setImageBitmap(BitmapFactory.decodeFile(path));
                imgFile = new File(path);
            }
            if (driverPlatPhotoNet != null) {
                networkPath = driverPlatPhotoNet;
            }
            tvNextCA.setText("修改完成");
        }
    }

    @Override
    public void onBackPressed() {
        showExitDoalog();
    }

    @Override
    public void doBusiness(Context mContext) {

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
                        exitDdialog.dismiss();
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

    private PhotoPromptDialog promptDialog;

    private void showPhotoPrompt() {
        if (promptDialog == null) {
            promptDialog = new PhotoPromptDialog(mContext);
        }
        promptDialog.setUploadPhoto(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                promptDialog.dismiss();
                showPhotoDialog();
            }
        });
        promptDialog.show();
    }

    private void showPhotoDialog() {
        if (dialog == null) {
            dialog = new ActionSheetDialog(mContext, stringItems, elv);
        }
        dialog.isTitleShow(false);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + +System.currentTimeMillis()
                + ".jpg";
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    dialog.dismiss();
                    selectPhoto();
                } else {
                    dialog.dismiss();
                    takePhoto();
                }
            }
        });
        dialog.show();
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
                    }
                    imgFile = new File(path);
                    ivPlatCA.setImageBitmap(BitmapFactory.decodeFile(path));
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

    //判断某一个类是否存在任务栈里面
    private boolean isExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);  //获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
