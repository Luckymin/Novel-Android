package com.startsmake.novel.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.startsmake.novel.Interfaces.OnInitTabLayoutCallback;
import com.startsmake.novel.R;
import com.startsmake.novel.ui.fragment.BookClassifyFragment;
import com.startsmake.novel.ui.fragment.BookshelfFragment;
import com.startsmake.novel.ui.fragment.BookRankingFragment;
import com.startsmake.novel.ui.fragment.ThemeBookListFragment;
import com.startsmake.novel.utils.Utils;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnInitTabLayoutCallback {

    public static final String EXTRA_SAVE_TAG = "com.startsmake.novel.saveCurrentTag";
    public static final String SAVE_IS_SHOW_CLASSIFY_TAB_LAYOUT = "com.startsmake.novel.saveIsShowClassifyTabLayout";
    public static final String SAVE_IS_SHOW_THEME_BOOK_TAB_LAYOUT = "com.startsmake.novel.saveIsShowThemeBookTabLayout";

    /**
     * 对应'书架'的Fragment的Index
     */
    private static final String TAG_PAGE_BOOKSHELF = Utils.getString(R.string.navigation_item_book_shelf);
    /**
     * 对应'分类'的Fragment的Index
     */
    private static final String TAG_PAGE_CLASSIFICATION = Utils.getString(R.string.navigation_item_classification);
    /**
     * 对应'排行榜'的Fragment的Index
     */
    private static final String TAG_PAGE_RANKING = Utils.getString(R.string.navigation_item_ranking);
    /**
     * 对应'排行榜'的Fragment的Index
     */
    private static final String TAG_PAGE_THEME_BOOK_LIST = Utils.getString(R.string.navigation_item_theme_book_list);

    /*当前显示的fragment的索引*/
    private String mCurrTag;
    /**/
    private boolean isShowClassifyTabLayout = false;
    private boolean isShowThemeBookTabLayout = false;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;
    private TabLayout mClassifyTabLayout;
    private TabLayout mThemeBookTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String tag = TAG_PAGE_BOOKSHELF;
        if (savedInstanceState != null) {
            mCurrTag = null;
            tag = savedInstanceState.getString(EXTRA_SAVE_TAG);
            isShowClassifyTabLayout = savedInstanceState.getBoolean(SAVE_IS_SHOW_CLASSIFY_TAB_LAYOUT);
            isShowThemeBookTabLayout = savedInstanceState.getBoolean(SAVE_IS_SHOW_THEME_BOOK_TAB_LAYOUT);
        }
        initToolbar();
        initInstances();
        hideAllFragment();
        gotoFragment(tag);
    }


    private void initInstances() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.navigation);
        mClassifyTabLayout = (TabLayout) findViewById(R.id.classifyTabLayout);
        mThemeBookTabLayout = (TabLayout) findViewById(R.id.themeBookListTabLayout);
        mClassifyTabLayout.setVisibility(isShowClassifyTabLayout ? View.VISIBLE : View.GONE);
        mThemeBookTabLayout.setVisibility(isShowThemeBookTabLayout ? View.VISIBLE : View.GONE);
        mNavigation.setNavigationItemSelectedListener(this);//侧边栏item选择监听
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, mToolbar, R.string.hello_world, R.string.hello_world);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    /**
     * 将tag对应fragment显示
     *
     * @param tag fragment对应标签
     */
    private void gotoFragment(String tag) {
        //如果抽屉是打开的，那边按下返回键时将关闭它
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isFragmentShown(transaction, tag)) {
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = getFragmentInstance(tag);
            transaction.add(R.id.contentFrame, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(tag);
    }

    /**
     * 判断要显示的fragment是否已经处于显示状态，不是的话会将之前的fragment隐藏
     *
     * @param transaction
     * @param newTag      要显示的fragment的标签
     * @return 已显示返回true, 否则返回false
     */
    private boolean isFragmentShown(FragmentTransaction transaction, String newTag) {
        if (newTag.equals(mCurrTag)) {
            return true;
        }
        if (TextUtils.isEmpty(mCurrTag)) {
            mCurrTag = newTag;
            return false;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mCurrTag);
        if (fragment != null && !fragment.isHidden()) {
            transaction.hide(fragment);
        }
        mCurrTag = newTag;
        return false;
    }

    /**
     * 根据tag得到fragment实例
     *
     * @param tag fragment对于标签
     * @return
     */
    public Fragment getFragmentInstance(String tag) {
        Fragment fragment = null;
        if (TextUtils.equals(tag, TAG_PAGE_BOOKSHELF)) {
            fragment = BookshelfFragment.newInstance();//bookshelf
        } else if (TextUtils.equals(tag, TAG_PAGE_CLASSIFICATION)) {
            fragment = BookClassifyFragment.newInstance();// classify
        } else if (TextUtils.equals(tag, TAG_PAGE_RANKING)) {
            fragment = BookRankingFragment.newInstance();// ranking
        } else if (TextUtils.equals(tag, TAG_PAGE_THEME_BOOK_LIST)) {
            fragment = ThemeBookListFragment.newInstance();
        }
        return fragment;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        menuItem.setChecked(true);
        switch (id) {
            case R.id.navigation_item_bookcase:
                isShowClassifyTabLayout = false;
                isShowThemeBookTabLayout = false;
                mClassifyTabLayout.setVisibility(View.GONE);
                mThemeBookTabLayout.setVisibility(View.GONE);
                gotoFragment(TAG_PAGE_BOOKSHELF);
                break;
            case R.id.navigation_item_classification:
                isShowClassifyTabLayout = true;
                isShowThemeBookTabLayout = false;
                mClassifyTabLayout.setVisibility(View.VISIBLE);
                mThemeBookTabLayout.setVisibility(View.GONE);
                gotoFragment(TAG_PAGE_CLASSIFICATION);
                break;
            case R.id.navigation_item_ranking:
                isShowClassifyTabLayout = false;
                isShowThemeBookTabLayout = false;
                mClassifyTabLayout.setVisibility(View.GONE);
                mThemeBookTabLayout.setVisibility(View.GONE);
                gotoFragment(TAG_PAGE_RANKING);
                break;
            case R.id.navigation_item_theme_book_list:
                isShowClassifyTabLayout = false;
                isShowThemeBookTabLayout = true;
                mClassifyTabLayout.setVisibility(View.GONE);
                mThemeBookTabLayout.setVisibility(View.VISIBLE);
                gotoFragment(TAG_PAGE_THEME_BOOK_LIST);
                break;
        }
        return false;

    }

    private void hideAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment bookRackFragment = getSupportFragmentManager().findFragmentByTag(TAG_PAGE_BOOKSHELF);
        if (bookRackFragment != null && !bookRackFragment.isHidden()) {
            transaction.hide(bookRackFragment);
        }
        Fragment classifyFragment = getSupportFragmentManager().findFragmentByTag(TAG_PAGE_CLASSIFICATION);
        if (classifyFragment != null && !classifyFragment.isHidden()) {
            transaction.hide(classifyFragment);
        }
        Fragment rankingFragment = getSupportFragmentManager().findFragmentByTag(TAG_PAGE_RANKING);
        if (rankingFragment != null && !rankingFragment.isHidden()) {
            transaction.hide(rankingFragment);
        }
        Fragment themeBookListFragment = getSupportFragmentManager().findFragmentByTag(TAG_PAGE_THEME_BOOK_LIST);
        if (themeBookListFragment != null && !themeBookListFragment.isHidden()) {
            transaction.hide(themeBookListFragment);
        }
        transaction.commit();
    }


    @Override
    protected void onClickHomeMenuItem() {
        if (mDrawerLayout != null && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);//打开抽屉
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //如果抽屉是打开的，那边按下返回键时将关闭它
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SAVE_TAG, mCurrTag);
        outState.putBoolean(SAVE_IS_SHOW_CLASSIFY_TAB_LAYOUT, isShowClassifyTabLayout);
    }

    @Override
    public void initialClassifyTabLayout(ViewPager viewPager) {
        mClassifyTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initialThemeBookListTabLayout(ViewPager viewPager) {
        mThemeBookTabLayout.setupWithViewPager(viewPager);
    }

    public void toggleDrawer() {
        if (mDrawerLayout == null) return;

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

}
