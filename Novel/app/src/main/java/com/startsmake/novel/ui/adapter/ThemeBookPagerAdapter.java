package com.startsmake.novel.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.BookList;
import com.startsmake.novel.bean.db.Books;
import com.startsmake.novel.databinding.ItemThemeBookListBinding;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.ui.adapter.viewholder.EmptyViewHolder;
import com.startsmake.novel.ui.fragment.ThemeBookListFragment;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class ThemeBookPagerAdapter extends BaseLoadingAdapter<BookList> {

    private static final int EMPTY_COLLECT_COUNT = 1;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_EMPTY_ITEM = 2;

    private final RequestManager mGlide;
    private final int mPagerType;

    private OnThemeBookItemClickListener mOnThemeBookItemClickListener;

    public ThemeBookPagerAdapter(Context context, RequestManager glide, int pagerType) {
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
            ThemeBookViewHolder holder = new ThemeBookViewHolder(binding.getRoot());
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

    static class ThemeBookViewHolder extends RecyclerView.ViewHolder {

        private ItemThemeBookListBinding mDataBinding;

        public ThemeBookViewHolder(View itemView) {
            super(itemView);
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

        void onItemMove(Books fromBook, Books toBook);

        void onItemDismiss(Books book);
    }

}
