package com.startsmake.novel.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * User:Shine
 * Date:2015-08-13
 * Description:
 */
public class NestedScrollView extends android.support.v4.widget.NestedScrollView {

    private OnScrollListener mOnScrollListener;

    public NestedScrollView(Context context) {
        super(context);
    }

    public NestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(x, y,oldX,oldY);
        }
    }


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public interface OnScrollListener {
        void onScroll(int x, int y,int oldX,int oldY);
    }

}
