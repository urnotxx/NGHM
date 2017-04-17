package com.zzx.haoniu.app_nghmuser.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.MainBusAdapter;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.dialog.CommentDialog;
import com.zzx.haoniu.app_nghmuser.dialog.PayDialog;
import com.zzx.haoniu.app_nghmuser.entity.AddressInfo;
import com.zzx.haoniu.app_nghmuser.entity.CarInfo;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.OrderInfo;
import com.zzx.haoniu.app_nghmuser.entity.PriceInfo;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.http.UserInfo;
import com.zzx.haoniu.app_nghmuser.overlay.BusRouteOverlay;
import com.zzx.haoniu.app_nghmuser.overlay.DrivingRouteOverlay;
import com.zzx.haoniu.app_nghmuser.utils.AMapUtil;
import com.zzx.haoniu.app_nghmuser.utils.AppUtils;
import com.zzx.haoniu.app_nghmuser.utils.L;
import com.zzx.haoniu.app_nghmuser.utils.PreferenceUtil;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;
import com.zzx.haoniu.app_nghmuser.view.Main1AddressView;
import com.zzx.haoniu.app_nghmuser.view.Main1BookingView;
import com.zzx.haoniu.app_nghmuser.view.Main2BookingView;
import com.zzx.haoniu.app_nghmuser.view.Main3WindCarView;
import com.zzx.haoniu.app_nghmuser.view.Main4BookingView;
import com.zzx.haoniu.app_nghmuser.view.Main5BusAdapterView;
import com.zzx.haoniu.app_nghmuser.view.Main5BusView;
import com.zzx.haoniu.app_nghmuser.websocket.WebSocketWorker;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ali.PayResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import iosdialog.animation.SlideExit.SlideBottomExit;
import iosdialog.animation.SlideExit.animation.BounceEnter.BounceTopEnter;
import iosdialog.dialog.listener.OnBtnClickL;
import iosdialog.dialog.widget.NormalDialog;
import self.androidbase.views.SelfLinearLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        LocationSource, AMapLocationListener, ActivityCompat.OnRequestPermissionsResultCallback, GeocodeSearch.OnGeocodeSearchListener {

    @Bind(R.id.ivLeftM)
    ImageView ivLeftM;
    @Bind(R.id.ivRightM)
    ImageView ivRightM;
    @Bind(R.id.tvMain1)
    TextView tvMain1;
    @Bind(R.id.tvMain2)
    TextView tvMain2;
    @Bind(R.id.tvMain3)
    TextView tvMain3;
    @Bind(R.id.tvMain4)
    TextView tvMain4;
    @Bind(R.id.tvMain5)
    TextView tvMain5;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.llTop)
    RelativeLayout llTop;
    @Bind(R.id.rlContent)
    RelativeLayout rlContent;
    @Bind(R.id.rlTop)
    RelativeLayout rlTop;

    private DrawerLayout drawer;
    private Context mContext;
    private int tag = 1;
    private boolean isLoc;//判断 是起点还是终点
    private boolean isShowTop;//判断 title 是否显示
    private final static int REQUEST_PLACE = 2;//地址返回结果码
    private TextView tvNameM, tvPhoneM;
    private CircleImageView civHeadM;
    private MapView mMapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private String cityCode;
    private int distance;
    private int time;
    private double amount;

    private OrderInfo runningOrderInfo;

    private boolean pdFlag = true;

    private Main1AddressView main1AddressView;
    private Main1BookingView main1BookingView;
    private Main2BookingView main2BookingView;
    private Main3WindCarView main3WindCarView;
    private Main4BookingView main4BookingView;
    private Main5BusView main5BusView;
    private int curIndex = 0;

    private GeocodeSearch geocodeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
