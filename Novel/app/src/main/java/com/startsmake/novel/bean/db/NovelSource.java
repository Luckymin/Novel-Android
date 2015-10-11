package com.startsmake.novel.bean.db;

import org.litepal.crud.DataSupport;

/**
 * User:Shine
 * Date:2015-08-24
 * Description:获取小说来源接口实体类
 */
public class NovelSource extends DataSupport {

    /**
     * link : http://novel.mse.sogou.com/http_interface/getDirData.php?bookname=null&author=null&id=null&md=8391604493760144985
     * host : novel.mse.sogou.com
     * name : 搜狗
     * chaptersCount : 181
     * isCharge : false
     * _id : 5559c43078b77f8142ea9394
     * source : asogou
     * starting : false
     * lastChapter : 第一百八十章  要不咱不考了吧
     * updated : 2015-08-08T02:53:37.338Z
     */
    public String _id;
    private String link;
    private String host;
    private String name;
    private int chaptersCount;
    private boolean isCharge;
    private String source;
    private boolean starting;
    private String lastChapter;
    private String updated;
    private String sourceID;//来源id 用于保存_id
    private String novelID;//小说id
    private long refreshDate;//刷新时间

    public String getNovelID() {
        return novelID;
    }

    public void setNovelID(String novelID) {
        this.novelID = novelID;
    }

    public long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(long refreshDate) {
        this.refreshDate = refreshDate;
    }

    public void setCharge(boolean isCharge) {
        this.isCharge = isCharge;
    }

    public boolean isCharge() {
        return isCharge;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public void setIsCharge(boolean isCharge) {
        this.isCharge = isCharge;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getLink() {
        return link;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public boolean isIsCharge() {
        return isCharge;
    }

    public String get_id() {
        return _id;
    }

    public String getSource() {
        return source;
    }

    public boolean isStarting() {
        return starting;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public String getUpdated() {
        return updated;
    }
}
