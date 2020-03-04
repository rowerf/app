package com.example.programmeerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDBHandler extends SQLiteOpenHelper {
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "UserDB.db";

    // User table name
    private static final String TABLE_USER = "User";

    // User table column names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PREFERENCES = "preferences";

    int i;


    public UserDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL Query
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT,"
                /*+ COLUMN_EMAIL + " TEXT,"*/ + COLUMN_PASSWORD + " TEXT," + COLUMN_PREFERENCES + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        // Drop table SQL Query
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
        db.execSQL(DROP_USER_TABLE);
        // Create tables again
        onCreate(db);
    }

    // Create user record
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_PREFERENCES, user.getPreferences());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // Fetch all users in a list
    public List<User> getAllUsers(){
        String[] columns = {COLUMN_USER_ID,COLUMN_USERNAME,COLUMN_PASSWORD,COLUMN_PREFERENCES};
        String sortOrder = COLUMN_USERNAME + "ASC";
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // SELECT id, name, email, password and preferences FROM user ORDER BY username
        Cursor c = db.query(TABLE_USER,columns, null, null, null,
                            null, sortOrder);
        // Go through cursor and add to userList
        if(c.moveToFirst()){
            do {
                User user = new User();
                user.setId(Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWORD)));
                user.setPreferences(c.getString(c.getColumnIndex(COLUMN_PREFERENCES)));
                // Adding user record to list
                userList.add(user);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        // return user list
        return userList;
    }

    // Fetch user_id based on username
    public int getUserId(String username){
        String[] columns = { COLUMN_USER_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        // Selection criteria, argument(s)
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor c = db.query(TABLE_USER, // table to query
                columns,                // columns to return
                selection,              // columns or the WHERE clause
                selectionArgs,          // values for the WHERE clause
                null,          // group the rows
                null,           // filter by row groups
                null);         // the sort order
        if (c.moveToFirst()){
            i = Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_USER_ID)));
        } while (c.moveToNext());
        c.close();
        db.close();
        return i;
    }

    // Fetch a user as object instance
    public User getUser(int id){
        String[] columns = {COLUMN_USER_ID,COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PREFERENCES};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USER,columns,selection,selectionArgs,null, null, null);
        User user = new User();

        if(c.moveToFirst()){
            do {
                user.setId(Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWORD)));
                user.setPreferences(c.getString(c.getColumnIndex(COLUMN_PREFERENCES)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return user;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Check if username not already exists in UserDB
    public boolean checkUser(String username) {
        // array of columns to fetch
        String[] columns = { COLUMN_USER_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria, argument(s)
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        // query user table: SELECT user_id FROM user WHERE username = 'someUsernameHere';
        Cursor cursor = db.query(TABLE_USER,// table to query
                columns,                    // columns to return
                selection,                  // columns for the WHERE clause
                selectionArgs,              // the values for the WHERE clause
                null,              // group the rows
                null,               // filter by row groups
                null);             // the sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        // when the username is already in the database
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /* This method to check user exist or not, based on username and password. Returns true/false */
    public boolean checkUser(String username, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        // query user table with conditions
        /* SELECT user_id FROM user WHERE user_email = 'hiereenemail@adres.com' AND user_password = 'qwerty';*/
        Cursor cursor = db.query(TABLE_USER, // Table to query
                columns,                     // columns to return
                selection,                   // columns for the WHERE clause
                selectionArgs,               // The values for the WHERE clause
                null,               // group the rows
                null,                // filter by row groups
                null);              // The sort order
        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
