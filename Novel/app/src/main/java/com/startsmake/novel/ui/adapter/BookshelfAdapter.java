package com.startsmake.novel.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.startsmake.novel.Interfaces.OnClickListener;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.Books;
import com.startsmake.novel.databinding.ItemNovelBinding;
import com.startsmake.novel.http.HttpConstant;

import java.util.List;

/**
 * User:Shine
 * Date:2015-10-14
 * Description:
 */
public class BookshelfAdapter extends RecyclerView.Adapter implements OnClickListener {

    private static final int EMPTY_BOOKSHELF_COUNT = 1;
    private static final int ITEM_TYPE_EMPTY = 1;
    private static final int ITEM_TYPE_BOOK = 2;

    private final LayoutInflater mInflater;
    private RequestManager mGlide;

    private BookShelfOnItemClickListener mBookShelfOnItemClickListener;
    private List<Books> mBookshelfList;

    public BookshelfAdapter(Context context, RequestManager glide) {
        mInflater = LayoutInflater.from(context);
        mGlide = glide;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_EMPTY) {
            View view = mInflater.inflate(R.layout.item_bookshelf_empty, parent, false);
            EmptyViewHolder holder = new EmptyViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBookShelfOnItemClickListener != null) {
                        mBookShelfOnItemClickListener.onClickEmptyItem();
                    }
                }
            });
            return holder;
        } else if (viewType == ITEM_TYPE_BOOK) {
            ItemNovelBinding dataBinding = DataBindingUtil.inflate(mInflater, R.layout.item_novel, parent, false);
            BookshelfViewHolder holder = new BookshelfViewHolder(dataBinding.getRoot(), this);
            holder.setDataBinding(dataBinding);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_TYPE_BOOK) {
            Books books = mBookshelfList.get(position);
            BookshelfViewHolder bookshelfViewHolder = (BookshelfViewHolder) holder;
            bookshelfViewHolder.getDataBinding().setBook(books);

            holder.itemView.setTag(bookshelfViewHolder.getDataBinding().ivNovelCover);
        }

    }

    @Override
    public int getItemCount() {
        if (mBookshelfList == null) {
            return EMPTY_BOOKSHELF_COUNT;
        }
        return mBookshelfList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mBookshelfList == null) {
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_BOOK;
    }

    @Override
    public void onClick(View view, int position) {
        if (mBookShelfOnItemClickListener != null) {
            mBookShelfOnItemClickListener.onBookShelfItemClick(view, (View) view.getTag(), mBookshelfList.get(position));
        }
    }


    static class BookshelfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemNovelBinding mDataBinding;
        private OnClickListener mOnClickListener;

        public BookshelfViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            this.mOnClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        public ItemNovelBinding getDataBinding() {
            return mDataBinding;
        }

        public void setDataBinding(ItemNovelBinding dataBinding) {
            mDataBinding = dataBinding;
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null)
                mOnClickListener.onClick(v, getAdapterPosition());
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface BookShelfOnItemClickListener {
        void onClickEmptyItem();

        void onBookShelfItemClick(View itemView, View coverView, Books book);
    }

    public void setBookshelfList(List<Books> bookshelfList) {
        if (bookshelfList != null && bookshelfList.size() > 0)
            mBookshelfList = bookshelfList;
    }

    public void setBookShelfOnItemClickListener(BookShelfOnItemClickListener bookShelfOnItemClickListener) {
        mBookShelfOnItemClickListener = bookShelfOnItemClickListener;
    }
}
