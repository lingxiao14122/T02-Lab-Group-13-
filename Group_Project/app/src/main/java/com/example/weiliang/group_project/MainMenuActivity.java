package com.example.weiliang.group_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    public final static String KEY_CARDS_SETTINGS = "com.example.weiliang.group_project.Cards_Settings";
    public final static String KEY_RECORD_HISTORY = "com.example.weiliang.group_project.Record_History";
    public final static int REQUEST_CODE = 1;

    private int userId;

    private int maximumCards;
    private boolean recordHistory;

    private DatabaseOpenHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mDb = new DatabaseOpenHelper(this);
        mDb.getReadableDatabase();

        Intent intent = getIntent();
        userId = intent.getIntExtra(LoginActivity.KEY_USERID, 0);

        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        maximumCards = Integer.parseInt(sharedPreferences.getString("maximum_cards", ""));
        recordHistory = sharedPreferences.getBoolean("record_history", true);

        Log.e(null, "Maximum Cards settings: " + String.valueOf(maximumCards));
        Log.e(null, "User ID: " + String.valueOf(userId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_game_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_history){
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(LoginActivity.KEY_USERID, userId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnPlay_clicked(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(KEY_CARDS_SETTINGS, maximumCards);
        intent.putExtra(KEY_RECORD_HISTORY, recordHistory);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String dateTime = data.getStringExtra(GameActivity.KEY_HISTORY_DATE_TIME);
            int totalRound = data.getIntExtra(GameActivity.KEY_HISTORY_TOTAL_ROUND, 0);
            int totalRoundWon = data.getIntExtra(GameActivity.KEY_HISTORY_TOTAL_ROUND_WON, 0);
            int cards_setting = data.getIntExtra(GameActivity.KEY_HISTORY_CARDS_SETTING, 0);

            Log.e(null, "Date and Time: " + dateTime);
            Log.e(null, "Total Round: " + String.valueOf(totalRound));
            Log.e(null, "Total Round Won: " + String.valueOf(totalRoundWon));
            Log.e(null, "Cards Setting: " + String.valueOf(cards_setting));

            mDb.insertHistory(userId, dateTime, totalRound, totalRoundWon, cards_setting);
        }
    }
}
