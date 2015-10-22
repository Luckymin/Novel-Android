package com.startsmake.novel.ui.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.Books;
import com.startsmake.novel.helper.OnStartDragListener;
import com.startsmake.novel.helper.SimpleItemTouchHelperCallback;
import com.startsmake.novel.ui.activity.MainActivity;
import com.startsmake.novel.ui.activity.ReaderNovelActivity;
import com.startsmake.novel.ui.adapter.BookshelfAdapter;
import com.startsmake.novel.ui.widget.itemdecoration.LinearSpaceItemDecoration;
import com.startsmake.novel.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * User:Shine
 * Date:2015-08-11
 * Description:
 */
public class BookshelfFragment extends BaseFragment implements BookshelfAdapter.BookShelfOnItemClickListener, OnStartDragListener {

    private List<Books> mBookshelfList;

    private RecyclerView mRecyclerView;
    private BookshelfAdapter mAdapter;

    private ItemTouchHelper mItemTouchHelper;

    public static Fragment newInstance() {
        Fragment fragment = new BookshelfFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_shelf, container, false);
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        initValue();
        initView(view);
    }

    private void initValue() {
        mBookshelfList = DataSupport.order("orderIndex asc").find(Books.class);
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new BookshelfAdapter(getActivity(), Glide.with(this));
        mAdapter.setBookShelfOnItemClickListener(this);
        mAdapter.setBookshelfList(mBookshelfList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8),true));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            List<Books> booksList = DataSupport.findAll(Books.class);
            if (booksList != null) {
                mBookshelfList.clear();
                mBookshelfList.addAll(booksList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClickEmptyItem() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).toggleDrawer();
        }
    }

    @Override
    public void onBookShelfItemClick(View itemView, View coverView, Books book) {
        ReaderNovelActivity.openActivity(getActivity(), book.getNovelID());
    }

    @Override
    public void onItemMove(Books fromBook, Books toBook) {
        int fromOrderIndex = fromBook.getOrderIndex();
        fromBook.setOrderIndex(toBook.getOrderIndex());
        toBook.setOrderIndex(fromOrderIndex);

        ContentValues fromValues = new ContentValues();
        fromValues.put("orderIndex", fromBook.getOrderIndex());

        ContentValues toValues = new ContentValues();
        toValues.put("orderIndex", toBook.getOrderIndex());

        DataSupport.update(Books.class, fromValues, fromBook.getId());
        DataSupport.update(Books.class, toValues, toBook.getId());
    }

    @Override
    public void onItemDismiss(Books book) {
        book.delete();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
