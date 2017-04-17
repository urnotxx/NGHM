package com.zzx.haoniu.nghmtaxi.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/2.
 */

public class UserInfo implements Serializable {


    /**
     * company : 2
     * creater : 36
     * gender : 1
     * id : 71
     * introducer_login_id : 115
     * last_update_time : 2016-12-13 14:17:16
     * login_id : 428
     * nick_name : 师傅
     * phone : 15385131053
     * real_name : 马国雷
     * status : 1
     * type : 4;1;2;3
     */

    private int company;
    private int creater;
    private int gender;
    private int id;
    private int introducer_login_id;
    private String last_update_time;
    private int login_id;
    private String nick_name;
    private String phone;
    private String real_name;
    private int status;
    private String type;
    /**
     * dj_re : 0
     * head_portrait : uploadfile/2017/1/20/89cd2aae-8a97-4bc7-9900-eb953bea0ddc.jpg
     * job_type : 0
     * kc_re : 0
     * texi_re : 0
     * zc_haohua_re : 0
     * zc_shangwu_re : 0
     * zc_sushi_re : 0
     */

    private int dj_re;
    private String head_portrait;
    private int job_type;
    private int kc_re;
    private int texi_re;
    private int zc_haohua_re;
    private int zc_shangwu_re;
    private int zc_sushi_re;

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIntroducer_login_id() {
        return introducer_login_id;
    }

    public void setIntroducer_login_id(int introducer_login_id) {
        this.introducer_login_id = introducer_login_id;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDj_re() {
        return dj_re;
    }

    public void setDj_re(int dj_re) {
        this.dj_re = dj_re;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public int getJob_type() {
        return job_type;
    }

    public void setJob_type(int job_type) {
        this.job_type = job_type;
    }

    public int getKc_re() {
        return kc_re;
    }

    public void setKc_re(int kc_re) {
        this.kc_re = kc_re;
    }

    public int getTexi_re() {
        return texi_re;
    }

    public void setTexi_re(int texi_re) {
        this.texi_re = texi_re;
    }

    public int getZc_haohua_re() {
        return zc_haohua_re;
    }

    public void setZc_haohua_re(int zc_haohua_re) {
        this.zc_haohua_re = zc_haohua_re;
    }

    public int getZc_shangwu_re() {
        return zc_shangwu_re;
    }

    public void setZc_shangwu_re(int zc_shangwu_re) {
        this.zc_shangwu_re = zc_shangwu_re;
    }

    public int getZc_sushi_re() {
        return zc_sushi_re;
    }

    public void setZc_sushi_re(int zc_sushi_re) {
        this.zc_sushi_re = zc_sushi_re;
    }
}
