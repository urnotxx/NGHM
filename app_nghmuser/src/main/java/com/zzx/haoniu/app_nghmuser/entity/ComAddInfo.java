package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ComAddInfo {

    /**
     * address : 安理教育一中八中名师辅导三里庵
     * address_title : 家
     * create_time : 2017-02-08 12:08:08
     * latitude : 32.0
     * longitude : 117.0
     * memberid : 283
     * uuid : 1d999a7083b44727b1d2c830c6847669
     */

    private String address;
    private String address_title;
    private String create_time;
    private double latitude;
    private double longitude;
    private int memberid;
    private String uuid;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_title() {
        return address_title;
    }

    public void setAddress_title(String address_title) {
        this.address_title = address_title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
