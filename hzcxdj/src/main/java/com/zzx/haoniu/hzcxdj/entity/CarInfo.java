package com.zzx.haoniu.hzcxdj.entity;

/**
 * Created by Administrator on 2017/1/11.
 */

public class CarInfo {

    /**
     * id : 123
     * plate_no : 皖A12345
     * color : 黄色
     * brand : 大众
     * model : 速腾
     */

    private int id;
    private String plate_no;
    private String color;
    private String brand;
    private String model;
    /**
     * create_time : 2017-01-17 18:57:49
     * real_name : 张正玄
     */

    private String create_time;
    private String real_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}
