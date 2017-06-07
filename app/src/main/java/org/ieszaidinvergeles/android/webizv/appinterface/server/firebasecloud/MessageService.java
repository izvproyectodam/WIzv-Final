package org.ieszaidinvergeles.android.webizv.appinterface.server.firebasecloud;

// WIzv

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.appinterface.user.notification.NotificationView;
import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notification;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;
import org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver.ColorRequest;
import org.ieszaidinvergeles.android.webizv.util.Preferences;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.BROADCAST_INTENT;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_SECRETKEY;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_SECRETMESSAGE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_URLSERVER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.MESSAGETYPE_COLOR;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.MESSAGETYPE_PRIVATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.MESSAGETYPE_PUBLIC;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.MESSAGETYPE_SECRETKEY;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.MESSAGETYPE_URLSERVER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.USER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_USERSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.STATEVALUE_SEND;

public class MessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            final Notification notification = PojoUtil.getNotification(remoteMessage.getData());
            final Preferences preferences = new Preferences(this);
            if(notification.getType().compareTo(MESSAGETYPE_PUBLIC) == 0) {
                saveNotification(notification);
            } else if(notification.getType().compareTo(MESSAGETYPE_COLOR) == 0) {
                final ColorRequest colorRequest = new ColorRequest(getApplicationContext());
                colorRequest.send();
            } else if(notification.getType().compareTo(MESSAGETYPE_SECRETKEY) == 0) {
                preferences.set(PREFERENCE_SECRETKEY, notification.getTitle());
                preferences.set(PREFERENCE_SECRETMESSAGE, notification.getMessage());
            } else if(notification.getType().compareTo(MESSAGETYPE_URLSERVER) == 0) {
                preferences.set(PREFERENCE_URLSERVER, notification.getMessage());
            } else if(notification.getType().compareTo(MESSAGETYPE_PRIVATE) == 0 &&
                    preferences.get(USER).compareTo("") != 0 &&
                    preferences.get(PREFERENCE_USERSTATE).compareTo(STATEVALUE_SEND) == 0) {
                saveNotification(notification);
            }
        }
        /*if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }*/
    }

    private void saveNotification(final Notification notification) {
        final AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                final long result = ContentUris.parseId(getApplicationContext().getContentResolver().insert(DatabaseContract.TABLE_NOTIFICATION_URI, PojoUtil.getContentValues(notification, false)));
                if(result > 0) {
                    sendNotification(notification);
                    sendBroadcast();
                }
                return null;
            }
        };
        task.execute();
    }

    private void sendBroadcast() {
        final Intent intent = new Intent(BROADCAST_INTENT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendNotification(Notification notification) {
        final Intent intent = new Intent(this, NotificationView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        final Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.iesicon)
                .setContentTitle(getString(R.string.ies))
                .setContentText(notification.getTitle())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}