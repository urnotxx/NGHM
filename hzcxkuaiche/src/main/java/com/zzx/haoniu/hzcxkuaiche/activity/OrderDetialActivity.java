package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.app.AppContext;
import com.zzx.haoniu.hzcxkuaiche.entity.CommonEventBusEnity;
import com.zzx.haoniu.hzcxkuaiche.entity.OrderInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import self.androidbase.utils.SelfMapUtils;
import self.androidbase.views.SelfLinearLayout;

public class OrderDetialActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.civHeadOD1)
    CircleImageView civHeadOD1;
    @Bind(R.id.tvStartOD1)
    TextView tvStartOD1;
    @Bind(R.id.tvEndOD1)
    TextView tvEndOD1;
    @Bind(R.id.ivCallOD1)
    ImageView ivCallOD1;
    @Bind(R.id.tvPriceOD1)
    TextView tvPriceOD1;
    @Bind(R.id.edGaosuFeeOD1)
    EditText edGaosuFeeOD1;
    @Bind(R.id.civHeadOD2)
    CircleImageView civHeadOD2;
    @Bind(R.id.tvStartOD2)
    TextView tvStartOD2;
    @Bind(R.id.tvEndOD2)
    TextView tvEndOD2;
    @Bind(R.id.ivCallOD2)
    ImageView ivCallOD2;
    @Bind(R.id.tvPriceOD2)
    TextView tvPriceOD2;
    @Bind(R.id.edGaosuFeeOD2)
    EditText edGaosuFeeOD2;
    @Bind(R.id.llBottomOD)
    LinearLayout llBottomOD;
    @Bind(R.id.llTopOD)
    LinearLayout llTopOD;
    @Bind(R.id.tvButtonOD1)
    TextView tvButtonOD1;
    @Bind(R.id.tvCancleOD1)
    TextView tvCancleOD1;
    @Bind(R.id.tvButtonOD2)
    TextView tvButtonOD2;
    @Bind(R.id.tvCancleOD2)
    TextView tvCancleOD2;
    @Bind(R.id.llNavOD2)
    LinearLayout llNavOD2;
    @Bind(R.id.activity_order_detial)
    LinearLayout activityOrderDetial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detial);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误，请重新接收!");
            finish();
        }
        steepStatusBar();
        setMargins(activityOrderDetial);
        initView();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("secOrderInfo")) {
            orderInfo2 = (OrderInfo) event.getMap().get("secOrderInfo");
            initView();
        } else if (event.getSendCode().equals("userCancleOrderInfo")) {
            if (orderInfo1 != null && orderInfo2 == null) {
                ToastUtils.showTextToast(mContext, "用户已取消订单!");
                finish();
            } else if (orderInfo1 == null && orderInfo2 != null) {
                ToastUtils.showTextToast(mContext, "用户已取消订单!");
                finish();
            } else if (orderInfo1 != null && orderInfo2 != null) {
                if (orderInfo1.getId() == Integer.parseInt(event.getMessage())) {
                    orderInfo1 = null;
                    ToastUtils.showTextToast(mContext, "用户取消订单!");
                    llTopOD.setVisibility(View.GONE);
                } else if (orderInfo2.getId() == Integer.parseInt(event.getMessage())) {
                    orderInfo2 = null;
                    ToastUtils.showTextToast(mContext, "用户取消订单!");
                    llBottomOD.setVisibility(View.GONE);
                }
            }
        } else if (event.getSendCode().equals("unLogInKC")) {
            finish();
        }
    }

    private List<OrderInfo> orderInfos;

    private OrderInfo orderInfo1, orderInfo2;
    private boolean isTake1, isTake2;
    private boolean isSend1, isSend2;

    @Override
    public void initView() {
        tvTitle.setText("订单详情");
        if (orderInfo1 != null) {
            if (orderInfo1.getReservationAddress() != null && !StringUtils.isEmpty(orderInfo1.getReservationAddress())) {
                tvStartOD1.setText(orderInfo1.getReservationAddress());
            }
            if (orderInfo1.getDestination() != null && !StringUtils.isEmpty(orderInfo1.getDestination())) {
                tvEndOD1.setText(orderInfo1.getDestination());
            }
            if (orderInfo1.getHead_portrait() != null && !StringUtils.isEmpty(orderInfo1.getHead_portrait())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo1.getHead_portrait(), civHeadOD1);
            }
            if (orderInfo1.getStatus() == 2) {
                if (isTake1) {
                    tvButtonOD1.setText("接到乘客");
                } else {
                    tvButtonOD1.setText("接TA");
                }
                tvCancleOD1.setClickable(false);
                tvCancleOD1.setBackgroundResource(R.drawable.shap_gray_30);
                tvCancleOD1.setTextColor(getResources().getColor(R.color.colorRed));
            } else if (orderInfo1.getStatus() == 3) {
                if (isSend1) {
                    tvButtonOD1.setText("到达目的地");
                } else {
                    tvButtonOD1.setText("送TA");
                }
                tvCancleOD1.setClickable(false);
                tvCancleOD1.setBackgroundResource(R.drawable.shap_gray_30);
                tvCancleOD1.setTextColor(getResources().getColor(R.color.colorRed));
            }
//            else if (orderInfo1.getStatus() == 7) {
//                tvCancleOD1.setClickable(false);
//                tvCancleOD1.setBackgroundResource(R.drawable.shap_gray_30);
//                tvCancleOD1.setTextColor(getResources().getColor(R.color.colorRed));
//                tvButtonOD1.setText("送TA");
//            } else if (orderInfo1.getStatus() == 8) {
//                tvCancleOD1.setClickable(false);
//                tvCancleOD1.setBackgroundResource(R.drawable.shap_gray_30);
//                tvCancleOD1.setTextColor(getResources().getColor(R.color.colorRed));
//                tvButtonOD1.setText("到达目的地");
//            }
            edGaosuFeeOD1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString() != null && !StringUtils.isEmpty(charSequence.toString())) {
                        tvPriceOD1.setText((price1 + Double.parseDouble(charSequence.toString())) + "");
                    } else {
                        tvPriceOD1.setText(price1 + "");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString() != null && !StringUtils.isEmpty(editable.toString())) {
                        gaoSuFee1 = Double.parseDouble(editable.toString());
                    }
                }
            });
        }
        if (orderInfo2 != null) {
            llBottomOD.setVisibility(View.VISIBLE);
            if (orderInfo2.getReservationAddress() != null && !StringUtils.isEmpty(orderInfo2.getReservationAddress())) {
                tvStartOD2.setText(orderInfo2.getReservationAddress());
            }
            if (orderInfo2.getDestination() != null && !StringUtils.isEmpty(orderInfo2.getDestination())) {
                tvEndOD2.setText(orderInfo2.getDestination());
            }
            if (orderInfo2.getHead_portrait() != null && !StringUtils.isEmpty(orderInfo2.getHead_portrait())) {
                ImageLoader.getInstance().displayImage(AppConfig.mainPicUrl + orderInfo2.getHead_portrait(), civHeadOD2);
            }
            if (orderInfo2.getStatus() == 2) {
                if (isTake2) {
                    tvButtonOD2.setText("接到乘客");
                } else {
                    tvButtonOD2.setText("接TA");
                }
                tvCancleOD2.setClickable(false);
                tvCancleOD2.setBackgroundResource(R.drawable.shap_gray_30);
                tvCancleOD2.setTextColor(getResources().getColor(R.color.colorRed));
            } else if (orderInfo2.getStatus() == 3) {
                if (isSend2) {
                    tvButtonOD2.setText("到达目的地");
                } else {
                    tvButtonOD2.setText("送TA");
                }
                tvCancleOD2.setClickable(false);
                tvCancleOD2.setBackgroundResource(R.drawable.shap_gray_30);
                tvCancleOD2.setTextColor(getResources().getColor(R.color.colorRed));
            }
//            if (orderInfo2.getStatus() == 5) {
//                tvButtonOD2.setText("接TA");
//            } else if (orderInfo2.getStatus() == 6) {
//                tvButtonOD2.setText("接到乘客");
//            } else if (orderInfo2.getStatus() == 7) {
//                tvCancleOD2.setClickable(false);
//                tvCancleOD2.setBackgroundResource(R.drawable.shap_gray_30);
//                tvCancleOD2.setTextColor(getResources().getColor(R.color.colorRed));
//                tvButtonOD2.setText("送TA");
//            } else if (orderInfo2.getStatus() == 8) {
//                tvCancleOD2.setClickable(false);
//                tvCancleOD2.setBackgroundResource(R.drawable.shap_gray_30);
//                tvCancleOD2.setTextColor(getResources().getColor(R.color.colorRed));
//                tvButtonOD2.setText("到达目的地");
//            }
            edGaosuFeeOD2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString() != null && !StringUtils.isEmpty(charSequence.toString())) {
                        tvPriceOD2.setText((price2 + Double.parseDouble(charSequence.toString())) + "");
                    } else {
                        tvPriceOD2.setText(price2 + "");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString() != null && !StringUtils.isEmpty(editable.toString())) {
                        gaoSuFee2 = Double.parseDouble(editable.toString());
                    }
                }
            });
        }
    }

    private double price1;
    private double price2;
    private double gaoSuFee1, gaoSuFee2;

    @Override
    public void initParms(Bundle parms) {
        orderInfos = (List<OrderInfo>) parms.get("orderInfos");
        isTake1 = parms.getBoolean("isTake1");
        isTake2 = parms.getBoolean("isTake2");
        isSend1 = parms.getBoolean("isSend1");
        isSend2 = parms.getBoolean("isSend2");
        if (orderInfos == null) {
            ToastUtils.showTextToast(mContext, "数据接收有误，请重新接收!");
            finish();
        } else {
            if (orderInfos.size() == 1) {
                orderInfo1 = orderInfos.get(0);
                price1 = orderInfo1.getYgAmount();
                tvPriceOD1.setText(price1 + "");
            } else if (orderInfos.size() == 2) {
                llBottomOD.setVisibility(View.VISIBLE);
                orderInfo1 = orderInfos.get(0);
                orderInfo2 = orderInfos.get(1);
                price1 = orderInfo1.getYgAmount();
                price2 = orderInfo2.getYgAmount();
                tvPriceOD1.setText(price1 + "");
                tvPriceOD2.setText(price2 + "");
            }
        }
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ivCallOD1, R.id.ivCallOD2, R.id.tvButtonOD1,
            R.id.tvCancleOD1, R.id.tvButtonOD2, R.id.tvCancleOD2, R.id.llNavOD1, R.id.llNavOD2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ivCallOD1:
                AppContext.getInstance().callUser(mContext, orderInfo1.getPhone());
                break;
            case R.id.tvButtonOD1:
                if (orderInfo1.getStatus() == 2) {
                    if (!isTake1) {
                        CommonEventBusEnity enity1 = new CommonEventBusEnity("pathPlan", "1");
                        EventBus.getDefault().post(enity1);
                        finish();
                    } else {
                        puckUp(1);
                    }
                } else if (orderInfo1.getStatus() == 3) {
                    if (isSend1) {
                        arrived(1);
                    } else {
                        CommonEventBusEnity enity2 = new CommonEventBusEnity("pathPlan", "2");
                        EventBus.getDefault().post(enity2);
                        finish();
                    }
                }
                break;
            case R.id.tvCancleOD1:
                cancleOrder(1);
                break;
            case R.id.tvButtonOD2:
//                if (orderInfo2.getStatus() == 5) {
//                    CommonEventBusEnity enity1 = new CommonEventBusEnity("pathPlan", "3");
//                    EventBus.getDefault().post(enity1);
//                    finish();
//                } else if (orderInfo2.getStatus() == 6) {
//                    puckUp(2);
//                } else if (orderInfo2.getStatus() == 7) {
//                    CommonEventBusEnity enity2 = new CommonEventBusEnity("pathPlan", "4");
//                    EventBus.getDefault().post(enity2);
//                    finish();
//                } else if (orderInfo2.getStatus() == 8) {
//                    arrived(2);
//                }
                if (orderInfo2.getStatus() == 2) {
                    if (!isTake2) {
                        CommonEventBusEnity enity1 = new CommonEventBusEnity("pathPlan", "3");
                        EventBus.getDefault().post(enity1);
                        finish();
                    } else {
                        puckUp(2);
                    }
                } else if (orderInfo2.getStatus() == 3) {
                    if (isSend2) {
                        arrived(2);
                    } else {
                        CommonEventBusEnity enity2 = new CommonEventBusEnity("pathPlan", "4");
                        EventBus.getDefault().post(enity2);
                        finish();
                    }
                }
                break;
            case R.id.tvCancleOD2:
                cancleOrder(2);
            case R.id.ivCallOD2:
                AppContext.getInstance().callUser(mContext, orderInfo2.getPhone());
                break;
            case R.id.llNavOD1:
                // 5  6  导航  接乘客  7  8 送乘客
                initLocation(1);
                break;
            case R.id.llNavOD2:
                // 5  6  导航  接乘客  7  8 送乘客
                initLocation(2);
                break;
        }
    }

    private void initLocation(final int tag) {
        SelfMapUtils.getInstance(mContext).startLocation(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AMapLocation aMapLocation = (AMapLocation) msg.obj;
                if (aMapLocation != null) {
                    double lat = aMapLocation.getLatitude();
                    double log = aMapLocation.getLongitude();
                    LatLng point_start = new LatLng(lat, log);
                    if (tag == 1) {
                        if (orderInfo1.getStatus() == 5 || orderInfo1.getStatus() == 6) {
                            startActivity(new Intent(mContext, NavActivity.class).putExtra("point_start", point_start)
                                    .putExtra("endPlace", orderInfo1.getReservationAddress())
                                    .putExtra("point_end", new LatLng(orderInfo1.getTrip().getStartLatitude(), orderInfo1.getTrip().getStartLongitude())));
                        } else {
                            startActivity(new Intent(mContext, NavActivity.class).putExtra("point_start", point_start)
                                    .putExtra("endPlace", orderInfo1.getDestination())
                                    .putExtra("point_end", new LatLng(orderInfo1.getTrip().getEndLatitude(), orderInfo1.getTrip().getEndLongitude())));
                        }
                    } else {
                        if (orderInfo2.getStatus() == 5 || orderInfo2.getStatus() == 6) {
                            startActivity(new Intent(mContext, NavActivity.class).putExtra("point_start", point_start)
                                    .putExtra("endPlace", orderInfo2.getReservationAddress())
                                    .putExtra("point_end", new LatLng(orderInfo2.getTrip().getStartLatitude(), orderInfo2.getTrip().getStartLongitude())));
                        } else {
                            startActivity(new Intent(mContext, NavActivity.class).putExtra("point_start", point_start)
                                    .putExtra("endPlace", orderInfo2.getDestination())
                                    .putExtra("point_end", new LatLng(orderInfo2.getTrip().getEndLatitude(), orderInfo2.getTrip().getEndLongitude())));
                        }
                    }
                } else {
                    ToastUtils.showTextToast(mContext, "当前位置为空!");
                }
            }
        });
    }

    /**
     * 取消订单
     */
    private void cancleOrder(final int tag) {
        Map<String, Object> map = new HashMap<>();
        if (tag == 1) {
            map.put("orderId", orderInfo1.getId());
        } else if (tag == 2) {
            map.put("orderId", orderInfo2.getId());
        }
        ApiClient.requestNetHandle(mContext, AppConfig.requestCancleOrder, "取消订单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "取消订单:" + json);
                }
                CommonEventBusEnity enity = null;
                if (tag == 1) {
                    if (orderInfo1 != null && orderInfo2 != null) {
                        orderInfo1 = null;
                        llTopOD.setVisibility(View.GONE);
                    } else {
                        finish();
                    }
                    enity = new CommonEventBusEnity("cancleOrder", orderInfo1.getId() + "");
                } else if (tag == 2) {
                    if (orderInfo1 != null && orderInfo2 != null) {
                        orderInfo2 = null;
                        llBottomOD.setVisibility(View.GONE);
                    } else {
                        finish();
                    }
                    enity = new CommonEventBusEnity("cancleOrder", orderInfo2.getId() + "");
                }
                EventBus.getDefault().post(enity);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * 客人到达目的地
     */
    private void arrived(final int tag) {
        Map<String, Object> map = new HashMap<>();
        if (tag == 1) {
            map.put("id", orderInfo1.getId());
            map.put("roadToll", gaoSuFee1);
        } else if (tag == 2) {
            map.put("id", orderInfo2.getId());
            map.put("roadToll", gaoSuFee2);
        }
        map.put("distance", 0);
        map.put("waitMinutes", 0);
        map.put("remoteFee", 0);
        map.put("otherCharges", 0);
        map.put("payType", 1);//支付类型(1:支付宝;2:代收;3:余额)
        ApiClient.requestNetHandle(mContext, AppConfig.requestArrived, "订单结算...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                }
                if (tag == 1) {
                    if (orderInfo1 != null && orderInfo2 != null) {
                        orderInfo1 = null;
                        llTopOD.setVisibility(View.GONE);
                    } else {
                        ToastUtils.showTextToast(mContext, "订单已完成!");
                        finish();
                    }
                } else if (tag == 2) {
                    if (orderInfo1 != null && orderInfo2 != null) {
                        orderInfo2 = null;
                        llBottomOD.setVisibility(View.GONE);
                    } else {
                        ToastUtils.showTextToast(mContext, "订单已完成!");
                        finish();
                    }
                }
                CommonEventBusEnity enity = new CommonEventBusEnity("arrived", tag + "");
                EventBus.getDefault().post(enity);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    /**
     * 接到用户
     */
    private void puckUp(final int tag) {
        Map<String, Object> map = new HashMap<>();
        if (tag == 1) {
            map.put("id", orderInfo1.getId());
        } else if (tag == 2) {
            map.put("id", orderInfo2.getId());
        }
        ApiClient.requestNetHandle(mContext, AppConfig.requestOrderStart, "接到客人...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "接到客人:" + json);
                }
                ToastUtils.showTextToast(mContext, "接到客人");
                CommonEventBusEnity enity = null;
                if (tag == 1) {
                    tvCancleOD1.setClickable(false);
                    tvCancleOD1.setTextColor(getResources().getColor(R.color.colorRed));
                    tvCancleOD1.setBackgroundResource(R.drawable.shap_gray_30);
                    enity = new CommonEventBusEnity("pickUp", "" + orderInfo1.getId());
                    orderInfo1.setStatus(3);
                    tvButtonOD1.setText("送TA");
                } else {
                    tvCancleOD2.setClickable(false);
                    tvCancleOD2.setTextColor(getResources().getColor(R.color.colorRed));
                    tvCancleOD2.setBackgroundResource(R.drawable.shap_gray_30);
                    enity = new CommonEventBusEnity("pickUp", "" + orderInfo2.getId());
                    orderInfo2.setStatus(3);
                    tvButtonOD2.setText("送TA");
                }
                EventBus.getDefault().post(enity);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
