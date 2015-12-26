package com.lm.android.gankapp.component;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.ShareListAdapter;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.models.SharePlatItem;
import com.lm.android.gankapp.models.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 2015/12/26.
 */
public class ShareDialog extends AlertDialog {
    public ShareDialog(Context context) {
        super(context);
        initDialog(context);
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        initDialog(context);
    }

    public ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog(context);
    }

    private void initDialog(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setTitle(context.getString(R.string.share_dialog_title));
        RecyclerView view = new RecyclerView(context);
        List<SharePlatItem> platList = new ArrayList<>();
        Resources resources = context.getResources();
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_logo_wechat, resources.getString(R.string.ssdk_wechat)));
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_logo_wechatmoments, resources.getString(R.string.ssdk_wechatmoments)));
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_logo_qq, resources.getString(R.string.ssdk_qq)));
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_logo_sinaweibo, resources.getString(R.string.ssdk_sinaweibo)));
        platList.add(new SharePlatItem(R.mipmap.ic_launcher, resources.getString(R.string.listitem_share_more)));
        final ShareListAdapter shareListAdapter = new ShareListAdapter(platList);
        shareListAdapter.setOnItemClickListener(new OnContentItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Utils.getToastShort(view.getContext(), "Click " + shareListAdapter.getItem(position).getPlatName()).show();
            }
        });
        view.setAdapter(shareListAdapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context));
        setView(view);
    }
}
