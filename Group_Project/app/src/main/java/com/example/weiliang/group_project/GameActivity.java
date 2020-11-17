package com.example.weiliang.group_project;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private static final int HORIZONTAL_ITEM_SPACE = 20;

    private int numberCardsSetting;

    private Button btn_gamePlay;
    private Button btn_hit;
    private Button btn_stand;

    private TextView tv_totalRound;
    private TextView tv_totalRoundWon;
    private TextView tv_currentScore;
    private int totalRound;
    private int totalRoundWon;
    private int currentScore;

    private LinearLayoutManager llmDealer;
    private LinearLayoutManager llmPlayer;
    private RecyclerView rvDealer;
    private RecyclerView rvPlayer;

    private ArrayList<PokerCard> cardPackList;
    private ArrayList<PokerCard> dealerCardsList;
    private ArrayList<PokerCard> playerCardsList;
    private ArrayList<Integer> usedCardsInt;

    private int playerHitLocation;

    private CardListAdapter mDealerAdapter;
    private CardListAdapter mPlayerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Intent intent = getIntent();
        numberCardsSetting = intent.getIntExtra(MainMenuActivity.KEY_CARDS_SETTINGS, 0);
        boolean recordHistory = intent.getBooleanExtra(MainMenuActivity.KEY_RECORD_HISTORY, false);

        btn_gamePlay = findViewById(R.id.btn_gamePlay);
        btn_hit = findViewById(R.id.btn_hit);
        btn_stand = findViewById(R.id.btn_stand);

        tv_totalRound = findViewById(R.id.tv_totalRound);
        tv_totalRoundWon = findViewById(R.id.tv_totalRoundWon);
        tv_currentScore = findViewById(R.id.tv_currentScore);
        totalRound = 0;
        totalRoundWon = 0;
        currentScore = 0;

        rvDealer = findViewById(R.id.rvDealerCards);
        rvPlayer = findViewById(R.id.rvPlayerCards);

        playerHitLocation = 0;

        cardPackList = new ArrayList<>();
        dealerCardsList = new ArrayList<>();
        playerCardsList = new ArrayList<>();
        usedCardsInt = new ArrayList<>();
        createCardsList();

        randomAllCards();

        llmDealer = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvDealer.setLayoutManager(llmDealer);
        mDealerAdapter = new CardListAdapter(this, dealerCardsList);
        rvDealer.addItemDecoration(new HorizontalSpaceItemDecoration(HORIZONTAL_ITEM_SPACE));
        rvDealer.setAdapter(mDealerAdapter);

        llmPlayer = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvPlayer.setLayoutManager(llmPlayer);
        mPlayerAdapter = new CardListAdapter(this, playerCardsList);
        rvPlayer.addItemDecoration(new HorizontalSpaceItemDecoration(HORIZONTAL_ITEM_SPACE));
        rvPlayer.setAdapter(mPlayerAdapter);

        hiddenItem();


    }

    public void btnGamePlay_clicked(View view) {

        btn_gamePlay.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn_gamePlay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        totalRound++;
        updateTextView();
        showItem();
    }

    public void btnHit_clicked(View view) {
        Log.e(null, String.valueOf(playerHitLocation));
        if(playerHitLocation < numberCardsSetting){

            if(playerHitLocation < llmPlayer.findFirstCompletelyVisibleItemPosition()){
                scrollPositionAndDirection(llmPlayer, rvPlayer, playerHitLocation, false);
            } else {
                scrollPositionAndDirection(llmPlayer, rvPlayer, playerHitLocation, true);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ImageView iv_card = rvPlayer.findViewHolderForAdapterPosition(playerHitLocation).itemView.findViewById(R.id.ivCard);

                    iv_card.setImageResource(playerCardsList.get(playerHitLocation).getCardResource());
                    rvPlayer.getAdapter().notifyDataSetChanged();
                    playerHitLocation++;
                }
            }, 300);
        } else {
            Toast.makeText(GameActivity.this, "Maximum cards reach", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnStand_clicked(View view) {
        Log.e(null, "total view: " + String.valueOf(rvPlayer.getAdapter().getItemCount()));

        Log.e(null, "last visible position: " + String.valueOf(llmPlayer.findLastVisibleItemPosition()));


        //rvPlayer.smoothScrollToPosition(rvPlayer.getAdapter().getItemCount() - 1);
    }

    public void createCardsList(){
        String[] cards_clubs_name = getResources().getStringArray(R.array.cards_clubs_name);
        String[] cards_diamonds_name = getResources().getStringArray(R.array.cards_diamonds_name);
        String[] cards_hearts_name = getResources().getStringArray(R.array.cards_hearts_name);
        String[] cards_spades_name = getResources().getStringArray(R.array.cards_spades_name);

        TypedArray cards_clubs_image = getResources().obtainTypedArray(R.array.cards_clubs_image);
        TypedArray cards_diamonds_image = getResources().obtainTypedArray(R.array.cards_diamonds_image);
        TypedArray cards_hearts_image = getResources().obtainTypedArray(R.array.cards_hearts_image);
        TypedArray cards_spades_image = getResources().obtainTypedArray(R.array.cards_spades_image);

        TypedArray cards_score = getResources().obtainTypedArray(R.array.cards_score);

        cardPackList.clear();

        cardPackList.add(new PokerCard("cards_cover", 0, R.drawable.cards_cover));

        for(int i = 0; i < cards_clubs_name.length; i++){
            cardPackList.add(new PokerCard(cards_clubs_name[i], cards_score.getResourceId(i, 0), cards_clubs_image.getResourceId(i, 0)));
        }

        for(int i = 0; i < cards_diamonds_name.length; i++){
            cardPackList.add(new PokerCard(cards_diamonds_name[i], cards_score.getResourceId(i, 0), cards_diamonds_image.getResourceId(i, 0)));
        }

        for (int i = 0; i < cards_hearts_name.length; i++){
            cardPackList.add(new PokerCard(cards_hearts_name[i], cards_score.getResourceId(i, 0), cards_hearts_image.getResourceId(i, 0)));
        }

        for (int i = 0; i < cards_spades_name.length; i++){
            cardPackList.add(new PokerCard(cards_spades_name[i], cards_score.getResourceId(i, 0), cards_spades_image.getResourceId(i, 0)));
        }

        Log.e(null, "cards Pack size: " + String.valueOf(cardPackList.size()));
    }

    public void randomAllCards(){
        usedCardsInt.clear();

        dealerCardsList = randomCards();
        playerCardsList = randomCards();
    }

    public ArrayList<PokerCard> randomCards(){
        int card = 0;
        ArrayList<PokerCard> cardPack = new ArrayList<>();

        for(int i = 0; i < numberCardsSetting; i++){
            card = (int) (Math.random() * 52 - 1 + 1) + 1;

            if(usedCardsInt.isEmpty()){
                cardPack.add(cardPackList.get(card));
                usedCardsInt.add(card);
            } else {
                loop:{
                    for(int j = 0; j < usedCardsInt.size(); j++){
                        if(card == usedCardsInt.get(j)){
                            i--;
                            break loop;
                        }
                    }

                    cardPack.add(cardPackList.get(card));
                    usedCardsInt.add(card);
                }
            }
            Log.e(null, "generate card no: " + String.valueOf(card));
        }

        return cardPack;
    }

    //if direction is true, which mean scroll to right, otherwise scroll to left
    public void scrollPositionAndDirection(LinearLayoutManager llm, RecyclerView rv, int position, boolean direction){
        if(direction){
            int lastItemVisible = llm.findLastCompletelyVisibleItemPosition();

            if(position > lastItemVisible){
                rv.smoothScrollToPosition(position);
            }
            return;
        } else {
            int firstItemVisible = llm.findFirstCompletelyVisibleItemPosition();

            if(position < firstItemVisible){
                rv.smoothScrollToPosition(position);
            }
            return;
        }
    }

    public void hiddenItem(){
        rvDealer.setVisibility(View.GONE);
        rvPlayer.setVisibility(View.GONE);
        btn_hit.setVisibility(View.GONE);
        btn_stand.setVisibility(View.GONE);

        tv_totalRound.setVisibility(View.GONE);
        tv_totalRoundWon.setVisibility(View.GONE);
        tv_currentScore.setVisibility(View.GONE);
    }

    public void showItem(){
        rvDealer.setAlpha(0f);
        rvDealer.setVisibility(View.VISIBLE);
        rvDealer.setTranslationY(-15);
        rvDealer.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);

        rvPlayer.setAlpha(0f);
        rvPlayer.setVisibility(View.VISIBLE);
        rvPlayer.setTranslationY(15);
        rvPlayer.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);

        btn_hit.setAlpha(0f);
        btn_hit.setVisibility(View.VISIBLE);
        btn_hit.setTranslationY(15);
        btn_hit.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);

        btn_stand.setAlpha(0f);
        btn_stand.setVisibility(View.VISIBLE);
        btn_stand.setTranslationY(15);
        btn_stand.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);

        tv_totalRound.setAlpha(0f);
        tv_totalRound.setVisibility(View.VISIBLE);
        tv_totalRound.setTranslationY(15);
        tv_totalRound.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);

        tv_totalRoundWon.setAlpha(0f);
        tv_totalRoundWon.setVisibility(View.VISIBLE);
        tv_totalRoundWon.setTranslationY(15);
        tv_totalRoundWon.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);

        tv_currentScore.setAlpha(0f);
        tv_currentScore.setVisibility(View.VISIBLE);
        tv_currentScore.setTranslationY(15);
        tv_currentScore.animate().alpha(1f).setDuration(300).translationY(0).setListener(null);
    }

    public void updateTextView(){
        tv_totalRound.setText("Total Round: " + totalRound);
        tv_totalRoundWon.setText("Total Round Won: " + totalRoundWon);
        tv_currentScore.setText("Current Score: " + currentScore + " Point/s");
    }
}
