package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.dao.FavoriteContent;
import com.lm.android.gankapp.dao.FavoriteContentDao;
import com.lm.android.gankapp.interfaces.ShareSDKOptCallback;
import com.lm.android.gankapp.listener.MyBmobDeleteListener;
import com.lm.android.gankapp.listener.MyBmobFindListener;
import com.lm.android.gankapp.listener.MyBmobSaveListener;
import com.lm.android.gankapp.listener.MyBmobUpdateListener;
import com.lm.android.gankapp.models.ContentItemFavorite;
import com.lm.android.gankapp.models.ContentItemReadHot;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.DrawableUtils;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.LogUtils;
import com.lm.android.gankapp.utils.ShareUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.Platform;
import de.greenrobot.dao.query.Query;
import icepick.State;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageButton btnFavorite;
    private ImageButton btnShare;
    private ImageButton btnOpenInBrowser;
    private LinearLayout controlBar;

    private GestureDetector gs = null;

    private ContentItemReadHot contentItemReadHot;
    private FavoriteContentDao favoriteDao;

    @State
    String url;
    @State
    String title;
    @State
    String contentObjectId;
    @State
    String who;
    @State
    String type;
    @State
    String publishAt;

    private ShareSDKOptCallback shareCallback;

    private boolean loadFinish = false;

    /**
     * 其他Activity启动DetailsActivity操作
     *
     * @param context
     * @param url
     * @param title
     */
    public static void actionStart(Context context, String contentObjectId, String url, String title, String who, String type, String publishAt) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("contentObjectId", contentObjectId);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("who", who);
        intent.putExtra("type", type);
        intent.putExtra("publishAt", publishAt);
        context.startActivity(intent);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initWebView();

        shareCallback = new ShareSDKOptCallback() {
            @Override
            public void onSuccess(Platform platform, HashMap<String, Object> result) {
                Utils.showToastShort(context, R.string.share_success);
            }

            @Override
            public void onFailed(Throwable throwable) {
                Utils.showToastShort(context, R.string.share_failed);
            }

            @Override
            public void onCancel() {
                Utils.showToastShort(context, R.string.share_cancel);
            }
        };

        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            contentObjectId = intent.getStringExtra("contentObjectId");
            who = intent.getStringExtra("who");
            type = intent.getStringExtra("type");
            publishAt = intent.getStringExtra("publishAt");

            BmobQuery<ContentItemReadHot> query = new BmobQuery<>();
            query.addWhereEqualTo("contentObjectId", contentObjectId);
            query.findObjects(context, new MyBmobFindListener<ContentItemReadHot>() {
                @Override
                protected void successOpt(List<ContentItemReadHot> list) {
                    if (ListUtils.isEmpty(list)) {
                        insertHotReadContent();
                    } else {
                        updateHotReadContent(list.get(0), list.get(0).getObjectId());
                    }
                }

                @Override
                protected void failureOpt(int i, String s) {
                }
            });
        }

        favoriteDao = gankApplication.getDaoSession().getFavoriteContentDao();
        if (!StringUtils.isEmpty(hasFavorite(contentObjectId))) {
            btnFavorite.setSelected(true);
        }

        setTitle(title);
        webView.loadUrl(url);
    }

    /**
     * 后台没有该条记录则插入并更新count
     */
    private void insertHotReadContent() {
        contentItemReadHot = new ContentItemReadHot(who, publishAt, title, type, url, contentObjectId);
        contentItemReadHot.save(context, new MyBmobSaveListener() {
            @Override
            protected void successOpt() {
                updateHotReadContent(contentItemReadHot, contentItemReadHot.getObjectId());
            }


            @Override
            protected void failureOpt(int i, String s) {
            }
        });
    }

    /**
     * 后台存在该条记录则只更新count
     *
     * @param objectId
     */
    private void updateHotReadContent(ContentItemReadHot contentItemReadHot, String objectId) {
        contentItemReadHot.increment("count");
        contentItemReadHot.update(context, objectId, new MyBmobUpdateListener() {
            @Override
            protected void successOpt() {
                LogUtils.logd("update " + title + " to bmob db");
            }

            @Override
            protected void failureOpt(int i, String s) {
            }
        });
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        super.onPause();
    }

    private void initView() {
        btnFavorite = (ImageButton) findViewById(R.id.btn_favorite);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnOpenInBrowser = (ImageButton) findViewById(R.id.btn_open_in_browser);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        webView = (WebView) findViewById(R.id.webview);
        controlBar = (LinearLayout) findViewById(R.id.control_bar);

        btnFavorite.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnOpenInBrowser.setOnClickListener(this);

        btnFavorite.setImageDrawable(DrawableUtils.getDrawableStateListRes(getResources(), R.mipmap.ic_favorite_white, R.color.button_favorite_color_tint_list));
        btnShare.setImageDrawable(DrawableUtils.getDrawableStateListRes(getResources(), R.mipmap.ic_share_white, R.color.button_normal_color_tint_list));
        btnOpenInBrowser.setImageDrawable(DrawableUtils.getDrawableStateListRes(getResources(), R.mipmap.ic_explore_white, R.color.button_normal_color_tint_list));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initWebView() {
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadFinish = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadFinish = true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogUtils.logd("progress : " + newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gs == null) {
                    gs = new GestureDetector(DetailActivity.this,
                            new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onDoubleTapEvent(MotionEvent e) {
                                    //Double Tap
                                    webView.zoomIn();//Zoom in
                                    return true;
                                }

                                @Override
                                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                                    if (loadFinish) {
                                        float ystart = e1.getY();
                                        float yend = e2.getY();
                                        if (ystart - yend > 50) {
                                            // 向上滚动，隐藏bar
                                            controlBar.setVisibility(View.GONE);
                                        } else if (yend - ystart > 50) {
                                            // 向下滚动，显示bar
                                            controlBar.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    return super.onScroll(e1, e2, distanceX, distanceY);
                                }
                            });
                }
                gs.onTouchEvent(event);
                return false;
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
            case R.id.btn_favorite:
                favoriteOpt();
                break;
            case R.id.btn_share:
                ShareUtils.showShare(context, shareCallback, url, title, null);
                break;
            case R.id.btn_open_in_browser:
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(openBrowser);
                break;
        }
    }

    private void favoriteOpt() {
        String userId = null;
        User currentUser = BmobUser.getCurrentUser(context, User.class);
        if (currentUser != null) {
            userId = currentUser.getObjectId();
        }

        if (StringUtils.isEmpty(userId)) {
            // 用户不存在则先进行登录操作
            Utils.showToastShort(context, getString(R.string.favorite_login_first));
            Intent login = new Intent(context, LoginActivity.class);
            startActivityForResult(login, Utils.REQUEST_CODE_LOGIN);
        } else {
            final String id = hasFavorite(contentObjectId);
            if (!StringUtils.isEmpty(id)) {
                final ContentItemFavorite item = new ContentItemFavorite();
                item.delete(context, id, new MyBmobDeleteListener() {
                    @Override
                    public void successOpt() {
                        delFavoriteFromDB(item.getContentObjectId());
                        btnFavorite.setSelected(false);
                    }

                    @Override
                    public void failureOpt(int i, String s) {
                    }
                });
            } else {
                final ContentItemFavorite item = new ContentItemFavorite(title, type, url, contentObjectId, System.currentTimeMillis(), userId);
                item.save(context, new MyBmobSaveListener() {
                    @Override
                    protected void successOpt() {
                        addFavoriteToDB(item);
                        btnFavorite.setSelected(true);
                    }

                    @Override
                    protected void failureOpt(int i, String s) {
                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Utils.REQUEST_CODE_LOGIN) {
                favoriteOpt();
            }
        }
    }

    /**
     * 判断是否已经收藏
     *
     * @param contentObjectId
     * @return 已收藏返回objectId，否则返回空
     */
    private String hasFavorite(String contentObjectId) {
        FavoriteContent item = getFavoriteEntity(contentObjectId);
        if (item == null) {
            return null;
        }
        return item.getObjectId();
    }

    /**
     * @param contentObjectId 文章id
     * @return 收藏entity
     */
    private FavoriteContent getFavoriteEntity(String contentObjectId) {
        Query query = favoriteDao.queryBuilder().where(FavoriteContentDao.Properties.ContentObjectId.eq(contentObjectId)).build();
        if (query != null) {
            List<FavoriteContent> list = query.list();
            if (ListUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        }
        return null;
    }

    /**
     * 将收藏数据添加到本地数据库以方便使用
     */
    private void addFavoriteToDB(ContentItemFavorite item) {
        FavoriteContent entity = new FavoriteContent();
        entity.setType(item.getType());
        entity.setDesc(item.getDesc());
        entity.setUrl(item.getUrl());
        entity.setContentObjectId(item.getContentObjectId());
        entity.setFavoriteAt(item.getFavoriteAt());
        entity.setObjectId(item.getObjectId());
        favoriteDao.insert(entity);
    }

    /**
     * 从数据库中删除收藏信息
     *
     * @param contentObjectId
     */
    private void delFavoriteFromDB(String contentObjectId) {
        favoriteDao.delete(getFavoriteEntity(contentObjectId));
    }

}
