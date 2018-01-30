package com.larapin.kendaliatap.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus on 24/12/2017.
 */

public class UserPreference {
    private String KEY_HOST = "host";
    private String KEY_MODE = "mode";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public UserPreference(Context context){
        String PREFS_NAME = "UserPref";
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setHost(String host){
        editor = preferences.edit();
        editor.putString(KEY_HOST, host);
        editor.apply();
    }

    public String getHost(){
        return preferences.getString(KEY_HOST, null);
    }

    public void setMode(String mode){
        editor = preferences.edit();
        editor.putString(KEY_MODE, mode);
        editor.apply();
    }

    public String getMode(){
        return preferences.getString(KEY_MODE, null);
    }
}
