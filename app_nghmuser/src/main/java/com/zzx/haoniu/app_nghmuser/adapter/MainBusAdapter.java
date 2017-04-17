package com.zzx.haoniu.app_nghmuser.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zzx.haoniu.app_nghmuser.view.Main5BusAdapterView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MainBusAdapter extends PagerAdapter {

    private List<Main5BusAdapterView> viewList;

    public MainBusAdapter(List<Main5BusAdapterView> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
