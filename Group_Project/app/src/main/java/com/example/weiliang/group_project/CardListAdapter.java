package com.example.weiliang.group_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    private ArrayList<PokerCard> cardList;
    private LayoutInflater mInflater;

    class CardViewHolder extends RecyclerView.ViewHolder{

        public final TextView mCount;
        public final ImageView iv_card;
        final CardListAdapter mAdapter;

        CardViewHolder(View itemView, CardListAdapter adapter){
            super(itemView);
            iv_card = itemView.findViewById(R.id.ivCard);
            mCount = itemView.findViewById(R.id.tvCount);
            this.mAdapter = adapter;
        }
    }

    CardListAdapter(Context context, ArrayList<PokerCard> cardList){
        mInflater = LayoutInflater.from(context);
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mItemView = mInflater.inflate(R.layout.cardlist_item, viewGroup, false);

        return new CardViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int position) {
        String cardName = cardList.get(position).getCardName();
        cardViewHolder.mCount.setText(cardName);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public void flipCard(CardViewHolder cardViewHolder, int position){
        Glide.with(cardViewHolder.iv_card).load(cardList.get(position).getCardResource());
    }
}
