package com.crawler.entities;

/**
 * Created by Dorinela on 4/26/2016.
 */
public class Review {

    private String game;
    private Double rate;

    public Review(String game, Double rate) {
        this.game = game;
        this.rate = rate;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "game='" + game + '\'' +
                ", rate=" + rate +
                '}';
    }
}
