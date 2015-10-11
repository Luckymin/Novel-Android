package com.startsmake.novel.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.minxiaoming.novel.R;
import com.startsmake.novel.ui.adapter.TabLayoutViewPagerAdapter;
import com.startsmake.novel.ui.fragment.NovelListFragment;
import com.startsmake.novel.utils.Constants;
import com.startsmake.novel.utils.Utils;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class ClassifyAndRankingNovelListActivity extends BaseActivity {

    private static final String EXTRA_CLASSIFY_TAG = "com.minxiaoming.novel.classifyTag";
    private static final String EXTRA_CLASSIFY_STARTING_LOCATION = "com.minxiaoming.novel.startingLocation";

    /*Activity退出动画持续时间*/
    private static final int ENTER_DURATION = 400;

    private String mClassifyTag;
    //分类点击跳转item的x.y位置
    private int[] mStartingLocation = new int[2];

    private TabLayout mTabLayout;
    private ViewPager mVpNovelClassifyFiltrate;
    private ViewGroup mRootLayout;

    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_and_ranking_list);
        initValue(savedInstanceState);
        initToolbar();
        initView();
        setupWindowAnimations(savedInstanceState);
    }

    private void initValue(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mClassifyTag = getIntent().getStringExtra(EXTRA_CLASSIFY_TAG);
            mStartingLocation = getIntent().getIntArrayExtra(EXTRA_CLASSIFY_STARTING_LOCATION);
        } else {
            mClassifyTag = savedInstanceState.getString(EXTRA_CLASSIFY_TAG);
            mStartingLocation = savedInstanceState.getIntArray(EXTRA_CLASSIFY_STARTING_LOCATION);
        }
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mVpNovelClassifyFiltrate = (ViewPager) findViewById(R.id.vpNovelClassifyFiltrate);
        mRootLayout = (ViewGroup) findViewById(R.id.rootLayout);
        TabLayoutViewPagerAdapter adapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NovelListFragment.newInstance(NovelListFragment.TAB_TYPE_ALL, mClassifyTag), getString(R.string.classify_tab_all));
        adapter.addFragment(NovelListFragment.newInstance(NovelListFragment.TAB_TYPE_CONTINUED, mClassifyTag), getString(R.string.classify_tab_to_be_continued));
        adapter.addFragment(NovelListFragment.newInstance(NovelListFragment.TAB_TYPE_OVER, mClassifyTag), getString(R.string.classify_tab_is_over));
        mVpNovelClassifyFiltrate.setAdapter(adapter);
        mVpNovelClassifyFiltrate.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mVpNovelClassifyFiltrate);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations(Bundle savedInstanceState) {
        if (savedInstanceState != null) return;

        mRootLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRootLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                animateRevealShow(mRootLayout);
                return true;
            }
        });

    }

    /**
     * activity进入动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow(ViewGroup viewRoot) {
        int cx = mStartingLocation[0];
        int cy = mStartingLocation[1];
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        anim.setDuration(Constants.SHOW_ANIMATE_REVEAL_DURATION);
        anim.start();
    }

    /**
     * activity 退出动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealHide(final View view) {
        /*int initialRadius = viewRoot.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, mStartingLocation[0], mStartingLocation[1], initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRootLayout.setVisibility(View.INVISIBLE);
                ClassifyAndRankingNovelListActivity.super.finish();
                overridePendingTransition(0, 0);
            }
        });
        anim.setDuration(Constants.HIDE_ANIMATE_REVEAL_DURATION);
        anim.start();*/
        view.animate().translationY(Utils.getScreenHeight())
                .setDuration(ENTER_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ClassifyAndRankingNovelListActivity.super.finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle(mClassifyTag);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            animateRevealHide(mVpNovelClassifyFiltrate);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CLASSIFY_TAG, mClassifyTag);
        outState.putIntArray(EXTRA_CLASSIFY_STARTING_LOCATION, mStartingLocation);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void openActivity(Activity activity, View view, String tag) {
        Intent intent = new Intent(activity, ClassifyAndRankingNovelListActivity.class);
        int[] startingLocation = new int[2];
        view.getLocationOnScreen(startingLocation);

        startingLocation[0] += view.getWidth() / 2;
//        startingLocation[1] -= (view.getHeight() * 1.5);
        intent.putExtra(EXTRA_CLASSIFY_TAG, tag);
        intent.putExtra(EXTRA_CLASSIFY_STARTING_LOCATION, startingLocation);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity);
        ActivityCompat.startActivity(activity, intent, transitionActivityOptions.toBundle());
//        activity.startActivity(intent);
//        activity.overridePendingTransition(0,0);
    }


}