package com.zzx.haoniu.hzcxdj.recriver;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import self.androidbase.utils.SelfMapUtils;

/**
 * Created by Administrator on 2016/12/8.
 */
public class PositionService extends Service {
    private LocationManager manager = null;
    private boolean isStop;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        updateLoc();
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    private Map<String, Object> map = null;
    private LatLng slatLng;

    private void updateLoc() {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (map != null) {
            map = null;
        }
        map = new HashMap<>();
        SelfMapUtils selfMapUtils = SelfMapUtils.getInstance(getApplication());
        selfMapUtils.startLocation(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AMapLocation aMapLocation = (AMapLocation) msg.obj;
                double lat = aMapLocation.getLatitude();
                double log = aMapLocation.getLongitude();
                if (!AppContext.getInstance().checkUser())
                    return;
                map.put("latitude", lat);
                map.put("longitude", log);
                map.put("speed", aMapLocation.getSpeed());
                map.put("accuracy", aMapLocation.getAccuracy());
                map.put("timestamp", aMapLocation.getTime());
                map.put("orientation", aMapLocation.getBearing());
                map.put("type", aMapLocation.getLocationType());
                LatLng endLatLng = new LatLng(lat, log);
                if (slatLng == null) {
                    slatLng = new LatLng(lat, log);
                    ApiClient.requestNetHandle(PositionService.this, AppConfig.modify_userPosition, null, map, new ResultListener() {
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
                    return;
                } else if (AMapUtils.calculateLineDistance(slatLng, endLatLng) > 20) {
                    L.d("TAG", AMapUtils.calculateLineDistance(slatLng, endLatLng) + "计算数据");
                    slatLng = endLatLng;
                    ApiClient.requestNetHandle(PositionService.this, AppConfig.modify_userPosition, null, map, new ResultListener() {
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
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isStop) {
                return;
            } else {
                updateLoc();
                handler.sendEmptyMessageDelayed(0, 30000);
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = false;
    }
}
