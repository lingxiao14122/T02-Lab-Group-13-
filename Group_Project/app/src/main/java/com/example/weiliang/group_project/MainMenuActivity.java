package com.example.weiliang.group_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    public final static String KEY_CARDS_SETTINGS = "com.example.weiliang.group_project.Cards_Settings";
    public final static String KEY_RECORD_HISTORY = "com.example.weiliang.group_project.Record_History";
    public final static int REQUEST_CODE = 1;

    private int maximumCards;
    private boolean recordHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        maximumCards = Integer.parseInt(sharedPreferences.getString("maximum_cards", ""));
        recordHistory = sharedPreferences.getBoolean("record_history", true);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnPlay_clicked(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(KEY_CARDS_SETTINGS, maximumCards);
        intent.putExtra(KEY_RECORD_HISTORY, recordHistory);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
