package com.zzx.haoniu.nghmtaxi.recriver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.zzx.haoniu.nghmtaxi.app.AppContext;
import com.zzx.haoniu.nghmtaxi.http.ApiClient;
import com.zzx.haoniu.nghmtaxi.http.AppConfig;
import com.zzx.haoniu.nghmtaxi.http.ResultListener;
import com.zzx.haoniu.nghmtaxi.utils.L;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
 */

public class MyLocationService extends Service implements AMapLocationListener {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public void onCreate() {
        mLocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10 * 1000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    private LatLng slatLng;
    private Map<String, Object> map = null;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (!AppContext.getInstance().checkUser())
            return;
        if (map != null) {
            map = null;
        }
        map = new HashMap<>();
        if (aMapLocation != null && aMapLocation.getLatitude() != 0 && aMapLocation.getLongitude() != 0) {
            if (aMapLocation.getLocationType() == 6) {
                return;
            }
            LatLng endLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            map.put("latitude", endLatLng.latitude);
            map.put("longitude", endLatLng.longitude);
            map.put("speed", aMapLocation.getSpeed());
            map.put("accuracy", aMapLocation.getAccuracy());
            map.put("timestamp", aMapLocation.getTime());
            map.put("orientation", aMapLocation.getBearing());
            map.put("type", aMapLocation.getLocationType());
            if (slatLng == null) {
                slatLng = endLatLng;
                ApiClient.requestNetHandle(MyLocationService.this, AppConfig.modify_userPosition, null, map, new ResultListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (json != null && !StringUtils.isEmpty(json)) {
                            L.d("TAG", "上传位置:" + json);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        L.d("TAG", msg);
                    }
                });
            } else if (AMapUtils.calculateLineDistance(slatLng, endLatLng) > 20) {
                L.d("TAG", AMapUtils.calculateLineDistance(slatLng, endLatLng) + "计算数据");
                slatLng = endLatLng;
                ApiClient.requestNetHandle(MyLocationService.this, AppConfig.modify_userPosition, null, map, new ResultListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (json != null && !StringUtils.isEmpty(json)) {
                            L.d("TAG", "上传位置:" + json);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        L.d("TAG", msg);
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }
}
