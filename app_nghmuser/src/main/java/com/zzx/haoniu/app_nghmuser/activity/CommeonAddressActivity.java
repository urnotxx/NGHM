package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.ComAddInfo;
import com.zzx.haoniu.app_nghmuser.entity.CommonEventBusEnity;
import com.zzx.haoniu.app_nghmuser.http.ApiClient;
import com.zzx.haoniu.app_nghmuser.http.AppConfig;
import com.zzx.haoniu.app_nghmuser.http.ResultListener;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;
import com.zzx.haoniu.app_nghmuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialogsamples.utils.L;
import self.androidbase.utils.SelfMapUtils;
import self.androidbase.views.SelfLinearLayout;

public class CommeonAddressActivity extends BaseActivity {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tvHomeCA)
    TextView tvHomeCA;
    @Bind(R.id.llHomeCA)
    LinearLayout llHomeCA;
    @Bind(R.id.tvCompanyCA)
    TextView tvCompanyCA;
    @Bind(R.id.llCompanyCA)
    LinearLayout llCompanyCA;
    private final static int REQUEST_COMMEONPLACE = 2;//地址返回结果码
    private int tag = 0;  //1  家   2  公司

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commeon_address);
        ButterKnife.bind(this);
        steepStatusBar();
        tvTitle.setText("常用地址");
        infos = new ArrayList<>();
        requestCommeonAddress();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(CommonEventBusEnity event) {
        if (event.getSendCode().equals("unLogInUser")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        return 0;
    }


    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.ll_back, R.id.llHomeCA, R.id.llCompanyCA})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.llHomeCA:
                tag = 1;
                initLocation();
                break;
            case R.id.llCompanyCA:
                tag = 2;
                initLocation();
                break;
        }
    }

    private void initLocation() {
        SelfMapUtils selfMapUtils = SelfMapUtils.getInstance(this);
        selfMapUtils.startLocation(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AMapLocation aMapLocation = (AMapLocation) msg.obj;
                if (aMapLocation.getLatitude() != 0 && aMapLocation.getLongitude() != 0) {
                    startActivityForResult(new Intent(mContext, InputSearchActivity.class)
                            .putExtra("city", aMapLocation.getCity()).putExtra("flag", 1), REQUEST_COMMEONPLACE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == InputSearchActivity.RESUALT_PLACE) {
            if (requestCode == REQUEST_COMMEONPLACE) {
                if (tag == 1) {
                    tvHomeCA.setText(data.getStringExtra("searchName"));
                } else if (tag == 2) {
                    tvCompanyCA.setText(data.getStringExtra("searchName"));
                }
                searchName = data.getStringExtra("searchName");
                searchLat = Double.parseDouble(data.getStringExtra("searchLat"));
                searchLng = Double.parseDouble(data.getStringExtra("searchLng"));
                addCommeonAddress();
            }
        }
    }

    private String searchName;
    private double searchLat, searchLng;

    private void addCommeonAddress() {
        Map<String, Object> map = new HashMap<>();
        map.put("address", searchName);
        map.put("longitude", searchLng);
        map.put("latitud", searchLat);
        if (tag == 1) {
            map.put("addressTitle", "家");
            map.put("homeOrCompany", 1);
        } else if (tag == 2) {
            map.put("addressTitle", "公司");
            map.put("homeOrCompany", 2);
        }
        ApiClient.requestNetHandle(mContext, AppConfig.requestAddCom, "添加常用地址中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "添加常用地址:" + json);
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private List<ComAddInfo> infos;


    private void requestCommeonAddress() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestComAdd, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null && !StringUtils.isEmpty(json)) {
                    L.d("TAG", "常用地址:" + json);
                    List<ComAddInfo> comAddInfos = JSON.parseArray(json, ComAddInfo.class);
                    if (comAddInfos != null && comAddInfos.size() > 0) {
                        infos.addAll(comAddInfos);
                        initView();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @Override
    public void initView() {
        if (infos != null && infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).getAddress_title().equals("家")) {
                    tvHomeCA.setText(infos.get(i).getAddress());
                } else if (infos.get(i).getAddress_title().equals("公司")) {
                    tvCompanyCA.setText(infos.get(i).getAddress());
                }
            }
        }
    }
}
