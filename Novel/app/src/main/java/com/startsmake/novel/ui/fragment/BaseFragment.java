package com.startsmake.novel.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.startsmake.novel.model.BaseModel;

/**
 * User:Shine
 * Date:2015-08-11
 * Description:
 */
public class BaseFragment extends Fragment {

    protected BaseModel mModel;

    private boolean isViewCreate = false;
    private boolean isInitialize = false;
    private boolean isVisibleHint = false;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreated(view);
        isViewCreate = true;
        if (isVisibleHint) {
            setUserVisibleHint(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isViewCreate && !isInitialize) {
                isInitialize = isVisibleToUser;
                onViewInitializeVisible(!isVisibleHint);
            }
        }
        isVisibleHint = isVisibleToUser;
    }

    protected void onViewCreated(View view) {

    }

    /**
     * @param isVisibleHint 当前视图是否通过预加载方式创建
     */
    protected void onViewInitializeVisible(boolean isVisibleHint) {
    }

    @Override
    public void onDestroy() {
        if (mModel != null) {
            mModel.cancelAll();
        }
        super.onDestroy();
    }
}
