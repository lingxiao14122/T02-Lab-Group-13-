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
        String userSqlStatement = "CREATE TABLE user_info(_id INTEGER PRIMARY KEY AUTOINCREMENT, full_name TEXT, username TEXT, password TEXT);";
        String historySqlStatement = "CREATE TABLE history(_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, date_time TEXT, total_round INTEGER, total_round_won INTEGER, cards_setting INTEGER);";

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
        Log.e(null, "Username: " + username);
        values.put("_id", ((int) userCount() + 1));
        values.put("full_name", fullName);
        values.put("username", username);
        values.put("password", password);

        try {
            if(writableDb == null) writableDb = getWritableDatabase();
            writableDb.insert(USER_INFO_TABLE_NAME, null, values);
            Log.e(null, "Successful insert user info");
        } catch(Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return newId;
    }

    public long insertHistory(int userId, String dateTime, int totalRound, int totalRoundWon, int cardsSetting){
        long newId = 0;

        ContentValues values = new ContentValues();
        values.put("_id", ((int) historyCount() + 1));
        values.put("userId", userId);
        values.put("date_time", dateTime);
        values.put("total_round", totalRound);
        values.put("total_round_won", totalRoundWon);
        values.put("cards_setting", cardsSetting);

        try {
            if(writableDb == null) writableDb = getWritableDatabase();
            writableDb.insert(HISTORY_TABLE_NAME, null, values);
            Log.e(null, "Successful insert history");
        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return newId;
    }

    public boolean checkAuth(String username, String password){
        boolean result = false;
        Cursor search = null;

        try{
            if(readableDb == null) readableDb = getReadableDatabase();
            String sqlStatement = "SELECT * FROM `" + USER_INFO_TABLE_NAME + "` WHERE `full_name` = '" + username + "' AND `password` = '" + password + "'";
            Log.e(null, sqlStatement);
            search = readableDb.rawQuery(sqlStatement, null);

            Log.e(null, String.valueOf(search.getCount()));

            if(search.moveToFirst()){
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return result;
    }

    public int getUserId(String username, String password){
        int result = 0;
        Cursor search = null;

        try{
            if (readableDb == null) readableDb = getReadableDatabase();
            String sqlStatement = "SELECT * FROM `" + USER_INFO_TABLE_NAME + "` WHERE `full_name` = '" + username + "' AND `password` = '" + password + "'";
            search = readableDb.rawQuery(sqlStatement, null);
            search.moveToFirst();

            Log.e(null, String.valueOf(search.getCount()));

            if(search.moveToFirst()){
                result = search.getInt(search.getColumnIndex("_id"));
            } else {
                result = 0;
            }
        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return result;
    }

    public Cursor getHistory(int userId){
        Cursor search = null;

        try{
            if(readableDb == null) readableDb = getReadableDatabase();
            String sqlStatement = "SELECT * FROM `" + HISTORY_TABLE_NAME + "` WHERE `userId` = '" + userId + "'";
            search = readableDb.rawQuery(sqlStatement, null);
        } catch (Exception e){
            Log.e("Database Exception: ", e.getMessage());
        }

        return search;
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
