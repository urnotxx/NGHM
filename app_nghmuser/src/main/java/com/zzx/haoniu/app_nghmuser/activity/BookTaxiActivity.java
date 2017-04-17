package com.zzx.haoniu.app_nghmuser.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.app.AppContext;
import com.zzx.haoniu.app_nghmuser.dialog.CommentDialog;
import com.zzx.haoniu.app_nghmuser.dialog.PromptDialog;
import com.zzx.haoniu.app_nghmuser.entity.CarInfo;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.entity.DriverLocInfo;
import com.zzx.haoniu.app_nghmuser.entity.OrderInfo;
import com.zzx.haoniu.app_nghmuser.entity.PayInfo;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.overlay.DrivingRouteOverlay;
import com.zzx.haoniu.app_nghmuser.utils.AMapUtil;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.TTSController_1;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ali.PayResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import iosdialog.animation.SlideExit.dialogsamples.utils.L;
import self.androidbase.views.SelfLinearLayout;

public class BookTaxiActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.civDriverHeadB)
    CircleImageView civDriverHeadB;
    @Bind(R.id.tvDriverNameB)
    TextView tvDriverNameB;
    @Bind(R.id.tvFrequencyB)
    TextView tvFrequencyB;
    @Bind(R.id.tvPlateB)
    TextView tvPlateB;
    @Bind(R.id.tvBelongB)
    TextView tvBelongB;
    @Bind(R.id.ratingBarB)
    RatingBar ratingBarB;
    @Bind(R.id.tvScoreB)
    TextView tvScoreB;
    @Bind(R.id.ivCallB)
    ImageView ivCallB;
    @Bind(R.id.llDriverB)
    LinearLayout llDriverB;
    @Bind(R.id.activity_book_taxi)
    LinearLayout activityBookTaxi;
    @Bind(R.id.tvPriceB)
    TextView tvPriceB;
    @Bind(R.id.tvMingxiB)
    TextView tvMingxiB;
    @Bind(R.id.tvPayB)
    TextView tvPayB;
    @Bind(R.id.llPayB)
    LinearLayout llPayB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_taxi);
        mMapView = (MapView) findViewById(R.id.mapBT);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
        setMargins(activityBookTaxi);
        if (getIntent().getExtras() != null) {
            flag = getIntent().getIntExtra("flag", 0);
            orderId = getIntent().getIntExtra("orderId", 0);
            int status = getIntent().getIntExtra("status", 0);
            if (status != 0) {
                orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
            }
        }
        initView();
        initMap();
        initLocation();
        if (orderInfo != null) {
            initOrderInfo();
        }
        EventBus.getDefault().register(this);
    }

    private TTSController_1 ttsController_1;
    private MapView mMapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean isFirstLoc = true;
    private int flag = 0;//判断  车子类型  1 出租车   2 快车   4 代驾
    private int orderId;

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("jieOrderInfo")) {//司机接到订单
            ttsController_1 = TTSController_1.getInstance(mContext);
            ttsController_1.startSpeck("您的订单已被接单，请您耐心等候！");
            orderInfo = (OrderInfo) event.getMap().get("orderInfo");
            getDriverCar();
            tvTitle.setText("等待接驾");
            llRight.setVisibility(View.GONE);
            llDriverB.setVisibility(View.VISIBLE);
            tvDriverNameB.setText(orderInfo.getNickName());
            if (orderInfo.getCar().getPlateNo() != null && !StringUtils.isEmpty(orderInfo.getCar().getPlateNo()))
                tvPlateB.setText(orderInfo.getCar().getPlateNo());
            tvSubmit.setText("");
            llRight.setClickable(false);
            if (orderInfo.getCar().getPicture() != null && !StringUtils.isEmpty(orderInfo.getCar().getPicture())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo.getCar().getPicture(), civDriverHeadB);
            }
            tag = 1;
        } else if (event.getSendCode().equals("picked")) {//司机接到客人
            tag = 2;
            tvTitle.setText("行程中");
            orderInfo.setStatus(3);
            setfromandtoMarker();
            showProgressDialog();
            searchRouteResult(new LatLonPoint(locLat, locLng), new LatLonPoint(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude()));
        } else if (event.getSendCode().equals("payInfo")) {//司机到达终点
            payInfo = (PayInfo) event.getMap().get("payInfo");
            tag = 3;
            tvTitle.setText("行程结束");
            llRight.setVisibility(View.GONE);
            llRight.setClickable(false);
            tvSubmit.setVisibility(View.GONE);
            llPayB.setVisibility(View.VISIBLE);
            tvPriceB.setText(payInfo.getRealPay() + "元");
        } else if (event.getSendCode().equals("cancleOrderInfo")) {
            OrderInfo info = (OrderInfo) event.getMap().get("orderInfo");
            if (info != null && info.getTrip().getOrderId() == orderInfo.getTrip().getOrderId()) {
                ToastUtils.showTextToast(mContext, "订单已被司机取消，请重新下单!");
                finish();
            }
        } else if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    private OrderInfo orderInfo;
    private double locLat, locLng;
    private PayInfo payInfo;

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setMyLocationButtonEnabled(true); // 显示默认的定位按钮
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
        }
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
        ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        myLocationStyle.myLocationIcon(giflist)
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

    @Override
    public int bindLayout() {
        return R.layout.activity_book_taxi;
    }

    private PromptDialog promptDialog;
    private CommentDialog commentDialog;
    private int tag = 0;//0  等待司机接单   1 司机接到订单  2司机接到人   开始行程  3 行程结束   去支付   4 支付成功  评价

    @OnClick({R.id.ll_right, R.id.ll_back, R.id.ivCallB, R.id.tvMingxiB, R.id.tvPayB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_right:
                if (tag != 3) {
//                    if (tag == 0) {
//                        if (promptDialog == null) {
//                            promptDialog = new PromptDialog(mContext);
//                        }
//                        showPromptDialog();
//                    } else {
//                        ToastUtils.showTextToast(mContext, "当前订单正在进行!");
//                    }
                    if (promptDialog == null) {
                        promptDialog = new PromptDialog(mContext);
                    }
                    showPromptDialog();
                } else {
                    ToastUtils.showTextToast(mContext, "请您支付当前订单!");
                }
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ivCallB:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
                break;
//            case R.id.tv2:
//                tag = 2;
//                tvTitle.setText("行程中");
//                tvSubmit.setText("");
//                llRight.setClickable(false);
//                break;
//            case R.id.tv3:
//                tag = 3;
//                tvTitle.setText("行程结束");
//                llBack.setVisibility(View.VISIBLE);
//                llPayB.setVisibility(View.VISIBLE);
//                break;
//            case R.id.tv4:
//                tag = 4;
//                break;
            case R.id.tvMingxiB:
                startActivity(new Intent(mContext, MingXiTaxiActivity.class)
                        .putExtra("totalFee", payInfo.getRealPay())
                        .putExtra("ygFee", payInfo.getAmount())
                        .putExtra("otherFee", (float) (payInfo.getRealPay() - payInfo.getAmount())));
                break;
            case R.id.tvPayB:
                getPayOrderInfo();
                break;
        }
    }

    private void showPromptDialog() {
        if (orderInfo == null) {
            promptDialog.tvContent.setText("正在为您叫车中，是否确认取消当前订单。");
        } else if (orderInfo != null) {
//            promptDialog.tvContent.setText("您的订单已经形成，当前如果取消订单，会收取您5元违约金，是否确定取消。");
            promptDialog.tvContent.setText("司机正在路上，是否确定取消。");
        }
        promptDialog.tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                cancleOrder();
            }
        });
        promptDialog.tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        });
        promptDialog.setCancelable(false);
        promptDialog.show();
    }

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
                finish();
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

    @Override
    public void initParms(Bundle parms) {

    }

    private void initOrderInfo() {
        if (orderInfo.getStatus() == 1) {
            tvTitle.setText("正在呼叫");
            tvSubmit.setText("取消订单");
            llBack.setVisibility(View.INVISIBLE);
        } else if (orderInfo.getStatus() == 2) {
            getDriverCar();
            tvTitle.setText("等待接驾");
            llRight.setVisibility(View.GONE);
            llDriverB.setVisibility(View.VISIBLE);
            tvDriverNameB.setText(orderInfo.getNickName());
            if (orderInfo != null && orderInfo.getCar() != null && orderInfo.getCar().getPlateNo() != null)
                tvPlateB.setText(orderInfo.getCar().getPlateNo());
            tvSubmit.setText("");
            llRight.setClickable(false);
            if (orderInfo.getCar() != null && orderInfo.getCar().getPicture() != null && !StringUtils.isEmpty(orderInfo.getCar().getPicture())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo.getCar().getPicture(), civDriverHeadB);
            }
            tag = 1;
        } else if (orderInfo.getStatus() == 3) {
            llDriverB.setVisibility(View.VISIBLE);
            tvDriverNameB.setText(orderInfo.getNickName());
            if (orderInfo.getCar() != null && orderInfo.getCar().getPlateNo() != null)
                tvPlateB.setText(orderInfo.getCar().getPlateNo());
            tvSubmit.setText("");
            llRight.setClickable(false);
            if (orderInfo.getCar().getPicture() != null && !StringUtils.isEmpty(orderInfo.getCar().getPicture())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo.getCar().getPicture(), civDriverHeadB);
            }
            tag = 2;
            tvTitle.setText("行程中");
            setfromandtoMarker();
            showProgressDialog();
            searchRouteResult(new LatLonPoint(locLat, locLng), new LatLonPoint(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude()));
        }
    }

    @Override
    public void initView() {
        tvTitle.setText("正在呼叫");
        tvSubmit.setText("取消订单");
        llBack.setVisibility(View.INVISIBLE);
    }

    @Override
    public void doBusiness(Context mContext) {

    }


    @Override
    public void onBackPressed() {
        if (tag == 4) {
            finish();
        } else if (tag == 3) {//待支付
            ToastUtils.showTextToast(mContext, "当前订单还未支付!");
        } else if (tag == 2) {
            ToastUtils.showTextToast(mContext, "当前订单还未结束!");
        } else {
            if (promptDialog == null)
                promptDialog = new PromptDialog(mContext);
            if (promptDialog.isShowing()) {
                promptDialog.dismiss();
            } else {
                showPromptDialog();
            }
            return;
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                locLat = aMapLocation.getLatitude();
                locLng = aMapLocation.getLongitude();
                mListener.onLocationChanged(aMapLocation);
                if (isFirstLoc) {
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    isFirstLoc = false;
                    if (tag == 2) {//司机接到客人  开始路径规划
                        searchRouteResult(new LatLonPoint(locLat, locLng), new LatLonPoint(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude()));
                    }
                }
                if (tag == 0) {
                    if (flag == 1) {
                        getCars(3, 42);//默认查询出租车
                    } else if (tag == 2) {
                        getCars(4, 44);//快车
                    } else if (tag == 3) {
                        getCars(2, 40);//代驾
                    }
                } else if (tag == 1) {
                    getDriverCar();
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
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 获取车辆信息
     */
    private void getCars(final int type, int serviceType) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("serviceType", serviceType);
        map.put("longitude", locLng);
        map.put("latitude", locLat);
        ApiClient.requestNetHandle(mContext, AppConfig.requestCars, "", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "获取车辆信息:" + json);
                    List<CarInfo> carInfos1 = JSON.parseArray(json, CarInfo.class);
                    if (carInfos1 != null && carInfos1.size() > 0) {
                        addCars(type, carInfos1);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private Marker marker;

    private void addCars(int type, List<CarInfo> carInfos) {
        aMap.clear(true);
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
            aMap.addMarker(markOption);
        }
    }

    /**
     * 获取接单司机的车辆信息
     */
    private void getDriverCar() {
        Map<String, Object> map = new HashMap<>();
        map.put("driverId", orderInfo.getDriverId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestDriverLoc, "", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "司机的实时位置:" + json);
                    DriverLocInfo locInfos = JSON.parseObject(json, DriverLocInfo.class);
                    if (locInfos != null) {
                        addDriverLoc(locInfos);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void addDriverLoc(DriverLocInfo locInfo) {
        if (marker != null) {
            marker.remove();
        }
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();//经纬度坐标矩形区域的生成器。
        LatLng latLng = new LatLng(locInfo.getLatitude(), locInfo.getLongitude());
        bounds.include(latLng);
        MarkerOptions markOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .rotateAngle(Float.parseFloat(locInfo.getOrientation() + ""))
                .position(latLng).draggable(false);
        if (flag == 1) {
            markOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_taxi));
        } else if (flag == 2) {
            markOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_kuaiche));
        }
        marker = aMap.addMarker(markOption);
    }

    /**
     * 取消订单
     */
    private void cancleOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        ApiClient.requestNetHandle(mContext, AppConfig.requestCancleOrder, "取消订单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "取消订单:" + json);
                }
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * 获取支付信息
     */
    private void getPayOrderInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
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
                PayTask alipay = new PayTask(BookTaxiActivity.this); // 构造PayTask 对象
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
                    Toast.makeText(mContext, "支付成功!", Toast.LENGTH_LONG).show();
                    tag = 4;
                    llBack.setVisibility(View.VISIBLE);

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

    /**
     * 评价
     */
    private void grandOrder(String content, float score) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("content", content);
        map.put("score", score);
        ApiClient.requestNetHandle(mContext, AppConfig.requestGrand, "正在评价...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "评价:" + json);
                }
                ToastUtils.showTextToast(mContext, "评论成功!");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, "评论成功!");
            }
        });
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(new LatLonPoint(locLat, locLng)))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(new LatLonPoint(orderInfo.getTrip().getEndLatitude(), orderInfo.getTrip().getEndLongitude())))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end)));
    }

    private RouteSearch routeSearch;

    /**
     * 开始搜索路径规划
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
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
                    aMap.clear();// 清理地图上的所有覆盖物
                    if (driveRouteResult != null && driveRouteResult.getPaths() != null
                            && driveRouteResult.getPaths().size() > 0) {
                        DriveRouteResult mDriveRouteResult = driveRouteResult;
                        DrivePath drivePath = driveRouteResult.getPaths().get(0);
                        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                mContext, aMap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                        drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                        drivingRouteOverlay.setThroughPointIconVisibility(true);
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.addToMap();
                        drivingRouteOverlay.zoomToSpan();
                    } else {
                        Toast.makeText(mContext, "对不起，没查询到结果", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else if (rCode == 27) {
                    L.d("TAG", "net error");
                } else {
                    L.d("TAG", "other error");
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
