package com.mycoloruniverse.familyteamx;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.google.android.material.tabs.TabItem;
import com.mycoloruniverse.familyteamx.model.Task;

@Database(entities = {Task.class, TabItem.class}, version = 1, exportSchema = false)
public abstract class FamilyTeamDatabase extends RoomDatabase {
    public abstract FamilyTeamDao getDaoDatabase();
}
