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
import com.zzx.haoniu.hzcxkuaiche.adapter.CarBrandAdapter;
import com.zzx.haoniu.hzcxkuaiche.entity.CarBrandInfo;
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

public class CarBrandActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lvBrandCB)
    ListView lvBrandCB;
    @Bind(R.id.lvModelCB)
    ListView lvModelCB;
    @Bind(R.id.activity_car_brand)
    LinearLayout activityCarBrand;

    public static final int RESUALT_CAR = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);
        ButterKnife.bind(this);
        steepStatusBar();
        setMargins(activityCarBrand);
        initView();
        requestBrandInfo();
    }

    private CarBrandAdapter adapter1, adapter2;
    private List<CarBrandInfo> infos1, infos2;
    private String brandId, modleId;
    private String brandName, modleName;

    @Override
    public void initView() {
        tvTitle.setText("设置车辆");
        infos1 = new ArrayList<>();
        infos2 = new ArrayList<>();
        adapter1 = new CarBrandAdapter(mContext, infos1);
        adapter2 = new CarBrandAdapter(mContext, infos2);
        lvBrandCB.setAdapter(adapter1);
        lvModelCB.setAdapter(adapter2);
        lvBrandCB.setOnItemClickListener(this);
        lvModelCB.setOnItemClickListener(this);
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

    private void requestBrandInfo() {
        ApiClient.requestNetHandle(mContext, AppConfig.requestCarBrand, "", null, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "车的信息:" + json);
                    List<CarBrandInfo> infos = JSON.parseArray(json, CarBrandInfo.class);
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

    private void requestCarModel(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("brand", infos1.get(position).getId());
        ApiClient.requestNetHandle(mContext, AppConfig.requestCarModel, "请求车辆信息中...", map, new ResultListener() {
            @Override
            public void onSuccess(String json) {
                if (json != null) {
                    L.d("TAG", "车类型的信息:" + json);
                    List<CarBrandInfo> infos = JSON.parseArray(json, CarBrandInfo.class);
                    if (infos != null) {
                        infos2.addAll(infos);
                    }
                }
                brandId = infos1.get(position).getId() + "";
                brandName = infos1.get(position).getName();
                adapter1.changeSelected(position);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.showTextToast(mContext, msg);
            }
        });
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.lvBrandCB) {
            requestCarModel(i);
        } else if (adapterView.getId() == R.id.lvModelCB) {
            adapter2.changeSelected(i);
            modleId = infos2.get(i).getId() + "";
            modleName = infos2.get(i).getName();
            Intent intent = new Intent();
            intent.putExtra("modleId", modleId);
            intent.putExtra("modleName", modleName);
            intent.putExtra("brandId", brandId);
            intent.putExtra("brandName", brandName);
            //通过intent对象返回结果，必须要调用一个setResult方法，
            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
            setResult(RESUALT_CAR, intent);
            finish();
        }
    }
}
