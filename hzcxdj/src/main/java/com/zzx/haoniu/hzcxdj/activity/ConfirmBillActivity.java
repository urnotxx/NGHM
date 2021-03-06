package com.zzx.haoniu.hzcxdj.activity;

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

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.hzcxdj.R;
import com.zzx.haoniu.hzcxdj.app.AppContext;
import com.zzx.haoniu.hzcxdj.entity.FeeInfo;
import com.zzx.haoniu.hzcxdj.entity.OrderInfo;
import com.zzx.haoniu.hzcxdj.http.ApiClient;
import com.zzx.haoniu.hzcxdj.http.AppConfig;
import com.zzx.haoniu.hzcxdj.http.ResultListener;
import com.zzx.haoniu.hzcxdj.utils.L;
import com.zzx.haoniu.hzcxdj.utils.StringUtils;
import com.zzx.haoniu.hzcxdj.utils.ToastUtils;
import com.zzx.haoniu.hzcxdj.view.SlidingButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @Bind(R.id.edChangTuFeeCB)
    EditText edChangTuFeeCB;
    @Bind(R.id.edOtherFeeCB)
    EditText edOtherFeeCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bill);
        ButterKnife.bind(this);
        steepStatusBar();
        if (getIntent().getExtras() != null) {
            initParms(getIntent().getExtras());
        } else {
            ToastUtils.showTextToast(mContext, "数据接收有误!");
            finish();
        }
        initView();
    }

    private double price;
    private Bundle bundle;
    private OrderInfo orderInfo;
    private float runDistance;
    private double gaoSuFee, changTuFee, otherFee;

    @Override
    public void initView() {
        llBack.setVisibility(View.GONE);
        tvStartCB.setText(orderInfo.getReservationAddress());
        tvEndCB.setText(orderInfo.getDestination());
        tvPriceCB.setText(orderInfo.getYgAmount() + "");
        tvTitle.setText("确认订单");
        tvSubmit.setText("清楚修改");
        edGaosuFeeCB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && !StringUtils.isEmpty(charSequence.toString())) {
                    tvPriceCB.setText((price + Double.parseDouble(charSequence.toString())) + changTuFee + otherFee + "");
                } else {
                    tvPriceCB.setText(price + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null && !StringUtils.isEmpty(editable.toString())) {
                    gaoSuFee = Double.parseDouble(editable.toString());
                }
            }
        });
        edChangTuFeeCB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && !StringUtils.isEmpty(charSequence.toString())) {
                    tvPriceCB.setText((price + Double.parseDouble(charSequence.toString())) + gaoSuFee + otherFee + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null && !StringUtils.isEmpty(editable.toString())) {
                    changTuFee = Double.parseDouble(editable.toString());
                }
            }
        });
        edOtherFeeCB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null && !StringUtils.isEmpty(charSequence.toString())) {
                    tvPriceCB.setText((price + Double.parseDouble(charSequence.toString())) + gaoSuFee + changTuFee + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null && !StringUtils.isEmpty(editable.toString())) {
                    otherFee = Double.parseDouble(editable.toString());
                }
            }
        });
    }

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        runDistance = parms.getFloat("runDistance");
        orderInfo = (OrderInfo) parms.getSerializable("orderInfo");
        price = orderInfo.getYgAmount();
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.ll_right, R.id.ivCallCB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                break;
            case R.id.ll_right:
                edGaosuFeeCB.setText("");
                tvPriceCB.setText(price + "");
                break;
            case R.id.ivCallCB:
                AppContext.getInstance().callUser(mContext, orderInfo.getPhone());
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
     * 客人到达目的地
     */
    private void arrived() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", orderInfo.getId());
        map.put("distance", runDistance);
        map.put("roadToll", gaoSuFee);
        map.put("waitMinutes", 0);
        map.put("remoteFee", changTuFee);
        map.put("otherCharges", otherFee);
        map.put("payType", 1);//支付类型(1:支付宝;2:代收;3:余额)
        ApiClient.requestNetHandle(mContext, AppConfig.requestArrived, "订单结算...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "订单结算:" + json);
                    FeeInfo feeInfo = JSON.parseObject(json, FeeInfo.class);
                    double totleFee = feeInfo.getRealPay();
                    bundle.putDouble("totleFee", totleFee);
                    startActivity(ConfirmFinishActivity.class, bundle);
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
