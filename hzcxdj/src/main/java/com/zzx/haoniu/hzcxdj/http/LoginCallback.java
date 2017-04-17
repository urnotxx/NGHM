package com.zzx.haoniu.hzcxdj.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/5.
 */

public class LoginCallback implements Serializable {

    /**
     * appType : 2
     * loginId : 114
     * secret : matiqrbiv30xkbk03zbffu9uvgegwngn
     * token : usr2kafbobrhhilitzsplgfiaaxvtcb7
     */

    private String appType;
    private String loginId;
    private String secret;
    private String token;
    private String carId;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
