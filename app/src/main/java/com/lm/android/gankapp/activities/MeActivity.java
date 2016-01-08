package com.lm.android.gankapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.UserInfoAdapter;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.listener.MyBmobUpdateListener;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.models.UserInfoModel;
import com.lm.android.gankapp.utils.StringUtils;
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

    private User currentUser;
    private boolean userInfoChange = false;

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
                switch (position) {
                    case 0:
                        // 头像
                        showAvatarDialog();
                        break;
                    case 1:
                        // 昵称
                        showInputDialog(position, currentUser.getNickName());
                        break;
                    case 2:
                        // 地区
                        break;
                    case 3:
                        // 账号绑定
                        break;
                    case 4:
                        // 个人主页
                        showInputDialog(position, currentUser.getHomePage());
                        break;
                    case 5:
                        // 个性签名
                        showInputDialog(position, currentUser.getSignature());
                        break;
                    case 6:
                        // 个人标签
                        break;
                }
            }
        };

        adapter = new UserInfoAdapter(userInfo);
        adapter.setOnItemClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        currentUser = BmobUser.getCurrentUser(context, User.class);
        setUserInfo(currentUser);
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
            case R.id.action_save:
                backOpt();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backOpt();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backOpt() {
        if (userInfoChange) {
            loadingDialog.show();
            currentUser.update(context, new MyBmobUpdateListener() {
                @Override
                protected void successOpt() {
                    loadingDialog.dismiss();
                    Utils.showToastShort(context, "用户信息更新成功");
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                protected void failureOpt(int i, String s) {
                    loadingDialog.dismiss();
                    Utils.showToastShort(context, "更新用户信息失败，请稍候重试");
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void initLoadingDialog() {
        loadingDialog = Utils.getLoadingDialog(context, getString(R.string.saving_user_info));
    }

    private void setUserInfo(User user) {
        userInfo.clear();
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_AVATAR, null, user.getAvatar()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, getString(R.string.nickname), user.getNickName()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "地区", (StringUtils.isEmpty(user.getProvinceName()) && (StringUtils.isEmpty(user.getCityName()))) ? null : (user.getProvinceName() + "-" + user.getCityName())));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_SNS_ACCOUNT, "账号绑定", String.valueOf((user.getWechatBinded() != null && user.getWechatBinded()) ? 1 : 0)
                + String.valueOf((user.getQqBinded() != null && user.getQqBinded()) ? 1 : 0)
                + String.valueOf((user.getSinaWeiboBinded() != null && user.getSinaWeiboBinded()) ? 1 : 0)));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, getString(R.string.homepage), user.getHomePage()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, getString(R.string.Signature), user.getSignature()));
        userInfo.add(new UserInfoModel(UserInfoAdapter.USER_INFO_TYPE_NORMAL_TEXT, "个人标签", "Android、Java、Linux"));

        adapter.refresh(userInfo);
    }

    private void showAvatarDialog() {
        AlertDialog avatarDialog = new AlertDialog.Builder(context).create();
        avatarDialog.setTitle(getString(R.string.set_user_avatar));
        avatarDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_avatar_dialog, null);
        avatarDialog.setView(view);
        avatarDialog.show();
    }

    /**
     * @param position 用户信息列表中的位置
     */
    private void showInputDialog(final int position, String oldValue) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_input_dialog, null);
        final TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.input);
        if (!StringUtils.isEmpty(oldValue)) {
            inputLayout.getEditText().setText(oldValue);
            inputLayout.getEditText().setSelection(oldValue.length());
        }
        AlertDialog inputDialog = new AlertDialog.Builder(context)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = inputLayout.getEditText().getText().toString().trim();
                        if (!StringUtils.isEmpty(input)) {
                            switch (position) {
                                case 1:
                                    currentUser.setNickName(input);
                                    break;
                                case 4:
                                    currentUser.setHomePage(input);
                                    break;
                                case 5:
                                    currentUser.setSignature(input);
                                    break;
                            }
                            setUserInfo(currentUser);
                            userInfoChange = true;
                        }
                    }
                }).setNegativeButton(getString(R.string.cancel), null).create();
        String title = null;
        if (position == 1) {
            title = getString(R.string.set_user_nickname);
            inputLayout.setHint(getString(R.string.nickname));
        } else if (position == 4) {
            title = getString(R.string.set_user_homepage);
            inputLayout.setHint(getString(R.string.homepage));
        } else if (position == 5) {
            title = getString(R.string.set_user_signature);
            inputLayout.setHint(getString(R.string.Signature));
        }
        inputDialog.setTitle(title);
        inputDialog.setCanceledOnTouchOutside(false);
        inputDialog.setView(view);
        inputDialog.show();
    }
}