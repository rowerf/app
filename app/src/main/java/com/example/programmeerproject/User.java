package com.example.programmeerproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class User implements Parcelable {
    /* Fields */
    private int id;
    private String username;
    private String email;
    private String password;
    private String preferences;

    /* Constructors */
    public User() {}

    public User(String username, String email, String password, String preferences){
        this.username = username;
        this.email = email;
        this.password = password;
        this.preferences = preferences;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /* Properties: Getters and setters */
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getUsername(){ return this.username; }
    public void setUsername(String username) {this.username = username; }

    public String getEmail(){ return this.email; }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {this.password = password;}

    public String getPreferences(){
        return preferences;
    }
    public void setPreferences(String preferences){
        this.preferences = preferences;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(preferences);
    }

    // retrieving user data from parcel object
    private User(Parcel in){
        this.username = in.readString();
        this.email = in.readString();
        this.preferences = in.readString();
    }
}
