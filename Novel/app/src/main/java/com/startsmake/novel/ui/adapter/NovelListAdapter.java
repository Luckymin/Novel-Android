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
import com.startsmake.novel.bean.NovelListBean;
import com.startsmake.novel.bean.db.Books;
import com.startsmake.novel.databinding.ItemNovelBinding;
import com.startsmake.novel.http.HttpConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class NovelListAdapter extends BaseLoadingAdapter<Books> implements OnClickListener {

    private static final int TYPE_ITEM = 1;

    private final RequestManager mGlide;

    //item点击监听
    private OnNovelListItemClickListener mOnNovelListItemClickListener;

    public NovelListAdapter(Context context, RequestManager glide) {
        super(context);
        this.mGlide = glide;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ItemNovelBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_novel, parent, false);
            NovelListViewHolder holder = new NovelListViewHolder(dataBinding.getRoot(), this);
            holder.setDataBinding(dataBinding);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && getItemViewType(position) == TYPE_ITEM) {
            NovelListViewHolder novelHolder = (NovelListViewHolder) holder;
            Books book = mData.get(position);
            novelHolder.getDataBinding().setBook(book);
            holder.itemView.setTag(novelHolder.getDataBinding().ivNovelCover);

            String coverUrl = HttpConstant.URL_PICTURE + book.getCover().replaceAll("\\\\", "");
            mGlide.load(coverUrl)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(novelHolder.getDataBinding().ivNovelCover);

        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected int getItemType(int position) {
        return TYPE_ITEM;
    }

    /*Item 点击监听*/
    @Override
    public void onClick(View view, int position) {
        if (mOnNovelListItemClickListener != null) {
            mOnNovelListItemClickListener.onNovelItemClick(view, (View) view.getTag(), mData.get(position));
        }
    }

    public static class NovelListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemNovelBinding mDataBinding;
        private OnClickListener mOnClickListener;

        public NovelListViewHolder(View itemView, OnClickListener onClickListener) {
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
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnNovelListItemClickListener {
        void onNovelItemClick(View itemView, View coverView, Books book);
    }

    public void setOnNovelListItemClickListener(OnNovelListItemClickListener onNovelListItemClickListener) {
        mOnNovelListItemClickListener = onNovelListItemClickListener;
    }

}
