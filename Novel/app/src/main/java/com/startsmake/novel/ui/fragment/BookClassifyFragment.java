package com.startsmake.novel.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.startsmake.novel.Interfaces.OnClassificationCallback;
import com.startsmake.novel.R;
import com.startsmake.novel.bean.BookClassificationBean;
import com.startsmake.novel.model.BookClassificationModel;
import com.startsmake.novel.ui.adapter.TabLayoutViewPagerAdapter;

import java.util.ArrayList;


/**
 * User:Shine
 * Date:2015-08-11
 * Description:分类
 */
public class BookClassifyFragment extends BaseFragment implements View.OnClickListener, BookClassificationModel.BookClassificationCallback {

    private TabLayoutViewPagerAdapter mPagerAdapter;

    private ViewPager mVpClassification;
    private ProgressBar mPbLoading;
    private LinearLayout mLlLoadingFailure;
    private Button mBtnRetry;

    public static Fragment newInstance() {
        Fragment fragment = new BookClassifyFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new BookClassificationModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_classification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVpClassification = (ViewPager) view.findViewById(R.id.vpClassification);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        mLlLoadingFailure = (LinearLayout) view.findViewById(R.id.llLoadingFailure);
        mBtnRetry = (Button) view.findViewById(R.id.btnRetry);
        mBtnRetry.setOnClickListener(this);
        //获取分类
        mModel.toObtainCats(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * 获取分类成功
     */
    @Override
    public void toObtainCatsSuccess(BookClassificationBean catsBean) {
        mPbLoading.setVisibility(View.GONE);
        mPagerAdapter = new TabLayoutViewPagerAdapter(getActivity().getSupportFragmentManager());
        mPagerAdapter.addFragment(SexClassifyGridFragment.newInstance((ArrayList<BookClassificationBean.BookCatsEntity>) catsBean.getMale()), getString(R.string.classification_tab_name_male));
        mPagerAdapter.addFragment(SexClassifyGridFragment.newInstance((ArrayList<BookClassificationBean.BookCatsEntity>) catsBean.getFemale()), getString(R.string.classification_tab_name_female));
        mVpClassification.setAdapter(mPagerAdapter);
        ((OnClassificationCallback) getActivity()).initialTabLayout(mVpClassification);
    }

    /**
     * 获取分类失败
     */
    @Override
    public void toObtainCatsError() {
        mPbLoading.setVisibility(View.GONE);
        mLlLoadingFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {//重新加载分类
            mModel.toObtainCats(this);
            mPbLoading.setVisibility(View.VISIBLE);
            mLlLoadingFailure.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
