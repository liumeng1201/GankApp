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
import com.lm.android.gankapp.listener.MyBmobFindListener;
import com.lm.android.gankapp.listener.MyBmobSaveListener;
import com.lm.android.gankapp.listener.MyBmobUpdateListener;
import com.lm.android.gankapp.models.ContentItemReadHot;
import com.lm.android.gankapp.utils.DrawableUtils;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.LogUtils;
import com.lm.android.gankapp.utils.ShareUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
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
                        insertAndUpdateContent();
                    } else {
                        updateContent(list.get(0), list.get(0).getObjectId());
                    }
                }

                @Override
                protected void failureOpt(int i, String s) {
                }
            });


        }

        setTitle(title);
        webView.loadUrl(url);
    }

    /**
     * 后台没有该条记录则插入并更新count
     */
    private void insertAndUpdateContent() {
        contentItemReadHot = new ContentItemReadHot(who, publishAt, title, type, url, contentObjectId);
        contentItemReadHot.save(context, new MyBmobSaveListener() {
            @Override
            protected void successOpt() {
                LogUtils.logd("Add " + title + " to bmob db");
                updateContent(contentItemReadHot, contentItemReadHot.getObjectId());
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
    private void updateContent(ContentItemReadHot contentItemReadHot, String objectId) {
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
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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
                                    float ystart = e1.getY();
                                    float yend = e2.getY();
                                    if (ystart - yend > 50) {
                                        // 向上滚动，隐藏bar
                                        controlBar.setVisibility(View.GONE);
                                    } else if (yend - ystart > 50) {
                                        // 向下滚动，显示bar
                                        controlBar.setVisibility(View.VISIBLE);
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
                break;
            case R.id.btn_share:
                ShareUtils.showShare(context, url, title);
                break;
            case R.id.btn_open_in_browser:
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(openBrowser);
                break;
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
}
