package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.dao.PropertyContentDao;
import com.lm.android.gankapp.listener.MyBmobSaveListener;
import com.lm.android.gankapp.models.PropertyUtils;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;

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
        edtPasswordConfirm.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerOpt();
                }
                return true;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOpt();
            }
        });
    }

    private void registerOpt() {
        final Editable username = edtUsername.getEditText().getText();
        final Editable password = edtPassword.getEditText().getText();
        Editable passwordConfirm = edtPasswordConfirm.getEditText().getText();
        if (canRegister(username, password, passwordConfirm)) {
            User user = new User();
            user.setUsername(username.toString().trim());
            user.setPassword(password.toString().trim());
            user.signUp(context, new MyBmobSaveListener() {
                @Override
                protected void successOpt() {
                    Utils.showToastShort(context, getString(R.string.register_success));
                    PropertyContentDao dao = gankApplication.getDaoSession().getPropertyContentDao();

                    PropertyUtils.saveUserName(username.toString().trim(), dao);
                    PropertyUtils.saveUserPassword(password.toString().trim(), dao);
                    PropertyUtils.saveUserLoginStatus("true", dao);

                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                protected void failureOpt(int i, String s) {
                    Utils.showToastShort(context, s + getString(R.string.please_try_again));
                }
            });
        }
    }

    private boolean canRegister(Editable username, Editable password, Editable passwordConfirm) {
        if (username != null && password != null && passwordConfirm != null) {
            if (!StringUtils.isEmpty(username.toString().trim())
                    && !StringUtils.isEmpty(username.toString().trim())
                    && !StringUtils.isEmpty(username.toString().trim())) {
                if (password.toString().trim().equals(passwordConfirm.toString().trim())) {
                    return true;
                } else {
                    Utils.showToastShort(context, getString(R.string.password_not_equal));
                    return false;
                }
            } else {
                Utils.showToastShort(context, getString(R.string.register_edt_null));
                return false;
            }
        } else {
            Utils.showToastShort(context, getString(R.string.register_edt_null));
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
