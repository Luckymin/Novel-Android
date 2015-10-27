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
import com.startsmake.novel.bean.BookListDetailsBean;
import com.startsmake.novel.bean.db.Book;
import com.startsmake.novel.databinding.ItemBookListDetailsHeaderBinding;
import com.startsmake.novel.databinding.ItemBookListNovelBinding;
import com.startsmake.novel.helper.GlideCircleTransform;
import com.startsmake.novel.http.HttpConstant;

import java.util.List;

/**
 * User:Shine
 * Date:2015-10-23
 * Description:
 */
public class BookListDetailsAdapter extends RecyclerView.Adapter implements OnClickListener {

    private static final int HEADER_ITEM_COUNT = 1;

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_NOVEL = 2;

    private final RequestManager mGlide;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private BookListDetailsBean mDetails;
    private OnNovelItemClickListener mOnNovelItemClickListener;
    private boolean isCleanItem;

    public BookListDetailsAdapter(Context context, RequestManager glide) {
        mContext = context;
        mGlide = glide;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            ItemBookListDetailsHeaderBinding dataBinding = DataBindingUtil.inflate(mInflater, R.layout.item_book_list_details_header, parent, false);
            dataBinding.btnCollect.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (mOnNovelItemClickListener != null)
                        mOnNovelItemClickListener.onCollectClick();
                }
            });
            HeaderViewHolder holder = new HeaderViewHolder(dataBinding.getRoot());
            holder.setDataBinding(dataBinding);
            return holder;
        } else {
            ItemBookListNovelBinding dataBinding = DataBindingUtil.inflate(mInflater, R.layout.item_book_list_novel, parent, false);
            NovelViewHolder holder = new NovelViewHolder(dataBinding.getRoot(), this);
            holder.setDataBinding(dataBinding);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            BookListDetailsBean.BookListEntity bookList = mDetails.getBookList();
            headerHolder.getDataBinding().setBookList(bookList);

            String coverUrl = HttpConstant.URL_PICTURE + bookList.getAuthor().getAvatar().replaceAll("\\\\", "");
            mGlide.load(coverUrl)
                    .asBitmap()
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(headerHolder.getDataBinding().ivUserCover);
        } else {
            BookListDetailsBean.BookListEntity.BooksEntity bookList = mDetails.getBookList().getBooks().get(position - 1);
            NovelViewHolder novelViewHolder = (NovelViewHolder) holder;
            novelViewHolder.getDataBinding().setBookList(bookList);
            novelViewHolder.itemView.setTag(novelViewHolder.getDataBinding().ivNovelCover);
            String coverUrl = HttpConstant.URL_PICTURE + bookList.getBook().getCover().replaceAll("\\\\", "");
            mGlide.load(coverUrl)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(novelViewHolder.getDataBinding().ivNovelCover);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NOVEL;
        }
    }

    @Override
    public int getItemCount() {
        if (isCleanItem || mDetails == null) {
            return 0;
        }
        return HEADER_ITEM_COUNT + mDetails.getBookList().getBooks().size();
    }


    public void setDetails(BookListDetailsBean details) {
        mDetails = details;
    }

    @Override
    public void onClick(View view, int position) {
        if (mOnNovelItemClickListener != null) {
            Book book = mDetails.getBookList().getBooks().get(position - 1).getBook();
            mOnNovelItemClickListener.onNovelClick(view, (View) view.getTag(), book);
        }
    }

    public void cleanItem() {
        isCleanItem = true;
        notifyDataSetChanged();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ItemBookListDetailsHeaderBinding mDataBinding;

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public ItemBookListDetailsHeaderBinding getDataBinding() {
            return mDataBinding;
        }

        public void setDataBinding(ItemBookListDetailsHeaderBinding dataBinding) {
            mDataBinding = dataBinding;
        }
    }

    public static class NovelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemBookListNovelBinding mDataBinding;
        private OnClickListener mOnClickListener;

        public NovelViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mOnClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        public ItemBookListNovelBinding getDataBinding() {
            return mDataBinding;
        }

        public void setDataBinding(ItemBookListNovelBinding dataBinding) {
            mDataBinding = dataBinding;
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnNovelItemClickListener {
        void onNovelClick(View view, View coverView, Book book);

        void onCollectClick();
    }

    public void setOnNovelItemClickListener(OnNovelItemClickListener onNovelItemClickListener) {
        mOnNovelItemClickListener = onNovelItemClickListener;
    }
}
