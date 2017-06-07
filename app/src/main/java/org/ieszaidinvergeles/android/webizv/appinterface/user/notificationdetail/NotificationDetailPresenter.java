package org.ieszaidinvergeles.android.webizv.appinterface.user.notificationdetail;

// WIzv

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

public class NotificationDetailPresenter implements NotificationDetailContract.UserActionsListener {

    private final NotificationDetailContract.View detailNotificationView;

    public NotificationDetailPresenter(@NonNull NotificationDetailContract.View notificacionDetailView) {
        detailNotificationView = notificacionDetailView;
    }

    @Override
    public void openNotification(@Nullable Notificacion notificacion) {
        detailNotificationView.setProgressIndicator(true);
        if (notificacion == null) {
            detailNotificationView.showMissingNotification();
        } else {
            showNotificacion(notificacion);
        }
        detailNotificationView.setProgressIndicator(false);
    }

    private void showNotificacion(Notificacion notificacion) {
        final String title = notificacion.getTitle();
        final String message = notificacion.getMessage();
        final String date = notificacion.getDate();
        if (title != null && title.isEmpty()) {
            detailNotificationView.hideTitle();
        } else {
            detailNotificationView.showTitle(title);
        }
        if (message != null && message.isEmpty()) {
            detailNotificationView.hideMessage();
        } else {
            detailNotificationView.showMessage(message);
        }
        if (date != null) {
            detailNotificationView.showDate(notificacion.getFormattedDate());
        } else {
            detailNotificationView.hideDate();
        }
    }

}