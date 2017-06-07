package org.ieszaidinvergeles.android.webizv.appinterface.user.common;

// WIzv

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;
import org.ieszaidinvergeles.android.webizv.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.TABLE_QUERY_URI;

public class CalendarPresenter implements CalendarContract {

    private Context context;

    public CalendarPresenter(Context context) {
        this.context = context;
    }

    /*final int[] getCalendarIds() {
        //<uses-permission android:name="android.permission.READ_CALENDAR" />
        final String[] projection = new String[] {CalendarContract.Calendars._ID,
                                                    CalendarContract.Calendars.ACCOUNT_NAME};
        final ContentResolver contentResolver = ((Fragment) notificationView).getContext().getContentResolver();
        final Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"), projection, null, null, null);
        if (cursor.moveToFirst()) {
            //final String[] columnNames = new String[cursor.getCount()];
            final int[] columnIds = new int[cursor.getCount()];
            for (int i = 0; i < columnIds.length; i++) {
                columnIds[i] = cursor.getInt(0);
                //columnNames[i] = cursor.getString(1);
                System.out.println(cursor.getInt(0) + " " + cursor.getString(1));
                cursor.moveToNext();
            }
            return columnIds;
        }
        return null;
    }*/

    @Override
    public void saveInCalendar(final Notificacion notification) {

        final AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                //String extra = "event; 2017-05-15; 16:15; 17:15";
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    return 0;
                }
                final Calendar calStart = Calendar.getInstance();
                final Calendar calEnd = Calendar.getInstance();
                final List<String> extraFields = Util.splitMessage(notification.getExtra());
                final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                final String strStart = extraFields.get(1) + " " + extraFields.get(2) + ":00"; //"2017-05-10 17:00:00";
                final String strEnd = extraFields.get(1) + " " + extraFields.get(3) + ":00";
                try {
                    startDate = df.parse(strStart);
                    endDate = df.parse(strEnd);
                } catch (ParseException e) {
                    return 0;
                }
                calStart.setTime(startDate);
                calEnd.setTime(endDate);
                final ContentResolver contentResolver = context.getContentResolver();
                final ContentValues calEvent = new ContentValues();
                calEvent.put(android.provider.CalendarContract.Events.CALENDAR_ID, 1); //1 - default ???
                calEvent.put(android.provider.CalendarContract.Events.TITLE, notification.getTitle());
                calEvent.put(android.provider.CalendarContract.Events.DESCRIPTION, notification.getMessage());
                calEvent.put(android.provider.CalendarContract.Events.DTSTART, calStart.getTimeInMillis());
                calEvent.put(android.provider.CalendarContract.Events.DTEND, calEnd.getTimeInMillis());
                calEvent.put(android.provider.CalendarContract.Events.EVENT_TIMEZONE, "" + java.util.Locale.getDefault());
                final Uri uri = contentResolver.insert(android.provider.CalendarContract.Events.CONTENT_URI, calEvent);
                final int id = Integer.parseInt(uri.getLastPathSegment());
                final ContentValues reminders = new ContentValues();
                reminders.put(android.provider.CalendarContract.Reminders.EVENT_ID, id);
                reminders.put(android.provider.CalendarContract.Reminders.METHOD, android.provider.CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(android.provider.CalendarContract.Reminders.MINUTES, 60);
                try {
                    contentResolver.insert(android.provider.CalendarContract.Reminders.CONTENT_URI, reminders);
                } catch(android.database.sqlite.SQLiteException e) {
                    return 0;
                }
                updateNotification(notification);
                return 1;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                int i = (int) o;
                if(context != null) {
                    if(i == 1){
                        Toast.makeText(context, R.string.calendario_si, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.calendario_no, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        task.execute();
    }

    private void updateNotification(final Notificacion notification) {
        notification.setRead(1);
        final Uri uri = ContentUris.withAppendedId(TABLE_QUERY_URI, notification.getId());
        context.getContentResolver().update(uri, PojoUtil.getContentValues(notification), null, null);
    }
}