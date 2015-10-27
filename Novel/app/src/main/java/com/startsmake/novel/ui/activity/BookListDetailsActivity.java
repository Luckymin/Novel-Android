package com.startsmake.novel.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.BookListDetailsBean;
import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.model.BookListDetailsModel;
import com.startsmake.novel.ui.adapter.BookListDetailsAdapter;
import com.startsmake.novel.ui.widget.itemdecoration.LinearSpaceItemDecoration;
import com.startsmake.novel.utils.Utils;

/**
 * User:Shine
 * Date:2015-10-22
 * Description:
 */
public class BookListDetailsActivity extends BaseActivity implements BookListDetailsModel.BookListDetailsListener {

    public static final String EXTRA_BOOK_LIST_ID = "com.startsmake.novel.bookListID";

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingProgress;
    private BookListDetailsAdapter mAdapter;
    private String mBookListID;
    private View mLlLoadingFailure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_details);
        initValue(savedInstanceState);
        initToolbar();
        initView();

    }

    private void initValue(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mBookListID = getIntent().getStringExtra(EXTRA_BOOK_LIST_ID);
        } else {
            mBookListID = savedInstanceState.getString(EXTRA_BOOK_LIST_ID);
        }
        mModel = new BookListDetailsModel();
    }

    private void initView() {
        mLlLoadingFailure = findViewById(R.id.llLoadingFailure);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pbLoading);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8), false, false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BookListDetailsAdapter(this, Glide.with(this));
        mAdapter.setOnNovelItemClickListener(new BookListDetailsAdapter.OnNovelItemClickListener() {
            @Override
            public void onNovelClick(View view, View coverView, Book book) {
                NovelIntroActivity.openActivity(BookListDetailsActivity.this, view, coverView, book);
            }

            @Override
            public void onCollectClick() {

            }

        });
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgress.setVisibility(View.VISIBLE);
                mLlLoadingFailure.setVisibility(View.GONE);
                mModel.getBookListDetails(mBookListID, BookListDetailsActivity.this);
            }
        });

        mModel.getBookListDetails(mBookListID, this);
    }

    @Override
    public void getBookListDetailsSuccess(BookListDetailsBean response) {
        mRecyclerView.setBackgroundResource(R.color.base_layout_color);
        mLoadingProgress.setVisibility(View.GONE);
        mAdapter.setDetails(response);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void getBookListDetailsError() {
        mLoadingProgress.setVisibility(View.GONE);
        mLlLoadingFailure.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle(R.string.book_list_details_text);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_BOOK_LIST_ID, mBookListID);
    }

    @Override
    public void onBackPressed() {
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mAdapter.cleanItem();
        super.onBackPressed();
    }

    public static void openActivity(Activity activity, View fromView, View titleView, String bookListID) {
        Intent intent = new Intent(activity, BookListDetailsActivity.class);
        intent.putExtra(EXTRA_BOOK_LIST_ID, bookListID);

        String fromViewName = activity.getString(R.string.transition_book_list_item);
//        ViewCompat.setTransitionName(fromView, fromViewName);

        String titleViewName = activity.getString(R.string.transition_book_list_title);
        ViewCompat.setTransitionName(titleView, titleViewName);


        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(fromView, fromViewName), Pair.create(titleView, titleViewName));

        ActivityCompat.startActivity(activity, intent, options.toBundle());
        activity.overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
    }
}
