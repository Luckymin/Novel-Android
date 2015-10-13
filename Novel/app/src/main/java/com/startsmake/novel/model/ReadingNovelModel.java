package com.startsmake.novel.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.startsmake.novel.bean.NovelBodyBean;
import com.startsmake.novel.bean.db.ChapterContent;
import com.startsmake.novel.bean.db.NovelChapters;
import com.startsmake.novel.bean.db.NovelSource;
import com.startsmake.novel.http.ApiParams;
import com.startsmake.novel.http.GsonRequest;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.utils.Constants;
import com.startsmake.novel.utils.GsonUtils;

import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-08-24
 * Description:
 */
public class ReadingNovelModel extends BaseModel {

    public void initNovelInfo(final String novelID, final ReadingNovelCallback callback) {
        ReadingNovelCallback initCallback = new ReadingNovelCallback() {
            @Override
            public void onGetSourceSuccess(List<NovelSource> sourceList) {
                //第二步 - 根据来源id获取小说章节目录
                getNovelChaptersBySourceID(novelID, sourceList.get(0).getSourceID(), this);
            }


            @Override
            public void onGetChaptersSuccess(NovelChapters novelChapters) {
                //第三步 - 根据章节名获取小说内容
                readingChapterBodyByChapterLink(novelChapters.getCurrChapterPosition(),0, novelChapters, callback);
            }

            @Override
            public void onGetSourceError() {
                callback.onInitError();
            }

            @Override
            public void onGetChaptersError() {
                callback.onInitError();
            }
        };
        //第一步 - 根据小说id获取小说来源
        getNovelSourceByID(novelID, initCallback);
    }

    @Override
    public void getNovelSourceByID(final String novelID, final ReadingNovelCallback callback) {
        List<NovelSource> locaSourceList = DataSupport.where("novelID = ? ", novelID).find(NovelSource.class);
        //数据库中已经存在来源json字符 并且 上次刷新时间小于一周
        final long timeMillis = System.currentTimeMillis();
        if (locaSourceList != null && locaSourceList.size() > 0 && timeMillis - locaSourceList.get(0).getRefreshDate() < Constants.MILLIS_WEEK) {
            Timber.d("getNovelSourceByID --> 数据库中获取小说来源");
            callback.onGetSourceSuccess(locaSourceList);
        } else {
            //拼接url地址获取小说来源列表
            ApiParams params = new ApiParams();
            params.setPath(HttpConstant.PATH_TOC);
            params.put("view", "summary");
            params.put("book", novelID);
            String url = params.getUrl();
            Timber.d("getNovelSourceByID --> 通过获取小说来源:%s", url);
            GsonRequest request = new GsonRequest<>(url, NovelSource[].class, new Response.Listener<NovelSource[]>() {
                @Override
                public void onResponse(NovelSource[] response) {
                    List<NovelSource> sourceList = Arrays.asList(response);
                    //设置来源对应的小说id ,刷新时间
                    for (NovelSource novelSource : sourceList) {
                        novelSource.setNovelID(novelID);
                        novelSource.setRefreshDate(timeMillis);
                        novelSource.setSourceID(novelSource.get_id());
                    }
                    //将来源list保存到本地数据库
                    DataSupport.saveAll(sourceList);
                    Timber.d("getNovelSourceByID --> 保存小说来源成功");
                    callback.onGetSourceSuccess(sourceList);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onGetSourceError();
                }
            });

            executeRequest(request);
        }

    }

