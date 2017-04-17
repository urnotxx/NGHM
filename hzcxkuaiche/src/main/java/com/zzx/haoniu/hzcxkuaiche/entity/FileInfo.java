package com.zzx.haoniu.hzcxkuaiche.entity;

/**
 * Created by Administrator on 2017/1/12.
 */

public class FileInfo {

    /**
     * id : 1430
     * name : 98d3dfb9-f8f6-417c-bdde-44301a2aeda9.jpg
     * path : uploadfile/2017/1/12/98d3dfb9-f8f6-417c-bdde-44301a2aeda9.jpg
     * size : 56
     * type : jpg
     * upload_time : 2017-01-12
     */

    private int id;
    private String name;
    private String path;
    private int size;
    private String type;
    private String upload_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
}
