package com.crawler.akka.messages;

/**
 * Created by Dorinela on 4/7/2016.
 */
public class InitialMessage {

    private String game;
    private String review;
    private Integer rate;
    private String reviewer;
    private Integer row;

    public InitialMessage(String game, String review, Integer rate, String reviewer, Integer row) {
        this.game = game;
        this.review = review;
        this.rate = rate;
        this.reviewer = reviewer;
        this.row = row;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "InitialMessage{" +
                "game='" + game + '\'' +
                ", review='" + review + '\'' +
                ", rate=" + rate +
                ", reviewer='" + reviewer + '\'' +
                ", row=" + row +
                '}';
    }
}

