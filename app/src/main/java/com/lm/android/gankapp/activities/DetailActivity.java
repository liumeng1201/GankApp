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
import android.widget.ProgressBar;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.utils.DrawableUtils;
import com.lm.android.gankapp.utils.ShareUtils;
import com.orhanobut.logger.Logger;

import icepick.State;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageButton btnFavorite;
    private ImageButton btnShare;
    private ImageButton btnOpenInBrowser;

    private GestureDetector gs = null;

    @State
    String url;
    @State
    String title;
    @State
    String objectId;

    /**
     * 其他Activity启动DetailsActivity操作
     *
     * @param context
     * @param url
     * @param title
     */
    public static void actionStart(Context context, String objectId, String url, String title) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("objectId", objectId);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
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
            objectId = intent.getStringExtra("objectId");
        }

        setTitle(title);
        webView.loadUrl(url);
    }

    private void initView() {
        btnFavorite = (ImageButton) findViewById(R.id.btn_favorite);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnOpenInBrowser = (ImageButton) findViewById(R.id.btn_open_in_browser);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        webView = (WebView) findViewById(R.id.webview);

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
                Logger.d("progress : " + newProgress);
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
