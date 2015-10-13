package com.startsmake.novel.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minxiaoming.novel.ClassificationItemDataBinding;
import com.startsmake.novel.Interfaces.OnClickListener;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.BookClassificationBean;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:分类列表适配器
 */
public class ClassificationListAdapter extends RecyclerView.Adapter<ClassificationListAdapter.ClassificationListViewHolder> implements OnClickListener {

    private final List<BookClassificationBean.BookCatsEntity> mData;
    private final OnClassifyItemClickListener mOnClassifyItemClickListener;

    public ClassificationListAdapter(List<BookClassificationBean.BookCatsEntity> bookCatsList, OnClassifyItemClickListener listener) {
        this.mData = bookCatsList;
        this.mOnClassifyItemClickListener = listener;
    }


    @Override
    public ClassificationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ClassificationItemDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_grid_sex_classify, parent, false);
        ClassificationListViewHolder holder = new ClassificationListViewHolder(dataBinding.getRoot(), this);
        holder.setDataBinding(dataBinding);
        return holder;
    }

    @Override
    public void onBindViewHolder(ClassificationListViewHolder holder, int position) {
        holder.getDataBinding().setCats(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View view, int position) {
        if (mOnClassifyItemClickListener != null) {
            mOnClassifyItemClickListener.onClickClassifyItem(view, mData.get(position));
        }
    }

    public static class ClassificationListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ClassificationItemDataBinding mDataBinding;
        private OnClickListener mOnClickListener;

        public ClassificationListViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mOnClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        public ClassificationItemDataBinding getDataBinding() {
            return mDataBinding;
        }

        public void setDataBinding(ClassificationItemDataBinding novelItemDataBinding) {
            mDataBinding = novelItemDataBinding;
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnClassifyItemClickListener {
        void onClickClassifyItem(View view, BookClassificationBean.BookCatsEntity bookCatsEntity);
    }


}
