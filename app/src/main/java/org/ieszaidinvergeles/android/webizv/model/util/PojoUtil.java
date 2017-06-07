package org.ieszaidinvergeles.android.webizv.model.util;

// WIzv

import android.content.ContentValues;
import android.database.Cursor;

import org.ieszaidinvergeles.android.webizv.model.data.Constants;
import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notification;
import org.ieszaidinvergeles.android.webizv.model.pojo.Sender;
import org.ieszaidinvergeles.android.webizv.model.pojo.Token;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.ID;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.TOKEN;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.USER;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_DATE;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_EXTRA;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_MESSAGE;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_READ;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_SENDER;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_TITLE;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.NotificationTable.COLUMN_NAME_TYPE;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.SenderTable.COLUMN_NAME_COLOR;
import static org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract.SenderTable.COLUMN_NAME_NAME;

public class PojoUtil {

    public static ContentValues getContentValues(Notificacion notificacion) {
        return PojoUtil.getContentValues(notificacion, true);
    }

    public static ContentValues getContentValues(Notificacion notificacion, boolean withId) {
        final ContentValues values = new ContentValues();
        if(withId) {
            values.put(DatabaseContract.NotificationTable._ID, notificacion.getId());
        }
        values.put(COLUMN_NAME_TITLE, notificacion.getTitle());
        values.put(COLUMN_NAME_MESSAGE, notificacion.getMessage());
        values.put(COLUMN_NAME_SENDER, notificacion.getIdsender());
        values.put(COLUMN_NAME_TYPE, notificacion.getType());
        values.put(COLUMN_NAME_EXTRA, notificacion.getExtra());
        values.put(COLUMN_NAME_DATE, notificacion.getDate());
        values.put(COLUMN_NAME_READ, notificacion.getRead());
        return values;
    }

    public static ContentValues getContentValues(Notification notification) {
        return PojoUtil.getContentValues(notification, true);
    }

    public static ContentValues getContentValues(Notification notification, boolean withId) {
        final ContentValues values = new ContentValues();
        if(withId) {
            values.put(DatabaseContract.NotificationTable._ID, notification.getId());
        }
        values.put(COLUMN_NAME_TITLE, notification.getTitle());
        values.put(COLUMN_NAME_MESSAGE, notification.getMessage());
        values.put(COLUMN_NAME_SENDER, notification.getIdsender());
        values.put(COLUMN_NAME_TYPE, notification.getType());
        values.put(COLUMN_NAME_EXTRA, notification.getExtra());
        values.put(COLUMN_NAME_DATE, notification.getDate());
        values.put(COLUMN_NAME_READ, notification.getRead());
        return values;
    }

    public static ContentValues getContentValues(Sender sender) {
        return PojoUtil.getContentValues(sender, true);
    }

    public static ContentValues getContentValues(Sender sender, boolean withId) {
        final ContentValues values = new ContentValues();
        if(withId) {
            values.put(DatabaseContract.SenderTable._ID, sender.getId());
        }
        values.put(COLUMN_NAME_NAME, sender.getName());
        values.put(COLUMN_NAME_COLOR, sender.getColor());
        return values;
    }

    public static Notificacion getNotificacion(Cursor c) {
        final Notificacion object = new Notificacion();
        object.setId(c.getLong(c.getColumnIndex(DatabaseContract.NotificationTable._ID)));
        object.setTitle(c.getString(c.getColumnIndex(COLUMN_NAME_TITLE)));
        object.setMessage(c.getString(c.getColumnIndex(COLUMN_NAME_MESSAGE)));
        object.setIdsender(c.getLong(c.getColumnIndex(COLUMN_NAME_SENDER)));
        object.setType(c.getString(c.getColumnIndex(COLUMN_NAME_TYPE)));
        object.setExtra(c.getString(c.getColumnIndex(COLUMN_NAME_EXTRA)));
        object.setDate(c.getString(c.getColumnIndex(COLUMN_NAME_DATE)));
        object.setRead(c.getLong(c.getColumnIndex(COLUMN_NAME_READ)));
        object.setName(c.getString(c.getColumnIndex(COLUMN_NAME_NAME)));
        object.setColor(c.getString(c.getColumnIndex(COLUMN_NAME_COLOR)));
        return object;
    }

