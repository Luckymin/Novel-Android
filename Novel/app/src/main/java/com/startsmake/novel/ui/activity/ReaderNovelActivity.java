package com.startsmake.novel.ui.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.martian.libsliding.SlidingLayout;
import com.martian.libsliding.slider.OverlappedSlider;
import com.minxiaoming.novel.R;
import com.startsmake.novel.bean.db.ChapterContent;
import com.startsmake.novel.bean.db.NovelChapters;
import com.startsmake.novel.model.ReadingNovelModel;
import com.startsmake.novel.ui.adapter.NovelSlidingAdapter;
import com.startsmake.novel.ui.widget.ReaderTextView;
import com.startsmake.novel.utils.Utils;

import org.litepal.crud.DataSupport;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-08-19
 * Description:小说阅读界面  小说文字展示界面
 */
public class ReaderNovelActivity extends BaseActivity implements NovelSlidingAdapter.OnReadListener {

    private static final String EXTRA_NOVEL_ID = "com.minxiaoming.novel.novelID";
    private static final int DIRECTION_NEXT = 1;
    private static final int DIRECTION_PREVIOUS = 2;

    private String mNovelID;
    //小说阅读器，用于小说分页浏览
    private SlidingLayout mSlidingLayout;
    //阅读器的适配器,用于mSlidingLayout
    private NovelSlidingAdapter mAdapter;
    //监听时间变化
    private TimeTickReceiver mTimeTickReceiver;

    private ReaderTextView mTextView;

    private Handler mHandler;

    private boolean isLoadingNext;
    private boolean isLoadingPrevious;

