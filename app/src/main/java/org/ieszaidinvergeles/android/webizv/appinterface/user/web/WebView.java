package org.ieszaidinvergeles.android.webizv.appinterface.user.web;

// WIzv

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.ieszaidinvergeles.android.webizv.R;

public class WebView extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private WebContract.View view;

    private void activateNavigationView() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.llNotification);
        navigationView = (NavigationView)findViewById(R.id.nvSideMenu);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_delete_notifications:
                                view.showDeleteNotifications();
                                break;
                            case R.id.menu_login:
                                view.showOpenSession();
                                break;
                            case R.id.menu_logout:
                                view.showCloseSession();
                                break;
                            case R.id.menu_exit_app:
                                view.showExitApp();
                                break;
                            case R.id.menu_view_notifications:
                                view.showNotifications();
                                break;
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    private void initFragment(Fragment webFragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, webFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        view = WebFragment.newInstance(getIntent().getExtras());
        initFragment((WebFragment)view);
        viewToolbar();
        activateNavigationView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final WebFragment fragment = (WebFragment) view;
        if(fragment.isLogged()) {
            navigationView.getMenu().findItem(R.id.menu_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_login).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.menu_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.menu_login).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void viewToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tbBar);
        setSupportActionBar(toolbar);
    }
}