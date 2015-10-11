package com.startsmake.novel;

import com.minxiaoming.novel.BuildConfig;
import com.startsmake.novel.http.RequestManager;
import com.startsmake.novel.timber.CrashReportingTree;

import org.litepal.LitePalApplication;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-08-09
 * Description:
 */
public class NovelApplication extends LitePalApplication {

   /* private static NovelApplication sApplication;


    public NovelApplication() {
        sApplication = this;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        RequestManager.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }


    /*public static NovelApplication getContext() {
        return sApplication;
    }*/

}
