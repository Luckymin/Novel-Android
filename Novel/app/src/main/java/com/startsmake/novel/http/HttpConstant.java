package com.startsmake.novel.http;

/**
 * User:Shine
 * Date:2015-08-11
 * Description:网络请求常量
 */
public class HttpConstant {

    public static final String URL = "http://api.zhuishushenqi.com";
    public static final String URL_PICTURE = "http://statics.zhuishushenqi.com";
    public static final String URL_CHAPTER = "http://chapter.zhuishushenqi.com";

    public static final String PATH_BOOK = "/book";
    public static final String PATH_TOC = "/toc";
    public static final String PATH_CHAPTER = "/chapter";
    /*分类*/
    public static final String PATH_BOOK_CLASSIFICATION = "/cats";
    /*分类-小说列表*/
    public static final String PATH_BOOK_CLASSIFICATION_LIST_BY_TAG = PATH_BOOK + "/by-tag";


    public static final int NOVEL_LIST_LIMIT = 50;


}
