package com.jashasweejena.ideapad.model;

import android.media.Image;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Idea extends RealmObject {

    @PrimaryKey
    private long id; //Represents id column of the table. Not automatically increment-able coz Realm

    private String tag; //Represents if the entry is an app or web or something else.

    private String name; //Represents name of the project

//    private String desc; //Represents description of the said task.

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
}
