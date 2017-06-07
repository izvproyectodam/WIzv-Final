package org.ieszaidinvergeles.android.webizv.util;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

/**
 * Created by skyfishjy on 10/31/14.
 * https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * leer: https://github.com/codepath/android_guides/wiki/Using-the-RecyclerView
 *
 */

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Context contexto;
    private Cursor cursor;
    private boolean isValidData;
    private int columnaId;
    private DataSetObserver observerDataSet;

    public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
        contexto = context;
        this.cursor = cursor;
        this.isValidData = cursor != null;
        this.columnaId = isValidData ? this.cursor.getColumnIndex("_id") : -1;
        this.observerDataSet = new ObervadorDeDataSetNotificador();
        if (this.cursor != null) {
            this.cursor.registerDataSetObserver(observerDataSet);
        }
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {
        if (isValidData && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (isValidData && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(columnaId);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!isValidData) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(viewHolder, cursor);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && observerDataSet != null) {
            oldCursor.unregisterDataSetObserver(observerDataSet);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (observerDataSet != null) {
                cursor.registerDataSetObserver(observerDataSet);
            }
            columnaId = newCursor.getColumnIndexOrThrow("_id");
            isValidData = true;
            notifyDataSetChanged();
        } else {
            columnaId = -1;
            isValidData = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class ObervadorDeDataSetNotificador extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            isValidData = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            isValidData = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}