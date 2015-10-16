package com.startsmake.novel.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.Books;
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
public class BookshelfFragment extends BaseFragment implements BookshelfAdapter.BookShelfOnItemClickListener {

    private List<Books> mBookshelfList;

    private RecyclerView mRecyclerView;
    private BookshelfAdapter mAdapter;

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
        mBookshelfList = DataSupport.findAll(Books.class);
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new BookshelfAdapter(getActivity(), Glide.with(this));
        mAdapter.setBookShelfOnItemClickListener(this);
        mAdapter.setBookshelfList(mBookshelfList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new LinearSpaceItemDecoration(Utils.dpToPx(8)));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            List<Books> booksList = DataSupport.findAll(Books.class);
            if (booksList != null && booksList.size() > 0) {
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
}
