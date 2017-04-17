package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.zzx.haoniu.hzcxkuaiche.view.SlidingButton;
import com.zzx.haoniu.hzcxkuaiche.websocket.WebSocketWorker;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import self.androidbase.views.SelfLinearLayout;

public class ConfirmBillActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.civHeadCB)
    CircleImageView civHeadCB;
    @Bind(R.id.tvStartCB)
    TextView tvStartCB;
    @Bind(R.id.tvEndCB)
    TextView tvEndCB;
    @Bind(R.id.ivCallCB)
    ImageView ivCallCB;
    @Bind(R.id.tvPriceCB)
    TextView tvPriceCB;
    @Bind(R.id.tvMileageFeeCB)
    TextView tvMileageFeeCB;
    @Bind(R.id.tvWaitFeeCB)
    TextView tvWaitFeeCB;
    @Bind(R.id.edGaosuFeeCB)
    EditText edGaosuFeeCB;
    @Bind(R.id.slidingButtonCB)
    SlidingButton slidingButtonCB;
    @Bind(R.id.tvPromptCB)
    TextView tvPromptCB;
    @Bind(R.id.tvShoucheCB)
    TextView tvShoucheCB;
    @Bind(R.id.activity_confirm_bill)
    LinearLayout activityConfirmBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bill);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityConfirmBill);
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误!");
            finish();
        }
        initView();
        EventBus.getDefault().register(this);
    }

    private double price;
    private Bundle bundle;
    private OrderInfo orderInfo;
    private double gaoSuFee;
    private int flag = 0;

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("userCancleOrderInfo")) {
            String orderId = event.getMessage();
            if (Integer.parseInt(orderId) == orderInfo.getId()) {
                ToastUtils.showTextToast(mContext, "乘客取消当前订单!");
                finish();
            } else {
                ToastUtils.showTextToast(mContext, "您有一条订单被乘客取消!");
            }
        }else if (event.getSendCode().equals("unLogInKC")){
            finish();
        }
    }

    @Override
    public void initView() {
        tvStartCB.setText(orderInfo.getReservationAddress());
        tvEndCB.setText(orderInfo.getDestination());
        tvPriceCB.setText(orderInfo.getYgAmount() + "");
        tvTitle.setText("确认订单");
        tvSubmit.setText("取消订单");
        edGaosuFeeCB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && !StringUtils.isEmpty(charSequence.toString())) {
                    tvPriceCB.setText((price + Double.parseDouble(charSequence.toString())) + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null && !StringUtils.isEmpty(editable.toString())) {
                    gaoSuFee = Double.parseDouble(editable.toString());
                }
            }
        });
    }

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        flag = parms.getInt("flag");
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
        price = orderInfo.getYgAmount();
        if (orderInfo.getStatus() == 11) {//  10  未接到客户  11 接到客户  12 订单结束
            tvShoucheCB.setText("已接到");
            tvShoucheCB.setBackgroundResource(R.drawable.shap_gray_0);
            tvShoucheCB.setClickable(false);
        } else {
            edGaosuFeeCB.setFocusable(false);
        }
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ll_right, R.id.ivCallCB, R.id.tvShoucheCB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_right:
                cancleOrder();
                break;
            case R.id.ivCallCB:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
                break;
            case R.id.tvShoucheCB:
                puckUp();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slidingButtonCB.handleActivityEvent(event)) {
            arrived();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 取消订单
     */
    private void cancleOrder() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestCancleOrder, "取消订单中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "取消订单:" + json);
                }
                CommonEventBusEnity commonEventBusEnity = new CommonEventBusEnity("cancaleOrder", orderInfo.getId() + "");
                EventBus.getDefault().post(commonEventBusEnity);
                finish();
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
    private void arrived() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", orderInfo.getId());
        map.put("distance", 0);
        map.put("roadToll", gaoSuFee);
        map.put("waitMinutes", 0);
        map.put("remoteFee", 0);
        map.put("otherCharges", 0);
        map.put("payType", 1);//支付类型(1:支付宝;2:代收;3:余额)
        ApiClient.requestNetHandle(mContext, AppConfig.requestArrived, "订单结算...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    ToastUtils.showTextToast(mContext, "接到客人");
                    CommonEventBusEnity busEnity = new CommonEventBusEnity("arrived", flag + "");
                    EventBus.getDefault().post(busEnity);
                    edGaosuFeeCB.setFocusable(true);
                    finish();
                }
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
                CommonEventBusEnity busEnity = new CommonEventBusEnity("puckUp", flag + "");
                EventBus.getDefault().post(busEnity);
                tvShoucheCB.setText("已接到");
                tvShoucheCB.setBackgroundResource(R.drawable.shap_gray_30);
                tvShoucheCB.setClickable(false);
                if (orderInfo.isPdFlag() && flag == 1) {
                    WebSocketWorker.jiedanState = 3;
                }
                finish();
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
