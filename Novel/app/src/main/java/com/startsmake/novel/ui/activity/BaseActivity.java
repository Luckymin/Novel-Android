package com.startsmake.novel.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.startsmake.novel.R;
import com.startsmake.novel.model.BaseModel;

/**
 * User:Shine
 * Date:2015-08-09
 * Description:
 */
public class BaseActivity extends AppCompatActivity {

    protected BaseModel mModel;
    protected Toolbar mToolbar;


    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onClickHomeMenuItem();
        }
        return super.onOptionsItemSelected(item);
    }


    protected void onClickHomeMenuItem() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mModel != null) {
            mModel.cancelAll();
        }
        super.onDestroy();
    }
}
