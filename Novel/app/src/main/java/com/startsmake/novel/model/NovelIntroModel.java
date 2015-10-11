package com.startsmake.novel.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.startsmake.novel.bean.NovelInfoBean;
import com.startsmake.novel.http.ApiParams;
import com.startsmake.novel.http.GsonRequest;
import com.startsmake.novel.http.HttpConstant;

/**
 * User:Shine
 * Date:2015-08-13
 * Description:
 */
public class NovelIntroModel extends BaseModel {

    @Override
    public void getNovelInfoByID(String novelID, final NovelIntroCallback callback) {
        ApiParams apiParams = new ApiParams();
        String url = apiParams.getUrl(HttpConstant.PATH_BOOK, novelID);
        GsonRequest request = new GsonRequest<>(url, NovelInfoBean.class, new Response.Listener<NovelInfoBean>() {
            @Override
            public void onResponse(NovelInfoBean response) {
                callback.getNovelInfoSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.getNovelInfoError();
            }
        });
        executeRequest(request);
    }


    public interface NovelIntroCallback {
        void getNovelInfoSuccess(NovelInfoBean bean);

        void getNovelInfoError();
    }
}
