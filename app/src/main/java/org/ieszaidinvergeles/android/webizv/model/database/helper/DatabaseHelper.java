package org.ieszaidinvergeles.android.webizv.model.database.helper;

// WIzv

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table if not exists " + DatabaseContract.SenderTable.TABLE_NAME +
                " (" +
                DatabaseContract.SenderTable._ID + " integer primary key, " +
                DatabaseContract.SenderTable.COLUMN_NAME_NAME + " text, " +
                DatabaseContract.SenderTable.COLUMN_NAME_COLOR + " text " +
                ")";
        db.execSQL(sql);
        sql="create table if not exists " + DatabaseContract.NotificationTable.TABLE_NAME +
                " (" +
                DatabaseContract.NotificationTable._ID + " integer primary key autoincrement , " +
                DatabaseContract.NotificationTable.COLUMN_NAME_TITLE + " text, " +
                DatabaseContract.NotificationTable.COLUMN_NAME_MESSAGE + " text, " +
                DatabaseContract.NotificationTable.COLUMN_NAME_SENDER + " integer, " +
                DatabaseContract.NotificationTable.COLUMN_NAME_TYPE + " text, " +
                DatabaseContract.NotificationTable.COLUMN_NAME_EXTRA + " text, " +
                DatabaseContract.NotificationTable.COLUMN_NAME_DATE + " text, " +
                DatabaseContract.NotificationTable.COLUMN_NAME_READ + " integer " +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists " + DatabaseContract.NotificationTable.TABLE_NAME;
        db.execSQL(sql);
        sql="drop table if exists " + DatabaseContract.SenderTable.TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}