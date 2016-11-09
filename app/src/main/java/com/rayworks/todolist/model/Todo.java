package com.rayworks.todolist.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shirley on 11/9/16.
 */

public class Todo {
    @SerializedName("item")
    private String title;

    public Todo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
