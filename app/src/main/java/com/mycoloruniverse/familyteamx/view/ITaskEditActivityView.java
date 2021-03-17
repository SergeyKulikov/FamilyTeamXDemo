package com.mycoloruniverse.familyteamx.view;

public interface ITaskEditActivityView {
    TaskItemAdapter getTaskItemAdapter();

    void updateView();

    String getTaskTitle();

    int getStatus();

    boolean getDivideSum();

    int getType();
}
