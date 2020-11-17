package com.example.weiliang.group_project;

public class PokerCard {

    private String cardName;
    private int score;
    private final int cardResource;

    public PokerCard(String cardName, int score, int cardResource){
        this.cardName = cardName;
        this.score = score;
        this.cardResource = cardResource;
    }

    public String getCardName() {
        return cardName;
    }

    public int getScore() {
        return score;
    }

    public int getCardResource() {
        return cardResource;
    }

}
