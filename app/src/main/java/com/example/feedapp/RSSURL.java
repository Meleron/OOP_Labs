package com.example.feedapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class RSSURL {
    private SharedPreferences preferences;
    private String defaultURL;
    private String rss;

    RSSURL(Activity activity){
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        defaultURL = activity.getResources().getString(R.string.default_url);
        rss = activity.getResources().getString(R.string.rss);
    }

    String get(){
        return preferences.getString(rss, defaultURL);
    }

    void set(String url){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(rss, url);
        editor.apply();
    }
}
