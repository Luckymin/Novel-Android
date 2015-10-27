package com.startsmake.novel.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startsmake.novel.Interfaces.OnInitTabLayoutCallback;
import com.startsmake.novel.R;
import com.startsmake.novel.ui.adapter.TabLayoutViewPagerAdapter;

/**
 * User:Shine
 * Date:2015-10-19
 * Description:
 */
public class ThemeBookListFragment extends BaseFragment {


    public static final int PAGER_TYPE_MY_COLLECT = 1;
    public static final int PAGER_TYPE_HOT = 2;
    public static final int PAGER_TYPE_LATEST_POSTS = 3;
    public static final int PAGER_TYPE_MOST_COLLECT = 4;

    private ViewPager mViewPager;
    private TabLayoutViewPagerAdapter mPagerAdapter;

    public static Fragment newInstance() {
        Fragment fragment = new ThemeBookListFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theme_book_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mPagerAdapter = new TabLayoutViewPagerAdapter(getActivity().getFragmentManager());
        mPagerAdapter.addFragment(BookListPagerFragment.newInstance(PAGER_TYPE_MY_COLLECT), getString(R.string.theme_book_tab_name_my_collect));
        mPagerAdapter.addFragment(BookListPagerFragment.newInstance(PAGER_TYPE_HOT), getString(R.string.theme_book_tab_name_hot));
        mPagerAdapter.addFragment(BookListPagerFragment.newInstance(PAGER_TYPE_LATEST_POSTS), getString(R.string.theme_book_tab_name_latest_posts));
        mPagerAdapter.addFragment(BookListPagerFragment.newInstance(PAGER_TYPE_MOST_COLLECT), getString(R.string.theme_book_tab_name_most_collect));
        mViewPager.setAdapter(mPagerAdapter);

        ((OnInitTabLayoutCallback) getActivity()).initialThemeBookListTabLayout(mViewPager);

    }

}
