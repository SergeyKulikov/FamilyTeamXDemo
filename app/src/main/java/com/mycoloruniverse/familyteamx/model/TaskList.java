package com.mycoloruniverse.familyteamx.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 21.12.2017.
 */

public class TaskList {
    private final List<Task> items = new ArrayList<>();

    public TaskList() {
        this.items.clear();
    }

    public List<Task> getItems() {
        return items;
    }

    public void setItems(List<Task> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
