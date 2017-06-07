package org.ieszaidinvergeles.android.webizv.appinterface.user.notificationdetail;

// WIzv

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.appinterface.user.common.CalendarPresenter;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PARAMETER_NOTIFICACION;

public class NotificationDetailView extends AppCompatActivity  {

    private final static int REQUEST_WRITE_CALENDAR = 1;

    private ImageView buttonAddToCalendar;
    private Notificacion notificacion;
    private CalendarPresenter listener;

    private void initFragment(Fragment detailFragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, detailFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Bundle datos = this.getIntent().getExtras();
        notificacion = datos.getParcelable(PARAMETER_NOTIFICACION);
        toolbar.setBackgroundColor(notificacion.getColor());
        final TextView subtitulo = (TextView)findViewById(R.id.tvMessage);
        subtitulo.setText(notificacion.getName());
        listener = new CalendarPresenter(this);
        buttonAddToCalendar =(ImageView) findViewById(R.id.button_add_to_calendar);
        buttonAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(NotificationDetailView.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NotificationDetailView.this, Manifest.permission.WRITE_CALENDAR)) {
                        showRequestPermission();
                    } else {
                        ActivityCompat.requestPermissions(NotificationDetailView.this,
                                new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_WRITE_CALENDAR);
                    }
                } else {
                    listener.saveInCalendar(notificacion);
                    buttonAddToCalendar.setVisibility(View.GONE);
                }
            }
        });
        buttonAddToCalendar.setVisibility(View.GONE);
        if(notificacion.getExtra().compareTo("") != 0 && notificacion.getRead() == 0) {
            buttonAddToCalendar.setVisibility(View.VISIBLE);
        }
        initFragment(NotificationDetailFragment.newInstance(notificacion));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_CALENDAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listener.saveInCalendar(notificacion);
                    buttonAddToCalendar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, R.string.calendario_permiso_no, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showRequestPermission() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.calendario_titulo);
        builder.setMessage(R.string.calendario_permiso);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(NotificationDetailView.this,
                        new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_WRITE_CALENDAR);
            }
        });
        //builder.setNegativeButton(R.string.cancelar, null);
        builder.show();
    }

}