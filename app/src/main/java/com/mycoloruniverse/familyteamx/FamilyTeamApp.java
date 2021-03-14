package com.mycoloruniverse.familyteamx;

import android.app.Application;

import androidx.room.Room;

public class FamilyTeamApp extends Application {
    public static FamilyTeamApp instance;
    private FamilyTeamDatabase familyTeamDatabase;

    public static FamilyTeamApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        familyTeamDatabase = Room.databaseBuilder(getApplicationContext(), FamilyTeamDatabase.class, "DaoDatabase_db").
                // .allowMainThreadQueries().
                        build();

        Preferences.setContext(getApplicationContext());
    }

    public FamilyTeamDatabase getDao() {
        return familyTeamDatabase;
    }

}
