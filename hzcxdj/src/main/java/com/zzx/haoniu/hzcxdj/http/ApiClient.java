package com.zzx.haoniu.hzcxdj.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;

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
            OKHttpUtils.getInstance().addHeader("deviceno", ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
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
                        L.d("回调数据:" + response + "    请求的接口:"+url);
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
                                CommonEventBusEnity busEnity = new CommonEventBusEnity("unLogInTaxi", null);
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

}
