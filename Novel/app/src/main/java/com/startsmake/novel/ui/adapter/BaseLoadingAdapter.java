package com.startsmake.novel.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startsmake.novel.R;
import com.startsmake.novel.ui.adapter.viewholder.LoadingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-10-21
 * Description:
 */
public abstract class BaseLoadingAdapter<T> extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = 123456789;

    protected final List<T> mData;
    private final LayoutInflater mInflater;
    //是否显示底部圆圈进度条
    private boolean isShowLoading = false;

    public BaseLoadingAdapter(Context context) {
        mData = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = mInflater.inflate(R.layout.item_list_footer_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            return onCreateViewHolder(mInflater, parent, viewType);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
            return getItemType(position);
        }
    }

    public void addItems(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
        if (isShowLoading) {
            notifyItemInserted(getItemCount() + 1);
        } else {
            notifyItemRemoved(getItemCount() + 1);
        }
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }

    public List<T> getData() {
        return mData;
    }

    protected abstract int getItemType(int position);

    public abstract RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

}
