package com.zzx.haoniu.app_nghmuser.http;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;


import com.zzx.haoniu.app_nghmuser.R;

import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;

import self.androidbase.utils.BaseAppUtils;
import self.androidbase.utils.OKHttpUtils;
import self.androidbase.views.SelfAlertView;

/**
 * 全局配置
 */
public class AppConfig {

    // 标记程序是否第一次打开的Key
    public static final String FIRST_OPEN_KEY = "first_open";
    public static String mainUrl = "http://192.168.1.162:8080/Jfinal";
    public static String mainPicUrl = "http://192.168.1.162:8080/Jfinal/";
    public static String mainWebSocker = "ws://192.168.1.162:8080/Jfinal/api/websocket";
//    public static String mainUrl = "http://60.173.83.27:8888/Jfinal";
//    public static String mainPicUrl = "http://60.173.83.27:8888/Jfinal/";
//    public static String mainWebSocker = "ws://60.173.83.27:8888/Jfinal/api/websocket";
    /**
     * 注册
     */
    public static String requestRegister = mainUrl + "/api/register";
    /**
     * 获取验证码
     */
    public static String requestGetCode = mainUrl + "/api/smscode";
//    /**
//     * 获取验证码
//     */
//    public static String requestGetCode = mainUrl + "/common/smsphone";
    /**
     * 登录
     */
    public static String requestLogin = mainUrl + "/api/login";
    /**
     * 登出
     */
    public static String requestLogout = mainUrl + "/api/logout";
    /**
     * 获取会员信息
     */
    public static String requestUserInfo = mainUrl + "/api/member";
    /**
     * 获取消息
     */
    public static String requestNoticesList = mainUrl + "/api/notices/list";
    /**
     * 获取全部消息
     */
    public static String requestNoticesListAll = mainUrl + "/api/notices/listAll";
    /**
     * 删除消息  noticeId
     */
    public static String requestDelMsgNotices = mainUrl + "/api/notices/delNotices";
    /**
     * 我的余额
     */
    public static String requestAmount = mainUrl + "/api/member/amount";
    /**
     * 修改会员信息
     */
    public static String modifyUserInfo = mainUrl + "/api/member/update";
    /**
     * 实名认证
     */
    public static String updateRealNameAuthentication = mainUrl + "/api/member/realNameAuthentication";
    /**
     * 车主认证
     */
    public static String updateCarownerAuthentication = mainUrl + "/api/member/carownerAuthentication";
    /**
     * 修改密码
     */
    public static String modifyPwd = mainUrl + "/api/member/update/password";
    /**
     * 上传文件
     */
    public static String updateFile = mainUrl + "/file";
    /**
     * 忘记密码
     */
    public static String modifyPwdByFor = mainUrl + "/api/member/reset/password";
    /**
     * 更新会员位置
     */
    public static String updateLoc = mainUrl + "/api/member/update/location";
    /**
     * 获取车辆列表
     */
    public static String requestCars = mainUrl + "/api/member/cars";
    /**
     * 获取对应的收费标准
     */
    public static String requestCalrule = mainUrl + "/api/calRule";
    /**
     * 账户明细
     */
    public static String requestAccount = mainUrl + "/api/accountlog";
    /**
     * 未完成订单
     */
    public static String requestRunningOrder = mainUrl + "/api/member/getOrder";
    /**
     * 计算价格
     */
    public static String requestCalaMount = mainUrl + "/api/order/calamount";
    /**
     * 下单
     */
    public static String requestCreateOrder = mainUrl + "/api/order/create";
    /**
     * 取消订单
     */
    public static String requestCancleOrder = mainUrl + "/api/order/cancel";
    /**
     * 我的订单历史记录
     */
    public static String requestOrderList = mainUrl + "/api/order/findByTripId";
//    /**
//     * 我的订单历史记录
//     */
//    public static String requestOrderList = mainUrl + "/api/order/myOrder";
    /**
     * 获取司机的实时位置
     */
    public static String requestDriverLoc = mainUrl + "/api/member/getDriverLocation";
    /**
     * 获取支付信息
     */
    public static String requestPayInfo = mainUrl + "/api/order/pay";
    /**
     * 评论
     */
    public static String requestGrand = mainUrl + "/api/member/grand";
    /**
     * 添加常用地址
     */
    public static String requestAddCom = mainUrl + "/api/member/saveOrUpdateCommonAddress";
    /**
     * 获取常用地址
     */
    public static String requestComAdd = mainUrl + "/api/member/addCommonAddress";


