package org.ieszaidinvergeles.android.webizv.model.database.contract;

// WIzv

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {

    public final static String DATABASE = "notificacion.sqlite";
    public final static String QUERY = "consultanotificacion";

    private DatabaseContract() {
    }

    public static abstract class NotificationTable implements BaseColumns {
        public static final String TABLE_NAME          = "notificacion";
        public static final String COLUMN_NAME_TITLE   = "titulo";
        public static final String COLUMN_NAME_MESSAGE = "mensaje";
        public static final String COLUMN_NAME_SENDER  = "idorigen";
        public static final String COLUMN_NAME_TYPE    = "tipo";
        public static final String COLUMN_NAME_EXTRA   = "extra";
        public static final String COLUMN_NAME_DATE    = "fecha";
        public static final String COLUMN_NAME_READ    = "leido";
    }

    public static abstract class SenderTable implements BaseColumns {
        public static final String TABLE_NAME        = "origen";
        public static final String COLUMN_NAME_NAME  = "nombre";
        public static final String COLUMN_NAME_COLOR = "color";
    }

    /*proveedor*/
    public final static String AUTHORITY = "org.ieszaidinvergeles.android.webizv.database";
    public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri TABLE_NOTIFICATION_URI = Uri.withAppendedPath(CONTENT_URI, NotificationTable.TABLE_NAME);
    public static final Uri TABLE_QUERY_URI = Uri.withAppendedPath(CONTENT_URI, QUERY);
    public static final Uri TABLE_SENDER_URI = Uri.withAppendedPath(CONTENT_URI, SenderTable.TABLE_NAME);

    /*tipos mime*/
    public final static String CONTENT_ITEM_TYPE_NOTIFICATION = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + NotificationTable.TABLE_NAME;
    public final static String CONTENT_ITEM_TYPE_QUERY = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + QUERY;
    public final static String CONTENT_ITEM_TYPE_SENDER = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + SenderTable.TABLE_NAME;
    public final static String CONTENT_TYPE_NOTIFICATION = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + NotificationTable.TABLE_NAME;
    public final static String CONTENT_TYPE_QUERY = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + QUERY;
    public final static String CONTENT_TYPE_SENDER = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + SenderTable.TABLE_NAME;
}