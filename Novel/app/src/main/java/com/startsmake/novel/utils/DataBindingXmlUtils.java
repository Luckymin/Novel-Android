package com.startsmake.novel.utils;


import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.startsmake.novel.NovelApplication;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.Book;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User:Shine
 * Date:2015-08-13
 * Description:DataBinding xml使用的公共类
 */
public class DataBindingXmlUtils {


    @BindingAdapter("visibilityByEmpty")
    public static void setVisibility(View view, String str) {
        view.setVisibility(TextUtils.isEmpty(str) ? view.GONE : View.VISIBLE);
    }

    @BindingAdapter("latelyFollowerAndWordCount")
    public static void setLatelyFollowerAndWordCount(TextView textView, Book book) {
        SpannableStringBuilder latelyFollowerText = setForegroundColor(book.getLatelyFollower(), "人在追");
        String wordCountStr = workCountTransition(book.getWordCount());

        SpannableStringBuilder wordCountText = setForegroundColor(wordCountStr, "万字");

        textView.setText(latelyFollowerText.append(" | ").append(wordCountText));

    }

    public static SpannableStringBuilder setForegroundColor(Object front, String back) {
        String frontStr = String.valueOf(front);
        int start;
        if ((start = frontStr.indexOf(back)) != -1) {
            frontStr = frontStr.substring(0, start);
        }

        Context context = NovelApplication.getContext();
        SpannableStringBuilder spannableString = new SpannableStringBuilder(frontStr + back);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, frontStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    public static String novelClassifyAndWordCountFormat(String cat, int wordCount, int followerCount) {
//        novelInfo.cat + @string/novel_intro_separated + DataBindingXmlUtils.workCountTransition(novelInfo.wordCount) + @string/novel_intro_separated + DataBindingXmlUtils.toString(novelInfo.followerCount) + @string/novel_intro_follower_count_suffix
        StringBuilder stringBuilder = new StringBuilder(cat);
        if (wordCount != 0) {
            stringBuilder.append(" | ").append(workCountTransition(wordCount));
        }
        if (followerCount != 0) {
            stringBuilder.append(" | ").append(followerCount).append(Utils.getString(R.string.novel_intro_follower_count_suffix));
        }
        return stringBuilder.toString();
    }

    /**
     * 字数转换 如 1234567 为 12万
     *
     * @param workCount
     */
    public static String workCountTransition(int workCount) {
        if (workCount > 10000) {
            return (workCount / 10000) + "万字";
        }
        return String.valueOf(workCount);
    }

    public static String toString(int obj) {
        return String.valueOf(obj);
    }

    /**
     * 时间转换 转换为几年、几日、几小时、几分钟前发的小说
     *
     * @param dateStr
     * @return
     */
    public static String dateFormat(String dateStr) {
        if (dateStr == null) {
            return "";
        }
        String dateFormat;
        try {
            SimpleDateFormat sdf;

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr.substring(0, dateStr.indexOf(".")).replace("T", " "));

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

            //获取是几年、几日、几小时、几分钟前发的小说
            calendar.setTime(date);

            int dateYear = calendar.get(Calendar.YEAR);
            int dateDay = calendar.get(Calendar.DAY_OF_YEAR);

            int diffYear = currentYear - dateYear;
            int diffDay = currentDay - dateDay;
            long diffTime = Long.valueOf((System.currentTimeMillis() - date.getTime()) / 1000);
            long minuteTime = diffTime / 60;
            long hourTime = minuteTime / 60;

            if (diffTime < 0 || diffYear >= 1) {//大于等于1年
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat = sdf.format(date);
            } else if (diffDay >= 2) {//大于等于2天
                sdf = new SimpleDateFormat("MM-dd HH:mm");
                dateFormat = sdf.format(date);
            } else if (diffDay == 1) {//昨天
                sdf = new SimpleDateFormat("HH:mm");
                dateFormat = "昨天 " + sdf.format(date);
            } else if (minuteTime >= 60) {//今天几小时前
                dateFormat = hourTime + " 小时之前";
            } else if (minuteTime >= 5) {//几分钟前
                dateFormat = minuteTime + " 分钟之前";
            } else {
                dateFormat = "刚刚";
            }
        } catch (Exception e) {
            e.printStackTrace();
            dateFormat = dateStr;
        }

        return dateFormat;
    }
}
