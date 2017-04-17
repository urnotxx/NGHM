package com.zzx.haoniu.nghmtaxi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/10.
 */

public class WebSocketInfo implements Serializable {

    /**
     * content : {"message":"{\"createTime\":1484038050000,\"reservationAddress\":\"安徽省合肥市蜀山区望江西路靠近市场星报社\",\"phone\":\"18655050310\",\"ygAmount\":11.00,\"otherCharges\":0.00,\"remark\":\"通过APP下单\",\"no\":\"SJDD20170110164730KCFMYN\",\"yhAmount\":0.00,\"setouttime\":1484038052000,\"type\":42,\"nick_name\":\"Lee\",\"id\":3008,\"amount\":0.00,\"baseAmount\":0.00,\"distance\":4.00,\"head_portrait\":\"uploadfile/2017/1/7/f73ae4d6-0a46-430f-8f72-412c57104d45.jpg\",\"realPay\":0.00,\"remoteFee\":0.00,\"trip\":{\"member\":276,\"startLongitude\":117.255014,\"endLatitude\":31.845687,\"startLatitude\":31.830208,\"endLongitude\":117.261385,\"orderId\":3008},\"roadToll\":0.00,\"fromType\":1,\"member\":276,\"realDistance\":0.00,\"status\":1,\"distanceAmount\":0.00,\"pdFlag\":false,\"timeOutAmount\":0.00,\"waitAmount\":0.00,\"serviceType\":3,\"destination\":\"绩溪路248号\",\"timeOut\":30,\"payStatus\":6,\"company\":2,\"setOutFlag\":true,\"delFlag\":false}","title":1}
     * type : 3
     */

    private ContentBean content;
    private int type;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class ContentBean {
        /**
         * message : {"createTime":1484038050000,"reservationAddress":"安徽省合肥市蜀山区望江西路靠近市场星报社","phone":"18655050310","ygAmount":11.00,"otherCharges":0.00,"remark":"通过APP下单","no":"SJDD20170110164730KCFMYN","yhAmount":0.00,"setouttime":1484038052000,"type":42,"nick_name":"Lee","id":3008,"amount":0.00,"baseAmount":0.00,"distance":4.00,"head_portrait":"uploadfile/2017/1/7/f73ae4d6-0a46-430f-8f72-412c57104d45.jpg","realPay":0.00,"remoteFee":0.00,"trip":{"member":276,"startLongitude":117.255014,"endLatitude":31.845687,"startLatitude":31.830208,"endLongitude":117.261385,"orderId":3008},"roadToll":0.00,"fromType":1,"member":276,"realDistance":0.00,"status":1,"distanceAmount":0.00,"pdFlag":false,"timeOutAmount":0.00,"waitAmount":0.00,"serviceType":3,"destination":"绩溪路248号","timeOut":30,"payStatus":6,"company":2,"setOutFlag":true,"delFlag":false}
         * title : 1
         */

        private String message;
        private int title;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getTitle() {
            return title;
        }

        public void setTitle(int title) {
            this.title = title;
        }
    }
}
