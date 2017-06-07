package org.ieszaidinvergeles.android.webizv.model.database.provider;

// WIzv

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.model.database.manager.DatabaseManager;
import org.ieszaidinvergeles.android.webizv.model.database.manager.NotificationsManager;
import org.ieszaidinvergeles.android.webizv.model.database.manager.SenderManager;
import org.ieszaidinvergeles.android.webizv.util.Util;

import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.CONTENT_ITEM_TYPE_QUERY;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.CONTENT_ITEM_TYPE_NOTIFICATION;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.CONTENT_ITEM_TYPE_SENDER;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.CONTENT_TYPE_QUERY;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.CONTENT_TYPE_NOTIFICATION;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.CONTENT_TYPE_SENDER;

public class NotificationsProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER;
    private static final int NOTIFICATION = 0;
    private static final int NOTIFICATIONS = 1;
    private static final int QUERY = 2;
    private static final int QUERIES = 3;
    private static final int SENDER = 4;
    private static final int SENDERS = 5;

    private DatabaseManager manager;
    private NotificationsManager notificationsManager;
    private SenderManager senderManager;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(DatabaseContract.AUTHORITY, DatabaseContract.NotificationTable.TABLE_NAME + "/#", NOTIFICATION);
        URI_MATCHER.addURI(DatabaseContract.AUTHORITY, DatabaseContract.NotificationTable.TABLE_NAME, NOTIFICATIONS);
        URI_MATCHER.addURI(DatabaseContract.AUTHORITY, DatabaseContract.QUERY + "/#", QUERY);
        URI_MATCHER.addURI(DatabaseContract.AUTHORITY, DatabaseContract.QUERY, QUERIES);
        URI_MATCHER.addURI(DatabaseContract.AUTHORITY, DatabaseContract.SenderTable.TABLE_NAME + "/#", SENDER);
        URI_MATCHER.addURI(DatabaseContract.AUTHORITY, DatabaseContract.SenderTable.TABLE_NAME, SENDERS);

    }

    public NotificationsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        final int type = URI_MATCHER.match(uri);
        final String id = uri.getLastPathSegment();
        switch (type) {
            case NOTIFICATION:
            case QUERY:
                selection = Util.getSQLConditions(selection, DatabaseContract.NotificationTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case NOTIFICATIONS:
            case QUERIES:
                deleted = notificationsManager.delete(selection, selectionArgs);
                break;
            case SENDER:
                selection = Util.getSQLConditions(selection, DatabaseContract.NotificationTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case SENDERS:
                deleted = senderManager.delete(selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete exception.");
        }
        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case NOTIFICATION:
                return CONTENT_ITEM_TYPE_NOTIFICATION;
            case NOTIFICATIONS:
                return CONTENT_TYPE_NOTIFICATION;
            case QUERIES:
                return CONTENT_TYPE_QUERY;
            case QUERY:
                return CONTENT_ITEM_TYPE_QUERY;
            case SENDER:
                return CONTENT_ITEM_TYPE_SENDER;
            case SENDERS:
                return CONTENT_TYPE_SENDER;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int type = URI_MATCHER.match(uri);
        long id;
        if (type == NOTIFICATIONS) {
            id = notificationsManager.insert(values);
        } else if(type == SENDERS) {
            id = senderManager.insert(values);
        } else {
            throw new IllegalArgumentException("Insert exception.");
        }
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new IllegalArgumentException("Insert exception.");
    }

    @Override
    public boolean onCreate() {
        manager = new DatabaseManager(getContext());
        notificationsManager = new NotificationsManager(getContext());
        senderManager = new SenderManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final Cursor c;
        final int type = URI_MATCHER.match(uri);
        final String id = uri.getLastPathSegment();
        switch (type) {
            case NOTIFICATION:
                selection = Util.getSQLConditions(selection, DatabaseContract.NotificationTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case NOTIFICATIONS:
                c = notificationsManager.getCursor(projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case QUERY:
                selection = Util.getSQLConditions(selection, DatabaseContract.NotificationTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case QUERIES:
                c = manager.getCursor(selection, selectionArgs);
                break;
            case SENDER:
                selection = Util.getSQLConditions(selection, DatabaseContract.SenderTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case SENDERS:
                c = senderManager.getCursor(projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query exception.");
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int type = URI_MATCHER.match(uri);
        int value;
        final String id = uri.getLastPathSegment();
        switch (type) {
            case NOTIFICATION:
            case QUERY:
                selection = Util.getSQLConditions(selection, DatabaseContract.NotificationTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case NOTIFICATIONS:
            case QUERIES:
                value = notificationsManager.update(values, selection, selectionArgs);
                break;
            case SENDER:
                selection = Util.getSQLConditions(selection, DatabaseContract.SenderTable._ID + " = ?");
                selectionArgs = Util.getNewArray(selectionArgs, id);
            case SENDERS:
                value = senderManager.update(values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update exception.");
        }
        if (value > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return value;
    }
}