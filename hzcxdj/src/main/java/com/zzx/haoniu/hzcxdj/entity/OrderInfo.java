package com.zzx.haoniu.hzcxdj.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/10.
 */

public class OrderInfo implements Serializable {

    /**
     * amount : 0.0
     * baseAmount : 0.0
     * company : 2
     * createTime : 1484033813000
     * delFlag : false
     * destination : 三里庵工商所
     * distance : 2.0
     * distanceAmount : 0.0
     * fromType : 1
     * head_portrait : uploadfile/2017/1/7/f73ae4d6-0a46-430f-8f72-412c57104d45.jpg
     * id : 3007
     * member : 276
     * nick_name : Lee
     * no : SJDD20170110153653MNOVIQ
     * otherCharges : 0.0
     * payStatus : 6
     * pdFlag : false
     * phone : 18655050310
     * realDistance : 0.0
     * realPay : 0.0
     * remark : 通过APP下单
     * remoteFee : 0.0
     * reservationAddress : 市场星报社
     * roadToll : 0.0
     * serviceType : 3
     * setOutFlag : true
     * setouttime : 1484033810000
     * status : 1    自己定义  status的状态  5 去接   6接到   7 去送  8到达目的地
     * timeOut : 30
     * timeOutAmount : 0.0
     * trip : {"endLatitude":31.847486,"endLongitude":117.25143,"member":276,"orderId":3007,"startLatitude":31.83021,"startLongitude":117.255021}
     * type : 42
     * waitAmount : 0.0
     * ygAmount : 8.0
     * yhAmount : 0.0
     */

    private double amount;
    private double baseAmount;
    private int company;
    private long createTime;
    private boolean delFlag;
    private String destination;
    private double distance;
    private double distanceAmount;
    private int fromType;
    private String head_portrait;
    private int id;
    private int member;
    private String nick_name;
    private String no;
    private double otherCharges;
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
    private int timeOut;
    private double timeOutAmount;
    private TripBean trip;
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

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public double getTimeOutAmount() {
        return timeOutAmount;
    }

    public void setTimeOutAmount(double timeOutAmount) {
        this.timeOutAmount = timeOutAmount;
    }

    public TripBean getTrip() {
        return trip;
    }

    public void setTrip(TripBean trip) {
        this.trip = trip;
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

    public static class TripBean implements Serializable {
        /**
         * endLatitude : 31.847486
         * endLongitude : 117.25143
         * member : 276
         * orderId : 3007
         * startLatitude : 31.83021
         * startLongitude : 117.255021
         */

        private double endLatitude;
        private double endLongitude;
        private int member;
        private int orderId;
        private double startLatitude;
        private double startLongitude;

        public double getEndLatitude() {
            return endLatitude;
        }

        public void setEndLatitude(double endLatitude) {
            this.endLatitude = endLatitude;
        }

        public double getEndLongitude() {
            return endLongitude;
        }

        public void setEndLongitude(double endLongitude) {
            this.endLongitude = endLongitude;
        }

        public int getMember() {
            return member;
        }

        public void setMember(int member) {
            this.member = member;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public double getStartLatitude() {
            return startLatitude;
        }

        public void setStartLatitude(double startLatitude) {
            this.startLatitude = startLatitude;
        }

        public double getStartLongitude() {
            return startLongitude;
        }

        public void setStartLongitude(double startLongitude) {
            this.startLongitude = startLongitude;
        }
    }
}
