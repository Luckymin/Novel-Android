package com.startsmake.novel.bean.db;

import org.litepal.crud.DataSupport;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class BookList extends DataSupport {

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

    private String _id;
    private String author;
    private String gender;
    private int bookCount;
    private String title;
    private String collectorCount;
    private String cover;
    private String desc;

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

    public String getDesc() {
        return desc;
    }

}
