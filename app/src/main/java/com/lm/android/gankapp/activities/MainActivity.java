package com.lm.android.gankapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
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
import com.lm.android.gankapp.utils.PropertyUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

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

    private String[] titles;

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

        super.onCreate(savedInstanceState);

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
                if (PropertyUtils.getUserLoginStatus(propertyContentDao)) {
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
                        navItemIndex = NAV_HOME;
                        break;
                    case R.id.nav_fav:
                        navItemIndex = NAV_FAV;
                        break;
                    case R.id.nav_gt:
                        navItemIndex = NAV_GT;
                        break;
                    case R.id.nav_fk:
                        navItemIndex = NAV_FK;
                        break;
                    case R.id.nav_setting:
                        navItemIndex = NAV_SET;
                        break;
                    default:
                        break;
                }
                setTitle(titles[navItemIndex]);
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                invalidateOptionsMenu();
                return true;
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (navItemIndex == NAV_FAV) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.REQUEST_CODE_LOGIN:
                    // 登录成功
                    setUserInfo();
                    break;
                case Utils.REQUEST_CODE_USERINFO:
                    // 用户信息变动
                    setUserInfo();
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
        if (user != null) {
            String displayName = PropertyUtils.getUserDisplayName(user);
            if (!StringUtils.isEmpty(displayName)) {
                userDisplayName.setText(displayName);
            }

            String avatarUrl = user.getAvatar();
            if (!StringUtils.isEmpty(avatarUrl)) {
                loadAvatar(avatarUrl);
            } else {
                loadAvatar(R.mipmap.default_avatar);
            }
        } else {
            loadAvatar(R.mipmap.default_avatar);
        }
    }

    private void loadAvatar(int resId) {
        Glide.with(context).load(resId).asBitmap().centerCrop().into(new BitmapImageViewTarget(userAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                userAvatar.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    private void loadAvatar(String url) {
        Glide.with(context).load(url).asBitmap().error(R.mipmap.default_avatar).centerCrop().into(new BitmapImageViewTarget(userAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                userAvatar.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
