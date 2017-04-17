package com.zzx.haoniu.app_nghmuser.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Main2BookingView extends FrameLayout implements View.OnClickListener {

    private TextView tvPriceM2P, tvPriceM2NP;
    private TextView tvPin, tvNPin;

    public Main2BookingView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_main2booking, this);
        tvPriceM2P = (TextView) findViewById(R.id.tvPriceM2P);
        tvPriceM2NP = (TextView) findViewById(R.id.tvPriceM2NP);
        tvPin = (TextView) findViewById(R.id.tvPin);
        tvNPin = (TextView) findViewById(R.id.tvNPin);
        findViewById(R.id.llPin).setOnClickListener(this);
        findViewById(R.id.llNPin).setOnClickListener(this);
        findViewById(R.id.tvBookingM2).setOnClickListener(this);
    }

    private OnClickListener pinListener, pinNoListener, bookListener;

    public void setListener(OnClickListener pinListener, OnClickListener pinNoListener, OnClickListener bookListener) {
        this.pinListener = pinListener;
        this.pinNoListener = pinNoListener;
        this.bookListener = bookListener;
    }

    public void setPrice(String pin, String noPin) {
        if (pin != null && !StringUtils.isEmpty(pin)) {
            tvPriceM2P.setText(pin);
        }
        if (noPin != null && !StringUtils.isEmpty(noPin)) {
            tvPriceM2NP.setText(noPin);
        }
    }

    public void setColor(boolean isPin) {
        if (isPin) {
            tvPin.setTextColor(getResources().getColor(R.color.colorRed));
            tvPriceM2P.setTextColor(getResources().getColor(R.color.colorRed));
            tvNPin.setTextColor(getResources().getColor(R.color.colorGrayText48));
            tvPriceM2NP.setTextColor(getResources().getColor(R.color.colorGrayText48));
        } else {
            tvNPin.setTextColor(getResources().getColor(R.color.colorRed));
            tvPriceM2NP.setTextColor(getResources().getColor(R.color.colorRed));
            tvPin.setTextColor(getResources().getColor(R.color.colorGrayText48));
            tvPriceM2P.setTextColor(getResources().getColor(R.color.colorGrayText48));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llPin:
                if (pinListener != null)
                    pinListener.onClick(this);
                break;
            case R.id.llNPin:
                if (pinNoListener != null)
                    pinNoListener.onClick(this);
                break;
            case R.id.tvBookingM2:
                if (bookListener != null)
                    bookListener.onClick(this);
                break;
        }
    }
}
