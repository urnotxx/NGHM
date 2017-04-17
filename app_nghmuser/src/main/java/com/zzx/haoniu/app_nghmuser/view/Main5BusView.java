package com.zzx.haoniu.app_nghmuser.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.zzx.haoniu.app_nghmuser.R;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Main5BusView extends FrameLayout {
    public ViewPager viewPager;
    public LinearLayout ll_dotGroup;

    public Main5BusView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_main5bus, this);
        viewPager = (ViewPager) findViewById(R.id.viewPage);
        ll_dotGroup = (LinearLayout) findViewById(R.id.ll_dian);
//        vpBus.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
//        vpBus.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //设置如果只有一组数据时不能滑动
    }
}
