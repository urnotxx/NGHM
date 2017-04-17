package com.zzx.haoniu.hzcxdj.app;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.zzx.haoniu.hzcxdj.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.LoginCallback;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.http.UserInfo;
import com.zzx.haoniu.hzcxdj.storage.Storage;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import self.androidbase.app.SelfAppContext;

/**
 * Created by Administrator on 2016/1/25.
 */
public class AppContext extends SelfAppContext {
    public static AppContext appContext;
    public Boolean isOpen = false;
    private boolean otherOpen = false;
    private boolean isInit = false;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    Vibrator mVibrator;
    private List activityList = new LinkedList();

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        appContext = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static AppContext getInstance() {
        if (appContext == null)
            return new AppContext();
        return appContext;
    }

    public void setIsOpen(Boolean isopen) {

        this.isOpen = isopen;

    }

    public Boolean getIsOpen() {

        return isOpen;

    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void finishs() {
        for (Object activity : activityList) {
            ((Activity) activity).finish();
        }
    }

    public boolean getOtherOpen() {
        return this.otherOpen;
    }

    public void setOtherOpen(boolean otherOpen) {
        this.otherOpen = otherOpen;
    }

    /**
     * 获取软件版本名称
     *
     * @return
     */
    public String getVersionName() {
        return getPackageInfo().versionName;
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    public int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取缓存用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return Storage.GetUserInfo() == null ? new UserInfo() : Storage
                .GetUserInfo();
    }

    /**
     * 获取缓存用户信息
     *
     * @return
     */
    public LoginCallback getLoginCallback() {
        return Storage.GetLoginCallback() == null ? new LoginCallback() : Storage
                .GetLoginCallback();
    }

    /**
     * 保存缓存用户信息
     *
     * @param user
     */
    public void saveUserInfo(final UserInfo user) {
        if (user != null) {
            Storage.ClearUserInfo();
            Storage.saveUsersInfo(user);
        }
    }

    /**
     * 保存缓存用户信息
     *
     * @param user
     */
    public void saveCallBack(final LoginCallback user) {
        if (user != null) {
            Storage.ClearLoginCallback();
            Storage.saveLoginCallback(user);
        }
    }

    /**
     * 保存缓存用户信息
     *
     * @param registrationId
     */
    public void saveRegistrationId(final String registrationId) {
        if (registrationId != null) {
            Storage.ClearRegistrationId();
            Storage.saveRegistrationId(registrationId);
        }
    }

    /**
     * 获取RegistrationId信息
     *
     * @return
     */
    public String getRegistrationId() {
        return Storage.getRegistrationId() == null ? "" : Storage
                .getRegistrationId();
    }

    /**
     * 用户存在是ture 否则是false
     *
     * @return
     */
    public boolean checkUser() {
        if (getUserInfo().getId() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 用户存在是ture 否则是false
     *
     * @return
     */
    public boolean checkCallBackUser() {
        if (getLoginCallback().getSecret() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 清除缓存用户信息
     *
     * @param
     */
    public void cleanUserInfo() {
        Storage.ClearUserInfo();
    }

    /**
     * 清除缓存用户信息
     *
     * @param
     */
    public void cleanLoginCallback() {
        Storage.ClearLoginCallback();
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private double lat = 31.22997;
    private double lon = 121.640756;
    public double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    //加密成为摩卡托坐标
    public double[] bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        double[] latlng = new double[2];
        latlng[0] = bd_lon;
        latlng[1] = bd_lat;
        return latlng;
    }

    public String getTimeName(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(time);
        return formatter.format(date);
    }

    //view 转bitmap

    public Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }

    public boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    /**
     * 18位或者15位身份证验证 18位的最后一位可以是字母x
     *
     * @param text 字符串
     * @return 是否是身份证
     */
    public boolean personIdValidation(String text) {
        boolean flag = false;
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        flag = text.matches(regx) || text.matches(reg1) || text.matches(regex);
        return flag;
    }

    /**
     * @param name
     * @return 中文  名字必须是中文
     */
    public boolean isName(String name) {
        if (name.length() == 0) {
            return false;
        }
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
            Matcher m = p.matcher(name);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private String startTime;

    public void onLine(final Context context, final int carId, String log) {
        Map<String, Object> map = new HashMap<>();
        if (carId == 0) {
            map.put("status", 0);
            map.put("endTime", System.currentTimeMillis());
            map.put("startTime", startTime);
        } else {
            map.put("status", getLoginCallback().getCarId());
            startTime = System.currentTimeMillis() + "";
        }
        ApiClient.requestNetHandle(context, AppConfig.requestDjOnLine, log, map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "上线/下线：" + json);
                }
                if (carId == 0) {
                    EventBus.getDefault().post(new CommonEventBusEnity("shouche", ""));
                } else {
                    if (json != null && !StringUtils.isEmpty(json)) {
                        L.d("TAG", "上线/下线：" + json);
                        org.json.JSONObject obj;
                        try {
                            obj = new org.json.JSONObject(json);
                            int serviceTime = obj.getInt("serviceTime");
                            CommonEventBusEnity enity = new CommonEventBusEnity("jiedan", "" + serviceTime);
                            EventBus.getDefault().post(enity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        EventBus.getDefault().post(new CommonEventBusEnity("jiedan", ""));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(context, msg);
            }
        });
    }

    public void callUser(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ToastUtils.showTextToast(context, "通话权限未打开!");
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Map<String, Object> toHashMap(String json) {
        Map<String, Object> map = new HashMap<>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.parseObject(json);
        for (Object o : jsonObject.keySet()) {
            String key = o.toString();
            String value = jsonObject.get(key).toString();
            if (value.indexOf("{") == -1) {
                map.put(key, value);
            } else {
                toHashMap(value);
            }
        }
        return map;
    }
}
