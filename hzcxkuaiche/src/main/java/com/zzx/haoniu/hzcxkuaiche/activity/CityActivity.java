package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.adapter.ProvinceAdapter;
import com.zzx.haoniu.hzcxkuaiche.entity.ProvinceInfo;
import com.zzx.haoniu.hzcxkuaiche.http.ApiClient;
import com.zzx.haoniu.hzcxkuaiche.http.AppConfig;
import com.zzx.haoniu.hzcxkuaiche.http.ResultListener;
import com.zzx.haoniu.hzcxkuaiche.utils.L;
import com.zzx.haoniu.hzcxkuaiche.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import self.androidbase.views.SelfLinearLayout;

public class CityActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.ll_back)
    SelfLinearLayout llBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lvProC)
    ListView lvProC;
    @Bind(R.id.lvCityC)
    ListView lvCityC;
    @Bind(R.id.lvAreaC)
    ListView lvAreaC;
    @Bind(R.id.activity_city)
    LinearLayout activityCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityCity);
        requestProInfo();
        initView();
    }

    private ProvinceAdapter adapter1, adapter2, adapter3;
    private List<ProvinceInfo> infos1, infos2, infos3;
    private String proCode, cityCode, areaCode;
    private String proName, cityName, areaName;
    public static final int RESUALT_CITY = 16;

    @Override
    public void initView() {
        tvTitle.setText("设置城市");
        infos1 = new ArrayList<>();
        infos2 = new ArrayList<>();
        infos3 = new ArrayList<>();
        adapter1 = new ProvinceAdapter(mContext, infos1);
        adapter2 = new ProvinceAdapter(mContext, infos2);
        adapter3 = new ProvinceAdapter(mContext, infos3);
        lvProC.setAdapter(adapter1);
        lvCityC.setAdapter(adapter2);
        lvAreaC.setAdapter(adapter3);
        lvProC.setOnItemClickListener(this);
        lvCityC.setOnItemClickListener(this);
        lvAreaC.setOnItemClickListener(this);
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

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.lvProC:
                requestCityInfo(i);
                break;
            case R.id.lvCityC:
                requestAreaInfo(i);
                break;
            case R.id.lvAreaC:
                areaCode = infos3.get(i).getAdcode();
                areaName = infos3.get(i).getName();
                adapter3.changeSelected(i);
                Intent intent = new Intent();
                intent.putExtra("proCode", proCode);
                intent.putExtra("proName", proName);
                intent.putExtra("cityCode", cityCode);
                intent.putExtra("cityName", cityName);
                intent.putExtra("areaCode", areaCode);
                intent.putExtra("areaName", areaName);
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(RESUALT_CITY, intent);
                finish();
                break;
        }
    }

    private void requestProInfo() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestPro, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "省的信息:" + json);
                    List<ProvinceInfo> infos = JSON.parseArray(json, ProvinceInfo.class);
                    if (infos != null) {
                        infos1.addAll(infos);
                    }
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void requestCityInfo(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("adcode", infos1.get(position).getAdcode());
        ApiClient.requestNetHandle(mContext, AppConfig.requestCity, "请求数据中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "市的信息:" + json);
                    List<ProvinceInfo> infos = JSON.parseArray(json, ProvinceInfo.class);
                    if (infos != null) {
                        infos2.clear();
                        infos2.addAll(infos);
                    }
                }
                proCode = infos1.get(position).getAdcode();
                proName = infos1.get(position).getName();
                adapter1.changeSelected(position);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    private void requestAreaInfo(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("adcode", infos2.get(position).getAdcode());
        ApiClient.requestNetHandle(mContext, AppConfig.requestCity, "请求数据中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "区的信息:" + json);
                    List<ProvinceInfo> infos = JSON.parseArray(json, ProvinceInfo.class);
                    if (infos != null) {
                        infos3.clear();
                        infos3.addAll(infos);
                    }
                }
                cityCode = infos2.get(position).getAdcode();
                cityName = infos2.get(position).getName();
                adapter2.changeSelected(position);
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }
}
