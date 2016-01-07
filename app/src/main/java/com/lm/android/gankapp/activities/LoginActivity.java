package com.lm.android.gankapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.interfaces.ShareSDKOptCallback;
import com.lm.android.gankapp.listener.MyBmobOtherLoginListener;
import com.lm.android.gankapp.listener.MyBmobSaveListener;
import com.lm.android.gankapp.listener.MyBmobUpdateListener;
import com.lm.android.gankapp.listener.MyPlatformActionListener;
import com.lm.android.gankapp.models.ThirdPartyOptType;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.LogUtils;
import com.lm.android.gankapp.utils.PropertyUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivityWithLoadingDialog implements View.OnClickListener {
    private Button btnLogin;
    private Button btnRegister;
    private ImageButton btnWechat;
    private ImageButton btnQQ;
    private ImageButton btnWeibo;
    private TextInputLayout edtUsername;
    private TextInputLayout edtPassword;

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.login);

        initView();
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnWechat = (ImageButton) findViewById(R.id.login_btn_wechat);
        btnQQ = (ImageButton) findViewById(R.id.login_btn_qq);
        btnWeibo = (ImageButton) findViewById(R.id.login_btn_sinaweibo);
        edtUsername = (TextInputLayout) findViewById(R.id.input_username);
        edtPassword = (TextInputLayout) findViewById(R.id.input_password);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnWechat.setOnClickListener(this);
        btnQQ.setOnClickListener(this);
        btnWeibo.setOnClickListener(this);
        edtPassword.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginOpt();
                }
                return true;
            }
        });
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginOpt();
                break;
            case R.id.btn_register:
                Intent toRegister = new Intent(context, RegisterActivity.class);
                startActivityForResult(toRegister, Utils.REQUEST_CODE_REGISTER);
                break;
            case R.id.login_btn_wechat:
                Utils.showToastShort(context, getString(R.string.login_wechat_not_support));
                break;
            case R.id.login_btn_qq:
                loginWithThirdAccount(ShareSDK.getPlatform(QQ.NAME));
                break;
            case R.id.login_btn_sinaweibo:
                loginWithThirdAccount(ShareSDK.getPlatform(SinaWeibo.NAME));
                break;
        }
    }

    private void loginOpt() {
        final Editable name = edtUsername.getEditText().getText();
        final Editable passwd = edtPassword.getEditText().getText();
        if (canLogin(name, passwd)) {
            showLoadingDialog();
            User user = new User();
            user.setUsername(name.toString().trim());
            user.setPassword(passwd.toString().trim());
            user.login(context, new MyBmobSaveListener() {
                @Override
                protected void successOpt() {
                    PropertyUtils.setUserLoginStatus("true", propertyContentDao);
                    dismissLoadingDialog();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                protected void failureOpt(int i, String s) {
                    dismissLoadingDialog();
                    Utils.showToastShort(context, getString(R.string.login_failed) + s);
                }
            });
        } else {
            Utils.showToastShort(context, getString(R.string.login_input_null));
        }
    }

    private boolean canLogin(Editable name, Editable passwd) {
        if (name == null || passwd == null) {
            return false;
        }
        if (!StringUtils.isEmpty(name.toString()) && !StringUtils.isEmpty(passwd.toString())) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Utils.REQUEST_CODE_REGISTER) {
                // 注册成功
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void loginWithThirdAccount(Platform platform) {
        if (platform == null) {
            return;
        }

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
                BmobUser.loginWithAuthData(context, authInfo, new MyBmobOtherLoginListener() {
                    @Override
                    public void successOpt(JSONObject jsonObject) {
                        User user = BmobUser.getCurrentUser(context, User.class);
                        user.setNickName(nickName);
                        user.setAvatar(avatarUrl);
                        if (snsType.equalsIgnoreCase(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ)) {
                            user.setQqBinded(true);
                        } else if (snsType.equalsIgnoreCase(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIBO)) {
                            user.setSinaWeiboBinded(true);
                        } else if (snsType.equalsIgnoreCase(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIXIN)) {
                            user.setWechatBinded(true);
                        }
                        user.update(context, user.getObjectId(), new MyBmobUpdateListener() {
                            @Override
                            protected void successOpt() {
                                thirdUserLoginSuccess(snsType);
                            }

                            @Override
                            protected void failureOpt(int i, String s) {
                                Utils.showToastShort(context, getString(R.string.update_userinfo_failed));
                                thirdUserLoginSuccess(snsType);
                            }
                        });
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
        platform.setPlatformActionListener(new MyPlatformActionListener(ThirdPartyOptType.LOGIN, shareSDKOptCallback));
        platform.showUser(null);
    }

    private void thirdUserLoginSuccess(String snsType) {
        PropertyUtils.setUserLoginStatus("true", propertyContentDao);

        dismissLoadingDialog();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void initLoadingDialog() {
        loadingDialog = Utils.getLoadingDialog(context, getString(R.string.logining));
    }
}
