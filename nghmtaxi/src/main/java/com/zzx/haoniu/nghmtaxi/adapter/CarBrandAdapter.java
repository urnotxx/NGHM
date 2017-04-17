package com.zzx.haoniu.nghmtaxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzx.haoniu.nghmtaxi.R;
import com.zzx.haoniu.nghmtaxi.entity.CarBrandInfo;
import com.zzx.haoniu.nghmtaxi.entity.ProvinceInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class CarBrandAdapter extends BaseAdapter {
    private Context mContext;
    private List<CarBrandInfo> infos = null;

    public CarBrandAdapter(Context mContext, List<CarBrandInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos == null ? 0 : infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_city_head, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_city);
        tv.setText(infos.get(position).getName());
        if (mSelect == position) {
            //选中项背景
            tv.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.colorBlack));  //其他项背景
        }
        return convertView;
    }

    int mSelect = 0;   //选中项

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

    class CityHoder {
        TextView tv_city;
    }
}
