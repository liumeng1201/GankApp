package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.UserInfoAdapter;
import com.lm.android.gankapp.component.DividerItemDecoration;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.models.UserInfoModel;
import com.lm.android.gankapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MeActivity extends BaseActivityWithLoadingDialog {
    private RecyclerView recyclerView;
    private UserInfoAdapter adapter;
    private OnContentItemClickListener itemClickListener;

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_me);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_activity_me);

        recyclerView = (RecyclerView) findViewById(R.id.personal_info);

        List<UserInfoModel> userInfo = new ArrayList<>();
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_AVATAR, null, "http://img2.3lian.com/2014/f5/158/d/86.jpg"));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_TEXT, "昵称", "BHawK"));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_TEXT, "地区", "上海市-杨浦区"));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_TEXT, "账号绑定", "QQ、微博"));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_TEXT, "个人主页", "http://test.com"));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_TEXT, "个性签名", "不战而屈人之兵"));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_TEXT, "个人标签", "Android、Java、Linux"));
        itemClickListener = new OnContentItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Utils.showToastShort(context, "click " + position);
            }
        };

        adapter = new UserInfoAdapter(userInfo);
        adapter.setOnItemClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initLoadingDialog() {
        loadingDialog = Utils.getLoadingDialog(context, getString(R.string.saving_user_info));
    }
}
