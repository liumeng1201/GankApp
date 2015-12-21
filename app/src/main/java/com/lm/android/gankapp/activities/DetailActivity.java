package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lm.android.gankapp.R;
import com.orhanobut.logger.Logger;

import icepick.State;

public class DetailActivity extends BaseAppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageButton btn_favorite;
    private ImageButton btn_zan;
    private ImageButton btn_comment;

    @State
    String url;
    @State
    String title;

    /**
     * 其他Activity启动DetailsActivity操作
     *
     * @param context
     * @param url
     * @param title
     */
    public static void actionStart(Context context, String url, String title) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        btn_favorite = (ImageButton) findViewById(R.id.btn_favorite);
        btn_zan = (ImageButton) findViewById(R.id.btn_share);
        btn_comment = (ImageButton) findViewById(R.id.btn_open_in_browser);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        webView = (WebView) findViewById(R.id.webview);

        btn_favorite.setImageDrawable(getDrawableStateListRes(getResources(), R.mipmap.ic_favorite_white, R.color.button_favorite_color_tint_list));
        btn_zan.setImageDrawable(getDrawableStateListRes(getResources(), R.mipmap.ic_share_white, R.color.button_normal_color_tint_list));
        btn_comment.setImageDrawable(getDrawableStateListRes(getResources(), R.mipmap.ic_explore_white, R.color.button_normal_color_tint_list));

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
        }

        setTitle(title);
        webView.loadUrl(url);
    }

    private Drawable getDrawableStateListRes(Resources res, int drawableId, int colorTintList) {
        ColorStateList colorStateList;
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorStateList = res.getColorStateList(colorTintList, null);
            drawable = DrawableCompat.wrap(res.getDrawable(drawableId, null));
        } else {
            colorStateList = res.getColorStateList(colorTintList);
            drawable = DrawableCompat.wrap(res.getDrawable(drawableId));
        }
        DrawableCompat.setTintList(drawable, colorStateList);
        return drawable;
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
