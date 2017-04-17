package com.zzx.haoniu.app_nghmuser.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/2/13.
 */

public class CusAddFreeView extends FrameLayout implements View.OnClickListener {

    TextView tvLocCF;
    TextView tvNumCF;
    TextView tvPriceCF;
    TextView tvBookingCF;
    TextView tvStartTimeCF;
    TextView tvDestinationCF;
    LinearLayout llLocCF;
    LinearLayout llPeopleNumCF;
    LinearLayout llStartTimeCF;
    LinearLayout llDestinationCF;


    public CusAddFreeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_cusaddfree, this);
        initView();
    }

    private void initView() {
        tvLocCF = (TextView) findViewById(R.id.tvLocCF);
        tvNumCF = (TextView) findViewById(R.id.tvNumCF);
        tvPriceCF = (TextView) findViewById(R.id.tvPriceCF);
        tvStartTimeCF = (TextView) findViewById(R.id.tvStartTimeCF);
        tvDestinationCF = (TextView) findViewById(R.id.tvDestinationCF);
        tvBookingCF = (TextView) findViewById(R.id.tvBookingCF);
        llLocCF = (LinearLayout) findViewById(R.id.llLocCF);
        llPeopleNumCF = (LinearLayout) findViewById(R.id.llPeopleNumCF);
        llStartTimeCF = (LinearLayout) findViewById(R.id.llStartTimeCF);
        llDestinationCF = (LinearLayout) findViewById(R.id.llDestinationCF);

        llLocCF.setOnClickListener(this);
        llPeopleNumCF.setOnClickListener(this);
        llDestinationCF.setOnClickListener(this);
        llStartTimeCF.setOnClickListener(this);
        tvBookingCF.setOnClickListener(this);
    }

    private OnClickListener locListener, desListener;

    public void setListener(OnClickListener locListener, OnClickListener desListener) {
        this.locListener = locListener;
        this.desListener = desListener;
    }

    public void setLoc(String loc) {
        if (loc != null && !StringUtils.isEmpty(loc)) {
            tvLocCF.setText(loc);
        }
    }

    public void setDes(String des) {
        if (des != null) {
            tvDestinationCF.setText(des);
        }
    }

    public void setPeopleNum(String num) {
        if (num != null) {
            tvNumCF.setText(num);
        }
    }

    public void setStartTime(String time) {
        if (time != null) {
            tvStartTimeCF.setText(time);
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
            case R.id.llLocCF:
                if (locListener != null)
                    locListener.onClick(this);
                break;
            case R.id.llDestinationCF:
                if (desListener != null)
                    desListener.onClick(this);
                break;
            case R.id.llPeopleNumCF:
                if (peopleNumListener != null)
                    peopleNumListener.onClick(this);
                break;
            case R.id.llStartTimeCF:
                if (timeListener != null)
                    timeListener.onClick(this);
                break;
            case R.id.tvBookingCF:
                if (bookListener != null)
                    bookListener.onClick(this);
                break;
        }
    }
}
