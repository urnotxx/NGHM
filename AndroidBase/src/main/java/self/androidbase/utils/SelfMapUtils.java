package self.androidbase.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Locale;

/**
 * 地图工具类
 * Created by Janesen on 16/1/28.
 */
public class SelfMapUtils implements AMapLocationListener {
    private static SelfMapUtils aMapUtils;

    public static SelfMapUtils getInstance(Context context) {
        aMapUtils = new SelfMapUtils();
        aMapUtils.context = context;
        return aMapUtils;
    }

    private SelfMapUtils() {
    }

    private Handler handler;
    private Context context;
    private AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    /**
     * 开始定位
     *
     * @param handler
     */
    public void startLocation(Handler handler) {
        this.handler = handler;
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Message message = new Message();
        message.what = SelfMapUtils.class.hashCode();
        message.obj = aMapLocation;
        handler.sendMessage(message);
        Locale.setDefault(Locale.CHINA);
        stopLocation();
    }


    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }
}
