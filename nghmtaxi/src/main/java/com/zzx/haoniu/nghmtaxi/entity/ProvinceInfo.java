package com.zzx.haoniu.nghmtaxi.entity;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ProvinceInfo {

    /**
     * adcode : 110000
     * citycode : 010
     * id : 3540
     * level : province
     * name : 北京市
     * parent : 0
     */

    private String adcode;
    private String citycode;
    private int id;
    private String level;
    private String name;
    private String parent;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
