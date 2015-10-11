package com.startsmake.novel.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.StringRes;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.startsmake.novel.NovelApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * User:Shine
 * Date:2015-08-10
 * Description:
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    /**
     * dp转px
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * dp转px
     */
    public static float dpToPxF(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 得到屏幕高度
     */
    public static int getScreenHeight() {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) NovelApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    /**
     * 得到屏幕宽度
     */
    public static int getScreenWidth() {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) NovelApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    /**
     * 判断当前android版本是否大于或等于5.0
     */
    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 转码为utf-8
     */
    public static String encoderUTF8(String inStr) {
        try {
            return URLEncoder.encode(inStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return inStr;
    }

    /**
     * 解码utf-8
     */
    public static String decodeUTF8(String inStr) {
        if (inStr == null) {
            return "";
        }
        try {
            return URLDecoder.decode(inStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return inStr;
    }


    public static void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }


    public static String getString(@StringRes int resID) {
        return NovelApplication.getContext().getString(resID);
    }

}
