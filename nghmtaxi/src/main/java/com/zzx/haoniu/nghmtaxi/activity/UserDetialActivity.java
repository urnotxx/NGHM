package com.zzx.haoniu.nghmtaxi.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.app.AppContext;
import com.zzx.haoniu.nghmtaxi.entity.CommonEventBusEnity;
import com.zzx.haoniu.nghmtaxi.entity.FileInfo;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.http.UserInfo;
import com.zzx.haoniu.nghmtaxi.utils.AppUtils;
import com.zzx.haoniu.nghmtaxi.utils.L;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;
import com.zzx.haoniu.nghmtaxi.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import iosdialog.dialog.listener.OnOperItemClickL;
import iosdialog.dialog.widget.ActionSheetDialog;
import iosdialog.dialogsamples.extra.CustomBaseDialog;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.utils.ImageUtils;
import self.androidbase.views.SelfLinearLayout;

public class UserDetialActivity extends BaseActivity {
    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.civHeadUD)
    CircleImageView civHeadUD;
    @Bind(R.id.edNameUD)
    EditText edNameUD;
    @Bind(R.id.llNameUD)
    LinearLayout llNameUD;
    @Bind(R.id.tvModifyPwdUD)
    TextView tvModifyPwdUD;
    @Bind(R.id.tvExitUD)
    TextView tvExitUD;
    @Bind(R.id.tvShoucheUD)
    TextView tvShoucheUD;
    @Bind(R.id.ivQuanUD)
    ImageView ivQuanUD;
    @Bind(R.id.tvQuanUD)
    TextView tvQuanUD;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.activity_user_detial)
    RelativeLayout activityUserDetial;
    private boolean orderState = true;//是否接单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detial);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityUserDetial);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        userInfo = AppContext.getInstance().getUserInfo();
        initView();
        EventBus.getDefault().register(this);
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
    }

    final String[] stringItems = {"相册", "相机"};
    private ExpandableListView elv;
    private View decorView;
    private ActionSheetDialog photoDialog;
    private String path;
    private File imgFile;
    private String networkPath;
    private UserInfo userInfo;

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("shouche")) {
            stopJieDan();
        } else if (event.getSendCode().equals("jiedan")) {
            startJieDan();
        } else if (event.getSendCode().equals("unLogInTaxi")) {
            finish();
        }
    }

    @Override
    public void initParms(Bundle parms) {
        orderState = parms.getBoolean("orderState", true);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_detial;
    }

    @Override
    public void initView() {
        tvTitle.setText("个人信息");
        tvSubmit.setText("完成");
        if (userInfo.getNick_name() != null) {
            edNameUD.setText(userInfo.getNick_name());
        }
        if (userInfo.getHead_portrait() != null
                && !StringUtils.isEmpty(userInfo.getHead_portrait())) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + userInfo.getHead_portrait(), civHeadUD);
        }
        if (orderState) {
            startAnim();
        } else {
            stopJieDan();
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.civHeadUD, R.id.llNameUD, R.id.tvModifyPwdUD, R.id.tvExitUD, R.id.tvShoucheUD, R.id.ivQuanUD, R.id.ll_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.civHeadUD:
                showPhotoDialog();
                break;
            case R.id.llNameUD:
                break;
            case R.id.tvModifyPwdUD:
                startActivity(ModifyPwdActivity.class);
                break;
            case R.id.tvExitUD:
                showExitDialog();
                break;
            case R.id.tvShoucheUD:
                AppContext.getInstance().onLine(mContext, 0, "收车中...");
                break;
            case R.id.ivQuanUD:
                AppContext.getInstance().onLine(mContext, 1, "上线中...");
                break;
            case R.id.ll_right:
                upDataUserInfo();
                break;
        }
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
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 600);
                    intent.putExtra("outputY", 600);
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
                        networkPath = info.getPath();
                        ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + networkPath, civHeadUD);
                    }
                    imgFile.delete();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
                imgFile.delete();
            }
        });
    }

    private void upDataUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("nickName", edNameUD.getText().toString());
        if (networkPath != null && !StringUtils.isEmpty(networkPath)) {
            map.put("headPortrait", networkPath);
        }
        ApiClient.requestNetHandle(mContext, AppConfig.modifyUserInfo, "修改中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "个人信息修改：" + json);
                    userInfo = JSON.parseObject(json, UserInfo.class);
                    AppContext.getInstance().saveUserInfo(userInfo);
                }
                ToastUtils.showTextToast(mContext, "个人信息更改成功!");
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
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
                dialog.dismiss();
//                logout();
                AppContext.getInstance().onLine(mContext, 0, "");
                EventBus.getDefault().post(new CommonEventBusEnity("exit", null));
                AppContext.getInstance().cleanLoginCallback();
                startActivity(LoginActivity.class);
                finish();
            }
        });
    }

    private void logout() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestLogout, "正在退出...", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "退出:" + json);
                }
                EventBus.getDefault().post(new CommonEventBusEnity("exit", null));
                AppContext.getInstance().cleanLoginCallback();
                AppContext.getInstance().cleanUserInfo();
                startActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void stopJieDan() {
        orderState = false;
        tvShoucheUD.setVisibility(View.GONE);
        tvQuanUD.setText("出车");
        tvQuanUD.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));//红色
        stopAnim();
    }

    private void startJieDan() {
        if (!orderState) {
            orderState = true;
            tvShoucheUD.setVisibility(View.VISIBLE);
            tvQuanUD.setText("接单中");
            tvQuanUD.setTextColor(Color.rgb(0x09, 0xa6, 0xed));//蓝色09A6ED
            startAnim();
        }
    }

    private Animation operatingAnim;

    public void startAnim() {
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_quan);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (null != operatingAnim) {
            ivQuanUD.startAnimation(operatingAnim);
        }
    }

    public void stopAnim() {
        if (operatingAnim != null) {
            ivQuanUD.clearAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
