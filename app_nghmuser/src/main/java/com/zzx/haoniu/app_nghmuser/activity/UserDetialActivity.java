package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.FileInfo;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.utils.AppUtils;
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
import de.hdodenhof.circleimageview.CircleImageView;
import iosdialog.dialog.listener.OnOperItemClickL;
import iosdialog.dialog.widget.ActionSheetDialog;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.utils.ImageUtils;
import self.androidbase.views.SelfLinearLayout;

public class UserDetialActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.llTop)
    RelativeLayout llTop;
    @Bind(R.id.edNameUD)
    EditText edNameUD;
    @Bind(R.id.tvSexUD)
    TextView tvSexUD;
    @Bind(R.id.tvAgeUD)
    TextView tvAgeUD;
    @Bind(R.id.llAgeUD)
    LinearLayout llAgeUD;
    @Bind(R.id.llDustryUD)
    LinearLayout llDustryUD;
    @Bind(R.id.edCompany)
    EditText edCompany;
    @Bind(R.id.edOccupationUD)
    EditText edOccupationUD;
    @Bind(R.id.llOccupationUD)
    LinearLayout llOccupationUD;
    @Bind(R.id.edAutographUD)
    EditText edAutographUD;
    @Bind(R.id.activity_user_detial)
    LinearLayout activityUserDetial;
    @Bind(R.id.civHeadUD)
    CircleImageView civHeadUD;
    @Bind(R.id.edMailbox)
    EditText edMailbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detial);
        ButterKnife.bind(this);
        userInfo = AppContext.getInstance().getUserInfo();
        initView();
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
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

    final String[] stringItems = {"相册", "相机"};
    private ExpandableListView elv;
    private View decorView;
    private ActionSheetDialog photoDialog;
    private String path;
    private File imgFile;
    private String networkPath;
    private UserInfo userInfo;

    @OnClick({R.id.ll_back, R.id.ll_right, R.id.llAgeUD, R.id.llDustryUD, R.id.llSexUD, R.id.llHeadUD})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.llHeadUD:
                showPhotoDialog();
                break;
            case R.id.ll_right:
                upDataUserInfo();
                break;
            case R.id.llAgeUD:
//                showAge();
                break;
            case R.id.llSexUD:
                showSex();
                break;
            case R.id.llDustryUD:
                ToastUtils.showTextToast(mContext, "行业");
                break;
        }
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_detial;
    }

    @Override
    public void initView() {
        tvTitle.setText("编辑资料");
        tvSubmit.setText("完成");
        edNameUD.setText(userInfo.getNick_name());
        if (userInfo.getHead_portrait() != null && !StringUtils.isEmpty(userInfo.getHead_portrait())) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + userInfo.getHead_portrait(), civHeadUD);
        }
        if (userInfo.getEmail() != null && !StringUtils.isEmpty(userInfo.getEmail())) {
            edMailbox.setText(userInfo.getEmail());
        }
        if (userInfo.getGender() == 1) {
            tvSexUD.setText("男");
        } else if (userInfo.getGender() == 2) {
            tvSexUD.setText("女");
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private ActionSheetDialog ageDialog;

    private void showAge() {
        final String[] stringItems = {"90后", "80后", "70后"};
        ageDialog = new ActionSheetDialog(mContext, stringItems, null);
        ageDialog.title("年龄选择")//
                .titleTextSize_SP(13.5f)//
                .show();

        ageDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvAgeUD.setText(stringItems[position]);
                ageDialog.dismiss();
            }
        });
    }

    private void showSex() {
        final String[] stringItems = {"男", "女"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("性别选择")//
                .titleTextSize_SP(13.5f)//
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvSexUD.setText(stringItems[position]);
                dialog.dismiss();
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
        ApiClient.requestNetHandle(mContext, AppConfig.updateFile, "上传图片中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "上传图片的接口:" + json);
                    FileInfo info = JSON.parseObject(json, FileInfo.class);
                    if (info != null && info.getPath() != null && !StringUtils.isEmpty(info.getPath())) {
                        networkPath = info.getPath();
                    }
                    ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + networkPath, civHeadUD);
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
        if (tvSexUD.getText().toString() != null) {
            if (tvSexUD.getText().toString().equals("男")) {
                map.put("gender", 1);
            } else if (tvSexUD.getText().toString().equals("女")) {
                map.put("gender", 2);
            }
        }
        if (edMailbox.getText().toString() != null && !StringUtils.isEmpty(edMailbox.getText().toString())) {
            map.put("email", edMailbox.getText().toString());
        }
        ApiClient.requestNetHandle(mContext, AppConfig.modifyUserInfo, "修改中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "个人信息修改：" + json);
                    userInfo = JSON.parseObject(json, UserInfo.class);
                    AppContext.getInstance().saveUserInfo(userInfo);
                }
                ToastUtils.showTextToast(mContext, "个人信息修改成功!");
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }
}
