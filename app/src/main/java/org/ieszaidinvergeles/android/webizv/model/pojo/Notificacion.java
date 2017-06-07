package org.ieszaidinvergeles.android.webizv.model.pojo;

// WIzv

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import org.ieszaidinvergeles.android.webizv.util.Util;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.DEFAULTCARDCOLOR;

public class Notificacion implements Parcelable {

    private long id;
    private String title, message;
    private long idsender;
    private String type, extra, date;
    private long read;
    private String name, color;

    public Notificacion() {
        this(0, null, null, 0, null, null, null, 0, null, null);
    }

    public Notificacion(long id, String title, String message, long idsender, String type, String extra, String date, long read, String name, String color) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.idsender = idsender;
        this.type = type;
        this.extra = extra;
        this.date = date;
        this.read = read;
        this.name = name;
        this.color = color;
    }

    protected Notificacion(Parcel in) {
        id = in.readLong();
        title = in.readString();
        message = in.readString();
        idsender = in.readLong();
        type = in.readString();
        extra = in.readString();
        date = in.readString();
        read = in.readLong();
        name = in.readString();
        color = in.readString();
    }

    public static final Creator<Notificacion> CREATOR = new Creator<Notificacion>() {
        @Override
        public Notificacion createFromParcel(Parcel in) {
            return new Notificacion(in);
        }

        @Override
        public Notificacion[] newArray(int size) {
            return new Notificacion[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getShortMessage() {
        return Util.subString(message, 50);
    }

    public long getIdsender() {
        return idsender;
    }

    public String getType() {
        return type;
    }

    public String getExtra() {
        return extra;
    }

    public String getDate() {
        return date;
    }

    public String getFormattedDate() {
        return Util.weekDayFormat(date);
    }

    public long getRead() {
        return read;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        try {
            return Color.parseColor(color);
        } catch(IllegalArgumentException e) {
            return Color.parseColor(DEFAULTCARDCOLOR);
        } catch(NullPointerException e){
            return Color.parseColor(DEFAULTCARDCOLOR);
        }
    }

    public String getRealColor() {
        return color;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIdsender(long idsender) {
        this.idsender = idsender;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRead(long read) {
        this.read = read;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
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
        dest.writeString(name);
        dest.writeString(color);
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", idsender=" + idsender +
                ", type='" + type + '\'' +
                ", extra='" + extra + '\'' +
                ", date='" + date + '\'' +
                ", read=" + read +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}