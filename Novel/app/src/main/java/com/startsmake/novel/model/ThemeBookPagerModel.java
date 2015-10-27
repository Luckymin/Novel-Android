package com.startsmake.novel.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.startsmake.novel.bean.ThemeBookListBean;
import com.startsmake.novel.bean.db.BookList;
import com.startsmake.novel.http.ApiParams;
import com.startsmake.novel.http.GsonRequest;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.ui.fragment.ThemeBookListFragment;

import org.litepal.crud.DataSupport;

import java.util.List;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class ThemeBookPagerModel extends BaseModel {

    @Override
    public void getThemeBookList(int pagerType, final int start, final ThemeBookPagerCallback callback) {
        if (pagerType == ThemeBookListFragment.PAGER_TYPE_MY_COLLECT) {//通过本地数据库获取我的收藏数据
            List<BookList> bookLists = DataSupport.order("orderIndex desc").find(BookList.class);
            callback.getThemeBookListSuccess(0, bookLists);
        } else {//通过网络获取数据
            String url = splicingUrl(pagerType, start);
            Timber.d("网络请求 --> type : %s ,url : %s", pagerType, url);
            GsonRequest request = new GsonRequest<>(url, ThemeBookListBean.class, new Response.Listener<ThemeBookListBean>() {
                @Override
                public void onResponse(ThemeBookListBean response) {
                    if (response.getOk()) {
                        callback.getThemeBookListSuccess(start, response.getBookLists());
                    } else {
                        callback.getThemeBookListError();
                    }
                    Timber.d("请求结果 --> %s", response.getOk(), response.getBookLists().size());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.getThemeBookListError();
                    Timber.d("请求结果 --> 请求失败 : %s", error.getMessage());
                }
            });

            executeRequest(request);
        }


    }

    private String splicingUrl(int pagerType, int start) {
        ApiParams params = new ApiParams();
        params.setPath(HttpConstant.PATH_BOOK_LIST);

        String durationValue = (pagerType == ThemeBookListFragment.PAGER_TYPE_HOT ? "last-seven-days" : "all");
        params.put("duration", durationValue);

        String sortValue = (pagerType == ThemeBookListFragment.PAGER_TYPE_LATEST_POSTS ? "created" : "collectorCount");
        params.put("sort", sortValue);

        params.put("start", start);

        return params.getUrl();
    }

    public interface ThemeBookPagerCallback {
        void getThemeBookListSuccess(int start, List<BookList> data);

        void getThemeBookListError();
    }
}
