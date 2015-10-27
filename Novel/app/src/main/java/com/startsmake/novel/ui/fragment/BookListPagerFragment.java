package com.startsmake.novel.ui.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.BookList;
import com.startsmake.novel.helper.OnStartDragListener;
import com.startsmake.novel.helper.SimpleItemTouchHelperCallback;
import com.startsmake.novel.model.ThemeBookPagerModel;
import com.startsmake.novel.ui.activity.BookListDetailsActivity;
import com.startsmake.novel.ui.adapter.BookListPagerAdapter;
import com.startsmake.novel.ui.widget.FooterRecyclerView;
import com.startsmake.novel.ui.widget.itemdecoration.LinearSpaceItemDecoration;
import com.startsmake.novel.utils.Constants;
import com.startsmake.novel.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class BookListPagerFragment extends BaseFragment implements ThemeBookPagerModel.ThemeBookPagerCallback,
        BookListPagerAdapter.OnThemeBookItemClickListener,OnStartDragListener {

    public static final String BROADCAST_ACTION_MY_COLLECCT = "com.startsmake.novel.actionMyCollect";

    private static final String EXTRA_PAGER_TYPE = "com.startsmake.novel.pagerType";
    private int mPagerType;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FooterRecyclerView mRecyclerView;
    private BookListPagerAdapter mAdapter;
    //从第几行开始获取数据
    private int mStartRow = 0;
    private ItemTouchHelper mItemTouchHelper;
    private ViewPager mParentViewPager;
    private BroadcastReceiver mReceiver;

    public static Fragment newInstance(int pagerType) {
        Fragment fragment = new BookListPagerFragment();
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
        if (mPagerType == ThemeBookListFragment.PAGER_TYPE_MY_COLLECT) {
            mReceiver = new CollectBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION_MY_COLLECCT);
            getActivity().registerReceiver(mReceiver, intentFilter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container instanceof ViewPager) {
            mParentViewPager = (ViewPager) container;
        }
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
        mRecyclerView.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8), false, mPagerType != ThemeBookListFragment.PAGER_TYPE_MY_COLLECT));

        if (mAdapter == null) {
            mAdapter = new BookListPagerAdapter(getActivity(), Glide.with(this), mPagerType);
            mAdapter.setOnThemeBookItemClickListener(this);
        }

        if (mPagerType == ThemeBookListFragment.PAGER_TYPE_MY_COLLECT) {
            mRecyclerView.setRefreshEnabled(false);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);

            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStartRow = 0;
                mModel.getThemeBookList(mPagerType, mStartRow, BookListPagerFragment.this);
            }
        });
        mRecyclerView.setOnRefreshEndListener(new FooterRecyclerView.OnRefreshEndListener() {
            @Override
            public void onEnd() {
                mStartRow = mAdapter.getData().size();
                mModel.getThemeBookList(mPagerType, mStartRow, BookListPagerFragment.this);
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
                mModel.getThemeBookList(mPagerType, mStartRow, BookListPagerFragment.this);
            }
        });
    }

    @Override
    public void getThemeBookListSuccess(int start, List<BookList> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (start == 0) {
            mAdapter.cleanData();
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
        if (mSwipeRefreshLayout == null || mRecyclerView == null) return;

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mRecyclerView.isRefresh()) {
            mRecyclerView.setRefresh(false);
        }
        Snackbar.make(mSwipeRefreshLayout, R.string.network_failure_hint_text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClickEmptyItem() {
        if (mParentViewPager != null) {
            mParentViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onThemeBookItemClick(View itemView, View titleView, BookList bookList) {
        BookListDetailsActivity.openActivity(getActivity(), itemView, bookList);
    }

    @Override
    public void onItemMove(BookList fromBook, BookList toBook) {
        int fromOrderIndex = fromBook.getOrderIndex();
        fromBook.setOrderIndex(toBook.getOrderIndex());
        toBook.setOrderIndex(fromOrderIndex);

        ContentValues fromValues = new ContentValues();
        fromValues.put("orderIndex", fromBook.getOrderIndex());

        ContentValues toValues = new ContentValues();
        toValues.put("orderIndex", toBook.getOrderIndex());

        DataSupport.update(BookList.class, fromValues, fromBook.getId());
        DataSupport.update(BookList.class, toValues, toBook.getId());
    }

    @Override
    public void onItemDismiss(BookList book) {
        book.delete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    class CollectBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<BookList> bookLists = DataSupport.findAll(BookList.class);
            mAdapter.cleanData();
            mAdapter.addItems(bookLists);
        }
    }

    public static void sendBroadcast(Context context) {
        Intent intent = new Intent(BROADCAST_ACTION_MY_COLLECCT);
        context.sendBroadcast(intent);
    }

}
