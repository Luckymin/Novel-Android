package com.startsmake.novel.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.minxiaoming.novel.R;
import com.startsmake.novel.utils.Utils;

/**
 * User:Shine
 * Date:2015-09-29
 * Description:显示电池电量的view
 */
public class BatteryView extends View {

    private int mPower = 100;

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //电池头 宽高
        int headerWidth = width / 8;
        int headerHeight = height / 2;

        //外边框的界限
        int batteryLeft = 0;
        int batteryTop = 0;
        int batteryRight = width - headerWidth;
        int batteryBottom = height;
        //内部电量的外边距
        int insideMargin = (int) Utils.dpToPxF(1.5f);

        //画外边框
        Paint batteryPaint = new Paint();
        batteryPaint.setColor(getResources().getColor(R.color.battery_view_color));


        batteryPaint.setAntiAlias(true);
        batteryPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(batteryLeft, batteryTop, batteryRight, batteryBottom, batteryPaint);

        Paint fillPaint = new Paint(batteryPaint);
        fillPaint.setStyle(Paint.Style.FILL);

        //画电池头
        int headerLeft = batteryLeft + batteryRight;
        int headerTop = (batteryTop + batteryBottom - headerHeight) / 2;
        int headerRight = width;
        int headerBottom = headerTop + headerHeight;
        canvas.drawLine(headerLeft, headerTop, headerRight, headerTop, fillPaint);
        canvas.drawLine(headerLeft, headerBottom, headerRight, headerBottom, fillPaint);
        canvas.drawLine(headerRight, headerTop, headerRight, headerBottom, fillPaint);

        //画电量
        if (mPower != 0) {
            float power_percent = mPower / 100.0f;
            int powerLeft = batteryLeft + insideMargin;
            int powerTop = batteryTop + insideMargin;

            int powerRight = (int) ((batteryRight - insideMargin) * power_percent);
            int powerBottom = batteryBottom - insideMargin;

            Rect powerRect = new Rect(powerLeft, powerTop, powerRight, powerBottom);
            canvas.drawRect(powerRect, fillPaint);
        }


    }

    public void setPower(int power) {
        mPower = power < 0 ? 0 : power;
        invalidate();
    }

}
