package com.lm.android.gankapp.views;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.lm.android.gankapp.R;

public class MainActivity extends BaseAppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private FrameLayout container;

    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titles = getResources().getStringArray(R.array.slide_menu);

        container = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);

        setSupportActionBar(toolbar);
        initDrawerLayout();
        initNavigationMenuItemClickListener();
    }

    private void initDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    if (Build.VERSION.SDK_INT >= 11) {
                        // API 11以上的系统会设置toolBar的透明度
                        toolbar.setAlpha(1 - slideOffset);
                    }
                }
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    private void initNavigationMenuItemClickListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int index = 0;
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        index = 0;
                        break;
                    case R.id.nav_fav:
                        index = 1;
                        break;
                    case R.id.nav_gt:
                        index = 2;
                        break;
                    case R.id.nav_fk:
                        index = 3;
                        break;
                    case R.id.nav_setting:
                        index = 4;
                        break;
                    default:
                        break;
                }
                setTitle(titles[index]);
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        setTitle(titles[0]);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
