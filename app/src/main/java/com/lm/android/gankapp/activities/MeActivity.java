package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.UserInfoAdapter;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.models.UserInfoModel;
import com.lm.android.gankapp.utils.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MeActivity extends BaseActivityWithLoadingDialog {
    private RecyclerView recyclerView;
    private UserInfoAdapter adapter;
    private OnContentItemClickListener itemClickListener;
    private List<UserInfoModel> userInfo;

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
        userInfo = new ArrayList<>();
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
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        User user = BmobUser.getCurrentUser(context, User.class);
        setUserInfo(user);
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

    private void setUserInfo(User user) {
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_AVATAR, null, user.getAvatar()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "昵称", user.getNickName()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "地区", user.getProvinceName() + "-" + user.getCityName()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_SNS_ACCOUNT, "账号绑定", String.valueOf(user.getWechatBinded() ? 1 : 0) + String.valueOf(user.getQqBinded() ? 1 : 0) + String.valueOf(user.getSinaWeiboBinded() ? 1 : 0)));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "个人主页", user.getHomePage()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "个性签名", user.getSignature()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "个人标签", "Android、Java、Linux"));

        adapter.refresh(userInfo);
    }
}
