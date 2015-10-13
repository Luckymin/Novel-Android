package com.startsmake.novel.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.NovelListBean;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.model.NovelListModel;
import com.startsmake.novel.ui.activity.NovelIntroActivity;
import com.startsmake.novel.ui.adapter.NovelListAdapter;
import com.startsmake.novel.ui.widget.FooterRecyclerView;
import com.startsmake.novel.ui.widget.itemdecoration.LinearSpaceItemDecoration;
import com.startsmake.novel.utils.Utils;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class NovelListFragment extends BaseFragment implements NovelListModel.NovelListCallback, FooterRecyclerView.OnRefreshEndListener, NovelListAdapter.OnNovelListItemClickListener {

    /*分类小说列表 全部*/
    public static final int TAB_TYPE_ALL = 1;
    /*分类小说列表 连载中*/
    public static final int TAB_TYPE_CONTINUED = 2;
    /*分类小说列表 已完结*/
    public static final int TAB_TYPE_OVER = 3;

    private static final String EXTRA_TAB_TYPE = "com.minxiaoming.novel.tabType";
    private static final String EXTRA_CLASSIFY_TAG = "com.minxiaoming.novel.classifyTag";
    /*进入动画延迟执行时间*/
    private static final int ENTER_START_DELAY = 400;
    /*进入动画持续时间*/
    private static final int ENTER_DURATION = 800;

    private Handler mHandler;
    //加载进度条
    private ProgressBar mPbLoading;
    private FooterRecyclerView mRvNovelList;
    private NovelListAdapter mAdapter;
    //tab页类型
    private int mTabType;
    //分类tag
    private String mClassifyTag;
    //当前小说条数
    private int mCurrStart = 0;
    private boolean mEnterAnimate = true;

    public static Fragment newInstance(int type, String classifyTag) {
        Fragment fragment = new NovelListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TAB_TYPE, type);
        bundle.putString(EXTRA_CLASSIFY_TAG, classifyTag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTabType = bundle.getInt(EXTRA_TAB_TYPE);
        mClassifyTag = bundle.getString(EXTRA_CLASSIFY_TAG);
        mModel = new NovelListModel();
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_novel_list, container, false);
    }


    @Override
    protected void onViewCreated(View view) {
        //实例化
        mRvNovelList = (FooterRecyclerView) view.findViewById(R.id.rvNovelList);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        //Adapter和RecyclerView
        mAdapter = new NovelListAdapter(getActivity(), Glide.with(this));
        mAdapter.setOnNovelListItemClickListener(this);
        mRvNovelList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvNovelList.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8)));
        mRvNovelList.setAdapter(mAdapter);
        mRvNovelList.setOnRefreshEndListener(this);
        mRvNovelList.setHasFixedSize(true);


    }

    @Override
    protected void onViewInitializeVisible(boolean isVisibleHint) {
        mModel.getNovelByTag(mClassifyTag, mTabType, mCurrStart, NovelListFragment.this);
    }


    @Override
    public void onNovelListSuccess(NovelListBean bean) {
        boolean isLoading = bean.getBooks().size() > 0;
        if (isLoading) {
            mAdapter.addItems(bean.getBooks());
        }
        if (mRvNovelList != null)
            mRvNovelList.setLoading(isLoading);
        if (mAdapter.isShowLoading())
            mAdapter.setShowLoading(false);
        if (mPbLoading.getVisibility() == View.VISIBLE) {
            mPbLoading.setVisibility(View.GONE);
        }
        runEnterAnimation(mRvNovelList);
    }

    /**
     * 获取小说列表错误
     */
    @Override
    public void onNovelListError() {
        if (mRvNovelList != null)
            mRvNovelList.setLoading(true);
        if (mAdapter.isShowLoading())
            mAdapter.setShowLoading(false);

    }

    /**
     * 进入动画，将view从屏幕外移动到初始位置
     *
     * @param view
     */
    private void runEnterAnimation(View view) {
        if (!mEnterAnimate) {
            return;
        }
        mEnterAnimate = false;
        int startDelay = mTabType == TAB_TYPE_ALL ? ENTER_START_DELAY : 0;
        view.setTranslationY(Utils.getScreenHeight());
        view.animate()
                .translationY(0)
                .setStartDelay(startDelay)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(ENTER_DURATION)
                .start();
    }

    /**
     * mRvNovelList啦到底部监听
     */
    @Override
    public void onEnd() {
        mAdapter.setShowLoading(true);
        mCurrStart += HttpConstant.NOVEL_LIST_LIMIT;
        mModel.getNovelByTag(mClassifyTag, mTabType, mCurrStart, this);
    }

    /**
     * 小说列表 item点击监听
     */
    @Override
    public void onNovelItemClick(View itemView, View coverView, NovelListBean.BooksEntity book) {
        NovelIntroActivity.openActivity(getActivity(), itemView, coverView, book.get_id(), book.getTitle(), book.getCover());
    }


    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}
