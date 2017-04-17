package com.zzx.haoniu.hzcxkuaiche.http;

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


import com.zzx.haoniu.hzcxkuaiche.R;

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
//    public static String mainPicUrl = "http://192.168.1.162:8080/Jfinal/";
//    public static String mainWebSocker = "ws://192.168.1.162:8080/Jfinal/api/websocket";
//    public static String mainUrl = "http://192.168.1.162:8080/Jfinal";
    public static String mainUrl = "http://60.173.83.27:8888/Jfinal";
    public static String mainPicUrl = "http://60.173.83.27:8888/Jfinal/";
    public static String mainWebSocker = "ws://60.173.83.27:8888/Jfinal/api/websocket";
    /**
     * 注册
     */
    public static String requestRegister = mainUrl + "/api/enroll";
    /**
     * 充值
     */
    public static String requestRecharge = mainUrl + "/api/order/sjrecharge";
    /**
     * 获取可提现金额
     */
    public static String requestTiXianJine = mainUrl + "/api/tixianjine";
    /**
     * 申请提现
     */
    public static String requestTiXian = mainUrl + "/api/hztixian";
    /**
     * 获取可用车辆
     */
    public static String requestCars = mainUrl + "/api/driver/cars";
    /**
     * 车辆品牌
     */
    public static String requestCarBrand = mainUrl + "/api/enroll/carBrand";
    /**
     * 车辆型号
     */
    public static String requestCarModel = mainUrl + "/api/enroll/carModel";
    /**
     * 省份
     */
    public static String requestPro = mainUrl + "/api/enroll/Province";
    /**
     * 城市或地区
     */
    public static String requestCity = mainUrl + "/api/enroll/cityAndDistrict";
    /**
     * 上线/下线
     */
    public static String requestDjOnLine = mainUrl + "/api/driver/online";
    /**
     * 获取当天消息
     */
    public static String requestNoticesList = mainUrl + "/api/notices/list";
    /**
     * 获取全部消息
     */
    public static String requestNoticesListAll = mainUrl + "/api/notices/listAll";
    /**
     * 删除消息  noticeId
     */
    public static String requestDelNotices = mainUrl + "/api/notices/del";
    /**
     * 删除消息  noticeId
     */
    public static String requestDelMsgNotices = mainUrl + "/api/notices/delNotices";
//    /**
//     * 获取验证码
//     */
//    public static String requestGetCode = mainUrl + "/common/smsphone";
    /**
     * 获取验证码
     */
    public static String requestGetCode = mainUrl + "/api/smscode";
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
     * 获取信息
     */
    public static String requestMsg = mainUrl + "/api/driver/countXJAndFWAndCJLByDriverId";
    /**
     * 我的余额
     */
    public static String requestAmount = mainUrl + "/api/driver/amount";
    /**
     * 修改会员信息
     */
    public static String modifyUserInfo = mainUrl + "/api/member/update";
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
     * 账户明细
     */
    public static String requestAccount = mainUrl + "/api/accountlog";
    /**
     * 我的订单历史记录
     */
    public static String requestOrderList = mainUrl + "/api/order/findByTripId";
    /**
     * 正在进行的订单
     */
    public static String requestRunningOrder = mainUrl + "/api/order/findDriverUnfinishOrder";
    /**
     * 抢单   orderId
     */
    public static String requestGrabOrder = mainUrl + "/api/driver/grabsingle";
    /**
     * 取消订单
     */
    public static String requestCancleOrder = mainUrl + "/api/order/cancel";
    /**
     * 听单状态
     */
    public static String requestDjDriverStatus = mainUrl + "/api/driver/changeDirverStatus";
    /**
     * 接到客人
     */
    public static String requestPuckUp = mainUrl + "/api/driver/pickupcustomer";
    /**
     * 接到客人
     */
    public static String requestOrderStart = mainUrl + "/api/driver/orderStart";
    /**
     * 到达司机目的地
     */
    public static String requestArrived = mainUrl + "/api/driver/orderArrived";
    /**
     * 账户明细
     */
    public static String modify_userPosition = mainUrl + "/api/driver/update/location";


    /**
     * 检查版本号 hnLoginName
     */
    public static String checkVersion = "http://www.hefeiapp.cn/checkVersion?entity.appCode=SheZhan&entity.appType=0";
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
