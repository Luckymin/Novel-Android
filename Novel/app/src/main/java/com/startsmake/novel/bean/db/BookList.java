package com.startsmake.novel.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class BookList extends DataSupport implements Parcelable {
    /**
     * _id : 533040bf16cda3e5180001ab
     * author : Dear‘’ 冷情绪、
     * gender : male
     * bookCount : 46
     * title : 神作
     * collectorCount : 6676
     * cover : /agent/http://image.cmfu.com/books/1209977/1209977.jpg
     * desc : 不看就后悔
     */
    @Column(ignore = true)
    private String _id;
    private String bookListID;
    private int id;
    private String author;
    private String gender;
    private int bookCount;
    private String title;
    private String collectorCount;
    private String cover;
    private String desc;
    private int orderIndex;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCollectorCount(String collectorCount) {
        this.collectorCount = collectorCount;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String get_id() {
        return _id;
    }

    public String getAuthor() {
        return author;
    }

    public String getGender() {
        return gender;
    }

    public int getBookCount() {
        return bookCount;
    }

    public String getTitle() {
        return title;
    }

    public String getCollectorCount() {
        return collectorCount;
    }

    public String getCover() {
        return cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public String getBookListID() {
        return bookListID;
    }

    public void setBookListID(String bookListID) {
        this.bookListID = bookListID;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public BookList() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.bookListID);
        dest.writeInt(this.id);
        dest.writeString(this.author);
        dest.writeString(this.gender);
        dest.writeInt(this.bookCount);
        dest.writeString(this.title);
        dest.writeString(this.collectorCount);
        dest.writeString(this.cover);
        dest.writeString(this.desc);
        dest.writeInt(this.orderIndex);
    }

    protected BookList(Parcel in) {
        this._id = in.readString();
        this.bookListID = in.readString();
        this.id = in.readInt();
        this.author = in.readString();
        this.gender = in.readString();
        this.bookCount = in.readInt();
        this.title = in.readString();
        this.collectorCount = in.readString();
        this.cover = in.readString();
        this.desc = in.readString();
        this.orderIndex = in.readInt();
    }

    public static final Creator<BookList> CREATOR = new Creator<BookList>() {
        public BookList createFromParcel(Parcel source) {
            return new BookList(source);
        }

        public BookList[] newArray(int size) {
            return new BookList[size];
        }
    };
}
