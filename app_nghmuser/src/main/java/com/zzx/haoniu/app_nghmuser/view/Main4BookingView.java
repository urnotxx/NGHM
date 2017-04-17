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

public class Main4BookingView extends FrameLayout implements View.OnClickListener {

    private TextView tvPriceM1;

    public Main4BookingView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_main1booking, this);
        tvPriceM1 = (TextView) findViewById(R.id.tvPriceM1);
        ((TextView) findViewById(R.id.tvBookingM1)).setText("预约代驾");
        findViewById(R.id.tvBookingM1).setOnClickListener(this);
    }

    private OnClickListener bookListener;

    public void setListener(OnClickListener bookListener) {
        this.bookListener = bookListener;
    }

    public void setPrice(String loc) {
        if (loc != null && !StringUtils.isEmpty(loc)) {
            tvPriceM1.setText(loc);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBookingM1:
                if (bookListener != null)
                    bookListener.onClick(this);
                break;
        }
    }
}
