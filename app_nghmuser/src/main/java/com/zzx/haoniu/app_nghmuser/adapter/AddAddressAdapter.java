package com.zzx.haoniu.app_nghmuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.zzx.haoniu.app_nghmuser.R;
import com.zzx.haoniu.app_nghmuser.entity.AddressInfo;
import com.zzx.haoniu.app_nghmuser.utils.StringUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/11/29.
 */
public class AddAddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddressInfo> infos;

    public AddAddressAdapter(Context mContext, List<AddressInfo> results) {
        this.mContext = mContext;
        this.infos = results;
    }


    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return infos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_addaddress, null);
        TextView tvName = (TextView) view.findViewById(R.id.tvAddressA);
        ImageView ivDel = (ImageView) view.findViewById(R.id.ivDelA);
        tvName.setText(infos.get(i).getAddressName());
        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infos.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

}
