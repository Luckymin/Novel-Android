package com.startsmake.novel.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * User:Shine
 * Date:2015-10-15
 * Description:
 */
public class Book extends DataSupport implements Parcelable {
    /**
     * cover : /agent/http://image.cmfu.com/books/2494758/2494758.jpg
     * site : qidian
     * author : 莫默
     * cat : 玄幻
     * retentionRatio : 79.71
     * latelyFollower : 1558
     * _id : 50bee5172033d09b2f00001b
     * title : 武炼巅峰
     * lastChapter : 第两千四百一十二章 紫雨
     * shortIntro : 武之巅峰，是孤独，是寂寞，是漫漫求索，是高处不胜寒 逆境中成长，绝地里求生，不屈不饶，才能堪破武之极道。 凌霄阁试炼弟子兼扫地小厮杨开偶获一本无字黑书，从此踏上...
     * tags : ["玄幻","热血","架空","巅峰","奇遇","升级练功","东方玄幻"]
     */

    @Column(ignore = true)
    private String _id;
    private int id;
    private String novelID;
    private String cover;
    private String site;
    private String author;
    private String cat;
    private double retentionRatio;
    private int latelyFollower;
    private String title;
    private String lastChapter;
    private String shortIntro;
    private List<String> tags;
    private int wordCount;
    private int banned;

    private int orderIndex;

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setRetentionRatio(double retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCover() {
        return cover;
    }

    public String getSite() {
        return site;
    }

    public String getAuthor() {
        return author;
    }

    public String getCat() {
        return cat;
    }

    public double getRetentionRatio() {
        return retentionRatio;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public String getShortIntro() {
        return shortIntro;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNovelID() {
        return novelID;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setNovelID(String novelID) {
        this.novelID = novelID;
    }

    public Book() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeInt(this.id);
        dest.writeString(this.novelID);
        dest.writeString(this.cover);
        dest.writeString(this.site);
        dest.writeString(this.author);
        dest.writeString(this.cat);
        dest.writeDouble(this.retentionRatio);
        dest.writeInt(this.latelyFollower);
        dest.writeString(this.title);
        dest.writeString(this.lastChapter);
        dest.writeString(this.shortIntro);
        dest.writeStringList(this.tags);
        dest.writeInt(this.wordCount);
        dest.writeInt(this.banned);
        dest.writeInt(this.orderIndex);
    }

    protected Book(Parcel in) {
        this._id = in.readString();
        this.id = in.readInt();
        this.novelID = in.readString();
        this.cover = in.readString();
        this.site = in.readString();
        this.author = in.readString();
        this.cat = in.readString();
        this.retentionRatio = in.readDouble();
        this.latelyFollower = in.readInt();
        this.title = in.readString();
        this.lastChapter = in.readString();
        this.shortIntro = in.readString();
        this.tags = in.createStringArrayList();
        this.wordCount = in.readInt();
        this.banned = in.readInt();
        this.orderIndex = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