    public static Notification getNotification(Cursor c) {
        final Notification object = new Notification();
        object.setId(c.getLong(c.getColumnIndex(DatabaseContract.NotificationTable._ID)));
        object.setTitle(c.getString(c.getColumnIndex(COLUMN_NAME_TITLE)));
        object.setMessage(c.getString(c.getColumnIndex(COLUMN_NAME_MESSAGE)));
        object.setIdsender(c.getLong(c.getColumnIndex(COLUMN_NAME_SENDER)));
        object.setType(c.getString(c.getColumnIndex(COLUMN_NAME_TYPE)));
        object.setExtra(c.getString(c.getColumnIndex(COLUMN_NAME_EXTRA)));
        object.setDate(c.getString(c.getColumnIndex(COLUMN_NAME_DATE)));
        object.setRead(c.getLong(c.getColumnIndex(COLUMN_NAME_READ)));
        return object;
    }

    public static Notification getNotification(Map<String, String> data) {
        try {
            return new Notification(URLDecoder.decode(data.get(COLUMN_NAME_TITLE), "UTF-8"),
                                    URLDecoder.decode(data.get(COLUMN_NAME_MESSAGE), "UTF-8"),
                                    data.get(COLUMN_NAME_SENDER),
                                    URLDecoder.decode(data.get(COLUMN_NAME_TYPE), "UTF-8"),
                                    URLDecoder.decode(data.get(COLUMN_NAME_EXTRA), "UTF-8"),
                                    data.get(COLUMN_NAME_DATE));
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static Notification getNotification(String json) {
        try {
            final JSONObject jObj = new JSONObject(json);
            return new Notification(0,
                                    jObj.getString(COLUMN_NAME_TITLE),
                                    jObj.getString(COLUMN_NAME_MESSAGE),
                                    jObj.getLong(COLUMN_NAME_SENDER),
                                    jObj.getString(COLUMN_NAME_TYPE),
                                    jObj.getString(COLUMN_NAME_EXTRA),
                                    jObj.getString(COLUMN_NAME_DATE),
                                    jObj.getLong(COLUMN_NAME_READ));
        } catch (JSONException e) {
        }
        return null;
    }

    public static Long getResponse(String json) {
        try {
            final JSONObject jObj = new JSONObject(json);
            return jObj.getLong("r");
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
        return null;
    }

    public static Sender getSender(Cursor c) {
        final Sender object = new Sender();
        object.setId(c.getLong(c.getColumnIndex(DatabaseContract.SenderTable._ID)));
        object.setName(c.getString(c.getColumnIndex(COLUMN_NAME_NAME)));
        object.setColor(c.getString(c.getColumnIndex(COLUMN_NAME_COLOR)));
        return object;
    }

    public static Sender getSender(JSONObject jObj) {
        try {
            return new Sender(jObj.getLong(Constants.ID),
                                jObj.getString(COLUMN_NAME_NAME),
                                jObj.getString(COLUMN_NAME_COLOR));
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
        return null;
    }

    public static Sender getSender(String json) {
        try {
            return getSender(new JSONObject(json));
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
        return null;
    }

    public static List<Sender> getSenders(String json) {
        final List<Sender> list = new ArrayList<>();
        if (json != null) {
            try {
                final JSONArray jsonArray = new JSONArray(json);
                final int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list.add(PojoUtil.getSender((JSONObject) jsonArray.get(i)));
                    } catch (JSONException e) {
                    }
                }
            } catch (JSONException e) {
            }
        }
        return list;
    }

    public static String toJson(Token token) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(ID, token.getId());
            jsonObj.put(USER, token.getSeneca());
            jsonObj.put(TOKEN, token.getToken());
            return jsonObj.toString();
        }
        catch(JSONException ex) {
        }
        return null;
    }
}