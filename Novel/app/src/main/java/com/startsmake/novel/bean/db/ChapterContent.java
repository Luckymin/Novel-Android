package com.startsmake.novel.bean.db;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-25
 * Description:小说章节内容
 */
public class ChapterContent extends DataSupport {

    private String sourceID;
    private String novelID;
    private String novelLink;
    private String title;
    private String body;
    public List<CharSequence> mPages;


    public List<CharSequence> getPages() {
        return mPages;
    }

    public void setPages(List<CharSequence> pages) {
        mPages = pages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getNovelID() {
        return novelID;
    }

    public void setNovelID(String novelID) {
        this.novelID = novelID;
    }

    public String getNovelLink() {
        return novelLink;
    }

    public void setNovelLink(String novelLink) {
        this.novelLink = novelLink;
    }
}
