package com.zzx.haoniu.hzcxdj.entity;

/**
 * Created by Administrator on 2017/1/21.
 */

public class WalletInfo {

    /**
     * ye : 1000.0
     * yesterday : 0.0
     * month : 0.0
     * today : 0.0
     */

    private double ye;
    private double yesterday;
    private double month;
    private double today;

    public double getYe() {
        return ye;
    }

    public void setYe(double ye) {
        this.ye = ye;
    }

    public double getYesterday() {
        return yesterday;
    }

    public void setYesterday(double yesterday) {
        this.yesterday = yesterday;
    }

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getToday() {
        return today;
    }

    public void setToday(double today) {
        this.today = today;
    }
}
