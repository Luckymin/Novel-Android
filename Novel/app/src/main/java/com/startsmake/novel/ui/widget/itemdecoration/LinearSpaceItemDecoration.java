package com.startsmake.novel.ui.widget.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.ui.adapter.BookshelfAdapter;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;
    private final boolean mHorizontalMargin;
    private final boolean mTopMargin;

    public LinearSpaceItemDecoration(int space) {
        this.mSpace = space;
        this.mHorizontalMargin = true;
        this.mTopMargin = true;
    }

    public LinearSpaceItemDecoration(int space, boolean horizontalMargin) {
        this.mSpace = space;
        this.mHorizontalMargin = horizontalMargin;
        this.mTopMargin = true;
    }

    public LinearSpaceItemDecoration(int space, boolean horizontalMargin, boolean TopMargin) {
        this.mSpace = space;
        this.mHorizontalMargin = horizontalMargin;
        this.mTopMargin = TopMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getAdapter() instanceof BookshelfAdapter) {
            List<Book> booksList = ((BookshelfAdapter) parent.getAdapter()).getBookshelfList();
            if (booksList == null || booksList.size() == 0) return;
        }

        int currPosition = parent.getChildAdapterPosition(view);
        if (mHorizontalMargin) {
            outRect.left = mSpace;
            outRect.right = mSpace;
        }

        if (currPosition == 0 && !mTopMargin) {
            outRect.top = 0;
        } else {
            outRect.top = mSpace;
        }


        if (currPosition == state.getItemCount() - 1) {
            outRect.bottom = mSpace;
        }
    }


}
