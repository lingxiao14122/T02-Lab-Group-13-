package com.example.weiliang.group_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {

    private ArrayList<String> dateTimeList;
    private ArrayList<Integer> totalRoundList;
    private ArrayList<Integer> totalRoundWonList;
    private ArrayList<Integer> cardsSettingList;
    private LayoutInflater mInflater;

    class HistoryViewHolder extends RecyclerView.ViewHolder{

        public final TextView tvDateTime;
        public final TextView tvTotalRound;
        public final TextView tvTotalRoundWon;
        public final TextView tvWinningPercentage;
        public final TextView tvCardsSetting;
        final HistoryListAdapter mAdapter;

        HistoryViewHolder(View itemView, HistoryListAdapter adapter){
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tv_dateTime);
            tvTotalRound = itemView.findViewById(R.id.tv_totalRound_history);
            tvTotalRoundWon = itemView.findViewById(R.id.tv_totalRoundWon_history);
            tvWinningPercentage = itemView.findViewById(R.id.tv_winningPercentage);
            tvCardsSetting = itemView.findViewById(R.id.tv_cards_setting);
            this.mAdapter = adapter;
        }
    }

    HistoryListAdapter(Context context, ArrayList<String> dateTimeList, ArrayList<Integer> totalRoundList, ArrayList<Integer> totalRoundWonList, ArrayList<Integer> cardsSettingList){
        mInflater = LayoutInflater.from(context);
        this.dateTimeList = dateTimeList;
        this.totalRoundList = totalRoundList;
        this.totalRoundWonList = totalRoundWonList;
        this.cardsSettingList = cardsSettingList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mItemView = mInflater.inflate(R.layout.historylist_item, viewGroup, false);

        return new HistoryViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int position) {
        String dateTime = dateTimeList.get(position);
        int totalRound = totalRoundList.get(position);
        int totalRoundWon = totalRoundWonList.get(position);
        double winningPercentage = (totalRoundWon / totalRound) * 100;
        int cardsSetting = cardsSettingList.get(position);

        historyViewHolder.tvDateTime.setText(dateTime);
        historyViewHolder.tvTotalRound.setText("Total Round: " + String.valueOf(totalRound));
        historyViewHolder.tvTotalRoundWon.setText("Total Round Won: " + String.valueOf(totalRoundWon));
        historyViewHolder.tvWinningPercentage.setText("Winning Percentage(%): " + String.valueOf(winningPercentage));
        historyViewHolder.tvCardsSetting.setText("Cards Setting: " + String.valueOf(cardsSetting));
    }

    @Override
    public int getItemCount() {
        return dateTimeList.size();
    }
}
