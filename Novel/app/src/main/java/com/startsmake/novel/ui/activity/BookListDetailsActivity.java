package com.startsmake.novel.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.startsmake.novel.R;
import com.startsmake.novel.bean.db.BookList;

/**
 * User:Shine
 * Date:2015-10-22
 * Description:
 */
public class BookListDetailsActivity extends BaseActivity {

    public static final String EXTRA_BOOK_LIST = "com.startsmake.novel.bookList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_details);

    }

    public static void openActivity(Context context, View view, BookList bookList) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BOOK_LIST, bookList);
        context.startActivity(intent);
    }


}
