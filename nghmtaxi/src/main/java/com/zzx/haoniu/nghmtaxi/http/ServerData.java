package com.zzx.haoniu.nghmtaxi.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ServerData implements Serializable {

    private static final long serialVersionUID = 1L;
    //    private int result;//0失败 1成功
    private String msg;//是否成功
    /**
     * status : OK
     * data : null
     */

    private String status;
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
