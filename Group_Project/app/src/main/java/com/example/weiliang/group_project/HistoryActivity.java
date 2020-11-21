package com.example.weiliang.group_project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private int userId;

    private TextView tvNohistory;
    private RecyclerView rvHistory;

    private ArrayList<String> dateTimeList;
    private ArrayList<Integer> totalRoundList;
    private ArrayList<Integer> totalRoundWonList;
    private ArrayList<Integer> cardsSettingList;
    private HistoryListAdapter mAdapter;

    private DatabaseOpenHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dateTimeList = new ArrayList<>();
        totalRoundList = new ArrayList<>();
        totalRoundWonList = new ArrayList<>();
        cardsSettingList = new ArrayList<>();

        Intent intent = getIntent();
        userId = intent.getIntExtra(LoginActivity.KEY_USERID, 0);

        mDb = new DatabaseOpenHelper(this);
        mDb.getReadableDatabase();

        tvNohistory = findViewById(R.id.tv_noHistory);
        rvHistory = findViewById(R.id.rvHistory);

        tvNohistory.setVisibility(View.GONE);
        rvHistory.setVisibility(View.GONE);

        Cursor result = mDb.getHistory(userId);
        result.moveToFirst();

        if(result == null || result.getCount() == 0){
            Log.e(null, "No History");
            tvNohistory.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < result.getCount(); i++){
                result.move(i);
                dateTimeList.add(result.getString(result.getColumnIndex("date_time")));
                totalRoundList.add(result.getInt(result.getColumnIndex("total_round")));
                totalRoundWonList.add(result.getInt(result.getColumnIndex("total_round_won")));
                cardsSettingList.add(result.getInt(result.getColumnIndex("cards_setting")));
            }

            mAdapter = new HistoryListAdapter(this, dateTimeList, totalRoundList, totalRoundWonList, cardsSettingList);
            rvHistory.setAdapter(mAdapter);
            rvHistory.setLayoutManager(new LinearLayoutManager(this));
            rvHistory.setVisibility(View.VISIBLE);
        }
    }
}