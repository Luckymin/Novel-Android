package com.startsmake.novel.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.martian.libsliding.SlidingAdapter;
import com.minxiaoming.novel.R;
import com.startsmake.novel.bean.db.ChapterContent;
import com.startsmake.novel.bean.db.NovelChapters;
import com.startsmake.novel.ui.widget.BatteryView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User:Shine
 * Date:2015-09-01
 * Description:
 */
public class NovelSlidingAdapter extends SlidingAdapter<Integer[]> {

    private final Context mContext;
    private final OnReadListener mOnReadListener;

    /*小说章节信息，包含小说id，来源id，小说章节信息，部分小说章节内容*/
    private NovelChapters mNovelChapters;

    /*电池电量*/
    private int mBatteryLevel;

    private SimpleDateFormat mSimpleDateFormat;

    public NovelSlidingAdapter(Context context, OnReadListener onReadListener) {
        this.mContext = context;
        this.mOnReadListener = onReadListener;
        //时间格式 12:24
        this.mSimpleDateFormat = new SimpleDateFormat("HH:mm");
    }

    @Override
    public View getView(View contentView, Integer[] positions) {
        final ViewHolder holder;
        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_sliding_novel_content, null);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        if (mNovelChapters == null) {
            holder.pbLoading.setVisibility(View.VISIBLE);
            holder.batterView.setVisibility(View.INVISIBLE);
        } else if (positions[0] < mNovelChapters.getChapters().size()) {//判断当前章节是否已经到最后
            setPageContent(holder, positions[0], positions[1]);
        }

