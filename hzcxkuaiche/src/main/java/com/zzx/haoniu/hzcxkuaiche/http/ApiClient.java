package com.zzx.haoniu.hzcxkuaiche.http;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;

import java.util.Map;

import de.greenrobot.event.EventBus;
import self.androidbase.utils.OKHttpUtils;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ApiClient {

    static ProgressDialog progressDialog = null;


    /**
     * 请求网络数据接口
     *
     * @param context
     * @param url
     * @param log
     * @param mapP
     * @param listener
     */
    public static void requestNetHandle(final Context context, String url, String log, Map<String, Object> mapP, final ResultListener listener) {
        requestNetHandle(context, url, log, mapP, listener, false);
    }

    /**
     * 请求网络数据接口
     *
     * @param context
     * @param url
     * @param log
     * @param mapP
     * @param listener
     */
    public static void requestNetHandle(final Context context, final String url, String log, Map<String, Object> mapP, final ResultListener listener, boolean isCache) {
        if (!AppContext.getInstance().isNetworkConnected()) {
            //没网络
            listener.onFailure("网络连接异常,请检查您的网络设置");
            return;
        }
        try {
            if (context.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE,
                    context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                OKHttpUtils.getInstance().addHeader("deviceno", ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
            } else {
                if (isShow) {
                    return;
                } else {
                    showMissingPermissionDialog(context);
                    return;
                }
            }
            if (!StringUtils.isEmpty(log)) {
                progressDialog = null;
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(log);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
            if (null != mapP && mapP.size() > 0) {
                L.d("TAG", "上传的参数:" + mapP.toString() + "请求地址:" + url);
            }
            OKHttpUtils.getInstance().addHeader("appType", "1");
            OKHttpUtils.getInstance().addHeader("devicetype", "1");
            if (AppContext.getInstance().checkCallBackUser()) {
                OKHttpUtils.getInstance().addHeader("secret", AppContext.getInstance().getLoginCallback().getSecret());
                OKHttpUtils.getInstance().addHeader("token", AppContext.getInstance().getLoginCallback().getToken());
            }
            OKHttpUtils.getInstance().post(url, mapP, new OKHttpUtils.HttpCallBack() {
                @Override
                public void onResult(boolean success, String response) {
                    if (success) {
                        if (null != progressDialog && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        L.d("回调数据:" + response + "    请求的接口:" + url);
                        if (StringUtils.isEmpty(response)) {
                            listener.onFailure("请求数据失败");
                            return;
                        }
                        Gson gson = new Gson();
                        //成功
                        ServerData serverData = JSON.parseObject(response, ServerData.class);
                        if (null != serverData) {
                            if (serverData.getStatus().equals("OK")) {
                                listener.onSuccess(serverData.getData());
                            } else if (serverData.getStatus().equals("NOAUTH")) {
                                CommonEventBusEnity busEnity = new CommonEventBusEnity("unLogInKC", null);
                                EventBus.getDefault().post(busEnity);
                            } else {
                                listener.onFailure(serverData.getMsg());
                            }
                        } else {
                            listener.onFailure("请求数据失败");
                        }
                    } else {
                        listener.onFailure(response);
                        if (null != progressDialog && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        //失败
                        // Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, isCache);
        } catch (Exception e) {
            listener.onFailure(e.getMessage());
        }

    }

    private static boolean isShow;

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private static void showMissingPermissionDialog(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n请点击\"设置\"---\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) mContext).finish();
                        isShow = false;
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(mContext);
                        isShow = false;
                    }
                });

        builder.setCancelable(false);
        builder.show();
        isShow = true;
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private static void startAppSettings(Context mContext) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }
}
