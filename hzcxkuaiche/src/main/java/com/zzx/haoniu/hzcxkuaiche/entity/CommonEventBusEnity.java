package com.zzx.haoniu.hzcxkuaiche.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CommonEventBusEnity implements Serializable {
    private String sendCode;
    private Map<String, Object> map = new HashMap<>();
    private String message;

    public CommonEventBusEnity(String type, String message) {
        this.sendCode = type;
        this.message = message;
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
