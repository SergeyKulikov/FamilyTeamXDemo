package com.mycoloruniverse.familyteamx.view;

import android.content.Context;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public interface ITaskEditActivityView {
    TaskItemAdapter getTaskItemAdapter();

    void updateView();

    String getTaskTitle();

    int getStatus();

    boolean getDivideSum();

    int getType();

    FloatingActionButton getFab();

    Context getContext();

}