    /**
     * 检查版本号 hnLoginName
     */
    public static String checkVersion = "http://www.hefeiapp.cn/checkVersion?entity.appCode=LouYu&entity.appType=0";
    //    public static String checkVersion = "http://www.hefeiapp.cn/checkVersion?entity.appCode=YouFuBao&entity.appType=0";
    public static Dialog dialog = null;

    /**
     * 检查android的最新版本号 hnLoginName
     */
    static ProgressDialog progressDialog;

    public static void checkVersion(final Context context,
                                    final Handler handler, int type, final int isShow) {
        if (type == 1) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("检测中");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    final SharedPreferences sp = context.getSharedPreferences(
                            "versionData", 0);

                    OKHttpUtils.getInstance().get(checkVersion, new OKHttpUtils.HttpCallBack() {
                        @Override
                        public void onResult(boolean success, String response) {
                            if (success) {
                                try {
                                    final JSONObject jsonObject = new JSONObject(response);
                                    double currVersionCode = context.getPackageManager()
                                            .getPackageInfo(context.getPackageName(), 0).versionCode;
                                    Message msg = new Message();
                                    msg.obj = jsonObject.getString("versionName");
                                    double passVersion = Double.parseDouble(sp.getString(
                                            "ignoreVersion", "0.0"));

                                    final double serverVersionCode = jsonObject
                                            .getDouble("versionCode");

                                    if (null != progressDialog) {
                                        progressDialog.dismiss();
                                    }

                                    if (serverVersionCode == passVersion) {
                                        msg.what = 2;// 最新版本
                                    } else {
//                                        L.d("TAG", "版本更新:" + "serverVersionCode"
//                                                + serverVersionCode + "currVersionCode:"
//                                                + currVersionCode);
                                        if (serverVersionCode > currVersionCode) {
                                            msg.what = 0;// 需要更新
                                            if (isShow == 0) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        // TODO Auto-generated method stub
                                                        try {
                                                            LinkedHashMap<String, View.OnClickListener> menus = new LinkedHashMap<String, View.OnClickListener>();
                                                            menus.put("立即更新",
                                                                    new View.OnClickListener() {

                                                                        @Override
                                                                        public void onClick(
                                                                                View arg0) {
                                                                            // TODO Auto-generated
                                                                            // method stub
                                                                            handler.post(new Runnable() {

                                                                                @Override
                                                                                public void run() {
                                                                                    // TODO
                                                                                    // Auto-generated
                                                                                    // method stub
                                                                                    try {
                                                                                        BaseAppUtils
                                                                                                .downloadFile(
                                                                                                        context,
                                                                                                        jsonObject
                                                                                                                .getString("downUrl"),
                                                                                                        BaseAppUtils
                                                                                                                .getApplicationName(context),
                                                                                                        "正在下载"
                                                                                                                + BaseAppUtils
                                                                                                                .getApplicationName(context)
                                                                                                                + "中…");
                                                                                    } catch (Exception e) {
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                            menus.put("以后再说", null);
                                                            if (jsonObject
                                                                    .getInt("versionImportant") == 0) {// 一般更新
                                                                menus.put("忽略此版本",
                                                                        new View.OnClickListener() {

                                                                            @Override
                                                                            public void onClick(
                                                                                    View arg0) {
                                                                                // TODO
                                                                                // Auto-generated
                                                                                // method stub
                                                                                SharedPreferences.Editor edit = sp
                                                                                        .edit();
                                                                                edit.putString(
                                                                                        "ignoreVersion",
                                                                                        serverVersionCode
                                                                                                + "");
                                                                                edit.commit();
                                                                            }
                                                                        });
                                                            }

                                                            Dialog dialog = SelfAlertView
                                                                    .showAlertView(
                                                                            context,
                                                                            R.mipmap.ic_launcher,
                                                                            "更新提醒（"
                                                                                    + jsonObject
                                                                                    .getString("versionImportantDesc")
                                                                                    + "）",
                                                                            jsonObject
                                                                                    .getString("versionDesc"),
                                                                            menus);
                                                            if (jsonObject
                                                                    .getInt("versionImportant") == 1) {// 重要更新
                                                                dialog.setCanceledOnTouchOutside(false);
                                                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                                                    @Override
                                                                    public void onCancel(
                                                                            DialogInterface arg0) {
                                                                        // TODO Auto-generated
                                                                        // method stub
                                                                        System.exit(0);
                                                                    }
                                                                });
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            msg.what = 1;// 最新版本
                                        }
                                    }
                                    handler.sendMessage(msg);

                                } catch (Exception e) {
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

}
