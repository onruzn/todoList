package com.oyisoftware.onuruzun.todolist.model;

import java.util.ArrayList;
import java.util.List;

public class ToDo {

    private int id;
    private String name;
    private String createdAt;
    private int userid ;
    private List items = (List)(new ArrayList());

    public final int getId() {
        return this.id;
    }

    public final void setId(int var1) {
        this.id = var1;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName( String name) {
        this.name = name;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public final List getItems() {
        return this.items;
    }

    public final void setItems( List items) {
        this.items = items;
    }
}
