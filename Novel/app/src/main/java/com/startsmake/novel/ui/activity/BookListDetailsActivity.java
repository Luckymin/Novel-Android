package com.startsmake.novel.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.BookListDetailsBean;
import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.bean.db.BookList;
import com.startsmake.novel.model.BookListDetailsModel;
import com.startsmake.novel.ui.adapter.BookListDetailsAdapter;
import com.startsmake.novel.ui.fragment.BookListPagerFragment;
import com.startsmake.novel.ui.widget.itemdecoration.LinearSpaceItemDecoration;
import com.startsmake.novel.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.List;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-10-22
 * Description:
 */
public class BookListDetailsActivity extends BaseActivity implements BookListDetailsModel.BookListDetailsListener {

    public static final String EXTRA_BOOK_LIST = "com.startsmake.novel.bookList";

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingProgress;
    private BookListDetailsAdapter mAdapter;
    private BookList mBookList;
    private View mLlLoadingFailure;
    private MaterialDialog mCollectDialog;
    private Handler mHandler;

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
            mBookList = getIntent().getParcelableExtra(EXTRA_BOOK_LIST);
        } else {
            mBookList = savedInstanceState.getParcelable(EXTRA_BOOK_LIST);
        }
        mHandler = new Handler();
        mModel = new BookListDetailsModel();
        List<BookList> bookLists = DataSupport.where("bookListID = ?", mBookList.getBookListID()).find(BookList.class);
        if (bookLists != null && bookLists.size() > 0) {
            mBookList.setId(bookLists.get(0).getId());
        }
    }

    private void initView() {
        mLlLoadingFailure = findViewById(R.id.llLoadingFailure);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pbLoading);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8), false, false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BookListDetailsAdapter(this, mBookList, Glide.with(this));
        mAdapter.setOnNovelItemClickListener(new BookListDetailsAdapter.OnNovelItemClickListener() {
            @Override
            public void onNovelItemClick(View view, View coverView, Book book) {
                NovelIntroActivity.openActivity(BookListDetailsActivity.this, view, coverView, book);
            }

            @Override
            public void onCollectClick(final Button btnCollect) {
                if (mCollectDialog == null) {
                    mCollectDialog = new MaterialDialog.Builder(BookListDetailsActivity.this)
                            .content(R.string.dialog_message_wait)
                            .progress(true, 0)
                            .widgetColor(getResources().getColor(R.color.colorPrimary))
                            .build();
                    mCollectDialog.setCanceledOnTouchOutside(false);
                }
                mCollectDialog.show();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mBookList.getId() > 0) {//取消收藏操作
                            int i = DataSupport.delete(BookList.class, mBookList.getId());
                            Timber.d("数据库删除操作 --> 取消收藏 : %s", i);

                            mBookList.setId(0);
                        } else {//添加收藏操作
                            boolean isSuccess = mBookList.save();
                            ContentValues values = new ContentValues();
                            values.put("orderIndex", mBookList.getId());
                            DataSupport.update(Book.class, values, mBookList.getId());

                            Timber.d("数据库添加操作 --> 收藏 : %s", isSuccess);
                        }
                        BookListPagerFragment.sendBroadcast(BookListDetailsActivity.this);
                        mAdapter.setCollectBtnView(btnCollect);
                        mCollectDialog.dismiss();
                    }
                }, 1000);

            }

        });
        mRecyclerView.setAdapter(mAdapter);
        //网络失败 重试按钮
        findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgress.setVisibility(View.VISIBLE);
                mLlLoadingFailure.setVisibility(View.GONE);
                mModel.getBookListDetails(mBookList.getBookListID(), BookListDetailsActivity.this);
            }
        });


        mModel.getBookListDetails(mBookList.getBookListID(), this);
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
        outState.putParcelable(EXTRA_BOOK_LIST, mBookList);
    }

    @Override
    public void onBackPressed() {
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mAdapter.cleanItem();
        super.onBackPressed();
    }

    public static void openActivity(Activity activity, View fromView, BookList bookList) {
        if (TextUtils.isEmpty(bookList.getBookListID())) {
            bookList.setBookListID(bookList.get_id());
        }

        Intent intent = new Intent(activity, BookListDetailsActivity.class);
        intent.putExtra(EXTRA_BOOK_LIST, bookList);

        String fromViewName = activity.getString(R.string.transition_book_list_item);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(fromView, fromViewName));

        ActivityCompat.startActivity(activity, intent, options.toBundle());
        activity.overridePendingTransition(R.anim.slide_up, R.anim.scale_down);

    }
}
