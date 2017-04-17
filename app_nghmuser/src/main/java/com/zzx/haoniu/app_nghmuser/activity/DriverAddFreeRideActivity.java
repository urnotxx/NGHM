package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.dialog.StartTimeDialog;
import com.zzx.haoniu.app_nghmuser.entity.AddressInfo;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;
import com.zzx.haoniu.app_nghmuser.view.CusAddFreeView;
import com.zzx.haoniu.app_nghmuser.view.DriverAddFreeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialog.listener.OnOperItemClickL;
import iosdialog.dialog.widget.ActionSheetDialog;
import iosdialog.dialogsamples.utils.ViewFindUtils;
import self.androidbase.views.SelfLinearLayout;

public class DriverAddFreeRideActivity extends BaseActivity implements LocationSource, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.rlContentDA)
    RelativeLayout rlContentDA;
    @Bind(R.id.activity_driver_add_free_ride)
    LinearLayout activityDriverAddFreeRide;

    private final static int REQUEST_PLACE = 5;//地址返回结果码


    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private GeocodeSearch geocodeSearch;
    private boolean isFirstLoc = true;
    private boolean isLoc;

    private ActionSheetDialog numDialog;
    private int num = 0;
    private String startPlace;
    private String endPlace;
    private double startLat, startLng, endLat, endLng;
    private String city;

    private DriverAddFreeView driverAddFreeView;
    final String[] stringItems = {"1人", "2人", "3人", "4人"};
    private ExpandableListView elv;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_free_ride);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        steepStatusBar();
        setMargins(activityDriverAddFreeRide);
        initView();
        initMap();
        initLocation();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
//            aMap.setOnCameraChangeListener(this);
//            aMap.setOnInfoWindowClickListener(this);
        }
        geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.setOnGeocodeSearchListener(this);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });
        initLocationStyle();
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        MarkerOptions markerOptions = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .title("在这里停车")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_center));
        final Marker screenMarker = aMap.addMarker(markerOptions);
        screenMarker.showInfoWindow();
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLonPoint latLonPoint = new LatLonPoint(screenMarker.getPosition().latitude, screenMarker.getPosition().longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP);
                //异步查询
                geocodeSearch.getFromLocationAsyn(query);
            }
        });
    }

    private void initLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    private void initLocation() {
        //设置定位回调监听，这里要实现AMapLocationListener接口，
        // AMapLocationListener接口只有onLocationChanged方法可以实现，
        // 用于接收异步返回的定位结果，参数是AMapLocation类型。
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(600000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        tvTitle.setText("发布行程");
        tvSubmit.setText("确 认 ");
        decorView = getWindow().getDecorView();
        elv = ViewFindUtils.find(decorView, R.id.elv);
        driverAddFreeView = new DriverAddFreeView(mContext);
        driverAddFreeView.setSelectListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartTimeDialog();
            }
        });
        driverAddFreeView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoc = true;
                if (city != null)
                    startActivityForResult(new Intent(mContext, InputSearchActivity.class)
                            .putExtra("flag", 2).putExtra("city", city), REQUEST_PLACE);
                else ToastUtils.showTextToast(mContext, "当前位置为空,请开启定位权限!");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoc = false;
                if (city != null)
                    startActivityForResult(new Intent(mContext, InputSearchActivity.class)
                            .putExtra("flag", 2).putExtra("city", city), REQUEST_PLACE);
                else ToastUtils.showTextToast(mContext, "当前位置为空,请开启定位权限!");
            }
        });
        driverAddFreeView.setBookListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showTextToast(mContext, "车主发布行程");
            }
        });
        rlContentDA.addView(driverAddFreeView);
    }

    private void showNumDialog() {
        if (numDialog == null) {
            numDialog = new ActionSheetDialog(mContext, stringItems, elv);
        }
        numDialog.title("可提供几人座");
        numDialog.titleTextColor(getResources().getColor(R.color.colorBlack));
        numDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                num = position;
                driverAddFreeView.setPeopleNum(stringItems[position]);
                numDialog.dismiss();
            }
        });
        numDialog.show();
    }

    private StartTimeDialog startTimeDialog;

    private String startTime;

    private void showStartTimeDialog() {
        if (startTimeDialog == null)
            startTimeDialog = new StartTimeDialog(mContext);
        startTimeDialog.setButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startTimeDialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (startTimeDialog.getSelectIndex() != null && startTimeDialog.getSelectIndex().length == 6) {
                    String[] selectIndex = startTimeDialog.getSelectIndex();
                    driverAddFreeView.setStartTime(selectIndex[3] + selectIndex[4] + selectIndex[5]);
                }
                startTimeDialog.dismiss();
            }
        });
        startTimeDialog.show();
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ll_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_right:
                break;
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    startPlace = aMapLocation.getPoiName();
                    startLat = aMapLocation.getLatitude();
                    startLng = aMapLocation.getLongitude();
                    driverAddFreeView.setLoc(startPlace);
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    isFirstLoc = false;
                    city = aMapLocation.getCity();
//                    addCenterMarker(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), true);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        List<AoiItem> aois = regeocodeAddress.getAois();
        if (aois != null && aois.size() > 0) {
            String aoiName = aois.get(0).getAoiName();
            if (aoiName != null && !StringUtils.isEmpty(aoiName)) {
                driverAddFreeView.setLoc(startPlace);
                AddressInfo info = new AddressInfo(aoiName, aois.get(0).getAoiCenterPoint().getLatitude()
                        , aois.get(0).getAoiCenterPoint().getLongitude());
                driverAddFreeView.addAddress(info);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == InputSearchActivity.RESUALT_PLACE) {
            if (requestCode == REQUEST_PLACE) {
                if (isLoc) {
                    startLat = data.getDoubleExtra("searchLat", 0);
                    startLng = data.getDoubleExtra("searchLng", 0);
                    startPlace = data.getStringExtra("searchName");
                    driverAddFreeView.setLoc(startPlace);
                } else {
                    endLat = data.getDoubleExtra("searchLat", 0);
                    endLng = data.getDoubleExtra("searchLng", 0);
                    endPlace = data.getStringExtra("searchName");
                    driverAddFreeView.setDes(endPlace);
                }
            }
        }
    }
}
