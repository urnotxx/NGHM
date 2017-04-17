package com.zzx.haoniu.app_nghmuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/11/29.
 */
public class SearchAdapterList extends BaseAdapter {
    private Context mContext;
    private List<PoiItem> items;

    public SearchAdapterList(Context mContext, List<PoiItem> results) {
        this.mContext = mContext;
        this.items = results;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_poisearch, null);
        TextView tvName = (TextView) view.findViewById(R.id.tvNameSearch);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddressSearch);
        tvName.setText(items.get(i).getTitle());
        if (items.get(i).getSnippet() != null && !StringUtils.isEmpty(items.get(i).getSnippet()))
            tvAddress.setText(items.get(i).getSnippet());
        else
            tvAddress.setText(items.get(i).getDirection());
        return view;
    }


}
