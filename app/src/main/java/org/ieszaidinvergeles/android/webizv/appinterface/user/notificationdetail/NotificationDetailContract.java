package org.ieszaidinvergeles.android.webizv.appinterface.user.notificationdetail;

// WIzv

import android.support.annotation.Nullable;

import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

public interface NotificationDetailContract {

    interface UserActionsListener {

        void openNotification(@Nullable Notificacion notificacion);

    }

    interface View {

        void hideDate();

        void hideMessage();

        void hideTitle();

        void setProgressIndicator(boolean active);

        void showDate(String date);

        void showMessage(String message);

        void showMissingNotification();

        void showTitle(String title);

    }
}