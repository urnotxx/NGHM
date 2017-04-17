package com.zzx.haoniu.app_nghmuser.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Main1AddressView extends FrameLayout implements View.OnClickListener {

    private LinearLayout llLoc, llDestination;
    private TextView tvLoc, tvDestination;

    public Main1AddressView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_main1address, this);
        llLoc = (LinearLayout) findViewById(R.id.llLoc);
        llDestination = (LinearLayout) findViewById(R.id.llDestination);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvDestination = (TextView) findViewById(R.id.tvDestination);
        llLoc.setOnClickListener(this);
        llDestination.setOnClickListener(this);
    }

    private OnClickListener locListener, desListener;

    public void setListener(OnClickListener locListener, OnClickListener desListener) {
        this.locListener = locListener;
        this.desListener = desListener;
    }

    public void setLoc(String loc) {
        if (loc != null && !StringUtils.isEmpty(loc)) {
            tvLoc.setText(loc);
        }
    }

    public void setDes(String des) {
        if (des != null) {
            tvDestination.setText(des);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llLoc:
                if (locListener != null)
                    locListener.onClick(this);
                break;
            case R.id.llDestination:
                if (desListener != null)
                    desListener.onClick(this);
                break;
        }
    }
}
