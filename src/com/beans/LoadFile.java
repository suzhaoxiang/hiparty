package com.beans;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/4/19.
 */
@Entity
@Table(name="loadfile")
public class LoadFile {

    private String type;
    private String filename;
    private int id;
    private String fileUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        filename = filename;
    }
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        fileUrl = fileUrl;
    }

}