        return contentView;
    }

    /**
     * 设置每页的内容
     *
     * @param holder
     * @param chapterPosition 章节位置
     * @param pagePosition    页位置
     */
    private void setPageContent(final ViewHolder holder, final int chapterPosition, final int pagePosition) {
        //获取小说章节信息
        NovelChapters.ChaptersInformation chaptersInformation = mNovelChapters.getChapters().get(chapterPosition);
        //获取小说章节内容
        final ChapterContent chapterContent = chaptersInformation.getChapterContent();

        holder.tvChapterTitle.setText(chaptersInformation.getTitle());//章节标题


        if (chapterContent != null) {
            holder.pbLoading.setVisibility(View.GONE);//隐藏加载进度条
            holder.batterView.setVisibility(View.VISIBLE);//显示电量
            holder.tvCurrentTime.setVisibility(View.VISIBLE);//显示时间
            holder.tvChapterBody.setVisibility(View.VISIBLE);//显示小说文字

            holder.tvCurrentTime.setText(getCurrentTime());//设置当前时间
            holder.batterView.setPower(mBatteryLevel);//设置电池电量

            //小说文字分页内容
            List<CharSequence> pages = chapterContent.getPages();
            if (pages != null && pagePosition >= 0 && pagePosition < pages.size()) {
                //设置当前页的文章
                CharSequence charSequence = pages.get(pagePosition);
                holder.tvChapterBody.setText(charSequence);
                //显示当前页位置 如:10/14
                holder.tvPagePosition.setText((pagePosition + 1) + "/" + pages.size());
            }
        } else {//小说内容为空需要通过网络获取
            holder.pbLoading.setVisibility(View.VISIBLE);
            holder.tvChapterBody.setText(""); //章节内容设为空
            holder.tvPagePosition.setText(""); //当前位置设为空
            holder.batterView.setVisibility(View.INVISIBLE); //隐藏电量
            holder.tvCurrentTime.setVisibility(View.INVISIBLE); //隐藏时间

            if (pagePosition >= 0) {
                mOnReadListener.onLoadingNextChapter(mNovelChapters);
            } else {
                mOnReadListener.onLoadingPreviousChapter(mNovelChapters);
            }

        }
    }

    @Override
    public Integer[] getCurrent() {
        if (mNovelChapters != null) {
            return new Integer[]{mNovelChapters.getCurrChapterPosition(), mNovelChapters.getCurrPagePosition()};
        }
        return null;
    }

    @Override
    public Integer[] getNext() {
        int chapterPosition = mNovelChapters.getCurrChapterPosition();
        int pagePosition = mNovelChapters.getCurrPagePosition() + 1;
        if (chapterPosition >= mNovelChapters.getChapters().size()) {
            return null;
        }

        if (mNovelChapters.getChapters().get(chapterPosition).getChapterContent() != null//章节内容不为null
                && mNovelChapters.getChapters().get(chapterPosition).getChapterContent().getPages() != null//分页后的list不为null
                && pagePosition >= mNovelChapters.getChapters().get(chapterPosition).getChapterContent().getPages().size()//当前分页的页数已经大于等于总得页数
                ) {
            pagePosition = 0;
            chapterPosition++;
        }
        return new Integer[]{chapterPosition, pagePosition};
    }

    @Override
    public Integer[] getPrevious() {
        int chapterPosition = mNovelChapters.getCurrChapterPosition();
        int pagePosition = mNovelChapters.getCurrPagePosition() - 1;

        boolean isPreviousChapter = chapterPosition > 0 //前翻的章节位置大于0
                && pagePosition < 0;//当前分页的页数已经小于0

        if (isPreviousChapter) {
            chapterPosition--;
            NovelChapters.ChaptersInformation chaptersInformation = mNovelChapters.getChapters().get(chapterPosition);
            //小说内容不为null，小说分页列表不为null，小说分页列表大于0
            if (chaptersInformation.getChapterContent() != null && chaptersInformation.getChapterContent().getPages() != null && chaptersInformation.getChapterContent().getPages().size() > 0) {
                pagePosition = mNovelChapters.getChapters().get(chapterPosition).getChapterContent().getPages().size() - 1;
            }
        }
        return new Integer[]{chapterPosition, pagePosition};
    }

    @Override
    public boolean hasNext() {
        if (mNovelChapters == null) return false;

        int chapterPosition = mNovelChapters.getCurrChapterPosition();
//        int pagePosition = mNovelChapters.getCurrPagePosition() + 1;
        boolean hasNext = chapterPosition < mNovelChapters.getChapters().size();//当前章节小于总得章节数
        if (hasNext) {
            //判断下一页的章节内容是否已经加载
            hasNext = mNovelChapters.getChapters().get(chapterPosition).getChapterContent() == null ? false : hasNext;
        }
        return hasNext;
    }

    @Override
    public boolean hasPrevious() {
        if (mNovelChapters == null) return false;
        int currChapterPosition = mNovelChapters.getCurrChapterPosition();
        int currPagePosition = mNovelChapters.getCurrPagePosition();

        if (currChapterPosition == 0 && currPagePosition <= 0) {
            return false;
        }

        if (currPagePosition == 0) {
            List<NovelChapters.ChaptersInformation> informationList = mNovelChapters.getChapters();
            if (informationList.get(currChapterPosition).getChapterContent() == null &&informationList.get(currChapterPosition - 1).getChapterContent() == null) {
                return false;
            }

        }
        return true;
    }

    @Override
    protected void computeNext() {
        int pagePosition = mNovelChapters.getCurrPagePosition() + 1;
        if (mNovelChapters.getChapters().get(mNovelChapters.getCurrChapterPosition()).getChapterContent().getPages() != null && pagePosition >= mNovelChapters.getChapters().get(mNovelChapters.getCurrChapterPosition()).getChapterContent().getPages().size()) {
            pagePosition = 0;
            mNovelChapters.setCurrChapterPosition(mNovelChapters.getCurrChapterPosition() + 1);
        }
        mNovelChapters.setCurrPagePosition(pagePosition);
    }

    @Override
    protected void computePrevious() {
        int pagePosition = mNovelChapters.getCurrPagePosition() - 1;
        if (pagePosition < 0) {
            int currChapterPosition = mNovelChapters.getCurrChapterPosition() - 1;
            if (currChapterPosition < 0) return;
            mNovelChapters.setCurrChapterPosition(currChapterPosition);
            NovelChapters.ChaptersInformation chaptersInformation = mNovelChapters.getChapters().get(currChapterPosition);

            if (chaptersInformation.getChapterContent() != null && chaptersInformation.getChapterContent().getPages() != null && chaptersInformation.getChapterContent().getPages().size() > 0) {
                pagePosition = mNovelChapters.getChapters().get(mNovelChapters.getCurrChapterPosition()).getChapterContent().getPages().size() - 1;
            }
        }
        mNovelChapters.setCurrPagePosition(pagePosition);
    }

    public String getCurrentTime() {
        Date date = new Date();
        String dateStr = mSimpleDateFormat.format(date);
        return dateStr;
    }

    public void setBatteryLevel(int level) {
        mBatteryLevel = level;
        setBatterViewPower(getCurrentView());
        setBatterViewPower(getPreviousView());
        setBatterViewPower(getNextView());
    }

    private void setBatterViewPower(View view) {
        if (view != null) {
            NovelSlidingAdapter.ViewHolder holder = (NovelSlidingAdapter.ViewHolder) view.getTag();
            holder.batterView.setPower(mBatteryLevel);
        }
    }


    public static class ViewHolder {
        public TextView tvChapterBody;
        public TextView tvChapterTitle;
        public TextView tvPagePosition;
        public TextView tvCurrentTime;
        public ProgressBar pbLoading;
        public BatteryView batterView;

        public ViewHolder(View itemView) {
            tvChapterBody = (TextView) itemView.findViewById(R.id.tvChapterBody);
            tvChapterTitle = (TextView) itemView.findViewById(R.id.tvChapterTitle);
            tvPagePosition = (TextView) itemView.findViewById(R.id.tvPagePosition);
            tvCurrentTime = (TextView) itemView.findViewById(R.id.tvCurrentTime);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pbLoading);
            batterView = (BatteryView) itemView.findViewById(R.id.batterView);

        }
    }

    public void setNovelChapter(NovelChapters novelChapter) {
        if (mNovelChapters != novelChapter)
            mNovelChapters = novelChapter;
    }

    public NovelChapters getNovelChapters() {
        return mNovelChapters;
    }


    public interface OnReadListener {

        void onLoadingNextChapter(NovelChapters novelChapters);

        void onLoadingPreviousChapter(NovelChapters novelChapters);
    }

}
