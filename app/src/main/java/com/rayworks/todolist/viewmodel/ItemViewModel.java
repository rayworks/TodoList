package com.rayworks.todolist.viewmodel;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

/**
 * Created by Sean on 11/30/16.
 */

public class ItemViewModel implements ItemPresentationModel<String> {
    private String txt;

    public ItemViewModel() {
    }

    @Override
    public void updateData(String str, ItemContext context) {
        this.txt = str;
    }

    public String getTxt() {
        return txt;
    }
}
