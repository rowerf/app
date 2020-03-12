package com.example.programmeerproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    /* Fields */
    private int user_id;
    private String username;
    private String password;
    private String preferences;

    /* Constructors */
    public User() {}

    public User(String username, String password, String preferences){
        this.username = username;
        this.password = password;
        this.preferences = preferences;
    }

    /* Properties: Getters and setters */
    public int getId() {return user_id; }
    public void setId(int id) {this.user_id = id; }

    public String getUsername(){ return this.username; }
    public void setUsername(String username) {this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) {this.password = password;}

    public String getPreferences(){
        return preferences;
    }
    public void setPreferences(String preferences){
        this.preferences = preferences;
    }
}
