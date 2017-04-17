package com.zzx.haoniu.hzcxkuaiche.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.NaviLatLng;
import com.zzx.haoniu.hzcxkuaiche.R;
import com.zzx.haoniu.hzcxkuaiche.utils.StringUtils;

public class NavActivity extends BaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取导航的起止地址（获取起始地址仅仅是为了，导航播报的第一句准确）
        LatLng startLatLng = getIntent().getParcelableExtra("point_start");
        LatLng endLatLng = getIntent().getParcelableExtra("point_end");
        endPlace = getIntent().getStringExtra("endPlace");
        if (startLatLng == null || endLatLng == null) {
            Toast.makeText(this, "数据传递错误,请稍后再试~", Toast.LENGTH_LONG).show();
            this.finish();
            return;
        }
        //对导航起止地址进行赋值
        this.mStartLatlng = new NaviLatLng(startLatLng.latitude, startLatLng.longitude);
        this.mEndLatlng = new NaviLatLng(endLatLng.latitude, endLatLng.longitude);
        setContentView(R.layout.activity_nav);
        mContext = this;
        steepStatusBar();
        mAMapNaviView = (AMapNaviView) findViewById(R.id.naviMap);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mStartList.clear();
        noStartCalculate();
        initView();
        initEvent();
    }

    /**
     * [沉浸状态栏]
     */
    public void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果使用无起点算路，请这样写
     */
    private void noStartCalculate() {
        //无起点算路须知：
        //AMapNavi在构造的时候，会startGPS，但是GPS启动需要一定时间
        //在刚构造好AMapNavi类之后立刻进行无起点算路，会立刻返回false
        //给人造成一种等待很久，依然没有算路成功 算路失败回调的错觉
        //因此，建议，提前获得AMapNavi对象实例，并判断GPS是否准备就绪
        if (mAMapNavi.isGpsReady())
            mAMapNavi.calculateDriveRoute(mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
    }

    @Override
    public void onCalculateRouteSuccess() {
        mAMapNavi.startNavi(NaviType.GPS);
    }

    private String endPlace;
    private Context mContext;

    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if (endPlace == null || !StringUtils.isEmpty(endPlace)) {
            tv_title.setText(endPlace);
        }
    }

    private void initEvent() {
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
