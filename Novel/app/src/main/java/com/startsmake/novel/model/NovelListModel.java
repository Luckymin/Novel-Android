package com.startsmake.novel.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.startsmake.novel.bean.NovelListBean;
import com.startsmake.novel.http.ApiParams;
import com.startsmake.novel.http.GsonRequest;
import com.startsmake.novel.http.HttpConstant;
import com.startsmake.novel.ui.fragment.NovelListFragment;
import com.startsmake.novel.utils.Utils;

import timber.log.Timber;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:
 */
public class NovelListModel extends BaseModel {


    @Override
    public void getNovelByTag(String tag, int tabType, int start, final NovelListCallback callback) {
        ApiParams apiParams = new ApiParams();
        apiParams.setPath(HttpConstant.PATH_BOOK_CLASSIFICATION_LIST_BY_TAG);
        apiParams.put("tag", Utils.encoderUTF8(tag));
        apiParams.put("start", start);
        apiParams.put("limit", HttpConstant.NOVEL_LIST_LIMIT);
        if (tabType != NovelListFragment.TAB_TYPE_ALL) {
            apiParams.put("isSerial", tabType == NovelListFragment.TAB_TYPE_CONTINUED);
        }
        String url = apiParams.getUrl();

        Timber.d("请求小说列表:%s", url);
        GsonRequest request = new GsonRequest<>(url, NovelListBean.class, new Response.Listener<NovelListBean>() {

            @Override
            public void onResponse(NovelListBean response) {
                if (response.isOk()) {
                    callback.onNovelListSuccess(response);
                } else {
                    callback.onNovelListError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onNovelListError();
            }
        });
        executeRequest(request);
    }


    public interface NovelListCallback {
        void onNovelListSuccess(NovelListBean bean);

        void onNovelListError();
    }
}
