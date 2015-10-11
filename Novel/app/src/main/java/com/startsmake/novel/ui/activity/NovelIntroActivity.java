package com.startsmake.novel.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.minxiaoming.novel.ActivityNovelIntroBinding;
import com.minxiaoming.novel.R;
import com.startsmake.novel.bean.NovelInfoBean;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.model.NovelIntroModel;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-08-13
 * Description:
 */
public class NovelIntroActivity extends BaseActivity implements NovelIntroModel.NovelIntroCallback,
        com.startsmake.novel.ui.widget.NestedScrollView.OnScrollListener, View.OnClickListener {

    public static final String EXTRA_NOVEL_ID = "com.minxiaoming.novel.novelID";
    public static final String EXTRA_NOVEL_NAME = "com.minxiaoming.novel.novelName";
    public static final String EXTRA_NOVEL_COVER_URL = "com.minxiaoming.novel.novelCoverUrl";

    /*小说id*/
    private String mNovelID;
    /*小说名称 书名*/
    private String mNovelName;
    /*小说封面图片的URL*/
    private String mNovelCoverUrl;
    /*scrollview y*/
    private int mCurrentScrollY;
    /*当前activity是否因为内存不足被销毁过,以此来判断退出当前activity时是否需要SharedElementReturnTransition*/
    private boolean isDestroy;

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
            mNovelID = getIntent().getStringExtra(EXTRA_NOVEL_ID);
            mNovelName = getIntent().getStringExtra(EXTRA_NOVEL_NAME);
            mNovelCoverUrl = getIntent().getStringExtra(EXTRA_NOVEL_COVER_URL);
        } else {
            isDestroy = true;
            mNovelID = savedInstanceState.getString(EXTRA_NOVEL_ID);
            mNovelName = savedInstanceState.getString(EXTRA_NOVEL_NAME);
            mNovelCoverUrl = savedInstanceState.getString(EXTRA_NOVEL_COVER_URL);
        }
//        mMaxScrollViewMarginTop = getResources().getDimensionPixelOffset(R.dimen.actionBarSize);
        mModel = new NovelIntroModel();
    }

    private void initView() {
        mDataBinding.btnStartReading.setOnClickListener(this);
        mDataBinding.scrollView.setOnScrollListener(this);
        Glide.with(this)
                .load(HttpConstant.URL_PICTURE + mNovelCoverUrl)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mDataBinding.ivNovelCover);
        mModel.getNovelInfoByID(mNovelID, this);
    }

    private void setupWindowAnimations() {
        scheduleStartPostponedTransition(mDataBinding.scrollView);
        Transition transition = getWindow().getSharedElementEnterTransition();
        transition.setDuration(400);
        transition.setStartDelay(200);
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
    }

    @Override
    public void getNovelInfoError() {

    }

    @Override
    public void onScroll(int x, int y, int oldX, int oldY) {
        mCurrentScrollY = y;

//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mDataBinding.scrollView.getLayoutParams();
//
//        int marginTop = mMaxScrollViewMarginTop + (oldY - y);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartReading:
                ReaderNovelActivity.openActivity(this, mNovelID);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle(mNovelName);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_NOVEL_ID, mNovelID);
        outState.putString(EXTRA_NOVEL_NAME, mNovelName);
        outState.putString(EXTRA_NOVEL_COVER_URL, mNovelCoverUrl);


    }


    @Override
    public void onBackPressed() {
        if (isDestroy || mCurrentScrollY >= mDataBinding.ivNovelCover.getBottom()) {
            getWindow().setSharedElementReturnTransition(new Fade());
        }
        super.onBackPressed();
    }


    public static void openActivity(Activity activity, View itemView, View coverView, String novelId, String novelName, String coverUrl) {
        Intent intent = new Intent(activity, NovelIntroActivity.class);
        intent.putExtra(EXTRA_NOVEL_ID, novelId);
        intent.putExtra(EXTRA_NOVEL_NAME, novelName);
        intent.putExtra(EXTRA_NOVEL_COVER_URL, coverUrl);


        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, coverView, activity.getString(R.string.transition_novel_cover));

        /*Pair<View, String> itemPair = Pair.create(itemView, activity.getString(R.string.transition_novel_list_item));
        Pair<View, String> coverPair = Pair.create(coverView, activity.getString(R.string.transition_novel_cover));
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, itemPair, coverPair);*/

        ActivityCompat.startActivity(activity, intent, transitionActivityOptions.toBundle());
    }
}
