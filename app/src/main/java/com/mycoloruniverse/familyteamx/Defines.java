package com.mycoloruniverse.familyteamx;

public interface Defines {
    int TYPE_NON = -1;
    int TYPE_FREE_CONTENT = 0;
    int TYPE_MARKET = 1;
    int TYPE_UTILITIES = 2;
    int TYPE_TRAVEL_LIST = 3;

    int IDD_STATUS_NON = -100;
    int IDD_STATUS_CLOSED = -4;
    int IDD_STATUS_NOT_CLOSED = -3;
    int IDD_STATUS_NOT_CANCELED = -2;
    int IDD_STATUS_ALL = -1;
    int IDD_STATUS_PROGRESS = 0;
    int IDD_STATUS_DONE = 1;
    int IDD_STATUS_CANCELLED = 2;
    int IDD_CHOOSE_CONTACTS = 3;


    int IDD_TASK_ADD = 10;
    int IDD_TASK_EDIT = 11;
    int IDD_TASK_DEL = 12;

    int IDD_TASK_ITEM_ADD = 1;
    int IDD_TASK_ITEM_EDIT = 2;
    int IDD_TASK_ITEM_DEL = 3;

    String TASK_OBJECT = "task_obj";
    String TASK_ITEM_OBJECT = "task_item_obj";

    String TASK_TYPE = "task_type";
    String GUID = "guid";
}
