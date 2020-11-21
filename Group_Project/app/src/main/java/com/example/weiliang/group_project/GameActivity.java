package com.example.weiliang.group_project;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    public final static String KEY_HISTORY_DATE_TIME = "history_date_time";
    public final static String KEY_HISTORY_TOTAL_ROUND = "history_total_round";
    public final static String KEY_HISTORY_TOTAL_ROUND_WON = "history_total_round_won";
    public final static String KEY_HISTORY_CARDS_SETTING = "history_cards_setting";

    private String currentDateTime;

    private static final int HORIZONTAL_ITEM_SPACE = 20;

    private int numberCardsSetting;
    private boolean recordHistory;

    private Button btn_gamePlay;
    private Button btn_firstQuitGame;

    private Button btn_hit;
    private Button btn_stand;

    private Button btn_gamePlayAgain;
    private Button btn_quitGame;

    private TextView tv_totalRound;
    private TextView tv_totalRoundWon;
    private TextView tv_currentScore;
    private int totalRound;
    private int totalRoundWon;

    private TextView tv_gameResult;

    private int playerCurrentScore;
    private int dealerCurrentScore;

    private LinearLayoutManager llmDealer;
    private LinearLayoutManager llmPlayer;
    private RecyclerView rvDealer;
    private RecyclerView rvPlayer;

    private ArrayList<PokerCard> cardPackList;
    private ArrayList<PokerCard> dealerCardsList;
    private ArrayList<PokerCard> playerCardsList;
    private ArrayList<Integer> usedCardsInt;

    private int dealerHitLocation;
    private int dealerHitLocationTemp;
    private int playerHitLocation;

    private CardListAdapter mDealerAdapter;
    private CardListAdapter mPlayerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        numberCardsSetting = intent.getIntExtra(MainMenuActivity.KEY_CARDS_SETTINGS, 0);
        recordHistory = intent.getBooleanExtra(MainMenuActivity.KEY_RECORD_HISTORY, false);

        currentDateTime = "";

        btn_gamePlay = findViewById(R.id.btn_gamePlay);
        btn_firstQuitGame = findViewById(R.id.btn_firstQuitGame);

        btn_hit = findViewById(R.id.btn_hit);
        btn_stand = findViewById(R.id.btn_stand);

        btn_gamePlayAgain = findViewById(R.id.btn_gamePlayAgain);
        btn_quitGame = findViewById(R.id.btn_quitGame);

        tv_totalRound = findViewById(R.id.tv_totalRound);
        tv_totalRoundWon = findViewById(R.id.tv_totalRoundWon);
        tv_currentScore = findViewById(R.id.tv_currentScore);
        totalRound = 0;
        totalRoundWon = 0;
        playerCurrentScore = 0;
        dealerCurrentScore = 0;

        tv_gameResult = findViewById(R.id.tv_gameResult);

        rvDealer = findViewById(R.id.rvDealerCards);
        rvPlayer = findViewById(R.id.rvPlayerCards);

        dealerHitLocation = 0;
        dealerHitLocationTemp = 0;
        playerHitLocation = 0;

        cardPackList = new ArrayList<>();
        dealerCardsList = new ArrayList<>();
        playerCardsList = new ArrayList<>();
        usedCardsInt = new ArrayList<>();
        createCardsList();

        hiddenItem();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btnGamePlay_clicked(View view) {

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

        btn_gamePlay.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn_gamePlay.setVisibility(View.GONE);
                firstFlipCards();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        btn_firstQuitGame.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn_firstQuitGame.setVisibility(View.GONE);
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

        if(recordHistory == true){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss a");
            LocalDateTime now = LocalDateTime.now();
            currentDateTime = dateTimeFormatter.format(now);
        }
    }

    public void btnFirstQuitGame_clicked(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void btnHit_clicked(View view) {
        flipPlayerCard();
    }

    public void btnStand_clicked(View view) {
        Log.e(null, "total view: " + String.valueOf(rvPlayer.getAdapter().getItemCount()));

        Log.e(null, "last visible position: " + String.valueOf(llmPlayer.findLastVisibleItemPosition()));

        if(playerCurrentScore < 15){
            Toast.makeText(GameActivity.this, "Score below 15 cannot stand.", Toast.LENGTH_SHORT).show();
        } else {
            btn_hit.setEnabled(false);
            btn_stand.setEnabled(false);
            dealerAction();
            decideWinLose();
        }
        //rvPlayer.smoothScrollToPosition(rvPlayer.getAdapter().getItemCount() - 1);
    }

    public void btnGamePlayAgain_clicked(View view) {
        totalRound++;
        dealerCurrentScore = 0;
        playerCurrentScore = 0;
        updateTextView();

        dealerHitLocation = 0;
        playerHitLocation = 0;

        btn_gamePlayAgain.animate().alpha(0).translationY(15).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn_gamePlayAgain.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        btn_quitGame.animate().alpha(0).translationY(15).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn_quitGame.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        randomAllCards();

        mDealerAdapter.setValues(dealerCardsList);
        mDealerAdapter.notifyDataSetChanged();
        mDealerAdapter.setUpdateImage(true);
        mDealerAdapter.notifyDataSetChanged();
        scrollPositionAndDirection(llmDealer, rvDealer, (numberCardsSetting - 1), true);

        mPlayerAdapter.setValues(playerCardsList);
        mPlayerAdapter.notifyDataSetChanged();
        mPlayerAdapter.setUpdateImage(true);
        mPlayerAdapter.notifyDataSetChanged();
        scrollPositionAndDirection(llmPlayer, rvPlayer, (numberCardsSetting - 1), true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollPositionAndDirection(llmDealer, rvDealer, 0, false);
                scrollPositionAndDirection(llmPlayer, rvPlayer, 0, false);

                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDealerAdapter.setUpdateImage(false);
                        mPlayerAdapter.setUpdateImage(false);
                        btn_hit.setEnabled(true);
                        btn_stand.setEnabled(true);
                        firstFlipCards();
                    }
                }, 500);
            }
        }, 500);
    }

    public void btnQuitGame_clicked(View view) {
        if(recordHistory == true){
            Intent intent = new Intent();
            intent.putExtra(KEY_HISTORY_DATE_TIME, currentDateTime);
            intent.putExtra(KEY_HISTORY_TOTAL_ROUND, totalRound);
            intent.putExtra(KEY_HISTORY_TOTAL_ROUND_WON, totalRoundWon);
            intent.putExtra(KEY_HISTORY_CARDS_SETTING, numberCardsSetting);

            setResult(RESULT_OK, intent);
            finish();

        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public void firstFlipCards(){

        ImageView dealerFirstCard = rvDealer.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.ivCard);
        ImageView dealerSecondCard = rvDealer.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.ivCard);

        ImageView playerFirstCard = rvPlayer.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.ivCard);
        ImageView playerSecondCard = rvPlayer.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.ivCard);

        dealerFirstCard.setImageResource(dealerCardsList.get(0).getCardResource());
        dealerSecondCard.setImageResource(dealerCardsList.get(1).getCardResource());

        playerFirstCard.setImageResource(playerCardsList.get(0).getCardResource());
        playerSecondCard.setImageResource(playerCardsList.get(1).getCardResource());

        dealerCurrentScore = dealerCurrentScore + decideScore(dealerCardsList.get(0).getCardName(), dealerCardsList.get(0).getScore(), dealerCurrentScore)
                + decideScore(dealerCardsList.get(1).getCardName(), dealerCardsList.get(1).getScore(), dealerCurrentScore);
        playerCurrentScore = playerCurrentScore + decideScore(playerCardsList.get(0).getCardName(), playerCardsList.get(0).getScore(), playerCurrentScore)
                + decideScore(playerCardsList.get(1).getCardName(), playerCardsList.get(1).getScore(), playerCurrentScore);
        updateTextView();

        if(playerCurrentScore == 21){
            //player win
            btn_hit.setEnabled(false);
            btn_stand.setEnabled(false);
            displayResult(true);
        } else {
            if(dealerCurrentScore == 21){
                //dealer win
                btn_hit.setEnabled(false);
                btn_stand.setEnabled(false);
                displayResult(false);
            }
        }

        dealerHitLocation += 2;
        playerHitLocation += 2;
    }

    public void flipPlayerCard(){
        Log.e(null, "Player hit location: " + String.valueOf(playerHitLocation));
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

                    playerCurrentScore = playerCurrentScore + decideScore(playerCardsList.get(playerHitLocation).getCardName(), playerCardsList.get(playerHitLocation).getScore(), playerCurrentScore);
                    Log.e(null, "Player Hit score: " + String.valueOf(playerCardsList.get(playerHitLocation).getScore()));
                    Log.e(null, "Current score: " + String.valueOf(playerCurrentScore));
                    updateTextView();
                    playerHitLocation++;
                }
            }, 300);
        } else {
            Toast.makeText(GameActivity.this, "Maximum cards reach", Toast.LENGTH_SHORT).show();
        }
    }

    public void flipDealerCard(){
        dealerHitLocationTemp = 2;
        for(int i = 2; i < dealerHitLocation; i++){
            if(i < llmDealer.findFirstCompletelyVisibleItemPosition()){
                scrollPositionAndDirection(llmDealer, rvDealer, i, false);
            } else {
                scrollPositionAndDirection(llmDealer, rvDealer, i, true);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ImageView iv_card = rvDealer.findViewHolderForAdapterPosition(dealerHitLocationTemp).itemView.findViewById(R.id.ivCard);

                    iv_card.setImageResource(dealerCardsList.get(dealerHitLocationTemp).getCardResource());
                    dealerHitLocationTemp++;
                }
            }, 300);
        }
    }

    public int decideScore(String card_name, int cardScore, int totalScore){
        int score = 0;
        if(card_name.equals("cards_clubs_ace") || card_name.equals("cards_diamonds_ace") || card_name.equals("cards_hearts_ace") || card_name.equals("cards_spades_ace")){
            if(totalScore >= 10){
                score = 1;
            } else {
                score = 11;
            }
        } else {
            score = cardScore;
        }

        return score;
    }

    public void dealerAction(){
        boolean status;
        do{
            if(dealerCurrentScore >= 15 || dealerHitLocation == numberCardsSetting){
                //dealer stand
                status = true;
            } else {
                //dealer hit
                dealerCurrentScore = dealerCurrentScore + decideScore(dealerCardsList.get(dealerHitLocation).getCardName()
                        , dealerCardsList.get(dealerHitLocation).getScore(), dealerCurrentScore);

                dealerHitLocation++;
                status = false;
            }
            Log.e(null, "Dealer score: " + String.valueOf(dealerCurrentScore));
        }while(!status);
        return;
    }

    public void decideWinLose(){
        if(playerCurrentScore < 17 && dealerCurrentScore < 17){
            if(playerCurrentScore >= dealerCurrentScore){
                //player win
                displayResult(true);
                flipDealerCard();
            } else {
                //dealer win
                displayResult(false);
                flipDealerCard();
            }
        } else if(playerCurrentScore > 21 && dealerCurrentScore > 21){
            if(playerCurrentScore > dealerCurrentScore){
                //dealer win
                displayResult(false);
                flipDealerCard();
            } else {
                //player win
                displayResult(true);
                flipDealerCard();
            }
        } else {
            if(playerCurrentScore > 21 || dealerCurrentScore > 21){
                if(playerCurrentScore > 21){
                    //dealer win
                    displayResult(false);
                    flipDealerCard();
                } else {
                    //player win
                    displayResult(true);
                    flipDealerCard();
                }
            } else if(playerCurrentScore < 17 || dealerCurrentScore < 17){
                if(playerCurrentScore < 17){
                    //player win
                    displayResult(true);
                    flipDealerCard();
                } else {
                    //dealer win
                    displayResult(false);
                    flipDealerCard();
                }
            } else if((playerCurrentScore >= 17 && playerCurrentScore <= 21) && (dealerCurrentScore >=17 && dealerCurrentScore <= 21)) {
                if(playerCurrentScore > dealerCurrentScore){
                    //player win
                    displayResult(true);
                    flipDealerCard();
                } else if(playerCurrentScore == dealerCurrentScore) {
                    //player win
                    displayResult(true);
                    flipDealerCard();
                } else {
                    //dealer win
                    displayResult(false);
                    flipDealerCard();
                }
            }
        }
    }

    public void displayResult(boolean playerWin){
        if(playerWin){
            tv_gameResult.setText("You Win");
            totalRoundWon++;
            updateTextView();
        } else {
            tv_gameResult.setText("You Lose");
        }

        tv_gameResult.setAlpha(0f);
        tv_gameResult.setVisibility(View.VISIBLE);

        tv_gameResult.animate().alpha(1f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_gameResult.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                tv_gameResult.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                    }
                }, 1000);

                btn_gamePlayAgain.setTranslationY(15);
                btn_gamePlayAgain.setAlpha(0f);
                btn_gamePlayAgain.setVisibility(View.VISIBLE);
                btn_gamePlayAgain.animate().alpha(1f).translationY(0).setListener(null);

                btn_quitGame.setTranslationY(15);
                btn_quitGame.setAlpha(0f);
                btn_quitGame.setVisibility(View.VISIBLE);
                btn_quitGame.animate().alpha(1f).translationY(0).setListener(null);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
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

        String[] cards_score = getResources().getStringArray(R.array.cards_score);
        //TypedArray cards_score = getResources().obtainTypedArray(R.array.cards_score);

        cardPackList.clear();

        cardPackList.add(new PokerCard("cards_cover", 0, R.drawable.cards_cover));

        for(int i = 0; i < cards_clubs_name.length; i++){
            cardPackList.add(new PokerCard(cards_clubs_name[i], Integer.parseInt(cards_score[i]), cards_clubs_image.getResourceId(i, 0)));
        }

        for(int i = 0; i < cards_diamonds_name.length; i++){
            cardPackList.add(new PokerCard(cards_diamonds_name[i], Integer.parseInt(cards_score[i]), cards_diamonds_image.getResourceId(i, 0)));
        }

        for (int i = 0; i < cards_hearts_name.length; i++){
            cardPackList.add(new PokerCard(cards_hearts_name[i], Integer.parseInt(cards_score[i]), cards_hearts_image.getResourceId(i, 0)));
        }

        for (int i = 0; i < cards_spades_name.length; i++){
            cardPackList.add(new PokerCard(cards_spades_name[i], Integer.parseInt(cards_score[i]), cards_spades_image.getResourceId(i, 0)));
        }

        Log.e(null, "cards Pack size: " + String.valueOf(cardPackList.size()));
    }

    public void randomAllCards(){
        dealerCardsList.clear();
        playerCardsList.clear();
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
            //Log.e(null, " card score: " + cardPack.get(i).getScore());
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
        btn_gamePlayAgain.setVisibility(View.GONE);
        btn_quitGame.setVisibility(View.GONE);

        rvDealer.setVisibility(View.GONE);
        rvPlayer.setVisibility(View.GONE);
        btn_hit.setVisibility(View.GONE);
        btn_stand.setVisibility(View.GONE);

        tv_totalRound.setVisibility(View.GONE);
        tv_totalRoundWon.setVisibility(View.GONE);
        tv_currentScore.setVisibility(View.GONE);

        tv_gameResult.setVisibility(View.GONE);
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
        tv_currentScore.setText("Score: " + playerCurrentScore + " Point/s");
    }
}
