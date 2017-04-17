package com.zzx.haoniu.app_nghmuser.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/11.
 */

public class OrderInfo implements Serializable {
    /**
     * amount : 8.0
     * car : {"brand":52,"color":"白色","id":113,"modelType":73,"picture":"attachment/2016/12/12/78c45256-8e90-44ff-b6b3-a798e4deecb6.jpg","plateNo":"皖C111111","status":1,"type":42,"vin":"v68768778866"}
     * destination : 三里庵街道社区卫生服务中心
     * driverId : 71
     * gender : 0
     * latitude : 0.0
     * longitude : 0.0
     * nickName : 马国雷
     * orderId : 3041
     * payChannel : -1
     * phone : 15385131053
     * realPay : 14.0
     * reservationAddress : 市场星报社
     * status : 4
     * trip : {"endLatitude":31.847043,"endLongitude":117.251852,"endTime":1484190581000,"member":276,"orderId":3041,"startLatitude":31.830224,"startLongitude":117.255028,"startTime":1484190529000}
     */
    private double amount;
    private CarBean car;
    private String destination;
    private int driverId;
    private int gender;
    private String latitude;
    private String longitude;
    private String nickName;
    private int orderId;
    private int payChannel;
    private String phone;
    private double realPay;
    private String reservationAddress;
    private int status;
    private int orderType;
    private TripBean trip;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CarBean getCar() {
        return car;
    }

    public void setCar(CarBean car) {
        this.car = car;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRealPay() {
        return realPay;
    }

    public void setRealPay(double realPay) {
        this.realPay = realPay;
    }

    public String getReservationAddress() {
        return reservationAddress;
    }

    public void setReservationAddress(String reservationAddress) {
        this.reservationAddress = reservationAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TripBean getTrip() {
        return trip;
    }

    public void setTrip(TripBean trip) {
        this.trip = trip;
    }

    public static class CarBean implements Serializable {
        /**
         * brand : 52
         * color : 白色
         * id : 113
         * modelType : 73
         * picture : attachment/2016/12/12/78c45256-8e90-44ff-b6b3-a798e4deecb6.jpg
         * plateNo : 皖C111111
         * status : 1
         * type : 42
         * vin : v68768778866
         */

        private int brand;
        private String color;
        private int id;
        private int modelType;
        private String picture;
        private String plateNo;
        private int status;
        private int type;
        private String vin;

        public int getBrand() {
            return brand;
        }

        public void setBrand(int brand) {
            this.brand = brand;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getModelType() {
            return modelType;
        }

        public void setModelType(int modelType) {
            this.modelType = modelType;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }
    }

    public static class TripBean implements Serializable {
        /**
         * endLatitude : 31.847043
         * endLongitude : 117.251852
         * endTime : 1484190581000
         * member : 276
         * orderId : 3041
         * startLatitude : 31.830224
         * startLongitude : 117.255028
         * startTime : 1484190529000
         */

        private double endLatitude;
        private double endLongitude;
        private long endTime;
        private int member;
        private int orderId;
        private double startLatitude;
        private double startLongitude;
        private long startTime;

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

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
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

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
    }
}
