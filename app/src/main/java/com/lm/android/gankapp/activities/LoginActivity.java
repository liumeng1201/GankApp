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
import com.lm.android.gankapp.listener.MyBmobSaveListener;
import com.lm.android.gankapp.models.PropertyUtils;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;

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

        loadingDialog = Utils.getLoadingDialog(context, "登录中...");

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
                break;
            case R.id.login_btn_qq:
                break;
            case R.id.login_btn_sinaweibo:
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
                    PropertyUtils.saveUserName(name.toString().trim(), propertyContentDao);
                    PropertyUtils.saveUserPassword(passwd.toString().trim(), propertyContentDao);
                    PropertyUtils.saveUserLoginStatus("true", propertyContentDao);

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
}
