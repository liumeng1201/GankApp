package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.ConversionListAdapter;
import com.lm.android.gankapp.utils.LogUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackActivity extends BaseActivityWithLoadingDialog implements View.OnClickListener {
    private TextInputLayout edtInput;
    private RecyclerView conversionList;
    private ImageButton btnSend;

    private ConversionListAdapter adapter;
    private FeedbackAgent agent;
    private Conversation mComversation;
    private UserInfo userInfo;
    private Map<String, String> contact;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initLoadingDialog() {
        // 不需要使用loading对话框
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agent = new FeedbackAgent(context);
        userInfo = agent.getUserInfo();
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        contact = userInfo.getContact();
        if (contact == null) {
            contact = new HashMap<>();
        }
        if (StringUtils.isEmpty(contact.get("phone")) &&
                StringUtils.isEmpty(contact.get("email")) &&
                StringUtils.isEmpty(contact.get("qq"))) {
            showInputDialog();
        }

        mComversation = agent.getDefaultConversation();
        adapter = new ConversionListAdapter(mComversation.getReplyList());
        initView();

        conversionList.setLayoutManager(new LinearLayoutManager(context));
        conversionList.setAdapter(adapter);
        conversionList.smoothScrollToPosition(adapter.getItemCount());
    }

    private void initView() {
        edtInput = (TextInputLayout) findViewById(R.id.input);
        conversionList = (RecyclerView) findViewById(R.id.conversion_list);
        btnSend = (ImageButton) findViewById(R.id.btn_send);

        btnSend.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                backOpt();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void backOpt() {
        if (gankApplication.getMainActivityRunning()) {
            finish();
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String content = edtInput.getEditText().getText().toString();
                edtInput.getEditText().getEditableText().clear();
                if (!StringUtils.isEmpty(content)) {
                    // 将内容添加到会话列表
                    mComversation.addUserReply(content);

                    // 数据同步
                    sync();

                    adapter.refresh(mComversation.getReplyList());
                    conversionList.smoothScrollToPosition(adapter.getItemCount());
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backOpt();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 数据同步
    private void sync() {
        mComversation.sync(new SyncListener() {
            @Override
            public void onSendUserReply(List<Reply> replyList) {
            }

            @Override
            public void onReceiveDevReply(List<Reply> replyList) {
            }
        });
    }

    private void showInputDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_input_conversion_userinfo, null);
        final TextInputLayout inputPhone = (TextInputLayout) view.findViewById(R.id.input_phone);
        final TextInputLayout inputEmail = (TextInputLayout) view.findViewById(R.id.input_email);
        final TextInputLayout inputQQ = (TextInputLayout) view.findViewById(R.id.input_qq);
        AlertDialog inputDialog = new AlertDialog.Builder(context)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phone = inputPhone.getEditText().getText().toString().trim();
                        String email = inputEmail.getEditText().getText().toString().trim();
                        String qq = inputQQ.getEditText().getText().toString().trim();
                        if (!StringUtils.isEmpty(phone)) {
                            contact.put("phone", phone);
                        }
                        if (!StringUtils.isEmpty(email)) {
                            contact.put("email", email);
                        }
                        if (!StringUtils.isEmpty(qq)) {
                            contact.put("qq", qq);
                        }
                        userInfo.setContact(contact);
                        agent.setUserInfo(userInfo);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean result = agent.updateUserInfo();
                                LogUtils.logi("update userinfo result = " + result);
                            }
                        }).start();
                    }
                }).setNegativeButton(getString(R.string.cancel), null).create();
        inputDialog.setCanceledOnTouchOutside(false);
        inputDialog.setView(view);
        inputDialog.show();
    }
}
