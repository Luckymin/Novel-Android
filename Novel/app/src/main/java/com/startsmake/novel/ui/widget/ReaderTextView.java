package com.startsmake.novel.ui.widget;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-08-25
 * Description:
 */
public class ReaderTextView extends TextView {

    public ReaderTextView(Context context) {
        super(context);
    }

    public ReaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReaderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public List<CharSequence> getPages(String body) {
        final StaticLayout layout = new StaticLayout(body, getPaint(), getWidth(), Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), getIncludeFontPadding());

        final int lines = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int viewHeight = getHeight();
        int height = viewHeight;

        List<CharSequence> pages = new ArrayList<>();

        for (int i = 0; i < lines; i++) {
            if (height < layout.getLineBottom(i)) {
                // When the layout height has been exceeded
                pages.add(text.subSequence(startOffset, layout.getLineStart(i)));
                startOffset = layout.getLineStart(i);
                height = layout.getLineTop(i) + viewHeight;
            }

            if (i == lines - 1) {
                // Put the rest of the text into the last page
                pages.add(text.subSequence(startOffset, layout.getLineEnd(i)));
                break;
            }
        }

        return pages;
    }


}
