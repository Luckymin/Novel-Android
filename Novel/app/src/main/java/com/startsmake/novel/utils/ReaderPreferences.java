package com.startsmake.novel.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.startsmake.novel.NovelApplication;

/**
 * User:Shine
 * Date:2015-10-12
 * Description:
 */
public class ReaderPreferences {
    private static final String PREFERENCES_NAME = "reader";
    private SharedPreferences mPref;

    public ReaderPreferences() {
        Context context = NovelApplication.getContext();
        mPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }



}
