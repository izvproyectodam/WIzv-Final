package org.ieszaidinvergeles.android.webizv.appinterface.user.notification.adapter;

// WIzv

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.appinterface.user.notification.NotificationFragment;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;
import org.ieszaidinvergeles.android.webizv.util.CursorRecyclerViewAdapter;

public class NotificationCursorAdapter extends CursorRecyclerViewAdapter<NotificationCursorAdapter.ViewHolder> {

    private boolean needsColors = false;
    private final NotificationFragment.NotificacionItemListener listener;

    public NotificationCursorAdapter(Context context, Cursor cursor, NotificationFragment.NotificacionItemListener listener) {
        super(context, cursor);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final Notificacion notificacion = PojoUtil.getNotificacion(cursor);
        viewHolder.title.setText(notificacion.getTitle());
        viewHolder.message.setText(notificacion.getMessage());
        viewHolder.buttonAddToCalendar.setVisibility(View.GONE);
        if(notificacion.getExtra().compareTo("") != 0 && notificacion.getRead() == 0) {
            viewHolder.buttonAddToCalendar.setVisibility(View.VISIBLE);
        }
        viewHolder.card.setCardBackgroundColor(notificacion.getColor());
        viewHolder.date.setText(notificacion.getFormattedDate());
        viewHolder.setNotificacion(notificacion);
        if(!needsColors && (notificacion.getRealColor() == null || notificacion.getRealColor().equals(""))) {
            needsColors = true;
            listener.onNeedsColors();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View notificacionView = inflater.inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(notificacionView, listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final CardView card;
        final TextView date, message, title;
        final ImageView buttonAddToCalendar;
        private Notificacion notificacion;
        private final NotificationFragment.NotificacionItemListener listener;

        public ViewHolder(View itemView, final NotificationFragment.NotificacionItemListener listener) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.notification_card);
            date = (TextView) itemView.findViewById(R.id.notification_date);
            buttonAddToCalendar= (ImageView) itemView.findViewById(R.id.notification_button_add);
            buttonAddToCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSaveInCalendar(notificacion);
                }
            });
            message = (TextView) itemView.findViewById(R.id.notification_message);
            title = (TextView) itemView.findViewById(R.id.notification_title);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public Notificacion getNotificacion() {
            return notificacion;
        }

        @Override
        public void onClick(View v) {
            listener.onNoteClick(notificacion);
        }

        public void setNotificacion(Notificacion notificacion) {
            this.notificacion = notificacion;
        }
    }
}