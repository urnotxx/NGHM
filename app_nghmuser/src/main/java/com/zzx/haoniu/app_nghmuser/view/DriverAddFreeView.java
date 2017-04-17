package com.zzx.haoniu.app_nghmuser.view;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.adapter.AddAddressAdapter;
import com.zzx.haoniu.app_nghmuser.entity.AddressInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class DriverAddFreeView extends FrameLayout implements View.OnClickListener {

    private TextView tvLocDF;
    private TextView tvNumDF;
    private TextView tvStartTimeDF;
    private TextView tvDestinationDF;
    private LinearLayout llLocDF;
    private LinearLayout llPeopleNumDF;
    private LinearLayout llStartTimeDF;
    private LinearLayout llDestinationDF;

    private GridView gridView;

    private Context mContext;
    private List<AddressInfo> addressInfos;
    private AddAddressAdapter addAddressAdapter;

    public DriverAddFreeView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_driveraddfree, this);
        initView();
        initData();
    }

    private void initView() {
        tvLocDF = (TextView) findViewById(R.id.tvLocDF);
        tvNumDF = (TextView) findViewById(R.id.tvNumDF);
        tvStartTimeDF = (TextView) findViewById(R.id.tvStartTimeDF);
        tvDestinationDF = (TextView) findViewById(R.id.tvDestinationDF);
        llLocDF = (LinearLayout) findViewById(R.id.llLocDF);
        llPeopleNumDF = (LinearLayout) findViewById(R.id.llPeopleNumDF);
        llStartTimeDF = (LinearLayout) findViewById(R.id.llStartTimeDF);
        llDestinationDF = (LinearLayout) findViewById(R.id.llDestinationDF);

        gridView = (GridView) findViewById(R.id.gridViewDF);

        llLocDF.setOnClickListener(this);
        llPeopleNumDF.setOnClickListener(this);
        llDestinationDF.setOnClickListener(this);
        llStartTimeDF.setOnClickListener(this);
        findViewById(R.id.tvBookingDF).setOnClickListener(this);
    }

    private void initData() {
        addressInfos = new ArrayList<>();
        int size = 6;
        int length = 90;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) gridView.getLayoutParams();
        params.width = gridviewWidth;
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
        addAddressAdapter = new AddAddressAdapter(mContext, addressInfos);
        gridView.setAdapter(addAddressAdapter);
        addAddressAdapter.notifyDataSetChanged();
    }

    private OnClickListener locListener, desListener;

    public void setListener(OnClickListener locListener, OnClickListener desListener) {
        this.locListener = locListener;
        this.desListener = desListener;
    }

    public void addAddress(AddressInfo info) {
        addressInfos.add(info);
        addAddressAdapter.notifyDataSetChanged();
    }

    public void setLoc(String loc) {
        if (loc != null && !StringUtils.isEmpty(loc)) {
            tvLocDF.setText(loc);
        }
    }

    public void setDes(String des) {
        if (des != null) {
            tvDestinationDF.setText(des);
        }
    }

    public void setPeopleNum(String num) {
        if (num != null) {
            tvNumDF.setText(num);
        }
    }

    public void setStartTime(String time) {
        if (time != null) {
            tvStartTimeDF.setText(time);
        }
    }

    private OnClickListener peopleNumListener, timeListener;

    public void setSelectListener(OnClickListener peopleNumListener, OnClickListener timeListener) {
        this.peopleNumListener = peopleNumListener;
        this.timeListener = timeListener;
    }

    private OnClickListener bookListener;

    public void setBookListener(OnClickListener bookListener) {
        this.bookListener = bookListener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llLocDF:
                if (locListener != null)
                    locListener.onClick(this);
                break;
            case R.id.llDestinationDF:
                if (desListener != null)
                    desListener.onClick(this);
                break;
            case R.id.llPeopleNumDF:
                if (peopleNumListener != null)
                    peopleNumListener.onClick(this);
                break;
            case R.id.llStartTimeDF:
                if (timeListener != null)
                    timeListener.onClick(this);
                break;
            case R.id.tvBookingDF:
                if (bookListener != null)
                    bookListener.onClick(this);
                break;
        }
    }
}
