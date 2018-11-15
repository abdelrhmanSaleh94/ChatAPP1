package com.example.abdelrahmansaleh.chatapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String name;
    String message;
    String url;
    String userId;

    public User(String name, String message, String url, String userId) {
        this.name = name;
        this.message = message;
        this.url = url;
        this.userId = userId;
    }

    protected User(Parcel in) {
        name = in.readString();
        message = in.readString();
        url = in.readString();
        userId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User( in );
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(String name, String profileImageUrl, String user1) {
        this.name=name;
        this.url=profileImageUrl;
        this.userId=user1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( name );
        dest.writeString( message );
        dest.writeString( url );
        dest.writeString( userId );
    }
}
