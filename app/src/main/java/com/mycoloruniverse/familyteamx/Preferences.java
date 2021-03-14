package com.mycoloruniverse.familyteamx;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Sergey on 5/6/2018.
 */

public class Preferences implements Defines {
    private static SharedPreferences preferences;

    Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    static void setContext(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getCurrencyDefault() {
        return preferences.getString("pref_currency_default", "USD").substring(0, 3);
    }

    public static boolean isMultiCurrency() {
        return preferences.getBoolean("pref_using_different_currency", false);
    }

    public static int getContentTypeDefault() {
        return preferences.getInt("pref_content_default", TYPE_FREE_CONTENT);
    }

    static boolean setUserGuid(String guid) {
        return preferences.edit().putString("user_fteam_guid", guid).commit();
    }

    public static String getUserGuid() {
        String currentGuid = preferences.getString("user_fteam_guid", "");
        if (currentGuid.equals("")) {
            String guid = Common.genGuid();
            if (setUserGuid(guid)) {
                currentGuid = guid;
            }
        }
        return currentGuid;
    }


}
