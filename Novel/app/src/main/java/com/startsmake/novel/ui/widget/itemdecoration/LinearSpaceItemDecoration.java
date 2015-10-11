package com.startsmake.novel.ui.widget.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public LinearSpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int currPosition = parent.getChildAdapterPosition(view);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
        if (currPosition == state.getItemCount() - 1) {
            outRect.bottom = mSpace;
        }
    }
}
