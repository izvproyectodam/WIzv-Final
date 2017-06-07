package org.ieszaidinvergeles.android.webizv.model.database.manager;

// WIzv

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.model.database.helper.DatabaseHelper;
import org.ieszaidinvergeles.android.webizv.model.pojo.Sender;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;

public class SenderManager extends AbstractManager<Sender> {

    public SenderManager(Context c, SQLiteOpenHelper helper) {
        super(c, helper);
    }

    public SenderManager(Context c){
        super(c, new DatabaseHelper(c));
    }

    public int delete(String condition, String[] args) {
        return delete(DatabaseContract.SenderTable.TABLE_NAME, condition, args);
    }
    
    @Override
    public int delete(Sender object) {
        final String condicion = DatabaseContract.SenderTable._ID + " = ?";
        final String[] args = { object.getId() + "" };
        return delete(DatabaseContract.SenderTable.TABLE_NAME, condicion, args);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(DatabaseContract.SenderTable.TABLE_NAME);
    }

    public Sender get(long id) {
        final String where = DatabaseContract.SenderTable._ID + " = ? ";
        final String[] args = {id + ""};
        final Cursor c = getCursor(null, where, args, null, null, DatabaseContract.SenderTable._ID);
        if(c.getCount() > 0) {
            c.moveToFirst();
            return PojoUtil.getSender(c);
        }
        return null;
    }
    
    @Override
    public Cursor getCursor() {
        return getCursor(DatabaseContract.SenderTable.TABLE_NAME, null, DatabaseContract.SenderTable._ID);
    }
    
    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(DatabaseContract.SenderTable.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @Override
    public long insert(Sender object) {
        return insert(DatabaseContract.SenderTable.TABLE_NAME, PojoUtil.getContentValues(object, true));
    }

    @Override
    public long insert(ContentValues object) {
        return insert(DatabaseContract.SenderTable.TABLE_NAME, object);
    }

    @Override
    public int update(Sender object) {
        final ContentValues valores = PojoUtil.getContentValues(object, true);
        final String where = DatabaseContract.SenderTable._ID + " = ? ";
        final String[] args = { object.getId() + "" };
        return update(DatabaseContract.SenderTable.TABLE_NAME, valores, where, args);
    }

    @Override
    public int update(ContentValues values, String condition, String[] args) {
        return update(DatabaseContract.SenderTable.TABLE_NAME, values, condition, args);
    }
}