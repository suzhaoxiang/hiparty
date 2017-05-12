package com.beans;

import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/4/16.
 */
@Entity
@Table(name="gamewords")
public class GameWords  {
    /**
     * 词组类型
     */
    private String type;
    /**
     * 词组
     */
    private String word;
    /**
     * 虚拟主键
     */
    private int id;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
