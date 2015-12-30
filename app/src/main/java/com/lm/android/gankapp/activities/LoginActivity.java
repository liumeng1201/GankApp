package com.lm.android.gankapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.utils.Utils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
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
                break;
            case R.id.btn_register:
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Utils.REQUEST_CODE_REGISTER) {
                // TODO 注册成功
            }
        }
    }
}
