package com.startsmake.novel.Interfaces;

import android.support.v4.view.ViewPager;

/**
 * User:Shine
 * Date:2015-08-11
 * Description:用于分类fragment调用父activity中的方法
 */
public interface OnInitTabLayoutCallback {
    void initialClassifyTabLayout(ViewPager viewPager);
    void initialThemeBookListTabLayout(ViewPager viewPager);
}
