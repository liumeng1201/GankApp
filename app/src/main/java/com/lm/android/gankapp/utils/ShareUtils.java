package com.lm.android.gankapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.ShareListAdapter;
import com.lm.android.gankapp.component.CustomLinearLayoutManager;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.models.SharePlatItem;
import com.lm.android.gankapp.models.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 2015/12/24.
 */
public class ShareUtils {
    /** http://www.cnblogs.com/smyhvae/p/4585340.html */

    public static void showShare(Context context, String contentUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_share_dialog, null);
        RecyclerView view = (RecyclerView) convertView.findViewById(R.id.share_plat_list);
        List<SharePlatItem> platList = new ArrayList<>();
        Resources resources = context.getResources();
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_wechat_checked, resources.getString(R.string.ssdk_wechat)));
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_wechatmoments_checked, resources.getString(R.string.ssdk_wechatmoments)));
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_qq_checked, resources.getString(R.string.ssdk_qq)));
        platList.add(new SharePlatItem(R.drawable.ssdk_oks_skyblue_logo_sinaweibo_checked, resources.getString(R.string.ssdk_sinaweibo)));
        platList.add(new SharePlatItem(R.drawable.ic_more_horiz_white_36dp, resources.getString(R.string.listitem_share_more)));
        final ShareListAdapter shareListAdapter = new ShareListAdapter(platList);
        shareListAdapter.setOnItemClickListener(new OnContentItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Utils.getToastShort(view.getContext(), "Click " + shareListAdapter.getItem(position).getPlatName()).show();
            }
        });
        view.setAdapter(shareListAdapter);
        view.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        builder.setView(convertView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
