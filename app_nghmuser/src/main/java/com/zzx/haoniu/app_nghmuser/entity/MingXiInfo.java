package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2016/12/26.
 */

public class MingXiInfo {

    /**
     * amount : 1234546.0
     * create_time : 2017-02-07 18:25:33
     * id : 2082
     * login_id : 448
     * no : 20170207182533uQAg
     * operater : 1
     * operaterTypeRemark : 后台充值
     * operation_type : 11
     * remark :
     * status : 1
     */

    private double amount;
    private String create_time;
    private int id;
    private int login_id;
    private String no;
    private int operater;
    private String operaterTypeRemark;
    private int operation_type;
    private String remark;
    private int status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getOperater() {
        return operater;
    }

    public void setOperater(int operater) {
        this.operater = operater;
    }

    public String getOperaterTypeRemark() {
        return operaterTypeRemark;
    }

    public void setOperaterTypeRemark(String operaterTypeRemark) {
        this.operaterTypeRemark = operaterTypeRemark;
    }

    public int getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(int operation_type) {
        this.operation_type = operation_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
