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
import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.databinding.ItemNovelBinding;
import com.startsmake.novel.helper.ItemTouchHelperAdapter;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.ui.adapter.viewholder.EmptyViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * User:Shine
 * Date:2015-10-14
 * Description:
 */
public class BookshelfAdapter extends RecyclerView.Adapter implements OnClickListener, ItemTouchHelperAdapter {

    private static final int EMPTY_BOOKSHELF_COUNT = 1;
    private static final int ITEM_TYPE_EMPTY = 1;
    private static final int ITEM_TYPE_BOOK = 2;

    private final LayoutInflater mInflater;
    private RequestManager mGlide;

    private BookShelfOnItemClickListener mBookShelfOnItemClickListener;
    private List<Book> mBookshelfList;

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
            Book books = mBookshelfList.get(position);
            BookshelfViewHolder bookshelfViewHolder = (BookshelfViewHolder) holder;
            bookshelfViewHolder.getDataBinding().setBook(books);

            holder.itemView.setTag(bookshelfViewHolder.getDataBinding().ivNovelCover);

            String coverUrl = HttpConstant.URL_PICTURE + books.getCover().replaceAll("\\\\", "");
            mGlide.load(coverUrl)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bookshelfViewHolder.getDataBinding().ivNovelCover);
        }

    }

    @Override
    public int getItemCount() {
        if (mBookshelfList == null || mBookshelfList.size() == 0) {
            return EMPTY_BOOKSHELF_COUNT;
        }
        return mBookshelfList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mBookshelfList == null || mBookshelfList.size() == 0) {
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mBookshelfList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if (mBookShelfOnItemClickListener != null) {
            mBookShelfOnItemClickListener.onItemMove(mBookshelfList.get(fromPosition), mBookshelfList.get(toPosition));
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Book books = mBookshelfList.get(position);
        mBookshelfList.remove(books);
        notifyItemRemoved(position);
        if (mBookShelfOnItemClickListener != null) {
            mBookShelfOnItemClickListener.onItemDismiss(books);
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return (mBookshelfList != null && mBookshelfList.size() > 0);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
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

    public interface BookShelfOnItemClickListener {
        void onClickEmptyItem();

        void onBookShelfItemClick(View itemView, View coverView, Book book);

        void onItemMove(Book fromBook, Book toBook);

        void onItemDismiss(Book book);
    }

    public void setBookshelfList(List<Book> bookshelfList) {
        if (bookshelfList != null)
            mBookshelfList = bookshelfList;
    }

    public List<Book> getBookshelfList() {
        return mBookshelfList;
    }

    public void setBookShelfOnItemClickListener(BookShelfOnItemClickListener bookShelfOnItemClickListener) {
        mBookShelfOnItemClickListener = bookShelfOnItemClickListener;
    }
}
