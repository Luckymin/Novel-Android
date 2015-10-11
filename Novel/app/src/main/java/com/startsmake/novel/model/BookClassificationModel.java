package com.startsmake.novel.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.startsmake.novel.bean.BookClassificationBean;
import com.startsmake.novel.http.ApiParams;
import com.startsmake.novel.http.GsonRequest;
import com.startsmake.novel.http.HttpConstant;

import timber.log.Timber;


/**
 * User:Shine
 * Date:2015-08-11
 * Description:
 */
public class BookClassificationModel extends BaseModel {


    @Override
    public void toObtainCats(final BookClassificationCallback callback) {
        ApiParams apiParams = new ApiParams();
        apiParams.setPath(HttpConstant.PATH_BOOK_CLASSIFICATION);
        String url = apiParams.getUrl();
        Timber.d("请求小说分类:%s", url);
        GsonRequest request = new GsonRequest<>(url, BookClassificationBean.class, new Response.Listener<BookClassificationBean>() {
            @Override
            public void onResponse(BookClassificationBean catsBean) {
                if (catsBean.isOk()) {
                    callback.toObtainCatsSuccess(catsBean);
                } else {
                    callback.toObtainCatsError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.toObtainCatsError();
            }
        });
        executeRequest(request);
    }


    public interface BookClassificationCallback {
        void toObtainCatsSuccess(BookClassificationBean catsBean);

        void toObtainCatsError();
    }

}
