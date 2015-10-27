package com.startsmake.novel.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.startsmake.novel.Interfaces.OnClickListener;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.BookList;
import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.databinding.ItemThemeBookListBinding;
import com.startsmake.novel.helper.ItemTouchHelperAdapter;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.ui.adapter.viewholder.EmptyViewHolder;
import com.startsmake.novel.ui.fragment.ThemeBookListFragment;

import java.util.Collections;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class BookListPagerAdapter extends BaseLoadingAdapter<BookList> implements OnClickListener, ItemTouchHelperAdapter {

    private static final int EMPTY_COLLECT_COUNT = 1;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_EMPTY_ITEM = 2;

    private final RequestManager mGlide;
    private final int mPagerType;

    private OnThemeBookItemClickListener mOnThemeBookItemClickListener;

    public BookListPagerAdapter(Context context, RequestManager glide, int pagerType) {
        super(context);
        mGlide = glide;
        mPagerType = pagerType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY_ITEM) {
            View view = inflater.inflate(R.layout.item_bookshelf_empty, parent, false);
            EmptyViewHolder holder = new EmptyViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnThemeBookItemClickListener != null) {
                        mOnThemeBookItemClickListener.onClickEmptyItem();
                    }
                }
            });
            holder.tvEmptyHint.setText(R.string.theme_book_list_empty_hint);
            return holder;
        } else if (viewType == TYPE_ITEM) {
            ItemThemeBookListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_theme_book_list, parent, false);
            ThemeBookViewHolder holder = new ThemeBookViewHolder(binding.getRoot(), this);
            holder.setDataBinding(binding);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == TYPE_ITEM) {
            ThemeBookViewHolder themeBookViewHolder = (ThemeBookViewHolder) holder;

            BookList bookList = mData.get(position);
            themeBookViewHolder.getDataBinding().setBookList(bookList);

            themeBookViewHolder.itemView.setTag(themeBookViewHolder.getDataBinding().tvBookListTitle);

            String coverUrl = HttpConstant.URL_PICTURE + bookList.getCover().replaceAll("\\\\", "");
            mGlide.load(coverUrl)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(themeBookViewHolder.getDataBinding().ivBookListCover);
        }
    }

    @Override
    public int getItemCount() {
        if (mPagerType == ThemeBookListFragment.PAGER_TYPE_MY_COLLECT && mData.size() == 0) {
            return EMPTY_COLLECT_COUNT;
        } else {
            return super.getItemCount();
        }
    }

    @Override
    protected int getItemType(int position) {
        if (mPagerType == ThemeBookListFragment.PAGER_TYPE_MY_COLLECT && mData.size() == 0) {
            return TYPE_EMPTY_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onClick(View view, int position) {
        if (mOnThemeBookItemClickListener != null) {
            TextView textView = (TextView) view.getTag();
            mOnThemeBookItemClickListener.onThemeBookItemClick(view, textView, mData.get(position));
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if (mOnThemeBookItemClickListener != null) {
            mOnThemeBookItemClickListener.onItemMove(mData.get(fromPosition), mData.get(toPosition));
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        BookList bookList = mData.get(position);
        mData.remove(bookList);
        notifyItemRemoved(position);
        if (mOnThemeBookItemClickListener != null) {
            mOnThemeBookItemClickListener.onItemDismiss(bookList);
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mData.size() > 0;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    static class ThemeBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemThemeBookListBinding mDataBinding;
        private OnClickListener mOnClickListener;

        public ThemeBookViewHolder(View itemView, OnClickListener listener) {
            super(itemView);
            mOnClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v, getAdapterPosition());
            }
        }

        public ItemThemeBookListBinding getDataBinding() {
            return mDataBinding;
        }

        public void setDataBinding(ItemThemeBookListBinding dataBinding) {
            mDataBinding = dataBinding;
        }
    }


    public void setOnThemeBookItemClickListener(OnThemeBookItemClickListener onThemeBookItemClickListener) {
        mOnThemeBookItemClickListener = onThemeBookItemClickListener;
    }

    public interface OnThemeBookItemClickListener {
        void onClickEmptyItem();

        void onThemeBookItemClick(View itemView, View coverView, BookList book);

        void onItemMove(BookList fromBook, BookList toBook);

        void onItemDismiss(BookList book);
    }

}
