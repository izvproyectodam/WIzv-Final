package org.ieszaidinvergeles.android.webizv.model.database.manager;

// WIzv

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.model.database.helper.DatabaseHelper;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends AbstractManager {

    public DatabaseManager(Context c, SQLiteOpenHelper helper) {
        super(c, helper);
    }

    public DatabaseManager(Context c) {
        super(c, new DatabaseHelper(c)) ;
    }

    @Override
    public int delete(Object object) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    public List<Notificacion> getAll() {
        final List<Notificacion> list = new ArrayList<>();
        final Cursor cursor = getCursor();
        Notificacion notificacion;
        while (cursor.moveToNext ()) {
            notificacion = PojoUtil.getNotificacion(cursor);
            list.add(notificacion);
        }
        cursor.close();
        return list;
    }

    @Override
    public Cursor getCursor() {
        return getCursor(null, null);
    }

    public Cursor getCursor(String selection, String[] selectionArgs) {
        String condition;
        if(selection == null) {
            condition = "";
        } else {
            condition = "where " + selection;
        }
        final String sql = "select n.*, s." + DatabaseContract.SenderTable.COLUMN_NAME_NAME + ", s." + DatabaseContract.SenderTable.COLUMN_NAME_COLOR +
                " from "+ DatabaseContract.NotificationTable.TABLE_NAME +" n " +
                " left join "+ DatabaseContract.SenderTable.TABLE_NAME +" s " +
                " on n." + DatabaseContract.NotificationTable.COLUMN_NAME_SENDER + " = s."+ DatabaseContract.SenderTable._ID +
                condition +
                " order by n." + DatabaseContract.NotificationTable._ID + " desc";
        return getDatabase().rawQuery(sql, selectionArgs);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return null;
    }

    @Override
    public long insert(Object object) {
        return 0;
    }

    @Override
    public long insert(ContentValues object) {
        return 0;
    }

    @Override
    public int update(Object object) {
        return 0;
    }

    @Override
    public int update(ContentValues values, String condition, String[] args) {
        return 0;
    }
}