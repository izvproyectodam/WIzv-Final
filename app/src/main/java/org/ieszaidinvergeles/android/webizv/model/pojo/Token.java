package org.ieszaidinvergeles.android.webizv.model.pojo;

// WIzv

import android.os.Parcel;
import android.os.Parcelable;

public class Token implements Parcelable {

    private long id;
    private String seneca, token;

    public Token() {
        this(0, "", "");
    }

    public Token(long id, String seneca, String token) {
        this.id = id;
        this.seneca = seneca;
        this.token = token;
    }

    public Token(String seneca, String token) {
        this(0, seneca, token);
    }

    public Token(String token) {
        this(0, "", token);
    }

    protected Token(Parcel in) {
        id = in.readLong();
        seneca = in.readString();
        token = in.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getSeneca() {
        return seneca;
    }

    public String getToken() {
        return token;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSeneca(String seneca) {
        this.seneca = seneca;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(seneca);
        dest.writeString(token);
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", seneca='" + seneca + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}