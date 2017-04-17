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
import android.widget.LinearLayout;
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
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.overlay.DrivingRouteOverlay;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.SensorEventHelper;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;
import com.zzx.haoniu.hzcxkuaiche.websocket.WebSocketWorker;

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
    @Bind(R.id.tvLookDetialTI)
    TextView tvLookDetialTI;
    @Bind(R.id.mapTI)
    MapView mapTI;
    @Bind(R.id.tvTop1TI)
    TextView tvTop1TI;
    @Bind(R.id.tvTop2TI)
    TextView tvTop2TI;
    @Bind(R.id.civHeadTI2)
    CircleImageView civHeadTI2;
    @Bind(R.id.tvTop1TI2)
    TextView tvTop1TI2;
    @Bind(R.id.tvTop2TI2)
    TextView tvTop2TI2;
    @Bind(R.id.tvLookDetialTI2)
    TextView tvLookDetialTI2;
    @Bind(R.id.llTopTI)
    LinearLayout llTopTI;
    @Bind(R.id.llBottomTI)
    LinearLayout llBottomTI;
    @Bind(R.id.activity_trip_interface)
    LinearLayout activityTripInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_interface);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityTripInterface);
        mapTI = (MapView) findViewById(R.id.mapTI);
        mapTI.onCreate(savedInstanceState);// 此方法必须重写
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        }
        initMap();
        initView();
        widthInPx = (DensityUtils.getWidthInPx(mContext) - DensityUtils.dip2px(mContext, 300)) / 2;
        heightInPx = DensityUtils.getHeightInPx(mContext) - DensityUtils.dip2px(mContext, 170);
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
    private float heightInPx, widthInPx;
    private RouteSearch routeSearch;
    private Bundle bundle;

    private int tag = 1;//  1 到A处   2到A终点   3到B处  4到B终点
    private OrderInfo orderInfo, secOrderInfo;

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("secondOrderInfo")) {
            secOrderInfo = (OrderInfo) event.getMap().get("orderInfo");
            secOrderInfo.setStatus(2);//还未接到第二位客人
            llBottomTI.setVisibility(View.VISIBLE);
            initSecondOrderInfo();
        } else if (event.getSendCode().equals("puckUp")) {//接到客人
            if (event.getMessage().equals("1")) {
                orderInfo.setStatus(3);//  10  未接到客户  11 接到客户  12 订单结束
                tag = 2;
                initLocation();
            } else {
                secOrderInfo.setStatus(3);
                if (AMapUtils.calculateLineDistance(locLatLng, endLatlngA) >= AMapUtils.calculateLineDistance(locLatLng, endLatlngB)) {
                    tag = 4;
                } else {
                    tag = 2;
                }
                initLocation();
            }
        } else if (event.getSendCode().equals("arrived")) {//订单结束
            if (event.getMessage().equals("1")) {
//                orderInfo.setStatus(12);//   10  未接到客户  11 接到客户  12 订单结束
                llTopTI.setVisibility(View.GONE);
                orderInfo = null;
                if (secOrderInfo == null) {
                    finish();
                    AppContext.getInstance().onLine(mContext, 1, "上线中...");
                } else {
                    tag = 4;
                    initLocation();
                }
            } else {
                llBottomTI.setVisibility(View.GONE);
                secOrderInfo = null;
                if (orderInfo == null) {
                    finish();
                    AppContext.getInstance().onLine(mContext, 1, "上线中...");
                } else {
                    tag = 2;
                    initLocation();
                }
            }
        } else if (event.getSendCode().equals("cancaleOrder")) {
            if (orderInfo != null && secOrderInfo == null) {
                finish();
            } else if (orderInfo == null && secOrderInfo != null) {
                finish();
            } else if (orderInfo != null && secOrderInfo != null) {
                if (Integer.parseInt(event.getMessage()) == orderInfo.getId()) {
                    orderInfo = null;
                    llTopTI.setVisibility(View.GONE);
                    if (secOrderInfo.getStatus() == 3) {
                        if (tag != 4) {
                            tag = 4;
                            initLocation();
                        }
                    }
                } else if (Integer.parseInt(event.getMessage()) == secOrderInfo.getId()) {
                    llBottomTI.setVisibility(View.GONE);
                    secOrderInfo = null;
                    if (tag != 2) {
                        tag = 2;
                        initLocation();
                    }
                }
            }
        } else if (event.getSendCode().equals("userCancleOrderInfo")) {
            String orderId = event.getMessage();
            if (orderInfo != null && secOrderInfo == null) {
                ToastUtils.showTextToast(mContext, "订单已被乘客取消!");
                WebSocketWorker.jiedanState = 0;
                AppContext.getInstance().onLine(mContext, 1, "上线中...");
                finish();
            } else if (orderInfo == null && secOrderInfo != null) {
                ToastUtils.showTextToast(mContext, "订单已被乘客取消!");
                WebSocketWorker.jiedanState = 0;
                AppContext.getInstance().onLine(mContext, 1, "上线中...");
                finish();
            } else if (orderInfo != null && secOrderInfo != null) {
                if (Integer.parseInt(orderId) == orderInfo.getId()) {
                    orderInfo = null;
                    llTopTI.setVisibility(View.GONE);
                    if (secOrderInfo.getStatus() == 3) {
                        if (tag != 4) {
                            tag = 4;
                            initLocation();
                        }
                    }
                } else if (Integer.parseInt(orderId) == secOrderInfo.getId()) {
                    llBottomTI.setVisibility(View.GONE);
                    secOrderInfo = null;
                    if (tag != 2) {
                        tag = 2;
                        initLocation();
                    }
                }
            }
        } else if (event.getSendCode().equals("unLogInKC")) {
            finish();
        }
    }

    private void initSecondOrderInfo() {//第二个订单
        startLatlngB = new LatLng(secOrderInfo.getTrip().getStartLatitude(), secOrderInfo.getTrip().getStartLongitude());
        endLatlngB = new LatLng(secOrderInfo.getTrip().getEndLatitude(), secOrderInfo.getTrip().getEndLongitude());
        tag = 3;
        initLocation();
        if (secOrderInfo.getNick_name() != null && !StringUtils.isEmpty(secOrderInfo.getNick_name())) {
            tvTop1TI2.setText("送" + secOrderInfo.getNick_name() + "至" + secOrderInfo.getDestination());
        }
        if (secOrderInfo.getDestination() != null && !StringUtils.isEmpty(secOrderInfo.getDestination())) {
            tvTop2TI2.setText("目的地:" + secOrderInfo.getDestination());
        }
        if (secOrderInfo.getHead_portrait() != null && !StringUtils.isEmpty(secOrderInfo.getHead_portrait())) {
            ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + secOrderInfo.getHead_portrait(), civHeadTI2);
        } else {
            civHeadTI2.setImageResource(R.mipmap.img_head);
        }
    }

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
        if (orderInfo.getStatus() == 2) {
            tag = 1;
        } else if (orderInfo.getStatus() == 3) {
            tag = 2;
        }
        if (secOrderInfo != null) {
            startLatlngB = new LatLng(secOrderInfo.getTrip().getStartLatitude(), secOrderInfo.getTrip().getStartLongitude());
            endLatlngB = new LatLng(secOrderInfo.getTrip().getEndLatitude(), secOrderInfo.getTrip().getEndLongitude());
            if (secOrderInfo.getNick_name() != null && !StringUtils.isEmpty(secOrderInfo.getNick_name())) {
                tvTop1TI2.setText("送" + secOrderInfo.getNick_name() + "至" + secOrderInfo.getDestination());
            }
            if (secOrderInfo.getDestination() != null && !StringUtils.isEmpty(secOrderInfo.getDestination())) {
                tvTop2TI2.setText("目的地:" + secOrderInfo.getDestination());
            }
            if (secOrderInfo.getHead_portrait() != null && !StringUtils.isEmpty(secOrderInfo.getHead_portrait())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + secOrderInfo.getHead_portrait(), civHeadTI2);
            } else {
                civHeadTI2.setImageResource(R.mipmap.img_head);
            }
        }
        initLocation();
    }

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
        if (parms.getBoolean("haveTwo")) {
            secOrderInfo = (OrderInfo) parms.getSerializable("orderInfo1");
        }
        startLatlngA = new LatLng(orderInfo.getTrip().getStartLatitude(), orderInfo.getTrip().getStartLongitude());
        endLatlngA = new LatLng(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude());
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onBackPressed() {
        ToastUtils.showTextToast(mContext, "当前订单还未结束!");
    }

    @OnClick({R.id.tvLookDetialTI, R.id.tvLookDetialTI2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLookDetialTI:
                bundle.putSerializable("orderInfo", orderInfo);
                bundle.putInt("flag", 1);
                startActivity(ConfirmBillActivity.class, bundle);
                break;
            case R.id.tvLookDetialTI2:
                bundle.putSerializable("orderInfo", secOrderInfo);
                bundle.putInt("flag", 2);
                startActivity(ConfirmBillActivity.class, bundle);
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
                ToastUtils.showTextToast(mContext, "接到客人");
                tvLookDetialTI.setVisibility(View.VISIBLE);
                if (drivingRouteOverlay != null) {
                    drivingRouteOverlay.removeFromMap();
                    initLocation();
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

}
