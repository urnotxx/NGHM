package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/3/22.
 */

public class AddressInfo {
    private String addressName;
    private double lat, lng;

    public AddressInfo(String addressName, double lat, double lng) {
        this.addressName = addressName;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
