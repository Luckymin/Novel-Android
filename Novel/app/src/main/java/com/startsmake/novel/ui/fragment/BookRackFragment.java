package com.startsmake.novel.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minxiaoming.novel.R;


/**
 * User:Shine
 * Date:2015-08-11
 * Description:
 */
public class BookRackFragment extends BaseFragment {

    public static Fragment newInstance() {
        Fragment fragment = new BookRackFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_rack, container, false);
    }

}
