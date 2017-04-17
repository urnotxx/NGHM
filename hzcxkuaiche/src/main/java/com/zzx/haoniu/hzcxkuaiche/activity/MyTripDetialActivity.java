package com.zzx.haoniu.hzcxkuaiche.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.overlay.DrivingRouteOverlay;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.SensorEventHelper;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import self.androidbase.utils.SelfMapUtils;

public class MyTripDetialActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    @Bind(R.id.mapMTI)
    MapView mapMTI;
    @Bind(R.id.activity_my_trip_detial)
    RelativeLayout activityMyTripDetial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_detial);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityMyTripDetial);
        mapMTI = (MapView) findViewById(R.id.mapMTI);
        mapMTI.onCreate(savedInstanceState);// 此方法必须重写
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        initMap();
        initView();
        colorList = new ArrayList<>();
        colorList.add(Color.argb(0xff, 0x76, 0xc9, 0x4b));
        initLocation();
        EventBus.getDefault().register(this);
    }

    private AMap aMap;
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    private Marker mLocMarker;
    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    private List<Integer> colorList;
    private RouteSearch routeSearch;
    private Bundle bundle;
    private DrivingRouteOverlay drivingRouteOverlay;

    private OrderInfo orderInfo, secOrderInfo;

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("pathPlan")) {//接/送
            tag = Integer.parseInt(event.getMessage());
            if (tag == 1) {
                orderInfo.setStatus(2);
                isTake1 = true;
                isTake2 = false;
            } else if (tag == 2) {
                orderInfo.setStatus(3);
                isSend1 = true;
                isSend2 = false;
            } else if (tag == 3) {
                isTake1 = false;
                isTake2 = true;
                secOrderInfo.setStatus(2);
            } else if (tag == 4) {
                secOrderInfo.setStatus(3);
                isSend2 = true;
                isSend1 = false;
            }
            initLocation();
        } else if (event.getSendCode().equals("userCancleOrderInfo")) {//用户取消
            if (orderInfo != null && secOrderInfo == null) {
                finish();
            } else if (orderInfo == null && secOrderInfo != null) {
                finish();
            } else if (orderInfo != null && secOrderInfo != null) {
                if (orderInfo.getId() == Integer.parseInt(event.getMessage())) {
                    orderInfo = null;
                } else if (secOrderInfo.getId() == Integer.parseInt(event.getMessage())) {
                    secOrderInfo = null;
                }
            }
        } else if (event.getSendCode().equals("cancleOrder")) {//司机取消订单
            if (orderInfo != null && secOrderInfo == null) {
                finish();
            } else if (orderInfo == null && secOrderInfo != null) {
                finish();
            } else if (orderInfo != null && secOrderInfo != null) {
                if (orderInfo.getId() == Integer.parseInt(event.getMessage())) {
                    orderInfo = null;
                } else if (secOrderInfo.getId() == Integer.parseInt(event.getMessage())) {
                    secOrderInfo = null;
                }
            }
        } else if (event.getSendCode().equals("secOrderInfo")) {//第二个订单
            secOrderInfo = (OrderInfo) event.getMap().get("secOrderInfo");
            secOrderInfo.setStatus(2);
        } else if (event.getSendCode().equals("pickUp")) {//接到用户
            if (orderInfo.getId() == Integer.parseInt(event.getMessage())) {
                orderInfo.setStatus(3);
            } else {
                secOrderInfo.setStatus(3);
            }
        } else if (event.getSendCode().equals("arrived")) {//到达目的地
            if (orderInfo != null && secOrderInfo == null) {
                finish();
            } else if (orderInfo == null && secOrderInfo != null) {
                finish();
            } else if (orderInfo != null && secOrderInfo != null) {
                if (event.getMessage().equals("1")) {
                    orderInfo = secOrderInfo;
                    secOrderInfo = null;
                } else {
                    secOrderInfo = null;
                }
            }
        } else if (event.getSendCode().equals("unLogInKC")) {
            finish();
        }
    }

    /**
     * 初始化
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapMTI.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    public void initParms(Bundle parms) {
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
        secOrderInfo = (OrderInfo) parms.getSerializable("orderInfo1");
        startLatlngA = new LatLng(orderInfo.getTrip().getStartLatitude(), orderInfo.getTrip().getStartLongitude());
        endLatlngA = new LatLng(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude());
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        if (orderInfo.getStatus() == 2) {
            tag = 1;
        } else if (orderInfo.getStatus() == 3) {
            tag = 2;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    private boolean isTake1, isTake2;
    private boolean isSend1, isSend2;

    @OnClick(R.id.rlDetialTD)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlDetialTD:
                List<OrderInfo> orderInfos = new ArrayList<>();
                if (orderInfo != null) {
                    orderInfos.add(orderInfo);
                }
                if (secOrderInfo != null) {
                    orderInfos.add(secOrderInfo);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderInfos", (Serializable) orderInfos);
                bundle.putBoolean("isTake1", isTake1);
                bundle.putBoolean("isTake2", isTake2);
                bundle.putBoolean("isSend1", isSend1);
                bundle.putBoolean("isSend2", isSend2);
                startActivity(OrderDetialActivity.class, bundle);
                break;
        }
    }

    private int tag = 1;//  1 到A处   2到A终点   3到B处  4到B终点

    private void initLocation() {
        SelfMapUtils selfMapUtils = SelfMapUtils.getInstance(this);
        selfMapUtils.startLocation(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AMapLocation aMapLocation = (AMapLocation) msg.obj;
                LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                if (tag == 1) {
                    searchRouteResult(latLonPoint, new LatLonPoint(startLatlngA.latitude, startLatlngA.longitude));
                } else if (tag == 2) {
                    searchRouteResult(latLonPoint, new LatLonPoint(endLatlngA.latitude, endLatlngA.longitude));
                } else if (tag == 3) {
                    searchRouteResult(latLonPoint, new LatLonPoint(startLatlngB.latitude, startLatlngB.longitude));
                } else if (tag == 4) {
                    searchRouteResult(latLonPoint, new LatLonPoint(endLatlngB.latitude, endLatlngB.longitude));
                }
            }
        });
    }

    /**
     * 开始搜索路径规划
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        List<LatLonPoint> latLonPoints = new ArrayList<>();
        latLonPoints.add(startPoint);
        latLonPoints.add(endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, latLonPoints, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        if (routeSearch == null)
            routeSearch = new RouteSearch(mContext);
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
                dissmissProgressDialog();
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (drivingRouteOverlay != null) {
                        drivingRouteOverlay.removeFromMap();
                    }
                    if (driveRouteResult != null && driveRouteResult.getPaths() != null
                            && driveRouteResult.getPaths().size() > 0) {
                        DriveRouteResult mDriveRouteResult = driveRouteResult;
                        DrivePath drivePath = driveRouteResult.getPaths().get(0);
                        drivingRouteOverlay = new DrivingRouteOverlay(
                                mContext, aMap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                        drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                        drivingRouteOverlay.setThroughPointIconVisibility(true);
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.addToMap();
                        drivingRouteOverlay.zoomToSpan();
                        drivePath.getTolls();
                    } else {
                        Toast.makeText(mContext, "对不起，没查询到结果", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else if (rCode == 27) {
//                    Toast.makeText(mContext, "net error", Toast.LENGTH_SHORT).show();
                } else {
                    L.d("TAG", "rCode:" + rCode);
//                    Toast.makeText(mContext, "other error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                locLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(locLatLng, aMapLocation.getAccuracy());//添加定位精度圆
                    addMarker(locLatLng);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                } else {
                    mCircle.setCenter(locLatLng);
                    mCircle.setRadius(aMapLocation.getAccuracy());
                    mLocMarker.setPosition(locLatLng);
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("tag", "AmapErr:" + errText);
            }
        }
    }

    private LatLng startLatlngA = new LatLng(31.8543800000, 117.2555240000);//终点
    private LatLng endLatlngA = new LatLng(31.8658980000, 117.2903070000);//终点131.8658980000,117.2903070000

    private LatLng startLatlngB = new LatLng(31.8543800000, 117.2555240000);//终点
    private LatLng endLatlngB = new LatLng(31.8658980000, 117.2903070000);//终点131.8658980000,117.2903070000

    private LatLng locLatLng;
    private ProgressDialog progDialog = null;// 搜索时进度条

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapMTI.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapMTI.onPause();
        deactivate();
        mFirstFix = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapMTI.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapMTI.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        ToastUtils.showTextToast(mContext, "当前行程还未结束!");
    }
}
