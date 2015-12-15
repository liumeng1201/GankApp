package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.TabAdapter;
import com.lm.android.gankapp.fragments.ContentFragment;

public class MainActivity extends BaseAppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabLayout tabs;
    private ViewPager viewPager;

    private String[] titles;

    private final int NAV_HOME = 0;
    private final int NAV_FAV = 1;
    private final int NAV_GT = 2;
    private final int NAV_FK = 3;
    private final int NAV_SET = 4;
    private int navItemIndex = NAV_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titles = getResources().getStringArray(R.array.slide_menu);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setSupportActionBar(toolbar);
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
        adapter.addFragment(new ContentFragment(), "List");
        adapter.addFragment(new ContentFragment(), "Tile");
        adapter.addFragment(new ContentFragment(), "Card");
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
}