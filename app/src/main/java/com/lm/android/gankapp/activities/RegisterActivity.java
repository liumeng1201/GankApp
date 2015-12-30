package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {
    private TextInputLayout edtUsername;
    private TextInputLayout edtPassword;
    private TextInputLayout edtPasswordConfirm;
    private Button btnRegister;

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.register);
        initView();
    }

    private void initView() {
        btnRegister = (Button) findViewById(R.id.btn_register);
        edtUsername = (TextInputLayout) findViewById(R.id.input_username);
        edtPassword = (TextInputLayout) findViewById(R.id.input_password);
        edtPasswordConfirm = (TextInputLayout) findViewById(R.id.input_password_again);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable username = edtUsername.getEditText().getText();
                Editable password = edtPassword.getEditText().getText();
                Editable passwordConfirm = edtPasswordConfirm.getEditText().getText();
                if (canRegister(username, password, passwordConfirm)) {
                    User user = new User();
                    user.setUsername(username.toString().trim());
                    user.setPassword(password.toString().trim());
                    user.signUp(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            // TODO 注册成功
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            // TODO 注册失败
                        }
                    });
                }
            }
        });
    }

    private boolean canRegister(Editable username, Editable password, Editable passwordConfirm) {
        if (username != null && password != null && passwordConfirm != null) {
            if (!StringUtils.isEmpty(username.toString().trim())
                    && !StringUtils.isEmpty(username.toString().trim())
                    && !StringUtils.isEmpty(username.toString().trim())) {
                if (password.toString().trim().equals(passwordConfirm.toString().trim())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Utils.getToastShort(context, getString(R.string.register_edt_null));
                return false;
            }
        } else {
            Utils.getToastShort(context, getString(R.string.register_edt_null));
            return false;
        }
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
}
