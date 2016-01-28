package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.TabAdapter;
import com.lm.android.gankapp.fragments.FavoriteFragment;
import com.lm.android.gankapp.models.ContentCategory;
import com.lm.android.gankapp.models.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by liumeng on 2016/1/12.
 */
public class FavoriteActivity extends BaseActivityWithLoadingDialog {
    private TabLayout tabs;
    private ViewPager viewPager;
    private String userId;

    @Override
    protected void initLoadingDialog() {
        // 不需要使用loading对话框
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_favorite);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.nav_fav);

        userId = BmobUser.getCurrentUser(context, User.class).getObjectId();
        initView();
    }

    private void initView() {
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(FavoriteFragment.newInstance(ContentCategory.ANDROID.getType(), userId), getString(R.string.category_android));
        adapter.addFragment(FavoriteFragment.newInstance(ContentCategory.IOS.getType(), userId), getString(R.string.category_ios));
        adapter.addFragment(FavoriteFragment.newInstance(ContentCategory.WEB.getType(), userId), getString(R.string.category_web));
        adapter.addFragment(FavoriteFragment.newInstance(ContentCategory.EXPAND.getType(), userId), getString(R.string.category_expand));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                SearchFavoriteActivity.actionStart(context, viewPager.getCurrentItem() + 1);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
