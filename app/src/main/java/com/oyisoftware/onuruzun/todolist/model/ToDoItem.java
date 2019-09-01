package com.oyisoftware.onuruzun.todolist.model;

public class ToDoItem {
    private int id ;
    private int toDoId ;
    private int userid ;
    private String itemName;
    private String descrp;
    private String deadline;
    private String createdAt;
    private boolean isCompleted;

    public final int getId() {
        return this.id;
    }

    public final void setId(int var1) {
        this.id = var1;
    }

    public final int getToDoId() {
        return this.toDoId;
    }

    public final void setToDoId(int var1) {
        this.toDoId = var1;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getDescrp() {
        return descrp;
    }

    public void setDescrp(String descrp) {
        this.descrp = descrp;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public final String getItemName() {
        return this.itemName;
    }

    public final void setItemName( String itemName) {
        this.itemName = itemName;
    }

    public final boolean isCompleted() {
        return this.isCompleted;
    }

    public final void setCompleted(boolean var1) {
        this.isCompleted = var1;
    }
}
