package org.ieszaidinvergeles.android.webizv.appinterface.user.notification;

// WIzv

import android.database.Cursor;
import android.support.annotation.NonNull;

import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

public interface NotificationContract {

    interface UserActionsListener {

        void deleteNotification(Notificacion notification);

        void loadColors();

        void loadNotifications();

        void openNotificationDetails(@NonNull Notificacion notification);

    }

    interface View {

        void setProgressIndicator(boolean active);

        void showNotificationDetails(Notificacion notification);

        void showNotifications(Cursor cursor);

        void showRequestPermission();

    }
}