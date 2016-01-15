package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.ConversionListAdapter;
import com.lm.android.gankapp.utils.StringUtils;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import java.util.List;

public class FeedbackActivity extends BaseActivityWithLoadingDialog implements View.OnClickListener {
    private TextInputLayout edtInput;
    private RecyclerView conversionList;
    private ImageButton btnSend;
    private Button btnSetUserInfo;

    private ConversionListAdapter adapter;
    private Conversation mComversation;

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

        mComversation = new FeedbackAgent(context).getDefaultConversation();
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
        btnSetUserInfo = (Button) findViewById(R.id.btn_set_user_contact);

        btnSend.setOnClickListener(this);
        btnSetUserInfo.setOnClickListener(this);
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
            case R.id.btn_send:
                String content = edtInput.getEditText().getText().toString();
                edtInput.getEditText().getEditableText().clear();
                if (!StringUtils.isEmpty(content)) {
                    // 将内容添加到会话列表
                    mComversation.addUserReply(content);

                    // 数据同步
                    sync();
                }
                break;
            case R.id.btn_set_user_contact:
                break;
        }
    }

    // 数据同步
    private void sync() {
        mComversation.sync(new SyncListener() {
            @Override
            public void onSendUserReply(List<Reply> replyList) {
            }

            @Override
            public void onReceiveDevReply(List<Reply> replyList) {
                adapter.refresh(mComversation.getReplyList());
                conversionList.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

}
