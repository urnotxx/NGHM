package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/3/22.
 */

public class WindCarInfo {
    private String name, time, startPlace, endPlace;
    private int score, state, money;

    public WindCarInfo(String name, String time, String startPlace, String endPlace, int score, int state, int money) {
        this.name = name;
        this.time = time;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.score = score;
        this.state = state;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
