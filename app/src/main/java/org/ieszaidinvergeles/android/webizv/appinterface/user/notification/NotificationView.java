package org.ieszaidinvergeles.android.webizv.appinterface.user.notification;

// WIzv

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.ieszaidinvergeles.android.webizv.R;

public class NotificationView extends AppCompatActivity {

    private Bundle bundle = null;

    private void back() {
        final Intent i = new Intent();
        if(bundle != null) {
            i.putExtras(bundle);
        }
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void hideBackground() {
        LinearLayout v = (LinearLayout) findViewById(R.id.llNotification);
        v.setBackground(null);
    }

    private void initFragment(Fragment notificationsFragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, notificationsFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = getIntent().getExtras();
        if (savedInstanceState == null) {
            initFragment(NotificationFragment.newInstance());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showBackground() {
        LinearLayout v = (LinearLayout) findViewById(R.id.llNotification);
        v.setBackgroundResource(R.drawable.no_notification_background);
    }
}