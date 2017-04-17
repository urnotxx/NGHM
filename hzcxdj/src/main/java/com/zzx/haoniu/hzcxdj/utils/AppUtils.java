package com.zzx.haoniu.hzcxdj.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/1/28.
 */
public class AppUtils {
    public final static int StartImages = 1;
    public final static int StartCamera = 2;
    public final static int StartCamera2 = 3;
    public final static int StartCutPicture = 4;
    public static DisplayImageOptions image_display_options = new DisplayImageOptions.Builder()
            //.showImageOnLoading(R.drawable.android_base_image_default)
//            .showImageForEmptyUri(R.drawable.img_def)
//            .showImageOnFail(R.drawable.img_def)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

    public static void saveBitmapToPath(Bitmap bitmap, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            fos = new FileOutputStream(path);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT < 20) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 20) {
            if (AndtoidRomUtil.isEMUI()) {  //华为手机系统
                ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
                localIntent.setComponent(comp);
            } else if (AndtoidRomUtil.isFlyme()) {//魅族手机系统
                localIntent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            } else if (AndtoidRomUtil.isMIUI()) {//小米手机系统
                localIntent.setAction("miui.intent.action.APP_PERM_EDITOR");
                ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.setComponent(componentName);
                localIntent.putExtra("extra_pkgname", context.getPackageName());
            } else {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            }
        }
        context.startActivity(localIntent);
    }


}
