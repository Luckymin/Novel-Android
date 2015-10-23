package com.startsmake.novel.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * User:Shine
 * Date:2015-10-22
 * Description:
 */
public class RevealBackgroundView extends View {

    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_FILL_STARTED = 1;
    public static final int STATE_FINISHED = 2;

    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private static final int FILL_TIME = 400;

    ObjectAnimator mRevealAnimator;

    private Paint mFillPaint;
    private int mCurrState = STATE_NOT_STARTED;
    private int mStartLocationX;
    private int mStartLocationY;
    private int mCurrentRadius;

    private OnStateChangeListener onStateChangeListener;

    public RevealBackgroundView(Context context) {
        this(context, null);
    }

    public RevealBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(Color.WHITE);
    }

    public void setFillPaintColor(int color) {
        mFillPaint.setColor(color);
    }


    public void startFromLocation(int[] tapLocationOnScreen) {
        changeState(STATE_FILL_STARTED);
        mStartLocationX = tapLocationOnScreen[0];
        mStartLocationY = tapLocationOnScreen[1];
        mRevealAnimator = ObjectAnimator.ofInt(this, "currentRadius", getWidth() + getHeight()).setDuration(FILL_TIME);
        mRevealAnimator.setInterpolator(INTERPOLATOR);
        mRevealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }
        });
        mRevealAnimator.start();
    }

    private void changeState(int state) {
        if (mCurrState == state) {
            return;
        }
        mCurrState = state;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(state);
        }
    }

    public void setToFinishedFrame() {
        changeState(STATE_FINISHED);
        invalidate();
    }

    public void setCurrentRadius(int radius) {
        this.mCurrentRadius = radius;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mCurrState == STATE_FINISHED) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mFillPaint);
        } else {
            canvas.drawCircle(mStartLocationX, mStartLocationY, mCurrentRadius, mFillPaint);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public static interface OnStateChangeListener {
        void onStateChange(int state);
    }
}
