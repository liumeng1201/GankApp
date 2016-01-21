package com.lm.android.gankapp.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lm.android.gankapp.BuildConfig;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.TabAdapter;
import com.lm.android.gankapp.fragments.ContentFragment;
import com.lm.android.gankapp.models.ContentCategory;
import com.lm.android.gankapp.models.ContentType;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.ImageUtils;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.PropertyUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabLayout tabs;
    private ViewPager viewPager;
    private LinearLayout navigationHeaderView;
    private ImageView userAvatar;
    private TextView userDisplayName;

    private RoundedBitmapDrawable circularBitmapDrawable;
    private String[] titles;

    private FeedbackAgent agent;

    private final int NAV_HOME = 0;
    private final int NAV_FAV = 1;
    private final int NAV_GT = 2;
    private final int NAV_FK = 3;
    private final int NAV_SET = 4;
    private int navItemIndex = NAV_HOME;

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化logger
        Logger.init(getString(R.string.app_name));
        // 初始化ShareSDK
        ShareSDK.initSDK(this);
        // 设置umeng统计是否为debug模式
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        // 禁止umeng统计默认的统计行为
        MobclickAgent.openActivityDurationTrack(false);

        // 友盟自动更新
        UmengUpdateAgent.setUpdateCheckConfig(BuildConfig.DEBUG);
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(this);

        // 友盟用户反馈
        agent = new FeedbackAgent(this);
        // 通知用户有新的反馈回复
        Conversation conversation = agent.getDefaultConversation();
        conversation.sync(new SyncListener() {
            @Override
            public void onReceiveDevReply(List<Reply> list) {
                if (!ListUtils.isEmpty(list)) {
                    if (PropertyUtils.getFbDevReplyTime(propertyContentDao) < list.get(list.size()).created_at) {
                        PropertyUtils.setFbDevReplyTime(list.get(list.size()).created_at, propertyContentDao);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText("您的反馈有了新回复，点击查看");
                        mBuilder.setTicker("您的反馈有了新回复");
                        mBuilder.setAutoCancel(true);
                        Intent details = new Intent(MainActivity.this, FeedbackActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 100,
                                details, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(1000, mBuilder.build());
                    }
                }
            }

            @Override
            public void onSendUserReply(List<Reply> list) {
            }
        });

        super.onCreate(savedInstanceState);
        gankApplication.setMainActivityRunning(true);

        circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), ImageUtils.getBitmapFromRes(getResources(), R.mipmap.default_avatar));
        circularBitmapDrawable.setCircular(true);

        titles = getResources().getStringArray(R.array.slide_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        navigationHeaderView = (LinearLayout) navigationView.getHeaderView(0);
        userAvatar = (ImageView) navigationHeaderView.findViewById(R.id.avatar_image);
        userDisplayName = (TextView) navigationHeaderView.findViewById(R.id.avatar_name);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        initNavigationMenuItemClickListener();
        setTitle(titles[0]);
        invalidateOptionsMenu();

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(Utils.getUserId(context))) {
                    Intent intent = new Intent(context, MeActivity.class);
                    startActivityForResult(intent, Utils.REQUEST_CODE_USERINFO);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivityForResult(intent, Utils.REQUEST_CODE_LOGIN);
                }
            }
        });

        setUserInfo();
    }

    private void initNavigationMenuItemClickListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_fav:
                        gotoFavorite();
                        break;
                    case R.id.nav_gt:
                        break;
                    case R.id.nav_fk:
                        FeedbackActivity.actionStart(context);
                        break;
                    case R.id.nav_setting:
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void gotoFavorite() {
        if (StringUtils.isEmpty(Utils.getUserId(context))) {
            Utils.showToastShort(context, getString(R.string.watch_after_login));
            Intent intent = new Intent(context, LoginActivity.class);
            startActivityForResult(intent, Utils.REQUEST_CODE_FAVORITE_LOGIN);
        } else {
            Intent fav = new Intent(context, FavoriteActivity.class);
            startActivity(fav);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(ContentFragment.newInstance(ContentType.NET.getType(), ContentCategory.ANDROID.getType()), getString(R.string.category_android));
        adapter.addFragment(ContentFragment.newInstance(ContentType.NET.getType(), ContentCategory.IOS.getType()), getString(R.string.category_ios));
        adapter.addFragment(ContentFragment.newInstance(ContentType.NET.getType(), ContentCategory.WEB.getType()), getString(R.string.category_web));
        adapter.addFragment(ContentFragment.newInstance(ContentType.NET.getType(), ContentCategory.EXPAND.getType()), getString(R.string.category_expand));
        adapter.addFragment(ContentFragment.newInstance(ContentType.NET.getType(), ContentCategory.VIDEO.getType()), getString(R.string.category_video));
        adapter.addFragment(ContentFragment.newInstance(ContentType.NET.getType(), ContentCategory.MEIZI.getType()), getString(R.string.categroy_meizi));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setUserInfo();
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.REQUEST_CODE_LOGIN:
                    // 登录成功
                case Utils.REQUEST_CODE_USERINFO:
                    // 用户信息变动
                    setUserInfo();
                    break;
                case Utils.REQUEST_CODE_FAVORITE_LOGIN:
                    setUserInfo();
                    gotoFavorite();
                    break;
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                exitTime = System.currentTimeMillis();
                Utils.showToastShort(context, getString(R.string.doublic_click_exit_app));
            } else {
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setUserInfo() {
        User user = BmobUser.getCurrentUser(context, User.class);
        String avatarUrl = null;
        if (user != null) {
            String displayName = PropertyUtils.getUserDisplayName(user);
            if (!StringUtils.isEmpty(displayName)) {
                userDisplayName.setText(displayName);
            }
            avatarUrl = user.getAvatar();
        }
        loadAvatar(avatarUrl);
    }

    private void loadAvatar(String url) {
        Glide.with(context).load(url).asBitmap().placeholder(circularBitmapDrawable).error(circularBitmapDrawable).centerCrop().into(new BitmapImageViewTarget(userAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularDrawable.setCircular(true);
                userAvatar.setImageDrawable(circularDrawable);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gankApplication.setMainActivityRunning(false);
    }
}
