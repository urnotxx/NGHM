package com.zzx.haoniu.app_nghmuser.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/2.
 */

public class UserInfo implements Serializable {

    /**
     * address : 安徽省合肥市包河区万泉河路靠近临滨C座
     * age : 1
     * charge_standard : 0
     * city : 合肥市
     * company : 2
     * county : 包河区
     * from_type : 1
     * gender : 1
     * head_portrait : attachment/2016/11/28/5b7d1f8a-a938-49b7-b216-f7c8f1681b72.jpg
     * id : 52
     * introducer_login_id : 157
     * last_update_time : 2016-11-28 14:47:24
     * level : 1
     * login_id : 114
     * nick_name : 先生/女士
     * phone : 18756075099
     * province : 安徽省
     * real_name : 先生/女士
     * status : 1
     */

    private String address;
    private int age;
    private int charge_standard;
    private String city;
    private int company;
    private String county;
    private int from_type;
    private int gender;
    private String head_portrait;
    private int id;
    private int introducer_login_id;
    private String last_update_time;
    private int level;
    private int login_id;
    private String nick_name;
    private String phone;
    private String province;
    private String real_name;
    private int status;
    /**
     * email : 19840310@qq.com
     * is_ensure_carowner : 1
     * is_ensure_idcard : 1
     */

    private String email;
    private String is_ensure_carowner;
    private String is_ensure_idcard;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCharge_standard() {
        return charge_standard;
    }

    public void setCharge_standard(int charge_standard) {
        this.charge_standard = charge_standard;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getFrom_type() {
        return from_type;
    }

    public void setFrom_type(int from_type) {
        this.from_type = from_type;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_ensure_carowner() {
        return is_ensure_carowner;
    }

    public void setIs_ensure_carowner(String is_ensure_carowner) {
        this.is_ensure_carowner = is_ensure_carowner;
    }

    public String getIs_ensure_idcard() {
        return is_ensure_idcard;
    }

    public void setIs_ensure_idcard(String is_ensure_idcard) {
        this.is_ensure_idcard = is_ensure_idcard;
    }
}
