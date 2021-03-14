package com.mycoloruniverse.familyteamx.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mycoloruniverse.familyteamx.Common;

@Entity
public class BaseRecord {
    @PrimaryKey
    @NonNull
    private String guid;
    private String title;

    public BaseRecord() {
        this.guid = Common.genGuid();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
