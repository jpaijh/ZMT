package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 短语的实体类
 */
public class PhraseEntity implements Serializable {

    private String text;
//    private boolean type;
    private int id;
//    private int  type;


    public PhraseEntity() {
    }

    public PhraseEntity(String text, int id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public String toString() {
        return "PhraseEntity{" +
                "text='" + text + '\'' +
                ", id=" + id +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
