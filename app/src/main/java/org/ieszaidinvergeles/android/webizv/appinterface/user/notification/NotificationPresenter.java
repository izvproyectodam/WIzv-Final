package org.ieszaidinvergeles.android.webizv.appinterface.user.notification;

// WIzv

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver.ColorRequest;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.TABLE_QUERY_URI;

public class NotificationPresenter implements LoaderManager.LoaderCallbacks<Cursor>, NotificationContract.UserActionsListener {

    private final static int ID = 1;

    private final NotificationContract.View notificationView;

    public NotificationPresenter(NotificationContract.View notificationView) {
        this.notificationView = notificationView;
        ((Fragment) notificationView).getLoaderManager().initLoader(ID, null, this);
    }

    @Override
    public void deleteNotification(final Notificacion notification) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                final Uri uri = ContentUris.withAppendedId(TABLE_QUERY_URI, notification.getId());
                ((Fragment) notificationView).getContext().getContentResolver().delete(uri, null, null);
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void loadColors() {
        final Context context = ((Fragment) notificationView).getContext();
        final ColorRequest colorRequest = new ColorRequest(context);
        colorRequest.send();
    }

    @Override
    public void loadNotifications() {
        ((Fragment) notificationView).getLoaderManager().restartLoader(ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        notificationView.setProgressIndicator(true);
        if (id == ID) {
            return new CursorLoader(((Fragment) notificationView).getContext(), TABLE_QUERY_URI, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == ID) {
            notificationView.showNotifications(data);
        }
        notificationView.setProgressIndicator(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ID) {
            notificationView.showNotifications(null);
        }
        notificationView.setProgressIndicator(false);
    }

    @Override
    public void openNotificationDetails(@NonNull Notificacion notification) {
        notificationView.showNotificationDetails(notification);
    }

}