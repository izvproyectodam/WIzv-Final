package org.ieszaidinvergeles.android.webizv.model.pojo;

// WIzv

import android.os.Parcel;
import android.os.Parcelable;

public class Sender implements Parcelable {

    private long id;
    private String name, color;

    public Sender() {
        this(0, null, null);
    }

    public Sender(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Sender(String name, String color) {
        this(0, name, color);
    }

    protected Sender(Parcel in) {
        id = in.readLong();
        name = in.readString();
        color = in.readString();
    }

    public static final Creator<Sender> CREATOR = new Creator<Sender>() {
        @Override
        public Sender createFromParcel(Parcel in) {
            return new Sender(in);
        }

        @Override
        public Sender[] newArray(int size) {
            return new Sender[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(color);
    }

    @Override
    public String toString() {
        return "Sender{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}