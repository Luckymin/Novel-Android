package com.startsmake.novel.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User:Shine
 * Date:2015-08-25
 * Description:
 */
public class GsonUtils {

    public static <T> T fromJson(String json, Class<T> classOfT) {
        T t = null;
        try {
            t = new Gson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }
}
