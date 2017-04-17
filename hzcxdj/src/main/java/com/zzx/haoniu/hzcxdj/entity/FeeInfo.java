package com.zzx.haoniu.hzcxdj.entity;

/**
 * Created by Administrator on 2017/1/12.
 */

public class FeeInfo {

    /**
     * order_otherCharges : 0.0
     * order_remoteFee : 0.0
     * order_roadToll : 2.0
     * realPay : 20.0
     */

    private double order_otherCharges;
    private double order_remoteFee;
    private double order_roadToll;
    private double realPay;

    public double getOrder_otherCharges() {
        return order_otherCharges;
    }

    public void setOrder_otherCharges(double order_otherCharges) {
        this.order_otherCharges = order_otherCharges;
    }

    public double getOrder_remoteFee() {
        return order_remoteFee;
    }

    public void setOrder_remoteFee(double order_remoteFee) {
        this.order_remoteFee = order_remoteFee;
    }

    public double getOrder_roadToll() {
        return order_roadToll;
    }

    public void setOrder_roadToll(double order_roadToll) {
        this.order_roadToll = order_roadToll;
    }

    public double getRealPay() {
        return realPay;
    }

    public void setRealPay(double realPay) {
        this.realPay = realPay;
    }
}
