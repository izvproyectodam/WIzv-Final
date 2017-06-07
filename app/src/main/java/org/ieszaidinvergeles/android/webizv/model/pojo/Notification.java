package org.ieszaidinvergeles.android.webizv.model.pojo;

// WIzv

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

    private long id;
    private String title, message;
    private long idsender;
    private String type, extra, date;
    private long read;

    public Notification() {
        this(0, null, null, 0, null, null, null, 0);
    }

    public Notification(long id, String title, String message, long idsender, String type, String extra, String date, long read) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.idsender = idsender;
        this.type = type;
        this.extra = extra;
        this.date = date;
        this.read = read;
    }

    public Notification(String title, String message, String idsender, String type, String extra, String date) {
        this(0, title, message, 0, type, extra, date, 0);
        try {
            final long id = Long.parseLong(idsender);
            this.idsender = id;
        } catch(NumberFormatException e) {
        }
    }

    protected Notification(Parcel in) {
        id = in.readLong();
        title = in.readString();
        message = in.readString();
        idsender = in.readLong();
        type = in.readString();
        extra = in.readString();
        date = in.readString();
        read = in.readLong();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getDate() {
        return date;
    }

    public String getExtra() {
        return extra;
    }

    public long getId() {
        return id;
    }

    public long getIdsender() {
        return idsender;
    }

    public String getMessage() {
        return message;
    }

    public long getRead() {
        return read;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIdsender(long idsender) {
        this.idsender = idsender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRead(long read) {
        this.read = read;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeLong(idsender);
        dest.writeString(type);
        dest.writeString(extra);
        dest.writeString(date);
        dest.writeLong(read);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", idsender=" + idsender +
                ", type='" + type + '\'' +
                ", extra='" + extra + '\'' +
                ", date='" + date + '\'' +
                ", read=" + read +
                '}';
    }
}