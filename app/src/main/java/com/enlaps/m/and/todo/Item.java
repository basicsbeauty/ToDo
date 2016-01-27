package com.enlaps.m.and.todo;

import java.sql.Date;

/**
 * Created by vsatish on 1/26/2016.
 */
public class Item {

    public int      id;
    public String   title;
    public Date     dueDate;
    public String   priority;

    public Item( int id, String title, Date dueDate, String priority) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
    }
}
