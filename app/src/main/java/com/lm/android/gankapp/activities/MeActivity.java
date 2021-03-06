package com.lm.android.gankapp.activities;

import android.content.DialogInterface;
import android.content.res.Resources;
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
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.ShareListAdapter;
import com.lm.android.gankapp.adapters.SimpleAdapter;
import com.lm.android.gankapp.adapters.UserInfoAdapter;
import com.lm.android.gankapp.component.CustomLinearLayoutManager;
import com.lm.android.gankapp.interfaces.ShareSDKOptCallback;
import com.lm.android.gankapp.listener.MyBmobUpdateListener;
import com.lm.android.gankapp.listener.MyBmobUploadListener;
import com.lm.android.gankapp.listener.MyPlatformActionListener;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.SharePlatItem;
import com.lm.android.gankapp.models.ThirdPartyOptType;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.models.UserInfoModel;
import com.lm.android.gankapp.utils.LogUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class MeActivity extends BaseActivityWithLoadingDialog {
    private RecyclerView recyclerView;
    private UserInfoAdapter adapter;
    private OnContentItemClickListener itemClickListener;
    private List<UserInfoModel> userInfo;

    private User currentUser;
    private boolean avatarChange = false;
    private boolean userInfoChange = false;

    private AlertDialog avatarDialog;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private final int REQUEST_CODE_GALLERY = 301;
    private final int REQUEST_CODE_CAMERA = 302;
    private AlertDialog snsBindDialog;

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
                        showSnsBindDialog();
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

        mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                String path = resultList.get(0).getPhotoPath();
                if (!StringUtils.isEmpty(path)) {
                    avatarChange = true;
                    currentUser.setAvatar(path);
                    setUserInfo(currentUser);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Utils.showToastShort(context, "获取照片失败，请重试");
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
        if (avatarChange) {
            // 头像发生变动
            updateAvatarAndSaveUserInfo(currentUser.getAvatar());
        } else if (userInfoChange) {
            // 头像之外的信息发生变动
            upLoadUserInfo();
        } else {
            finish();
        }
    }

    private void upLoadUserInfo() {
        showLoadingDialog();
        currentUser.update(context, new MyBmobUpdateListener() {
            @Override
            protected void successOpt() {
                dismissLoadingDialog();
                Utils.showToastShort(context, "用户信息更新成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            protected void failureOpt(int i, String s) {
                dismissLoadingDialog();
                Utils.showToastShort(context, "更新用户信息失败，请稍候重试");
                finish();
            }
        });
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

    /**
     * 显示用户拍照、从相册选取图片对话框
     */
    private void showAvatarDialog() {
        if (avatarDialog == null) {
            avatarDialog = new AlertDialog.Builder(context).create();
            avatarDialog.setCanceledOnTouchOutside(true);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_avatar_dialog, null);
            RecyclerView avatarList = (RecyclerView) view.findViewById(R.id.take_avatar_option);
            List<String> options = new ArrayList<>();
            options.add(getString(R.string.avatar_from_camera));
            options.add(getString(R.string.avatar_from_image));
            SimpleAdapter adapter = new SimpleAdapter(options);
            adapter.setItemClickListener(new OnContentItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    avatarDialog.dismiss();
                    if (position == 0) {
                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                    } else if (position == 1) {
                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                    }
                }
            });
            avatarList.setAdapter(adapter);
            avatarList.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            avatarList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
            avatarDialog.setView(view);
            avatarDialog.show();
        } else {
            if (!avatarDialog.isShowing()) {
                avatarDialog.show();
            }
        }
    }

    /**
     * @param position 用户信息列表中的位置
     */
    private void showInputDialog(final int position, String oldValue) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_input_dialog, null);
        final TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.input);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title);
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
        dialogTitle.setText(title);
        inputDialog.setCanceledOnTouchOutside(false);
        inputDialog.setView(view);
        inputDialog.show();
    }

    /**
     * 显示绑定第三方账号对话框
     */
    private void showSnsBindDialog() {
        if (snsBindDialog == null) {
            snsBindDialog = new AlertDialog.Builder(context).create();
            View convertView = LayoutInflater.from(context).inflate(R.layout.layout_share_dialog, null);
            RecyclerView view = (RecyclerView) convertView.findViewById(R.id.share_plat_list);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText("绑定第三方账号");
            List<SharePlatItem> platList = new ArrayList<>();
            Resources resources = context.getResources();
            platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_wechat_checked, resources.getString(R.string.ssdk_wechat_title)));
            platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_qq_checked, resources.getString(R.string.ssdk_qq)));
            platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_sinaweibo_checked, resources.getString(R.string.ssdk_sinaweibo)));
            final ShareListAdapter bindListAdapter = new ShareListAdapter(platList);
            view.setAdapter(bindListAdapter);
            view.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context).build());
            snsBindDialog.setView(convertView);
            ShareSDKOptCallback shareSDKOptCallback = new ShareSDKOptCallback() {
                @Override
                public void onSuccess(Platform platform, HashMap<String, Object> result) {
                    // 第三方授权成功，可以获取第三方用户信息，之后进行用户注册
                    showLoadingDialog();

                    String accesstoken = platform.getDb().getToken();
                    String experseIn = Long.toString(platform.getDb().getExpiresIn());
                    String userId = platform.getDb().getUserId();
                    final String nickName = platform.getDb().get("nickname");
                    final String avatarUrl = platform.getDb().getUserIcon();
                    LogUtils.logi(accesstoken + "\n" + experseIn + "\n" + userId + "\n" + nickName + "\n" + avatarUrl);

                    BmobUser.BmobThirdUserAuth authInfo = null;
                    if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                        // 使用QQ登录
                        authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, accesstoken, experseIn, userId);
                    } else if (platform.getName().equalsIgnoreCase(SinaWeibo.NAME)) {
                        // 使用新浪微博登录
                        authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIBO, accesstoken, experseIn, userId);
                    } else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                        authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIXIN, accesstoken, experseIn, userId);
                        return;
                    }
                    final String snsType = authInfo.getSnsType();
                    BmobUser.associateWithAuthData(context, authInfo, new MyBmobUpdateListener() {
                        @Override
                        protected void successOpt() {
                            thirdUserBindSuccess(snsType, nickName, avatarUrl);
                        }

                        @Override
                        public void failureOpt(int i, String s) {
                            dismissLoadingDialog();
                            Utils.showToastShort(context, getString(R.string.login_failed) + s);
                        }
                    });
                }

                @Override
                public void onFailed(Throwable throwable) {
                    Utils.showToastShort(context, getString(R.string.login_failed) + throwable.getMessage());
                }

                @Override
                public void onCancel() {
                    Utils.showToastShort(context, getString(R.string.login_cancel));
                }
            };
            final MyPlatformActionListener platformActionListener = new MyPlatformActionListener(ThirdPartyOptType.LOGIN, shareSDKOptCallback);
            bindListAdapter.setOnItemClickListener(new OnContentItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    Platform platform = null;
                    switch (bindListAdapter.getItem(position).getPlatImage()) {
                        case R.drawable.ssdk_oks_skyblue_logo_wechat_checked:
                            Utils.showToastShort(context, "暂不支持绑定微信");
                            return;
                        case R.drawable.ssdk_oks_skyblue_logo_qq_checked:
                            platform = ShareSDK.getPlatform(QQ.NAME);
                            break;
                        case R.drawable.ssdk_oks_skyblue_logo_sinaweibo_checked:
                            platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                            break;
                    }
                    platform.setPlatformActionListener(platformActionListener);
                    platform.showUser(null);
                    snsBindDialog.dismiss();
                }
            });
            snsBindDialog.setCanceledOnTouchOutside(true);
            snsBindDialog.show();
        } else if (!snsBindDialog.isShowing()) {
            snsBindDialog.show();
        }
    }

    private void thirdUserBindSuccess(String snsType, String nickName, String avatarUrl) {
        // 设置第三方绑定
        if (snsType.equalsIgnoreCase(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ)) {
            currentUser.setQqBinded(true);
        } else if (snsType.equalsIgnoreCase(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIBO)) {
            currentUser.setSinaWeiboBinded(true);
        } else if (snsType.equalsIgnoreCase(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIXIN)) {
            currentUser.setWechatBinded(true);
        }
        if (StringUtils.isEmpty(currentUser.getNickName())) {
            currentUser.setNickName(nickName);
        }
        if (StringUtils.isEmpty(currentUser.getAvatar())) {
            currentUser.setAvatar(avatarUrl);
        }
        setUserInfo(currentUser);
        dismissLoadingDialog();
        userInfoChange = true;
    }

    /**
     * 保存用户头像之后更新用户信息
     *
     * @param path 用户头像在本地SD卡上的路径
     */
    private void updateAvatarAndSaveUserInfo(String path) {
        showLoadingDialog();
        Utils.uploadSingleFile(context, path, new MyBmobUploadListener() {
            @Override
            public void onSuccess(String fileName, String url, BmobFile file, String accessUrl) {
                currentUser.setAvatar(accessUrl);
                // 更新用户信息到bmob后台
                upLoadUserInfo();
            }

            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                dismissLoadingDialog();
                Utils.showToastShort(context, "更新用户信息失败，请稍候重试");
            }
        });
    }
}