//        steepStatusBar();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mContext = this;
        getRunningOrder();
        initView();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvNameM = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvNameM);
        tvPhoneM = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvPhoneM);
        civHeadM = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.civHeadM);
        carInfos = new ArrayList<>();
        initMap();
        initEvent();
        initLocation();
        EventBus.getDefault().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initService();
            }
        }).start();
    }

    private void initView() {
        // 选择地址view
        main1AddressView = new Main1AddressView(mContext);
        rlContent.addView(main1AddressView);
        main1AddressView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoc = true;
                startActivityForResult(new Intent(mContext, InputSearchActivity.class)
                        .putExtra("flag", 2).putExtra("city", city), REQUEST_PLACE);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoc = false;
                startActivityForResult(new Intent(mContext, InputSearchActivity.class)
                        .putExtra("flag", 2).putExtra("city", city), REQUEST_PLACE);
            }
        });
        //出租车  显示价格  及预订
        main1BookingView = new Main1BookingView(mContext);
        main1BookingView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });
        //快车车  显示价格(拼单，不拼单)  及预订
        main2BookingView = new Main2BookingView(mContext);
        main2BookingView.setColor(true);
        //pinListener, pinNoListener, bookListener
        main2BookingView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main2BookingView.setColor(true);
                pdFlag = true;
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main2BookingView.setColor(false);
                pdFlag = false;
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdFlag) {
                    Boolean pinDanDialog = PreferenceUtil.getBoolean(mContext, "pinDanDialog", true);//拼单时，是否显示人数提示
                    if (pinDanDialog) {
                        PreferenceUtil.putBoolean(mContext, "pinDanDialog", false);
                        showPinDanDialog();
                    } else {
                        createOrder();
                    }
                } else {
                    createOrder();
                }
            }
        });
        main3WindCarView = new Main3WindCarView(mContext);
        main3WindCarView.setBottomListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main3WindCarView.setCus(true);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main3WindCarView.setCus(false);
            }
        });

        main3WindCarView.setLevel1Listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, CustomAddFreeRideActivity.class));
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, CommeonAddressActivity.class));
            }
        });

        main3WindCarView.setLevel2Listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = AppContext.getInstance().getUserInfo();
                if (userInfo.getIs_ensure_idcard() == null
                        || StringUtils.isEmpty(userInfo.getIs_ensure_idcard())
                        || !userInfo.getIs_ensure_idcard().equals("1")) {
                    // 个人信息认证结束之后  专车认证
                    startActivity(new Intent(mContext, NameAuthenticationActivity.class).putExtra("tag", 1));
                } else {
                    startActivity(new Intent(mContext, CarAuthenticationActivity.class));
                }
            }
        });

        main3WindCarView.setLevel3Listener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DriverAddFreeRideActivity.class));
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showTextToast(mContext, "车主添加常用路线!");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, WindCarOrderInfoActivity.class));
            }
        });

        main4BookingView = new Main4BookingView(mContext);
        //代驾  显示价格  及预订
        main4BookingView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });
        main5BusView = new Main5BusView(mContext);
        main5BusView.viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setMyLocationButtonEnabled(true); // 显示默认的定位按钮
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
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
        mLocationOption.setInterval(6000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void initEvent() {
        civHeadM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppContext.getInstance().checkCallBackUser()) {
                    startActivity(new Intent(mContext, UserInfoActivity.class));
                } else {
                    ToastUtils.showTextToast(mContext, "请先登录!");
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
//                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        tvTitle.setText("确认呼叫");
    }

    @OnClick({R.id.rlLeft, R.id.rlRight, R.id.tvMain1, R.id.tvMain2, R.id.tvMain3, R.id.tvMain4, R.id.tvMain5, R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlLeft:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.rlRight:
                startActivity(new Intent(mContext, MsgInfoActivity.class));
                break;
            case R.id.tvMain1:
                tag = 1;
                initMainView();
                initFiveText();
                tvMain1.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                aMap.clear(true);
                getCars(3, 42);//查询出租车
                break;
            case R.id.tvMain2:
                tag = 2;
                initMainView();
                initFiveText();
                tvMain2.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                aMap.clear(true);
                getCars(4, 44);//查询快车
                break;
            case R.id.tvMain3:
                tag = 3;
                initMainView();
                initFiveText();
                tvMain3.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                break;
            case R.id.tvMain4:
                tag = 4;
                initFiveText();
                initMainView();
                tvMain4.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                aMap.clear(true);
                getCars(2, 40);//查询代驾
                break;
            case R.id.tvMain5:
                tag = 5;
                initFiveText();
                initMainView();
                tvMain5.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                aMap.clear(true);
                addMarkerInScreenCenter();
                break;
            case R.id.ll_back:
                isShowTop = false;
                main1AddressView.setDes("你要去哪");
                aMap.clear(true);
                addMarkerInScreenCenter();
                initMainView();
                if (tag == 1) {//出租车
                    getCars(3, 42);
                } else if (tag == 2) {//快车
                    getCars(4, 44);
                } else if (tag == 4) {//代驾
                    getCars(2, 40);
                }
                break;
        }
    }

    private NormalDialog pinDanDialog;

    private void showPinDanDialog() {
        if (pinDanDialog == null)
            pinDanDialog = new NormalDialog(mContext);
        pinDanDialog.content("拼单的乘车人员最多两位，确认下单么？")//
                .isTitleShow(false)
                .show();
        pinDanDialog.setLeftText("取消下单");
        pinDanDialog.setRightText("继续下单");
        pinDanDialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        pinDanDialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        pinDanDialog.dismiss();
                        createOrder();
                    }
                });
        pinDanDialog.show();
    }

    private boolean isCalated = false;//价格是否已经计算

    private String startPlace;
    private String endPlace;
    private double startLat, startLng, endLat, endLng;
    private RouteSearch routeSearch;
    private String city;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == InputSearchActivity.RESUALT_PLACE) {
            if (requestCode == REQUEST_PLACE) {
                if (isLoc) {
                    startLat = Double.parseDouble(data.getStringExtra("searchLat"));
                    startLng = Double.parseDouble(data.getStringExtra("searchLng"));
                    startPlace = data.getStringExtra("searchName");
                    main1AddressView.setLoc(startPlace);
                    if (endLng != 0 && endLat != 0) { //终点已确定
                        isShowTop = true;
                        isCalated = true;
                        initMainView();
                    }
                } else {
                    endLat = Double.parseDouble(data.getStringExtra("searchLat"));
                    endLng = Double.parseDouble(data.getStringExtra("searchLng"));
                    endPlace = data.getStringExtra("searchName");
                    main1AddressView.setDes(endPlace);
                    isShowTop = true;
                    isCalated = true;
                    initMainView();
                }
            }
        }
    }

    private void initMainView() {
        if (isShowTop) {//判断 title 是否显示
            rlTop.setVisibility(View.GONE);
            llTop.setVisibility(View.VISIBLE);
            if (tag == 1) {
                rlContent.removeAllViews();
                rlContent.addView(main1BookingView);
                showProgressDialog();
                setfromandtoMarker();
                searchRouteResult(new LatLonPoint(startLat, startLng), new LatLonPoint(endLat, endLng), RouteSearch.DrivingDefault, false);
            } else if (tag == 2) {
                rlContent.removeAllViews();
                rlContent.addView(main2BookingView);
                showProgressDialog();
                setfromandtoMarker();
                searchRouteResult(new LatLonPoint(startLat, startLng), new LatLonPoint(endLat, endLng), RouteSearch.DrivingDefault, false);
            } else if (tag == 3) {
                rlContent.removeAllViews();
                rlContent.addView(main3WindCarView);
            } else if (tag == 4) {
                rlContent.removeAllViews();
                rlContent.addView(main4BookingView);
                showProgressDialog();
                setfromandtoMarker();
                searchRouteResult(new LatLonPoint(startLat, startLng), new LatLonPoint(endLat, endLng), RouteSearch.DrivingDefault, false);
            } else if (tag == 5) {
                rlContent.removeAllViews();
                rlContent.addView(main5BusView);
                showProgressDialog();
                setfromandtoMarker();
                searchRouteResult(new LatLonPoint(startLat, startLng), new LatLonPoint(endLat, endLng), RouteSearch.BusDefault, true);
            }
        } else {
            if (tag == 1 || tag == 2 || tag == 4 || tag == 5) {
                rlContent.removeAllViews();
                rlContent.addView(main1AddressView);
                llTop.setVisibility(View.GONE);
                rlTop.setVisibility(View.VISIBLE);
            } else {
                rlContent.removeAllViews();
                rlContent.addView(main3WindCarView);
                llTop.setVisibility(View.GONE);
                rlTop.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 上面五个TextView颜色设置
     */
    private void initFiveText() {
        tvMain1.setTextColor(Color.rgb(0x68, 0x68, 0x68));
        tvMain2.setTextColor(Color.rgb(0x68, 0x68, 0x68));
        tvMain3.setTextColor(Color.rgb(0x68, 0x68, 0x68));
        tvMain4.setTextColor(Color.rgb(0x68, 0x68, 0x68));
        tvMain5.setTextColor(Color.rgb(0x68, 0x68, 0x68));
    }

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private DrivingRouteOverlay drivingRouteOverlay;
    private BusRouteOverlay mBusrouteOverlay;
    private BusRouteResult busRouteResult;

    /**
     * 开始搜索路径规划
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint, int myrouteSearch, boolean isBus) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        List<LatLonPoint> latLonPoints = new ArrayList<>();
        latLonPoints.add(startPoint);
        latLonPoints.add(endPoint);
        if (routeSearch == null)
            routeSearch = new RouteSearch(mContext);
        if (!isBus) {
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, myrouteSearch, latLonPoints, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else {
            // 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, myrouteSearch, city, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            routeSearch.calculateBusRouteAsyn(query);// 异步路径规划驾车模式查询
        }
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {
                dissmissProgressDialog();
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (busRouteResult != null && busRouteResult.getPaths() != null) {
                        if (busRouteResult.getPaths().size() > 0) {
                            MainActivity.this.busRouteResult = busRouteResult;
                            List<Main5BusAdapterView> views = new ArrayList<>();
                            for (int i = 0; i < busRouteResult.getPaths().size(); i++) {
                                views.add(new Main5BusAdapterView(mContext, busRouteResult.getPaths().get(i)));
                            }
                            main5BusView.viewPager.setAdapter(new MainBusAdapter(views));
                            initPoints(views.size());
                            mBusrouteOverlay = new BusRouteOverlay(mContext, aMap, busRouteResult.getPaths().get(0),
                                    busRouteResult.getStartPos(), busRouteResult.getTargetPos());
                            initBusMap();
                        } else if (busRouteResult != null && busRouteResult.getPaths() == null) {
                            ToastUtils.showTextToast(mContext, "对不起，没有搜索到相关数据！");
                        }
                    } else {
                        ToastUtils.showTextToast(mContext, "对不起，没有搜索到相关数据！");
                    }
                } else {
                    ToastUtils.showTextToast(mContext, errorCode + "");
                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
                dissmissProgressDialog();
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    aMap.clear();// 清理地图上的所有覆盖物
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
                        time = (int) drivePath.getDuration() / 60;
                        distance = (int) drivePath.getDistance() / 1000;
                        calaMount();
                    } else {
                        Toast.makeText(mContext, "对不起，没查询到结果", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else if (rCode == 27) {
                    Toast.makeText(mContext, "net error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "other error", Toast.LENGTH_SHORT).show();
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

    private void initBusMap() {
//        drivingRouteOverlay = new DrivingRouteOverlay(
//                mContext, aMap, drivePath,
//                mDriveRouteResult.getStartPos(),
//                mDriveRouteResult.getTargetPos(), null);
        aMap.clear();// 清理地图上的所有覆盖物
        mBusrouteOverlay.setNodeIconVisibility(true);//设置节点marker是否显示
        mBusrouteOverlay.removeFromMap();
        mBusrouteOverlay.addToMap();
        mBusrouteOverlay.zoomToSpan();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isShowTop) {
            isShowTop = false;
            main1AddressView.setDes("设置去哪");
            initMainView();
            aMap.clear(true);
            addMarkerInScreenCenter();
            if (tag == 1) {//出租车
                getCars(3, 42);
            } else if (tag == 2) {//快车
                getCars(4, 44);
            } else if (tag == 4) {//代驾
                getCars(2, 40);
            }
        }
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!isShowTop && !drawer.isDrawerOpen(GravityCompat.START)) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallet) {
            // Handle the camera action
            startActivity(new Intent(mContext, MyWalletActivity.class));
        } else if (id == R.id.nav_trip) {
            startActivity(new Intent(mContext, MyTripActivity.class));
        } else if (id == R.id.nav_kefu) {
            AppContext.getInstance().callUser(mContext, "68993318");
        } else if (id == R.id.nav_set) {
            startActivity(new Intent(mContext, SetActivity.class));
        }
        return true;
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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

    private NormalDialog locationDialog = null;
    private boolean isAllow = false;
    private boolean isFirstLoc = true;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                isAllow = true;
                if (locationDialog != null && locationDialog.isShowing()) {
                    locationDialog.dismiss();
                    locationDialog = null;
                }
                locLat = aMapLocation.getLatitude() + "";
                locLng = aMapLocation.getLongitude() + "";
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    startPlace = aMapLocation.getPoiName();
                    startLat = aMapLocation.getLatitude();
                    startLng = aMapLocation.getLongitude();
                    city = aMapLocation.getCity();
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    main1AddressView.setLoc(aMapLocation.getPoiName());
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    isFirstLoc = false;
                    cityCode = aMapLocation.getAdCode();
                    getCars(3, 42);//默认查询出租车
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                if (!isAllow)
                    showLocationDialog();
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        } else if (!isAllow) {
            showLocationDialog();
        }
    }

    /**
     * dialog 载入载出动画
     */
    public BounceTopEnter bas_in = new BounceTopEnter();
    public SlideBottomExit bas_out = new SlideBottomExit();

    private void showLocationDialog() {
        if (locationDialog != null && locationDialog.isShowing()) {
            return;
        }
        locationDialog = new NormalDialog(mContext);
        locationDialog.setLeftText("取消");
        locationDialog.setRightText("去设置");
        locationDialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#FFFFFF"))//
                .cornerRadius(5)//
                .contentTextSize(13f)
                .content("定位功能未开启,地图功能无法使用,去设置?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#111111"))//
                .dividerColor(Color.parseColor("#333333"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#007AFF"), Color.parseColor("#007AFF"))//
                .btnPressColor(Color.parseColor("#eeeeeeee"))//
                .widthScale(0.85f)//
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .show();
        locationDialog.setOnBtnClickL(
                new OnBtnClickL() {//left
                    @Override
                    public void onBtnClick() {
                        locationDialog.dismiss();
                        locationDialog = null;
                        mLocationClient.stopLocation();
                    }
                },
                new OnBtnClickL() {  //right
                    @Override
                    public void onBtnClick() {
                        locationDialog.dismiss();
                        AppUtils.getAppDetailSettingIntent(mContext);
                    }
                });
        locationDialog.setCancelable(false);
    }

    /**
     * 设置起点终点经纬度、图标
     */
    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(new LatLonPoint(startLat, startLng)))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(new LatLonPoint(endLat, endLng)))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end)));
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

    private String locLat, locLng;
    private List<CarInfo> carInfos;

    /**
     * 获取车辆信息
     */
    private void getCars(final int type, int serviceType) {
        if (!isShowTop) {
            addMarkerInScreenCenter();
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("serviceType", serviceType);
        map.put("longitude", locLng);
        map.put("latitude", locLat);
        ApiClient.requestNetHandle(mContext, AppConfig.requestCars, "", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (markerList.size() > 0) {
                    for (int i = 0; i < markerList.size(); i++) {
                        markerList.get(i).remove();
                    }
                }
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "获取车辆信息:" + json);
                    List<CarInfo> carInfos1 = JSON.parseArray(json, CarInfo.class);
                    if (carInfos1 != null && carInfos1.size() > 0) {
                        carInfos.clear();
                        carInfos.addAll(carInfos1);
                        addCars(type);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private List<Marker> markerList = new ArrayList<>();

    private void addCars(int type) {
//        LatLngBounds.Builder bounds = new LatLngBounds.Builder();//经纬度坐标矩形区域的生成器。
        for (int i = 0; i < carInfos.size(); i++) {
            LatLng latLng = new LatLng(carInfos.get(i).getLatitude(), carInfos.get(i).getLongitude());
//            bounds.include(latLng);
            MarkerOptions markOption = new MarkerOptions().anchor(0.5f, 0.5f)
                    .rotateAngle(Float.parseFloat(carInfos.get(i).getOrientation() + ""))
                    .position(latLng).draggable(false);
            if (type == 3) {//出租车
                markOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_taxi));
            } else if (type == 4) {//快车
                markOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_kuaiche));
            } else if (type == 2) {
                markOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_daijia));
            }
            markerList.add(aMap.addMarker(markOption));
        }
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        MarkerOptions markerOptions = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .title("在这里上车")
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

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        List<AoiItem> aois = regeocodeAddress.getAois();
        if (aois != null && aois.size() > 0) {
            String aoiName = aois.get(0).getAoiName();
            if (aoiName != null && !StringUtils.isEmpty(aoiName)) {
                startLat = aois.get(0).getAoiCenterPoint().getLatitude();
                startLng = aois.get(0).getAoiCenterPoint().getLongitude();
                startPlace = aoiName;
                main1AddressView.setLoc(startPlace);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 会员未完成的订单
     */
    private void getRunningOrder() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestRunningOrder, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "会员未完成的订单 :" + json);
                    runningOrderInfo = JSON.parseObject(json, OrderInfo.class);
                    if (runningOrderInfo != null) {
                        if (runningOrderInfo.getStatus() == 1 || runningOrderInfo.getStatus() == 2 || runningOrderInfo.getStatus() == 3) {//下单且未被接单
                            int flag = 0;
                            if (runningOrderInfo.getOrderType() == 42) {//出租车
                                flag = 1;
                            } else if (runningOrderInfo.getOrderType() == 44) {//快车
                                flag = 2;
                            } else if (runningOrderInfo.getOrderType() == 40) {//代驾
                                flag = 2;
                            }
                            startActivity(new Intent(mContext, BookTaxiActivity.class)
                                    .putExtra("orderId", runningOrderInfo.getOrderId())
                                    .putExtra("status", runningOrderInfo.getStatus())
                                    .putExtra("orderInfo", runningOrderInfo)
                                    .putExtra("flag", flag));
                        } else if (runningOrderInfo.getStatus() == 4) {
                            showPayDialog();
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private PayDialog payDialog;

    private void showPayDialog() {
        if (payDialog == null) {
            payDialog = new PayDialog(mContext);
        }
        payDialog.setCancelable(false);
        if (runningOrderInfo.getNickName() != null) {
            payDialog.tvDriverNameB.setText(runningOrderInfo.getNickName());
        }
        if (runningOrderInfo.getRealPay() != 0) {
            payDialog.tvPriceB.setText(runningOrderInfo.getRealPay() + "元");
        }
        if (runningOrderInfo.getCar() != null) {
            if (runningOrderInfo.getCar().getPlateNo() != null) {
                payDialog.tvPlateB.setText(runningOrderInfo.getCar().getPlateNo());
            }
        }
        payDialog.setPayClick(new DialogInterface.OnClickListener() {//支付
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPayOrderInfo();
            }
        }, new DialogInterface.OnClickListener() {//明细
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(mContext, MingXiTaxiActivity.class)
                        .putExtra("totalFee", runningOrderInfo.getRealPay())
                        .putExtra("ygFee", runningOrderInfo.getAmount())
                        .putExtra("otherFee", (float) (runningOrderInfo.getRealPay() - runningOrderInfo.getAmount())));
            }
        }, new DialogInterface.OnClickListener() {//打电话
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppContext.getInstance().callUser(mContext, runningOrderInfo.getPhone());
            }
        });
        payDialog.show();
    }

    /**
     * 获取支付信息
     */
    private void getPayOrderInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", runningOrderInfo.getOrderId());
        map.put("payType", 1);
        map.put("couponId", 0);
        ApiClient.requestNetHandle(mContext, AppConfig.requestPayInfo, "正在获取支付信息...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "支付信息:" + json);
                    pay(json);
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void pay(final String msg) {
        new Thread() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(MainActivity.this); // 构造PayTask 对象
                String result = alipay.pay(msg, true); // 调用支付接口，获取支付结果
                Message msg = new Message();
                msg.what = PAYFLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private static final int PAYFLAG = 310;
    /**
     * 支付宝支付异步通知
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == PAYFLAG) {
                String resultStatus = new PayResult((String) msg.obj).getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if ("9000".equals(resultStatus)) {
                    payDialog.dismiss();
                    Toast.makeText(mContext, "支付成功!", Toast.LENGTH_LONG).show();
                    showCommentDialog();
                    //后续业务处理
                } else if ("8000".equals(resultStatus)) { //"8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    Toast.makeText(mContext, "支付结果确认中!", Toast.LENGTH_LONG).show();
                } else { //其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    Toast.makeText(mContext, "支付失败!", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    private CommentDialog commentDialog;

    /**
     * 评价dialog
     */
    private void showCommentDialog() {
        if (commentDialog == null) {
            commentDialog = new CommentDialog(mContext);
        }
        commentDialog.setCanConClick(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                commentDialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String grand = "";
                if (commentDialog.isOne) {
                    L.d("TAG", "评价" + 1);
                    grand = "车内整洁";
                }
                if (commentDialog.isTwo) {
                    L.d("TAG", "评价" + 2);
                    grand = grand + ",活地图认路准";
                }
                if (commentDialog.isThree) {
                    L.d("TAG", "评价" + 3);
                    grand = grand + ",驾驶平稳";
                }
                if (commentDialog.isFour) {
                    L.d("TAG", "评价" + 4);
                    grand = grand + ",服务态度好";
                }
                if (commentDialog.getComText() != null) {
                    L.d("TAG", "评价:" + commentDialog.getComText());
                    grand = grand + commentDialog.getComText();
                }
                grandOrder(grand, commentDialog.ratingBarDC.getRating());
                commentDialog.dismiss();
            }
        });
        commentDialog.setComClick(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                commentDialog.isOne = !commentDialog.isOne;
                if (commentDialog.isOne) {
                    commentDialog.tvCom1DC.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                } else {
                    commentDialog.tvCom1DC.setTextColor(Color.rgb(0x68, 0x68, 0x68));
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                commentDialog.isTwo = !commentDialog.isTwo;
                if (commentDialog.isTwo) {
                    commentDialog.tvCom2DC.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                } else {
                    commentDialog.tvCom2DC.setTextColor(Color.rgb(0x68, 0x68, 0x68));
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                commentDialog.isThree = !commentDialog.isThree;
                if (commentDialog.isThree) {
                    commentDialog.tvCom3DC.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                } else {
                    commentDialog.tvCom3DC.setTextColor(Color.rgb(0x68, 0x68, 0x68));
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                commentDialog.isFour = !commentDialog.isFour;
                if (commentDialog.isFour) {
                    commentDialog.tvCom4DC.setTextColor(Color.rgb(0xf9, 0x3d, 0x5a));
                } else {
                    commentDialog.tvCom4DC.setTextColor(Color.rgb(0x68, 0x68, 0x68));
                }
            }
        });
        commentDialog.ratingBarDC.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            }
        });
        commentDialog.setCancelable(false);
        commentDialog.show();
    }

    /**
     * 评价
     */
    private void grandOrder(String content, float score) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", runningOrderInfo.getOrderId());
        map.put("content", content);
        map.put("score", score);
        ApiClient.requestNetHandle(mContext, AppConfig.requestGrand, "正在评价...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    iosdialog.animation.SlideExit.dialogsamples.utils.L.d("TAG", "评价:" + json);
                }
                ToastUtils.showTextToast(mContext, "评论成功!");
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, "评论成功!");
            }
        });
    }

    private PriceInfo priceInfo;

    /**
     * 计算价格
     */
    private void calaMount() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        Map<String, Object> map = new HashMap<>();
        map.put("setOutTime", str);
        if (tag == 1) {
            map.put("type", 3);//出租车
        } else if (tag == 2) {
            map.put("type", 4);//快车
        } else if (tag == 4) {
            map.put("type", 2);//代驾
        }
        map.put("cityCode", cityCode);
        map.put("distance", distance);
        ApiClient.requestNetHandle(mContext, AppConfig.requestCalaMount, "计算价格中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "计算价格：" + json);
                    List<PriceInfo> priceInfos = JSON.parseArray(json, PriceInfo.class);
                    if (priceInfos != null && priceInfos.size() > 0) {
                        priceInfo = priceInfos.get(0);
                        amount = priceInfo.getTotalAmount();
                        if (tag == 1) {
                            main1BookingView.setPrice("约" + priceInfo.getTotalAmount() + "元");
                        } else if (tag == 2) {
                            DecimalFormat df = new java.text.DecimalFormat("#.0");
                            String format = df.format(priceInfo.getTotalAmount() * 0.8);
                            main2BookingView.setPrice("约" + format + "元", "约" + priceInfo.getTotalAmount() + "元");
                        } else if (tag == 4) {
                            main4BookingView.setPrice("约" + priceInfo.getTotalAmount() + "元");
                        }
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * 下单
     */
    private void createOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("cityCode", cityCode);
        if (tag == 1) {
            map.put("type", 3);//出租车
            map.put("orderType", 42);//出租车
            map.put("pdFlag", 0);
            map.put("amount", amount);
        } else if (tag == 2) {
            map.put("type", 4);//快车
            map.put("orderType", 44);//快车
            map.put("pdFlag", pdFlag);
            if (pdFlag) {
                map.put("amount", amount * 0.8);
            } else {
                map.put("amount", amount);
            }
        } else if (tag == 4) {
            map.put("type", 2);//代驾
            map.put("orderType", 40);//代驾
            map.put("pdFlag", 0);
        }
        map.put("distance", distance);
        map.put("time", time);
        map.put("startLongitude", startLng);
        map.put("startLatitude", startLat);
        map.put("endLongitude", endLng);
        map.put("endLatitude", endLat);
        map.put("reservationAddress", startPlace);
        map.put("destination", endPlace);
        ApiClient.requestNetHandle(mContext, AppConfig.requestCreateOrder, "下单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                int flag = 0;
                if (json != null && !StringUtils.isEmpty(json)) {
                    if (tag == 1) {
                        flag = 1;
                    } else if (tag == 2) {
                        flag = 2;
                    } else if (tag == 4) {
                        flag = 4;
                    }
                    startActivity(new Intent(mContext, BookTaxiActivity.class).putExtra("flag", flag).putExtra("orderId", Integer.parseInt(json)));
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("exit")) {
            finish();
        } else if (event.getSendCode().equals("unLogInUser")) {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
            ToastUtils.showTextToast(mContext, "登录已过期,请重新登录!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (AppContext.getInstance().checkCallBackUser() && AppContext.getInstance().checkUser()) {
            UserInfo userInfo = AppContext.getInstance().getUserInfo();
            if (userInfo.getNick_name() != null && !StringUtils.isEmpty(userInfo.getNick_name())) {
                tvNameM.setText(userInfo.getNick_name());
            }
            tvPhoneM.setText(userInfo.getPhone());
            if (userInfo.getHead_portrait() != null && !StringUtils.isEmpty(userInfo.getHead_portrait())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + userInfo.getHead_portrait(), civHeadM);
            }
        }
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
        EventBus.getDefault().unregister(this);
        mMapView.onDestroy();
    }

    private void initService() {
        URI uri = null;
        try {
            uri = new URI(AppConfig.mainWebSocker + "?" + AppContext.getInstance().getLoginCallback().getSecret() +
                    AppContext.getInstance().getLoginCallback().getToken() + AppContext.getInstance().getLoginCallback().getAppType());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            L.d("TAG", "initService1：" + e.toString());
        }
        WebSocketWorker webSocketWorker = new WebSocketWorker(uri, new Draft_17());
        try {
            webSocketWorker.connectBlocking();//此处如果用webSocketWorker.connect();会出错，需要多注意
            try {
                String json = "{content:\"" + "客户端" +
                        "\",type:\"" + 1 + "\"}";
                webSocketWorker.send(json);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            com.nostra13.universalimageloader.utils.L.d("TAG", "initService2：" + e.toString());
        }
    }

    private void initPoints(int count) {
        main5BusView.ll_dotGroup.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    15, 15);
            params.setMargins(0, 0, 20, 0);
            iv.setLayoutParams(params);

            iv.setImageResource(R.drawable.shape_item_index_white);
            main5BusView.ll_dotGroup.addView(iv);
        }
        curIndex = 0;
        ((ImageView) main5BusView.ll_dotGroup.getChildAt(curIndex))
                .setImageResource(R.drawable.shape_item_index_red);
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ImageView imageView1 = (ImageView) main5BusView.ll_dotGroup.getChildAt(position);
            ImageView imageView2 = (ImageView) main5BusView.ll_dotGroup.getChildAt(curIndex);
            if (imageView1 != null) {
                imageView1.setImageResource(R.drawable.shape_item_index_red);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(R.drawable.shape_item_index_white);
            }
            curIndex = position;
            mBusrouteOverlay = new BusRouteOverlay(mContext, aMap, busRouteResult.getPaths().get(curIndex),
                    busRouteResult.getStartPos(), busRouteResult.getTargetPos());
            initBusMap();
        }

        boolean b = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                    b = false;
                    break;
                case 2:
                    b = true;
                    break;
                case 0:
                    if (main5BusView.viewPager.getCurrentItem() == main5BusView.viewPager.getAdapter()
                            .getCount() - 1 && !b) {
                        main5BusView.viewPager.setCurrentItem(0);
                    } else if (main5BusView.viewPager.getCurrentItem() == 0 && !b) {
                        main5BusView.viewPager.setCurrentItem(main5BusView.viewPager.getAdapter()
                                .getCount() - 1);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private static final int PERMISSON_REQUESTCODE = 0;
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    /**
     *
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n请点击\"设置\"---\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
