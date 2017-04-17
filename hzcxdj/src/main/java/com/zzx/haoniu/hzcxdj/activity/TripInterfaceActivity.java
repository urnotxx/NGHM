package com.zzx.haoniu.hzcxdj.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxdj.entity.OrderInfo;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.overlay.DrivingRouteOverlay;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.SensorEventHelper;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;
import com.zzx.haoniu.hzcxdj.view.SlidingButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import self.androidbase.utils.DensityUtils;
import self.androidbase.utils.SelfMapUtils;

public class TripInterfaceActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    @Bind(R.id.civHeadTI)
    CircleImageView civHeadTI;
    @Bind(R.id.ivCallTI)
    ImageView ivCallTI;
    @Bind(R.id.tvLookDetialTI)
    TextView tvLookDetialTI;
    @Bind(R.id.tvPromptTI)
    TextView tvPromptTI;
    @Bind(R.id.mapTI)
    MapView mapTI;
    @Bind(R.id.slidingButtonTI)
    SlidingButton slidingButtonTI;
    @Bind(R.id.tvTop1TI)
    TextView tvTop1TI;
    @Bind(R.id.tvTop2TI)
    TextView tvTop2TI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_interface);
        ButterKnife.bind(this);
        steepStatusBar();
        mapTI = (MapView) findViewById(R.id.mapTI);
        mapTI.onCreate(savedInstanceState);// 此方法必须重写
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        initMap();
        initView();
        widthInPx = (DensityUtils.getWidthInPx(mContext) - DensityUtils.dip2px(mContext, 300)) / 2;
        heightInPx = DensityUtils.getHeightInPx(mContext) - DensityUtils.dip2px(mContext, 170);
        initLocation();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("userCancleOrderInfo")) {
            ToastUtils.showTextToast(mContext, "订单已被乘客取消!");
            AppContext.getInstance().onLine(mContext, 1, "");
            finish();
        }
        if (event.getSendCode().equals("cancleOrder")) {
            AppContext.getInstance().onLine(mContext, 1, "");
            finish();
        }
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
    private float heightInPx, widthInPx;
    private RouteSearch routeSearch;
    private Bundle bundle;

    private void initLocation() {
        SelfMapUtils selfMapUtils = SelfMapUtils.getInstance(this);
        selfMapUtils.startLocation(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AMapLocation aMapLocation = (AMapLocation) msg.obj;
                runFlag = 2;
                slatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                if (!isTake) {
                    searchRouteResult(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), new LatLonPoint(endLatlng.latitude, endLatlng.longitude));
                } else {
                    searchRouteResult(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), new LatLonPoint(endLatlng1.latitude, endLatlng1.longitude));
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapTI.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    private Boolean isOntouch = true;
    private boolean isTake = false;

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (slidingButtonTI.handleActivityEvent(motionEvent)) {
                    if (isTake) {
                        bundle.putFloat("runDistance", runDistance / 1000);
                        startActivity(ConfirmBillActivity.class, bundle);
                        finish();
                    } else {//接到客人
                        puckUp();
                    }
                    mUiSettings.setScrollGesturesEnabled(false);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    isOntouch = true;
                }
                if (widthInPx <= motionEvent.getX() && motionEvent.getX() <= (widthInPx + DensityUtils.dip2px(mContext, 300))
                        && heightInPx <= motionEvent.getY() && motionEvent.getY() <= (heightInPx + DensityUtils.dip2px(mContext, 50))) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        isOntouch = false;
                    }
                    mUiSettings.setScrollGesturesEnabled(false);
                } else {
                    if (isOntouch) {
                        mUiSettings.setScrollGesturesEnabled(true);
                    }
                }
            }
        });
    }

    @Override
    public void initView() {
        colorList = new ArrayList<>();
        colorList.add(Color.argb(0xff, 0x76, 0xc9, 0x4b));
        if (orderInfo.getNick_name() != null && !StringUtils.isEmpty(orderInfo.getNick_name())) {
            tvTop1TI.setText("送" + orderInfo.getNick_name() + "至" + orderInfo.getDestination());
        }
        if (orderInfo.getDestination() != null && !StringUtils.isEmpty(orderInfo.getDestination())) {
            tvTop2TI.setText("目的地:" + orderInfo.getDestination());
        }
        if (orderInfo.getHead_portrait() != null && !StringUtils.isEmpty(orderInfo.getHead_portrait())) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo.getHead_portrait(), civHeadTI);
        } else {
            civHeadTI.setImageResource(R.mipmap.img_head);
        }
    }

    private DriveRouteResult mDriveRouteResult;
    private OrderInfo orderInfo;

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
        endLatlng = new LatLng(orderInfo.getTrip().getStartLatitude(), orderInfo.getTrip().getStartLongitude());
        endLatlng1 = new LatLng(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude());
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ivCallTI, R.id.tvLookDetialTI})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCallTI:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
                break;
            case R.id.tvLookDetialTI:
                startActivity(TripDetialActivity.class, bundle);
                break;
        }
    }

    private DrivingRouteOverlay drivingRouteOverlay;

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
                        mDriveRouteResult = driveRouteResult;
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
//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
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
                LatLng location = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                if (runFlag == 2 && slatLng != null) {
                    runDistance = runDistance + AMapUtils.calculateLineDistance(slatLng, location);
                    slatLng = location;
                }
                if (!mFirstFix) {
                    mFirstFix = true;
                    addCircle(location, aMapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(aMapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("tag", "AmapErr:" + errText);
            }
        }
    }

    private LatLng endLatlng = new LatLng(31.8543800000, 117.2555240000);//终点
    private LatLng endLatlng1 = new LatLng(31.8658980000, 117.2903070000);//终点131.8658980000,117.2903070000

    private LatLng slatLng, elatLng;
    private float runDistance;
    private int runFlag = 0;// 1 行程开始   2行程结束

    /**
     * 接到用户
     */
    private void puckUp() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", orderInfo.getId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestOrderStart, "接到客人...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "接到客人:" + json);
                }
                tvPromptTI.setText("右滑 到达目的地");
                isTake = true;
                ToastUtils.showTextToast(mContext, "接到客人");
                tvLookDetialTI.setVisibility(View.VISIBLE);
                ivCallTI.setVisibility(View.GONE);
                if (drivingRouteOverlay != null) {
                    drivingRouteOverlay.removeFromMap();
                    initLocation();
                    runFlag = 1;
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapTI.onResume();
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
        mapTI.onPause();
        deactivate();
        mFirstFix = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapTI.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapTI.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    private ProgressDialog progDialog = null;// 搜索时进度条

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
