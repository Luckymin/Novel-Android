package com.startsmake.novel.bean.db;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-25
 * Description:小说章节目录实体类
 */
public class NovelChapters extends DataSupport {


    /**
     * chapters : [{"unreadble":false,"link":"http://m.baidu.com/tc?appui=alaxs&gid=1760017907&tn=utouch&page=ct&url=http%3A%2F%2Fwww.bxwx.org%2Fb%2F120%2F120489%2F21346437.html&cid=1760017907%7C12009018240851632962#2","title":"第1章 寒门之子"},{"unreadble":false,"link":"http://m.baidu.com/tc?appui=alaxs&gid=1760017907&tn=utouch&page=ct&url=http%3A%2F%2Fwww.kewaishu.net%2Fyuedu%2F64%2F64675%2F13606257.html&cid=1760017907%7C12029480723485716304#2","title":"第2章 气运与中邪"},{"unreadble":false,"link":"http://m.baidu.com/tc?appui=alaxs&gid=1760017907&tn=utouch&page=ct&url=http%3A%2F%2Fwww.bxwx.org%2Fb%2F120%2F120489%2F21346439.html&cid=1760017907%7C12070405688753882988#2","title":"第3章 农家小院是非多"}]
     * host : m.baidu.com
     * name : 百度
     * link : http://m.baidu.com/tc?appui=alaxs&gid=1760017907&tn=utouch&page=lp
     * _id : 55832b6c0cdaddae4e48c0cb
     * updated : 2015-08-06T11:29:38.034Z
     */
    public String _id;
    private int id;
    private List<ChaptersInformation> chapters;
    private String host;
    private String name;
    private String link;
    private String updated;
    private String chaptersJson;
    private String sourceID;
    private String novelID;
    private long refreshDate;
    private int currChapterPosition;//当前阅读到的章节位置
    private int currPagePosition;//当前分页位置

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(long refreshDate) {
        this.refreshDate = refreshDate;
    }

    public String getChaptersJson() {
        return chaptersJson;
    }

    public void setChaptersJson(String chaptersJson) {
        this.chaptersJson = chaptersJson;
    }

    public void setChapters(List<ChaptersInformation> chapters) {
        this.chapters = chapters;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<ChaptersInformation> getChapters() {
        return chapters;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String get_id() {
        return _id;
    }

    public String getUpdated() {
        return updated;
    }

    public int getCurrChapterPosition() {
        return currChapterPosition;
    }

    public void setCurrChapterPosition(int currChapterPosition) {
        this.currChapterPosition = currChapterPosition;
    }

    public int getCurrPagePosition() {
        return currPagePosition;
    }

    public void setCurrPagePosition(int currPagePosition) {
        this.currPagePosition = currPagePosition;
    }

    public static class ChaptersInformation {
        /**
         * unreadble : false
         * link : http://m.baidu.com/tc?appui=alaxs&gid=1760017907&tn=utouch&page=ct&url=http%3A%2F%2Fwww.bxwx.org%2Fb%2F120%2F120489%2F21346437.html&cid=1760017907%7C12009018240851632962#2
         * title : 第1章 寒门之子
         */
        private boolean unreadble;
        private String link;//章节链接
        private String title;//章节标题
        private ChapterContent chapterContent;//章节内容


        public ChapterContent getChapterContent() {
            return chapterContent;
        }

        public void setChapterContent(ChapterContent chapterContent) {
            this.chapterContent = chapterContent;
        }

        public void setUnreadble(boolean unreadble) {
            this.unreadble = unreadble;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isUnreadble() {
            return unreadble;
        }

        public String getLink() {
            return link;
        }

        public String getTitle() {
            return title;
        }

    }
}
