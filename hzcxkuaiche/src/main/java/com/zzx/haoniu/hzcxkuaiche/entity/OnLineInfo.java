package com.zzx.haoniu.hzcxkuaiche.entity;

/**
 * Created by Administrator on 2017/2/5.
 */

public class OnLineInfo {


    /**
     * btotalOrderNumber : 8
     * closeRate : 87.50%
     * driveramount : 7.0
     */

    private int btotalOrderNumber;
    private String closeRate;
    private double driveramount;

    public int getBtotalOrderNumber() {
        return btotalOrderNumber;
    }

    public void setBtotalOrderNumber(int btotalOrderNumber) {
        this.btotalOrderNumber = btotalOrderNumber;
    }

    public String getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(String closeRate) {
        this.closeRate = closeRate;
    }

    public double getDriveramount() {
        return driveramount;
    }

    public void setDriveramount(double driveramount) {
        this.driveramount = driveramount;
    }
}
