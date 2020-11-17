package com.example.weiliang.group_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "card_game";
    private static final String USER_INFO_TABLE_NAME = "user_info";
    private static final String HISTORY_TABLE_NAME = "history";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase readableDb, writableDb;

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userSqlStatement = "CREATE TABLE user_info(_id INTEGER PRIMARY KEY, full_name TEXT, username TEXT, password TEXT);";
        String historySqlStatement = "CREATE TABLE history(_id INTEGER PRIMARY KEY, userId INTEGER, date_time TEXT, total_round INTEGER, total_round_won INTEGER);";

        try{
            sqLiteDatabase.execSQL(userSqlStatement);
            sqLiteDatabase.execSQL(historySqlStatement);
        } catch(Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }
    }

    public long insertUserInfo(String fullName, String username, String password){
        long newId = 0;

        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("username", username);
        values.put("password", password);

        try {
            if(writableDb == null) writableDb = getWritableDatabase();
            writableDb.insert(USER_INFO_TABLE_NAME, null, values);
        } catch(Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return newId;
    }

    public boolean checkAuth(String username, String password){
        boolean result = false;
        Cursor search = null;

        try{
            if(readableDb == null) readableDb = getReadableDatabase();
            String sqlStatement = "SELECT * FROM `" + USER_INFO_TABLE_NAME + "`";
            search = readableDb.rawQuery(sqlStatement, null);
            search.moveToFirst();

            loop:{
                for(int i = 0; i < search.getCount(); i ++){
                    if(username.equals(search.getString(search.getColumnIndex("username"))) && password.equals(search.getString(search.getColumnIndex("password")))){
                        result = true;
                        break loop;
                    }
                }
            }

        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return result;
    }

    public long userCount(){
        long count = 0;
        try{
            if(readableDb == null) readableDb = getReadableDatabase();
            count = DatabaseUtils.queryNumEntries(readableDb, USER_INFO_TABLE_NAME);
        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }
        return count;
    }

    public long historyCount(){
        long count = 0;
        try{
            if(readableDb == null) readableDb = getReadableDatabase();
            count = DatabaseUtils.queryNumEntries(readableDb, HISTORY_TABLE_NAME);
        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }
        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
