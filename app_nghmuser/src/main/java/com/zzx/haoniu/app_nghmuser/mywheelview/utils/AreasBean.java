package com.zzx.haoniu.app_nghmuser.mywheelview.utils;

import java.util.List;

public class AreasBean {

    private String level;
    private String parent_id;
    private String name;
    private String postcode;
    private String id;
    private List<AreasBean> subarea;
    private boolean isChecked;
    public boolean isChecked() {
        return isChecked;
    }
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setSubarea(List<AreasBean> subarea) {
        this.subarea = subarea;
    }
    public String getLevel() {
        return level;
    }
    public String getParent_id() {
        return parent_id;
    }
    public String getName() {
        return name;
    }
    public String getPostcode() {
        return postcode;
    }
    public String getId() {
        return id;
    }
    public List<AreasBean> getSubarea() {
        return subarea;
    }
}