package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lm.android.gankapp.R;

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
                // TODO 注册操作
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
}
