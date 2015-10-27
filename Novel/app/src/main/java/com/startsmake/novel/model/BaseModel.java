package com.startsmake.novel.model;

import com.android.volley.Request;
import com.startsmake.novel.bean.db.NovelChapters;
import com.startsmake.novel.http.RequestManager;


/**
 * User:Shine
 * Date:2015-08-11
 * Description:
 */
public abstract class BaseModel {


    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    public void cancelAll() {
        RequestManager.cancelAll(this);
    }

    /**
     * 获取小说分类
     */
    public void toObtainCats(BookClassificationModel.BookClassificationCallback callback) {
    }

    /**
     * 获取小说列表 根据分类
     *
     * @param tag     分类tag 玄幻 都市...
     * @param tabType 页类型 1.全部 2.连载 3.完结
     */
    public void getNovelByTag(String tag, int tabType, int start, final NovelListModel.NovelListCallback callback) {

    }

    /**
     * 获取小说信息
     *
     * @param novelID 小说id
     */
    public void getNovelInfoByID(String novelID, NovelIntroModel.NovelIntroCallback callback) {
    }


    /**
     * 获取小说来源
     *
     * @param novelID 小说id
     */
    public void getNovelSourceByID(String novelID, ReadingNovelModel.ReadingNovelCallback callback) {

    }

    /**
     * 根据小说来源id获取小说章节目录
     *
     * @param novelID  小说的id
     * @param sourceID 小说来源的id
     */
    public void getNovelChaptersBySourceID(String novelID, String sourceID, ReadingNovelModel.ReadingNovelCallback callback) {

    }


    /**
     * 小说阅读器界面初始化时获取小说内容 流程  获取小说来源 - 获取小说章节目录 - 根据章节获取小说内容
     *
     * @param novelID  小说ID
     * @param callback
     */
    public void initNovelInfo(final String novelID, ReadingNovelModel.ReadingNovelCallback callback) {

    }

    /**
     * 读取小说章节内容根据小说章节标题
     *
     * @param startReadPosition
     * @param novelChapters     小说章节信息，包含章节列表，来源id，等等内容
     */
    public void readingChapterBodyByChapterLink(int startReadPosition, int Direction, NovelChapters novelChapters, ReadingNovelModel.ReadingNovelCallback callback) {
    }

    /**
     * 获取主题书单
     *
     * @param pagerType 类型 - 我的收藏，本周最热，最新发布，最多收藏
     * @param start     从第几行开始读取数据
     */
    public void getThemeBookList(int pagerType, int start, ThemeBookPagerModel.ThemeBookPagerCallback callback) {
    }

    /**
     * 获取书单详情
     *
     * @param bookListID 书单id
     */
    public void getBookListDetails(String bookListID, BookListDetailsModel.BookListDetailsListener callback) {
    }
}
