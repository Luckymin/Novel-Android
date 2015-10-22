package com.startsmake.novel.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.startsmake.novel.ui.adapter.BaseLoadingAdapter;

/**
 * User:Shine
 * Date:2015-06-02
 * Description:监听到RecyclerView拉倒底部
 */
public class FooterRecyclerView extends RecyclerView {

    private boolean isRefreshEnabled;
    private OnRefreshEndListener mEndListener;


    public FooterRecyclerView(Context context) {
        super(context);
        init();
    }

    public FooterRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        isRefreshEnabled = true;
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }


    public void onScrolled(int dx, int dy) {
        if (isRefreshEnabled && !isRefresh()) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                linearLayoutScrolled(dy);
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutScrolled(dy);
            }


        }
    }

    private void linearLayoutScrolled(int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager != null && layoutManager != null) {
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            if (lastVisibleItem + 1 >= totalItemCount && dy > 0) {
                if (mEndListener != null) {
                    mEndListener.onEnd();
                }
                setRefresh(true);
            }
        }
    }

    private void StaggeredGridLayoutScrolled(int dy) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
        if (layoutManager != null && layoutManager != null) {

            int[] lastItemPositions = layoutManager.findLastCompletelyVisibleItemPositions(null);

            int lastVisibleItem = lastItemPositions[lastItemPositions.length - 1];
            int totalItemCount = layoutManager.getItemCount();

            if (lastVisibleItem + 1 >= totalItemCount && dy > 0) {
                if (mEndListener != null) {
                    mEndListener.onEnd();
                }
                setRefresh(true);
            }
        }
    }

    public void setRefresh(boolean refresh) {
        if (getAdapter() instanceof BaseLoadingAdapter) {
            BaseLoadingAdapter adapter = (BaseLoadingAdapter) getAdapter();
            adapter.setShowLoading(refresh);
        }
    }

    public boolean isRefresh() {
        if (getAdapter() instanceof BaseLoadingAdapter) {
            BaseLoadingAdapter adapter = (BaseLoadingAdapter) getAdapter();
            return adapter.isShowLoading();
        }
        return false;
    }

    public boolean isRefreshEnabled() {
        return isRefreshEnabled;
    }

    public void setRefreshEnabled(boolean enabled) {
        this.isRefreshEnabled = enabled;
    }

    public void setOnRefreshEndListener(OnRefreshEndListener endListener) {
        mEndListener = endListener;
    }

    public interface OnRefreshEndListener {
        void onEnd();
    }
}
