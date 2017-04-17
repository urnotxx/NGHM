package com.zzx.haoniu.app_nghmuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.SearchAdapterList;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import iosdialog.dialogsamples.utils.L;
import self.androidbase.views.SelfLinearLayout;
import self.androidbase.views.SelfTextView;

public class InputSearchActivity extends BaseActivity implements TextWatcher, Inputtips.InputtipsListener {

    @Bind(R.id.llSearchIS)
    SelfLinearLayout llSearchIS;
    @Bind(R.id.edSearchIS)
    AutoCompleteTextView edSearchIS;
    @Bind(R.id.tvCancleIS)
    SelfTextView tvCancleIS;
    @Bind(R.id.tvHomeS)
    TextView tvHomeS;
    @Bind(R.id.llHomeIS)
    LinearLayout llHomeIS;
    @Bind(R.id.tvCompanyS)
    TextView tvCompanyS;
    @Bind(R.id.llCompangIS)
    LinearLayout llCompangIS;
    @Bind(R.id.llTopS)
    LinearLayout llTopS;
    @Bind(R.id.inputlistIS)
    ListView inputlistIS;
    @Bind(R.id.activity_input_search)
    LinearLayout activityInputSearch;

    public static final int RESUALT_PLACE = 12;
    private String city;
    private int flag = 0;// 1 添加常用地址   2 搜索地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_search);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityInputSearch);
        if (getIntent().getExtras() != null) {
            city = getIntent().getStringExtra("city");
            flag = getIntent().getIntExtra("flag", 0);
        }
        initView();
        initData();
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

    private void initData() {
        infos = new ArrayList<>();
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
        edSearchIS.addTextChangedListener(this);
        if (flag == 1) {
            llTopS.setVisibility(View.GONE);
        } else {
            llTopS.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.tvCancleIS, R.id.llHomeIS, R.id.llCompangIS, R.id.llSearchIS})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancleIS:
                edSearchIS.setText("");
                tvCancleIS.setVisibility(View.GONE);
                break;
            case R.id.llSearchIS:
                finish();
                break;
            case R.id.llHomeIS:
                if (infos != null && infos.size() > 0) {
                    if (infos.size() == 1) {
                        if (infos.get(0).getAddress_title().equals("家")) {
                            Intent intent = new Intent();
                            intent.putExtra("searchName", infos.get(0).getAddress());
                            intent.putExtra("searchLat", infos.get(0).getLatitude() + "");
                            intent.putExtra("searchLng", infos.get(0).getLongitude() + "");
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                            setResult(RESUALT_PLACE, intent);
                            finish();
                        } else {
                            startActivity(new Intent(mContext, CommeonAddressActivity.class));
                            finish();
                        }
                    } else if (infos.size() == 2) {
                        for (int i = 0; i < infos.size(); i++) {
                            if (infos.get(i).getAddress_title().equals("家")) {
                                Intent intent = new Intent();
                                intent.putExtra("searchName", infos.get(i).getAddress());
                                intent.putExtra("searchLat", infos.get(i).getLatitude() + "");
                                intent.putExtra("searchLng", infos.get(i).getLongitude() + "");
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                                setResult(RESUALT_PLACE, intent);
                                finish();
                            }
                        }
                    }
                } else {
                    startActivity(new Intent(mContext, CommeonAddressActivity.class));
                    finish();
                }
                break;
            case R.id.llCompangIS:
                if (infos != null && infos.size() > 0) {
                    if (infos.size() == 1) {
                        if (infos.get(0).getAddress_title().equals("公司")) {
                            Intent intent = new Intent();
                            intent.putExtra("searchName", infos.get(0).getAddress());
                            intent.putExtra("searchLat", infos.get(0).getLatitude() + "");
                            intent.putExtra("searchLng", infos.get(0).getLongitude() + "");
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                            setResult(RESUALT_PLACE, intent);
                            finish();
                        } else {
                            startActivity(new Intent(mContext, CommeonAddressActivity.class));
                            finish();
                        }
                    } else if (infos.size() == 2) {
                        for (int i = 0; i < infos.size(); i++) {
                            if (infos.get(i).getAddress_title().equals("公司")) {
                                Intent intent = new Intent();
                                intent.putExtra("searchName", infos.get(i).getAddress());
                                intent.putExtra("searchLat", infos.get(i).getLatitude() + "");
                                intent.putExtra("searchLng", infos.get(i).getLongitude() + "");
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                                setResult(RESUALT_PLACE, intent);
                                finish();
                            }
                        }
                    }
                } else {
                    startActivity(new Intent(mContext, CommeonAddressActivity.class));
                    finish();
                }
                break;
        }
    }

    private void setResualt(String name) {

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
                        initCommeonAddress();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void initCommeonAddress() {
        if (infos != null && infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).getAddress_title().equals("家")) {
                    tvHomeS.setText(infos.get(i).getAddress());
                } else if (infos.get(i).getAddress_title().equals("公司")) {
                    tvCompanyS.setText(infos.get(i).getAddress());
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tvCancleIS.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String newText = charSequence.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(mContext, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            final List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
//                String address = tipList.get(i).getAddress();
//                String district = tipList.get(i).getDistrict();
//                String name = tipList.get(i).getName();
//                L.d("TAG","address:"+address+" ,district:"+district+" ,name"+name);
                map.put("searchName", tipList.get(i).getName());
//                map.put("searchPlace", tipList.get(i).getAddress());
                map.put("searchLat", tipList.get(i).getPoint().getLatitude() + "");
                map.put("searchLng", tipList.get(i).getPoint().getLongitude() + "");
                listString.add(map);
            }
            SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_layout,
                    new String[]{"searchName", "searchPlace"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
            inputlistIS.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
            inputlistIS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HashMap<String, String> stringStringHashMap = listString.get(i);
                    Intent intent = new Intent();
                    intent.putExtra("searchName", stringStringHashMap.get("searchName"));
                    intent.putExtra("searchLat", stringStringHashMap.get("searchLat"));
                    intent.putExtra("searchLng", stringStringHashMap.get("searchLng"));
//                    intent.putExtra("searchName", poiItems.get(i).getTitle());
                    setResult(RESUALT_PLACE, intent);
                    finish();
                }
            });
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), rCode);
//            ToastUtils.showTextToast(mContext, rCode + "");
        }
    }
}
