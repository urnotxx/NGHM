package com.zzx.haoniu.app_nghmuser.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/13.
 */

public class PayInfo implements Serializable {

    /**
     * amount : 8.0
     * baseAmount : 8.0
     * car : 128
     * company : 2
     * createTime : 1484270681000
     * delFlag : false
     * destination : 三里庵工商所
     * distance : 2.0
     * distanceAmount : 0.0
     * driver : 130
     * fromType : 1
     * id : 3055
     * lastUpdateTime : 1484270738820
     * member : 276
     * no : SJDD20170113092441PWRBAX
     * otherCharges : 0.0
     * payChannel : -1
     * payStatus : 6
     * pdFlag : false
     * phone : 18655050310
     * realDistance : 2.0
     * realPay : 9.0
     * remark : 通过APP下单
     * remoteFee : 0.0
     * reservationAddress : 贾不假包子店(望江西路)
     * roadToll : 1.0
     * serviceType : 3
     * setOutFlag : true
     * setouttime : 1484270688000
     * status : 4
     * timeOutAmount : 0.0
     * type : 42
     * waitAmount : 0.0
     * ygAmount : 8.0
     * yhAmount : 0.0
     */

    private double amount;
    private double baseAmount;
    private int car;
    private int company;
    private long createTime;
    private boolean delFlag;
    private String destination;
    private double distance;
    private double distanceAmount;
    private int driver;
    private int fromType;
    private int id;
    private long lastUpdateTime;
    private int member;
    private String no;
    private double otherCharges;
    private int payChannel;
    private int payStatus;
    private boolean pdFlag;
    private String phone;
    private double realDistance;
    private double realPay;
    private String remark;
    private double remoteFee;
    private String reservationAddress;
    private double roadToll;
    private int serviceType;
    private boolean setOutFlag;
    private long setouttime;
    private int status;
    private double timeOutAmount;
    private int type;
    private double waitAmount;
    private double ygAmount;
    private double yhAmount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistanceAmount() {
        return distanceAmount;
    }

    public void setDistanceAmount(double distanceAmount) {
        this.distanceAmount = distanceAmount;
    }

    public int getDriver() {
        return driver;
    }

    public void setDriver(int driver) {
        this.driver = driver;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public double getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(double otherCharges) {
        this.otherCharges = otherCharges;
    }

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public boolean isPdFlag() {
        return pdFlag;
    }

    public void setPdFlag(boolean pdFlag) {
        this.pdFlag = pdFlag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRealDistance() {
        return realDistance;
    }

    public void setRealDistance(double realDistance) {
        this.realDistance = realDistance;
    }

    public double getRealPay() {
        return realPay;
    }

    public void setRealPay(double realPay) {
        this.realPay = realPay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getRemoteFee() {
        return remoteFee;
    }

    public void setRemoteFee(double remoteFee) {
        this.remoteFee = remoteFee;
    }

    public String getReservationAddress() {
        return reservationAddress;
    }

    public void setReservationAddress(String reservationAddress) {
        this.reservationAddress = reservationAddress;
    }

    public double getRoadToll() {
        return roadToll;
    }

    public void setRoadToll(double roadToll) {
        this.roadToll = roadToll;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isSetOutFlag() {
        return setOutFlag;
    }

    public void setSetOutFlag(boolean setOutFlag) {
        this.setOutFlag = setOutFlag;
    }

    public long getSetouttime() {
        return setouttime;
    }

    public void setSetouttime(long setouttime) {
        this.setouttime = setouttime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTimeOutAmount() {
        return timeOutAmount;
    }

    public void setTimeOutAmount(double timeOutAmount) {
        this.timeOutAmount = timeOutAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(double waitAmount) {
        this.waitAmount = waitAmount;
    }

    public double getYgAmount() {
        return ygAmount;
    }

    public void setYgAmount(double ygAmount) {
        this.ygAmount = ygAmount;
    }

    public double getYhAmount() {
        return yhAmount;
    }

    public void setYhAmount(double yhAmount) {
        this.yhAmount = yhAmount;
    }
}