    @Override
    public void getNovelChaptersBySourceID(final String novelID, String sourceID, final ReadingNovelCallback callback) {
        //获取小说章节信息
        List<NovelChapters> locaChapterList = DataSupport.where("sourceID = ? ", sourceID).find(NovelChapters.class);
        final long timeMillis = System.currentTimeMillis();
        if (locaChapterList != null && locaChapterList.size() > 0 && timeMillis - locaChapterList.get(0).getRefreshDate() < Constants.MILLIS_DAY) {
            //获取小说目录的json字符串
            String chaptersJson = locaChapterList.get(0).getChaptersJson();
            //转换成实体类
            NovelChapters novelChapters = GsonUtils.fromJson(chaptersJson, NovelChapters.class);
            Timber.d("getNovelChaptersBySourceID -->  数据库查询小说章节:%s", novelChapters.getName());
            if (novelChapters != null) {//如果json转换成功那么将跳出此方法，否则将进行网络请求
                locaChapterList.get(0).setChapters(novelChapters.getChapters());
                callback.onGetChaptersSuccess(locaChapterList.get(0));
                return;
            }
            Timber.d("getNovelChaptersBySourceID --> 小说章节信息json转换失败");
        }

        //拼接url
        ApiParams params = new ApiParams();
        params.setPath(HttpConstant.PATH_TOC + "/" + sourceID);
        params.put("view", "chapters");
        String url = params.getUrl();
        Timber.d("getNovelChaptersBySourceID --> 网络获取小说章节信息:%s", url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //将章节信息json字符串转换成实体类
                NovelChapters novelChapters = GsonUtils.fromJson(response, NovelChapters.class);
                if (novelChapters != null) {
                    //设置刷新时间,以及保存_id到sourceID中避免和数据库中的id冲突
                    novelChapters.setRefreshDate(System.currentTimeMillis());
                    novelChapters.setSourceID(novelChapters.get_id());
                    novelChapters.setChaptersJson(response);
                    novelChapters.setNovelID(novelID);
                    //保存目录信息保存到novelChapters表中
                    boolean isSuccess = novelChapters.save();
                    Timber.d("getNovelChaptersBySourceID --> 保存章节信息到本地:%s", isSuccess);
                    callback.onGetChaptersSuccess(novelChapters);
                } else {
                    callback.onGetSourceError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onGetChaptersError();
            }
        });
        executeRequest(request);

    }


    @Override
    public void readingChapterBodyByChapterLink(final int startReadPosition, final int Direction, final NovelChapters novelChapters, final ReadingNovelCallback callback) {
        final String novelID = novelChapters.getNovelID();//小说id
        final String sourceID = novelChapters.getSourceID();//章节来源id

        final NovelChapters.ChaptersInformation chaptersInformation = novelChapters.getChapters().get(startReadPosition);
        final String chapterContentLink = chaptersInformation.getLink();//章节内容链接

        //先去本地查找是否以及存在此章节内容，不存在在通过网络获取
        List<ChapterContent> chapterContents = DataSupport.where("novelID = ? and sourceID = ? and novelLink = ? ", novelID, sourceID, chapterContentLink).find(ChapterContent.class);
        if (chapterContents != null && chapterContents.size() > 0) {
            chaptersInformation.setChapterContent(chapterContents.get(0));
            //通知视图显示内容
            callback.onReadingChapterBodySuccess(startReadPosition,Direction, novelChapters);
            Timber.d("readingChapterBodyByChapterLink --> 本地获取章节内容成功:1、novelID:%s,  2、sourceID:%s , 3、chapterLink:%s", novelID, sourceID, chapterContentLink);
        } else {
            try {
                String url = HttpConstant.URL_CHAPTER + HttpConstant.PATH_CHAPTER + "/" + URLEncoder.encode(chapterContentLink, "UTF-8");
                Timber.d("readingChapterBodyByChapterLink --> 网络请求章节内容:%s", url);
                GsonRequest request = new GsonRequest<>(url, NovelBodyBean.class, new Response.Listener<NovelBodyBean>() {
                    @Override
                    public void onResponse(NovelBodyBean response) {
                        ChapterContent chapter = response.getChapter();
                        //设置小说id,来源id,小说章节链接
                        chapter.setNovelID(novelID);
                        chapter.setSourceID(sourceID);
                        chapter.setNovelLink(chapterContentLink);

                        chapter.setBody(optimizeNovelTextByBody(chapter.getBody()));

                        boolean isSuccess = chapter.save();
                        chaptersInformation.setChapterContent(chapter);
                        //通知视图显示内容
                        callback.onReadingChapterBodySuccess(startReadPosition,Direction, novelChapters);
                        Timber.d("readingChapterBodyByChapterLink --> 数据库保存章节内容:%s", isSuccess);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onReadingChapterBodyError();
                    }
                });
                executeRequest(request);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public String optimizeNovelTextByBody(String body) {
        String optimizeText = body.replaceAll("\\s*\n\\s*", "\n        ");
        return "        " + optimizeText;
    }

    public static class ReadingNovelCallback {
        //获取小说来源成功
        public void onGetSourceSuccess(List<NovelSource> sourceList) {
        }

        //获取小说来源失败
        public void onGetSourceError() {
        }

        //获取小说章节目录成功
        public void onGetChaptersSuccess(NovelChapters novelChapters) {
        }

        //获取小说章节目录失败
        public void onGetChaptersError() {
        }

        //获取小说章节内容成功
        public void onReadingChapterBodySuccess(int readPosition, int startReadPosition, NovelChapters novelChapters) {
        }

        //获取小说章节内容失败
        public void onReadingChapterBodyError() {
        }

        //初始化失败
        public void onInitError() {
        }
    }

}
