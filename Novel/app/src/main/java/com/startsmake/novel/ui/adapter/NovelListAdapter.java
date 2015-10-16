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
public class NovelListAdapter extends RecyclerView.Adapter implements OnClickListener {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private final List<Books> mData;
    private final RequestManager mGlide;
    private final LayoutInflater mInflater;

    //是否显示底部圆圈进度条
    private boolean isShowLoading = false;
    //item点击监听
    private OnNovelListItemClickListener mOnNovelListItemClickListener;

    public NovelListAdapter(Context context, RequestManager glide) {
        this.mGlide = glide;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ItemNovelBinding dataBinding = DataBindingUtil.inflate(mInflater, R.layout.item_novel, parent, false);
            NovelListViewHolder holder = new NovelListViewHolder(dataBinding.getRoot(), this);
            holder.setDataBinding(dataBinding);
            return holder;
        } else {
            View view = mInflater.inflate(R.layout.item_list_footer_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            NovelListViewHolder novelHolder = (NovelListViewHolder) holder;
            Books book = mData.get(position);
            novelHolder.getDataBinding().setBook(book);
            holder.itemView.setTag(novelHolder.getDataBinding().ivNovelCover);
        }
    }

    @Override
    public int getItemCount() {
        int count = isShowLoading ? mData.size() + 1 : mData.size();
        return count;
    }


    @Override
    public int getItemViewType(int position) {
        if (isShowLoading && position == getItemCount() - 1) {
            return TYPE_LOADING;
        } else {
            return TYPE_ITEM;
        }
    }

    /*Item 点击监听*/
    @Override
    public void onClick(View view, int position) {
        if (mOnNovelListItemClickListener != null) {
            mOnNovelListItemClickListener.onNovelItemClick(view, (View) view.getTag(), mData.get(position));
        }
    }

    public void addItems(List<Books> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /*添加或删除底部item*/
    public void setShowLoading(boolean isShow) {
        isShowLoading = isShow;
        if (isShow)
            notifyItemInserted(mData.size() + 1);
        else
            notifyItemRemoved(mData.size() + 1);
    }


    public boolean isShowLoading() {
        return isShowLoading;
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
