package org.ieszaidinvergeles.android.webizv.model.database.manager;

// WIzv

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.model.database.helper.DatabaseHelper;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notification;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationsManager extends AbstractManager<Notification> {

    public NotificationsManager(Context c, SQLiteOpenHelper helper) {
        super(c, helper);
    }

    public NotificationsManager(Context c) {
        super(c, new DatabaseHelper(c));
    }

    public int delete(long id) {
        final String condicion = DatabaseContract.NotificationTable._ID + " = ?";
        final String[] args = { id + "" };
        return delete(DatabaseContract.NotificationTable.TABLE_NAME, condicion, args);
    }

    public int delete(String condition, String[] args) {
        return delete(DatabaseContract.NotificationTable.TABLE_NAME, condition, args);
    }

    @Override
    public int delete(Notification object) {
        return delete(object.getId());
    }

    @Override
    public int deleteAll() {
        return deleteAll(DatabaseContract.NotificationTable.TABLE_NAME);
    }

    public Notification get(long id) {
        final String where = DatabaseContract.NotificationTable._ID + " = ? ";
        final String[] args = {id + ""};
        final Cursor c = getCursor(null, where, args, null, null, DatabaseContract.NotificationTable._ID);
        if(c.getCount() > 0) {
            c.moveToFirst();
            return PojoUtil.getNotification(c);
        }
        return null;
    }

    public List<Notification> getAll() {
        final List<Notification> list = new ArrayList<>();
        final Cursor cursor = getCursor();
        Notification notification;
        while (cursor.moveToNext ()) {
            notification = PojoUtil.getNotification(cursor);
            list.add(notification);
        }
        cursor.close();
        return list;
    }

    @Override
    public Cursor getCursor() {
        return getCursor(DatabaseContract.NotificationTable.TABLE_NAME, null, DatabaseContract.NotificationTable._ID + " desc");
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(DatabaseContract.NotificationTable.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @Override
    public long insert(Notification object) {
        return insert(DatabaseContract.NotificationTable.TABLE_NAME, PojoUtil.getContentValues(object, false));
    }

    @Override
    public long insert(ContentValues object) {
        return insert(DatabaseContract.NotificationTable.TABLE_NAME, object);
    }

    @Override
    public int update(Notification object) {
        final ContentValues valores = PojoUtil.getContentValues(object, true);
        final String where = DatabaseContract.NotificationTable._ID + " = ? ";
        final String[] args = { object.getId() + "" };
        return update(DatabaseContract.NotificationTable.TABLE_NAME, valores, where, args);
    }

    @Override
    public int update(ContentValues values, String condition, String[] args) {
        return update(DatabaseContract.NotificationTable.TABLE_NAME, values, condition, args);
    }
}