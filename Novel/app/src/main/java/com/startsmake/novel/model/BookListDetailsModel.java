package com.startsmake.novel.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.startsmake.novel.bean.BookListDetailsBean;
import com.startsmake.novel.http.ApiParams;
import com.startsmake.novel.http.GsonRequest;
import com.startsmake.novel.http.HttpConstant;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-10-26
 * Description:
 */
public class BookListDetailsModel extends BaseModel {


    @Override
    public void getBookListDetails(String bookListID, final BookListDetailsListener callback) {
        ApiParams params = new ApiParams();
        String url = params.getUrl(HttpConstant.PATH_BOOK_LIST, bookListID);
        Timber.d("网络请求 ---> 书单详情url : %s", url);
        GsonRequest request = new GsonRequest<>(url, BookListDetailsBean.class, new Response.Listener<BookListDetailsBean>() {
            @Override
            public void onResponse(BookListDetailsBean response) {
                if (response.isOk()) {
                    callback.getBookListDetailsSuccess(response);
                } else {
                    callback.getBookListDetailsError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.getBookListDetailsError();
            }
        });
        executeRequest(request);
    }

    public interface BookListDetailsListener {
        void getBookListDetailsSuccess(BookListDetailsBean response);

        void getBookListDetailsError();
    }
}
