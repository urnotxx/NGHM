package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/1/7.
 */

public class PriceInfo {

    /**
     * baseAmount : 8.0
     * distanceAmount : 3.0
     * timeAmount : 0.0
     * totalAmount : 11.0
     * typeId : 42
     */

    private double baseAmount;
    private double distanceAmount;
    private double timeAmount;
    private double totalAmount;
    private int typeId;

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public double getDistanceAmount() {
        return distanceAmount;
    }

    public void setDistanceAmount(double distanceAmount) {
        this.distanceAmount = distanceAmount;
    }

    public double getTimeAmount() {
        return timeAmount;
    }

    public void setTimeAmount(double timeAmount) {
        this.timeAmount = timeAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
