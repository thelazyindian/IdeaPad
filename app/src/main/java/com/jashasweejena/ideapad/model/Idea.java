package com.jashasweejena.ideapad.model;

import android.graphics.Bitmap;
import android.media.Image;

import com.rm.freedrawview.FreeDrawSerializableState;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Idea extends RealmObject {

    @PrimaryKey
    private long id; //Represents id column of the table. Not automatically increment-able coz Realm

    private String name; //Represents name of the project

    private String desc; //Represents description of the project.

    private byte[] drawing; //Represents the bitmap of drawing


    public byte[] getDrawing() {
        return drawing;
    }

    public void setDrawing(byte[] drawing) {
        this.drawing = drawing;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() { return desc; }

    public void setDesc(String desc) { this.desc = desc; }

}
