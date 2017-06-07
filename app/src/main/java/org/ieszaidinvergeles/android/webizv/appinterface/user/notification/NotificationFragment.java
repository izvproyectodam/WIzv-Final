package org.ieszaidinvergeles.android.webizv.appinterface.user.notification;

// WIzv

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.appinterface.user.common.CalendarPresenter;
import org.ieszaidinvergeles.android.webizv.appinterface.user.notification.adapter.NotificationCursorAdapter;
import org.ieszaidinvergeles.android.webizv.appinterface.user.notificationdetail.NotificationDetailView;

import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.BROADCAST_INTENT;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PARAMETER_NOTIFICACION;

public class NotificationFragment extends Fragment implements NotificationContract.View {

    private final static int REQUEST_WRITE_CALENDAR = 1;

    public interface NotificacionItemListener {

        void onNeedsColors();

        void onNoteClick(Notificacion clickedNote);

        void onSaveInCalendar(Notificacion notificacion);

    }

    private BroadcastReceiver broadcastReceiver;
    private CalendarPresenter calendarListener;
    private Notificacion notificacion;
    private NotificationCursorAdapter cursorAdapter;
    private NotificationContract.UserActionsListener listener;

    public NotificationFragment() {
    }

    private void cancelNotification() {
        final NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = new NotificationPresenter(this);
        calendarListener = new CalendarPresenter(getActivity());
        final NotificacionItemListener itemListener = new NotificacionItemListener() {

            @Override
            public void onNeedsColors() {
                listener.loadColors();
            }

            @Override
            public void onNoteClick(Notificacion clickedNote) {
                listener.openNotificationDetails(clickedNote);
            }

            @Override
            public void onSaveInCalendar(Notificacion notificacion) {
                NotificationFragment.this.notificacion = notificacion;
                if (ActivityCompat.checkSelfPermission(NotificationFragment.this.getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NotificationFragment.this.getActivity(), Manifest.permission.WRITE_CALENDAR)) {
                        showRequestPermission();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_WRITE_CALENDAR);
                    }
                } else {
                    calendarListener.saveInCalendar(notificacion);
                }
            }
        };
        cursorAdapter = new NotificationCursorAdapter(getContext(), null, itemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.notification_fragment, container, false);
        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.notes_list);
        recyclerView.setAdapter(cursorAdapter);
        recyclerView.setHasFixedSize(true);
        final ItemTouchHelper.Callback callback = new NotificationTouchHelper(listener);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.loadNotifications();
            }
        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_CALENDAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    calendarListener.saveInCalendar(notificacion);
                } else {
                    Toast.makeText(this.getContext(), R.string.calendario_permiso_no, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.loadNotifications();
        registerReceiver();
        cancelNotification();
    }

    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                cancelNotification();
                listener.loadNotifications();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_INTENT));
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showNotificationDetails(Notificacion notification) {
        final Intent intent = new Intent(getContext(), NotificationDetailView.class);
        intent.putExtra(PARAMETER_NOTIFICACION, notification);
        startActivity(intent);
    }

    @Override
    public void showRequestPermission() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.calendario_titulo);
        builder.setMessage(R.string.calendario_permiso);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_WRITE_CALENDAR);
            }
        });
        //builder.setNegativeButton(R.string.cancelar, null);
        builder.show();
    }

    @Override
    public void showNotifications(Cursor cursor) {
        if(cursor == null || cursor.getCount() > 0){
            ((NotificationView)getActivity()).hideBackground();
        } else {
            ((NotificationView)getActivity()).showBackground();
        }
        cursorAdapter.swapCursor(cursor);
    }

}