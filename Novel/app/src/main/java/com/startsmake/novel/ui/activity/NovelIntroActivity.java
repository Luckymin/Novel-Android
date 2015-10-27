package com.startsmake.novel.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.startsmake.novel.ActivityNovelIntroBinding;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.NovelInfoBean;
import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.model.NovelIntroModel;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-13
 * Description:
 */
public class NovelIntroActivity extends BaseActivity implements NovelIntroModel.NovelIntroCallback,
        com.startsmake.novel.ui.widget.NestedScrollView.OnScrollListener, View.OnClickListener {

    public static final String EXTRA_BOOKS = "com.startsmake.novel.books";

    private Book mBooks;
    /*scrollview y*/
    private int mCurrentScrollY;
    /*当前activity是否因为内存不足被销毁过,以此来判断退出当前activity时是否需要SharedElementReturnTransition*/
    private boolean isDestroy;
    /*是否已经加入书架*/
    private int mBookshelfID;

    private Handler mHandler;
    private MaterialDialog mBookshelfDialog;

    private ActivityNovelIntroBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_novel_intro);
        initToolbar();
        initValue(savedInstanceState);
        initView();
        setupWindowAnimations();


    }

    private void initValue(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            isDestroy = false;
            mBooks = getIntent().getParcelableExtra(EXTRA_BOOKS);
        } else {
            isDestroy = true;
            mBooks = savedInstanceState.getParcelable(EXTRA_BOOKS);
        }
        mBooks.setNovelID(mBooks.get_id());
        List<Book> bookshelfList = DataSupport.where("novelID = ?", mBooks.getNovelID()).find(Book.class);
        if (bookshelfList != null && bookshelfList.size() > 0) {
            mBookshelfID = bookshelfList.get(0).getId();
        }
        mHandler = new Handler();
        mModel = new NovelIntroModel();
    }

    private void initView() {
        mDataBinding.btnStartReading.setOnClickListener(this);
        mDataBinding.btnAddBookshelf.setOnClickListener(this);
        mDataBinding.scrollView.setOnScrollListener(this);

        mDataBinding.btnAddBookshelf.setText(mBookshelfID > 0 ? R.string.novel_remove_book_rack : R.string.novel_add_book_rack);

        mDataBinding.setBook(mBooks);

        mModel.getNovelInfoByID(mBooks.getNovelID(), this);
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleStartPostponedTransition(mDataBinding.scrollView);
            Transition transition = getWindow().getSharedElementEnterTransition();
            transition.setDuration(400);
            transition.setStartDelay(200);
        }
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });

    }

    @Override
    public void getNovelInfoSuccess(NovelInfoBean bean) {
        mDataBinding.setNovelInfo(bean);

        String coverUrl = HttpConstant.URL_PICTURE + bean.getCover().replaceAll("\\\\", "");
        Glide.with(this).load(coverUrl)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mDataBinding.ivNovelCover);

    }

    @Override
    public void getNovelInfoError() {

    }

    @Override
    public void onScroll(int x, int y, int oldX, int oldY) {
        mCurrentScrollY = y;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartReading:
                ReaderNovelActivity.openActivity(this, mBooks.getNovelID());
                break;
            case R.id.btnAddBookshelf:
                if (mBookshelfDialog == null) {
                    mBookshelfDialog = new MaterialDialog.Builder(this)
                            .content(R.string.dialog_message_wait)
                            .progress(true, 0)
                            .widgetColor(getResources().getColor(R.color.colorPrimary))
                            .build();
                    mBookshelfDialog.setCanceledOnTouchOutside(false);
                }
                mBookshelfDialog.show();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mBookshelfID > 0) {
                            DataSupport.delete(Book.class, mBookshelfID);
                            mBookshelfID = 0;
                            mDataBinding.btnAddBookshelf.setText(R.string.novel_add_book_rack);
                            Snackbar.make(mDataBinding.coordinatorLayout, "已从书架中移除《" + mBooks.getTitle() + "》", Snackbar.LENGTH_LONG).show();
                        } else {
                            mBooks.save();
                            mBooks.setOrderIndex(mBooks.getId());
                            ContentValues values = new ContentValues();
                            values.put("orderIndex", mBooks.getId());
                            DataSupport.update(Book.class, values, mBooks.getId());

                            mBookshelfID = mBooks.getId();
                            mDataBinding.btnAddBookshelf.setText(R.string.novel_remove_book_rack);
                            Snackbar.make(mDataBinding.coordinatorLayout, "已将《" + mBooks.getTitle() + "》添加到书架", Snackbar.LENGTH_LONG).show();
                        }
                        mBookshelfDialog.dismiss();
                    }
                }, 500);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle(mBooks.getTitle());
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_BOOKS, mBooks);
    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && (isDestroy || mCurrentScrollY >= mDataBinding.ivNovelCover.getBottom())) {
            getWindow().setSharedElementReturnTransition(new Fade());
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public static void openActivity(Activity activity, View itemView, View coverView, Book book) {
        Intent intent = new Intent(activity, NovelIntroActivity.class);
        intent.putExtra(EXTRA_BOOKS, book);


        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, coverView, activity.getString(R.string.transition_novel_cover));

        /*Pair<View, String> itemPair = Pair.create(itemView, activity.getString(R.string.transition_novel_list_item));
        Pair<View, String> coverPair = Pair.create(coverView, activity.getString(R.string.transition_novel_cover));
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, itemPair, coverPair);*/

        ActivityCompat.startActivity(activity, intent, transitionActivityOptions.toBundle());
    }
}
