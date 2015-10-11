package com.startsmake.novel.ui.widget.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class GridLayoutSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;
    private final int mRank;

    public GridLayoutSpaceItemDecoration(int space, int rank) {
        this.mSpace = space;
        this.mRank = rank;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int currPosition = parent.getChildAdapterPosition(view);
        int currRank = currPosition % mRank;

        if (currRank == 0) {
            outRect.left = mSpace;
        } else if (currRank == mRank - 1) {
            outRect.right = mSpace;
        } else {
            outRect.right = mSpace;
            outRect.left = mSpace;
        }

        if (currPosition >= 0 && currPosition < mRank) {
            outRect.top = mSpace * 2;
        }
        outRect.bottom = mSpace;
    }
}
