package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/2/8.
 */

public class UserGuild {
    private String num, title, content;

    public UserGuild(String num, String title, String content) {
        this.num = num;
        this.title = title;
        this.content = content;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
