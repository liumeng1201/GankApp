package com.lm.android.gankapp.utils;

import android.content.Context;
import android.content.Intent;
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
import com.lm.android.gankapp.interfaces.ShareSDKOptCallback;
import com.lm.android.gankapp.listener.MyPlatformActionListener;
import com.lm.android.gankapp.models.SharePlatItem;
import com.lm.android.gankapp.models.ThirdPartyOptType;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by liumeng on 2015/12/24.
 */
public class ShareUtils {
    /**
     * 显示分享dialog
     * sharesdk使用参考http://www.cnblogs.com/smyhvae/p/4585340.html
     *
     * @param context
     * @param contentUrl  要分享的网址url，可以为空
     * @param contentText 要分享的内容，可以为空
     * @param imageUrl    要分享的图片的url，可以为空
     */
    public static void showShare(final Context context, ShareSDKOptCallback shareCallback, final String contentUrl, final String contentText, final String imageUrl) {
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
        view.setAdapter(shareListAdapter);
        view.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        builder.setView(convertView);
        final AlertDialog dialog = builder.create();
        final MyPlatformActionListener platformActionListener = new MyPlatformActionListener(ThirdPartyOptType.SHARE, shareCallback);
        shareListAdapter.setOnItemClickListener(new OnContentItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Platform.ShareParams sp = new Platform.ShareParams();
                sp.setTitle(context.getString(R.string.app_name));
                sp.setTitleUrl(Utils.share_title_url);
                if (!StringUtils.isEmpty(contentUrl)) {
                    sp.setText(contentText + " " + contentUrl);
                    sp.setUrl(contentUrl);
                }
                if (!StringUtils.isEmpty(imageUrl)) {
                    sp.setImageUrl(imageUrl);
                }
                switch (shareListAdapter.getItem(position).getPlatImage()) {
                    case R.drawable.ssdk_oks_skyblue_logo_wechat_checked:
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(platformActionListener);
                        wechat.share(sp);
                        break;
                    case R.drawable.ssdk_oks_skyblue_logo_wechatmoments_checked:
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(platformActionListener);
                        wechatMoments.share(sp);
                        break;
                    case R.drawable.ssdk_oks_skyblue_logo_qq_checked:
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(platformActionListener);
                        qq.share(sp);
                        break;
                    case R.drawable.ssdk_oks_skyblue_logo_sinaweibo_checked:
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(platformActionListener);
                        sinaWeibo.share(sp);
                        break;
                    case R.drawable.ic_more_horiz_white_36dp:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        String shareText = context.getString(R.string.app_name);
                        if (!StringUtils.isEmpty(contentText)) {
                            shareText = shareText + "\n" + contentText;
                        }
                        if (!StringUtils.isEmpty(contentUrl)) {
                            shareText = shareText + "\n" + contentUrl;
                        }
                        if (!StringUtils.isEmpty(imageUrl)) {
                            shareText = shareText + "\n" + imageUrl;
                        }
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        shareIntent.setType("text/plain");
                        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.share_dialog_title)));
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
