package com.startsmake.novel.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.BookList;
import com.startsmake.novel.bean.db.Books;
import com.startsmake.novel.model.ThemeBookPagerModel;
import com.startsmake.novel.ui.activity.BookListDetailsActivity;
import com.startsmake.novel.ui.adapter.ThemeBookPagerAdapter;
import com.startsmake.novel.ui.widget.FooterRecyclerView;
import com.startsmake.novel.ui.widget.itemdecoration.LinearSpaceItemDecoration;
import com.startsmake.novel.utils.Constants;
import com.startsmake.novel.utils.Utils;

import java.util.List;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class ThemeBookPagerFragment extends BaseFragment implements ThemeBookPagerModel.ThemeBookPagerCallback, ThemeBookPagerAdapter.OnThemeBookItemClickListener {

    private static final String EXTRA_PAGER_TYPE = "com.startsmake.novel.pagerType";

    private int mPagerType;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FooterRecyclerView mRecyclerView;
    private ThemeBookPagerAdapter mAdapter;
    //从第几行开始获取数据
    private int mStartRow = 0;

    public static Fragment newInstance(int pagerType) {
        Fragment fragment = new ThemeBookPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PAGER_TYPE, pagerType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerType = getArguments().getInt(EXTRA_PAGER_TYPE);
        mModel = new ThemeBookPagerModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theme_book_pager, container, false);
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (FooterRecyclerView) view.findViewById(R.id.recyclerView);

        mSwipeRefreshLayout.setColorSchemeResources(Constants.COLOR_SCHEME_RESOURCES);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8), false));

        if (mAdapter == null) {
            mAdapter = new ThemeBookPagerAdapter(getActivity(), Glide.with(this), mPagerType);
            mAdapter.setOnThemeBookItemClickListener(this);
        }
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStartRow = 0;
                mModel.getThemeBookList(mPagerType, mStartRow, ThemeBookPagerFragment.this);
            }
        });
        mRecyclerView.setOnRefreshEndListener(new FooterRecyclerView.OnRefreshEndListener() {
            @Override
            public void onEnd() {
                mStartRow = mAdapter.getData().size();
                mModel.getThemeBookList(mPagerType, mStartRow, ThemeBookPagerFragment.this);
            }
        });

    }

    @Override
    protected void onViewInitializeVisible(boolean isVisibleHint) {
        mSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mSwipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mSwipeRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mSwipeRefreshLayout.setRefreshing(true);
                mModel.getThemeBookList(mPagerType, mStartRow, ThemeBookPagerFragment.this);
            }
        });
    }

    @Override
    public void getThemeBookListSuccess(int start, List<BookList> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (start == 0) {
            mAdapter.getData().clear();
        }
        boolean isLoading = data.size() > 0;
        if (isLoading) {
            mAdapter.addItems(data);
        }
        mRecyclerView.setRefreshEnabled(isLoading);
        if (mRecyclerView.isRefresh()) {
            mRecyclerView.setRefresh(false);
        }

    }

    @Override
    public void getThemeBookListError() {

    }

    @Override
    public void onClickEmptyItem() {

    }

    @Override
    public void onThemeBookItemClick(View itemView, View coverView, BookList bookList) {
        BookListDetailsActivity.openActivity(getActivity(), itemView, bookList);
    }

    @Override
    public void onItemMove(Books fromBook, Books toBook) {

    }

    @Override
    public void onItemDismiss(Books book) {

    }

}
