package org.ieszaidinvergeles.android.webizv.appinterface.user.notification;

// WIzv

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.ieszaidinvergeles.android.webizv.appinterface.user.notification.adapter.NotificationCursorAdapter;

public class NotificationTouchHelper extends ItemTouchHelper.SimpleCallback {

    private NotificationContract.UserActionsListener listener;

    public NotificationTouchHelper(NotificationContract.UserActionsListener listener) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        NotificationCursorAdapter.ViewHolder vh = (NotificationCursorAdapter.ViewHolder)viewHolder;
        listener.deleteNotification(vh.getNotificacion());
    }
}