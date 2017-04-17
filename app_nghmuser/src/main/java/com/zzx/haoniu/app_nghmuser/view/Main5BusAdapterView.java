package com.zzx.haoniu.app_nghmuser.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.utils.AMapUtil;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Main5BusAdapterView extends FrameLayout {
    private TextView tvBus, tvDetial;

    public Main5BusAdapterView(Context context, BusPath busPath) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.adapter_mainbus, this);
        tvBus = (TextView) findViewById(R.id.tvBusMB);
        tvDetial = (TextView) findViewById(R.id.tvDetialMB);
        tvBus.setText(AMapUtil.getBusPathTitle(busPath));
        tvDetial.setText(AMapUtil.getBusPathDes(busPath));
    }
}
