package org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver;

// WIzv

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.ieszaidinvergeles.android.webizv.model.pojo.Sender;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;
import org.ieszaidinvergeles.android.webizv.util.ServerConnection;

import java.util.List;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.BROADCAST_INTENT;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLSERVERCOLOR;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.TABLE_SENDER_URI;

public class ColorRequest implements ServerConnection.ServerConnectionResponseListener {

    final private Context context;

    public ColorRequest(Context context) {
        this.context = context;
    }

    @Override
    public void onServerResponse(final String response) {
        final AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                sendBroadcast();
                final List<Sender> list = PojoUtil.getSenders(response);
                for(Sender sender: list) {
                    final ContentValues values = PojoUtil.getContentValues(sender);
                    try {
                        final Uri uri = context.getContentResolver().insert(TABLE_SENDER_URI, values);
                    } catch(Exception e) {
                        final Uri updateUri = ContentUris.withAppendedId(TABLE_SENDER_URI, sender.getId());
                        context.getContentResolver().update(updateUri, values, null, null);
                    }
                }
                sendBroadcast();
                return null;
            }
        };
        task.execute();
    }

    public boolean send() {
        final ServerConnection connection = new ServerConnection(context, URLSERVERCOLOR, "", this);
        return connection.send();
    }

    private void sendBroadcast() {
        final Intent intent = new Intent(BROADCAST_INTENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}