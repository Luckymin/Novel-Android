package com.startsmake.novel.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startsmake.novel.R;
import com.startsmake.novel.bean.BookClassificationBean;
import com.startsmake.novel.ui.activity.ClassifyAndRankingNovelListActivity;
import com.startsmake.novel.ui.adapter.ClassificationListAdapter;
import com.startsmake.novel.ui.widget.itemdecoration.GridLayoutSpaceItemDecoration;
import com.startsmake.novel.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-08-11
 * Description:
 */
public class SexClassifyGridFragment extends BaseFragment implements ClassificationListAdapter.OnClassifyItemClickListener {

    private static final String EXTRA_BOOK_CATS = "com.minxiaoming.bookCatsList";

    //小说分类列表 玄幻 都市...
    private List<BookClassificationBean.BookCatsEntity> mBookCatsList;

    //分类列表view
    private RecyclerView rvClassificationList;

    public static Fragment newInstance(ArrayList<BookClassificationBean.BookCatsEntity> bookCatsList) {
        Fragment fragment = new SexClassifyGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_BOOK_CATS, bookCatsList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookCatsList = getArguments().getParcelableArrayList(EXTRA_BOOK_CATS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sex_classify_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvClassificationList = (RecyclerView) view.findViewById(R.id.rvClassificationList);
        rvClassificationList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvClassificationList.setAdapter(new ClassificationListAdapter(mBookCatsList, this));
        rvClassificationList.addItemDecoration(new GridLayoutSpaceItemDecoration(Utils.dpToPx(8), 3));
        rvClassificationList.setHasFixedSize(true);

    }

    @Override
    public void onClickClassifyItem(View view, BookClassificationBean.BookCatsEntity bookCatsEntity) {
        ClassifyAndRankingNovelListActivity.openActivity(getActivity(), view,bookCatsEntity.getName());
    }
}
