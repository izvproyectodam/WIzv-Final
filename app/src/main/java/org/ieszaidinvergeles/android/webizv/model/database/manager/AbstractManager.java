package org.ieszaidinvergeles.android.webizv.model.database.manager;

// WIzv

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractManager<T> {

    final private SQLiteOpenHelper helper;
    private SQLiteDatabase bd;

    public AbstractManager(Context c, SQLiteOpenHelper helper) {
        this(c, helper, true);
    }

    public AbstractManager(Context c, SQLiteOpenHelper helper, boolean write) {
        this.helper = helper;
        if(write) {
            this.open();
        } else {
            this.openRead();
        }
    }

    public AbstractManager(SQLiteOpenHelper helper, SQLiteDatabase bd){
        this.helper = helper;
        this.bd = bd;
    }

    public void close() {
        helper.close();
    }

    public abstract int delete(T object);

    public int delete(String table, String condition, String[] args) {
        return bd.delete(table, condition, args);
    }

    public abstract int deleteAll();

    public int deleteAll(String table){
        return this.delete(table, "1=1", null);
    }

    public abstract Cursor getCursor();

    public abstract Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    public Cursor getCursor(String table, String[] projection, String orderby) {
        return getCursor(table, projection, null, null, null, null, orderby);
    }

    public Cursor getCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        final Cursor cursor = bd.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    public SQLiteDatabase getDatabase() {
        return bd;
    }

    public SQLiteOpenHelper getHelper() {
        return helper;
    }

    public abstract long insert(T object);

    public abstract long insert(ContentValues object);

    public long insert(String table, ContentValues values) {
        return bd.insert(table, null, values);
    }

    public void open() {
        bd = helper.getWritableDatabase();
    }

    public void openRead() {
        bd = helper.getReadableDatabase();
    }

    public Cursor query(String sql, String[] args){
        return bd.rawQuery(sql, args);
    }

    public abstract int update(T object);

    public abstract int update(ContentValues values, String condition, String[] args);

    public int update(String table, ContentValues values, String condition, String[] args) {
        return bd.update(table, values, condition, args);
    }
}