    private final ReadingNovelModel.ReadingNovelCallback mReadingNovelCallback = new ReadingNovelModel.ReadingNovelCallback() {

        @Override
        public void onReadingChapterBodySuccess(final int startReadPosition, final int direction, final NovelChapters novelChapters) {

            View currentView = mAdapter.getCurrentView();
            final ReaderTextView readerTextView = (ReaderTextView) currentView.findViewById(R.id.tvChapterBody);

            if (readerTextView.getLayout() == null) {
                readerTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        readerTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        updateSlidingView(startReadPosition, direction, novelChapters, readerTextView);
                    }
                });
            } else {
                updateSlidingView(startReadPosition, direction, novelChapters, readerTextView);
            }

        }

        @Override
        public void onReadingChapterBodyError() {
            super.onReadingChapterBodyError();
        }

    };

    private void updateSlidingView(int startReadPosition, int direction, NovelChapters novelChapters, ReaderTextView readerTextView) {
        final ChapterContent chapterContent = novelChapters.getChapters().get(startReadPosition).getChapterContent();
        //得到当前要显示的章节完整的内容
        chapterContent.setPages(readerTextView.getPages(chapterContent.getBody()));
        mAdapter.setNovelChapter(novelChapters);

        if (direction == 0 || (direction == DIRECTION_NEXT && isLoadingNext)) {
            isLoadingNext = false;

            mAdapter.getUpdatedCurrentView();
            View nextView = mAdapter.getUpdatedNextView();
            if (nextView != null) {
                if (nextView.getParent() == null) {
                    mSlidingLayout.addView(nextView, 0);
                }
                nextView.scrollTo(0, 0);
            }
            View previousView = mAdapter.getUpdatedPreviousView();
            if (previousView != null) {
                if (previousView.getParent() == null) {
                    mSlidingLayout.addView(previousView);
                }
                previousView.scrollTo(Utils.getScreenWidth(), 0);
            }


        } else if (direction == DIRECTION_PREVIOUS && isLoadingPrevious) {
            isLoadingPrevious = false;

            mAdapter.getUpdatedCurrentView();
            mAdapter.getUpdatedPreviousView();
        }

//        if (novelChapters.getCurrChapterPosition() != startReadPosition) return;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reader_novel);
        initValue(savedInstanceState);
        initView();
    }

    private void initValue(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mNovelID = getIntent().getStringExtra(EXTRA_NOVEL_ID);
        } else {
            mNovelID = savedInstanceState.getString(EXTRA_NOVEL_ID);
        }
        mHandler = new Handler();
        mModel = new ReadingNovelModel();
        //注册广播监听器
        mTimeTickReceiver = new TimeTickReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//时间变化
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);//电量变化
        registerReceiver(mTimeTickReceiver, intentFilter);
    }

    private void initView() {
        mTextView = (ReaderTextView) findViewById(R.id.tvChapterBody);
        ((ViewGroup) mTextView.getParent()).setVisibility(View.INVISIBLE);

        mSlidingLayout = (SlidingLayout) findViewById(R.id.slidingNovelContainer);
        mSlidingLayout.setSlider(new OverlappedSlider()); // 左右覆盖滑动


        mAdapter = new NovelSlidingAdapter(this, this);
        mSlidingLayout.setAdapter(mAdapter);

        mModel.initNovelInfo(mNovelID, mReadingNovelCallback);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_NOVEL_ID, mNovelID);
    }

    /*加载下一页的章节内容*/
    @Override
    public void onLoadingNextChapter(final NovelChapters novelChapters) {
        if (isLoadingNext) return;
        isLoadingNext = true;
        final int index = novelChapters.getCurrChapterPosition() + 1;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mModel.readingChapterBodyByChapterLink(index, DIRECTION_NEXT, novelChapters, mReadingNovelCallback);
            }
        }, 2000);
    }

    /*加载上一页的章节内容*/
    @Override
    public void onLoadingPreviousChapter(final NovelChapters novelChapters) {
        if (isLoadingPrevious) return;
        isLoadingPrevious = true;
        final int index = novelChapters.getCurrChapterPosition() - 1;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mModel.readingChapterBodyByChapterLink(index, DIRECTION_PREVIOUS, novelChapters, mReadingNovelCallback);
            }
        }, 2000);
    }


    @Override
    protected void onStop() {
        super.onStop();

        NovelChapters novelChapters = mAdapter.getNovelChapters();

        if (novelChapters != null && novelChapters.getId() != 0) {
            ContentValues values = new ContentValues();
            values.put("currChapterPosition", novelChapters.getCurrChapterPosition());
            values.put("currPagePosition", novelChapters.getCurrPagePosition());

            int row = DataSupport.update(NovelChapters.class, values, novelChapters.getId());

            Timber.d("update row ---> %s , chapterPosition : %s , pagePosition : %s", row, novelChapters.getCurrChapterPosition(), novelChapters.getCurrPagePosition());

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeTickReceiver != null) {
            unregisterReceiver(mTimeTickReceiver);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    class TimeTickReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_TIME_TICK://时间变化
                    String currentTime = mAdapter.getCurrentTime();
                    setUpCurrentTime(mAdapter.getCurrentView(), currentTime);
                    setUpCurrentTime(mAdapter.getNextView(), currentTime);
                    setUpCurrentTime(mAdapter.getPreviousView(), currentTime);
                    break;
                case Intent.ACTION_BATTERY_CHANGED://电量变化
                    if (mAdapter == null || mAdapter.getNovelChapters() == null) {
                        break;
                    }
                    //获取当前电量
                    int level = intent.getIntExtra("level", 0);
                    mAdapter.setBatteryLevel(level);
                    break;
            }
        }
    }

    private void setUpCurrentTime(View view, String dateStr) {
        if (view == null) return;
        if (!(view.getTag() instanceof NovelSlidingAdapter.ViewHolder)) return;

        NovelSlidingAdapter.ViewHolder holder = (NovelSlidingAdapter.ViewHolder) view.getTag();
        holder.tvCurrentTime.setText(dateStr);
    }


    public static void openActivity(Context context, String novelID) {
        Intent intent = new Intent(context, ReaderNovelActivity.class);
        intent.putExtra(EXTRA_NOVEL_ID, novelID);
        context.startActivity(intent);
    }
}
