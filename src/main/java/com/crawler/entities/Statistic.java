package com.crawler.entities;

/**
 * Created by Dorinela on 1/3/2016.
 */
public class Statistic {

    private String game;
    private String reviewer;
    private int rating;
    private double scoreStanford;
    private double scoreStanfordLength;
    private double veryNegative;
    private double negative;
    private  double neutral;
    private double positive;
    private double veryPositive;

    public Statistic(String game, String reviewer, int rating, double scoreStanford, double scoreStanfordLength, double veryNegative, double negative, double neutral, double positive, double veryPositive) {
        this.game = game;
        this.reviewer = reviewer;
        this.rating = rating;
        this.scoreStanford = scoreStanford;
        this.scoreStanfordLength = scoreStanfordLength;
        this.veryNegative = veryNegative;
        this.negative = negative;
        this.neutral = neutral;
        this.positive = positive;
        this.veryPositive = veryPositive;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getScoreStanford() {
        return scoreStanford;
    }

    public void setScoreStanford(double scoreStanford) {
        this.scoreStanford = scoreStanford;
    }

    public double getScoreStanfordLength() {
        return scoreStanfordLength;
    }

    public void setScoreStanfordLength(double scoreStanfordLength) {
        this.scoreStanfordLength = scoreStanfordLength;
    }

    public double getVeryNegative() {
        return veryNegative;
    }

    public void setVeryNegative(double veryNegative) {
        this.veryNegative = veryNegative;
    }

    public double getNegative() {
        return negative;
    }

    public void setNegative(double negative) {
        this.negative = negative;
    }

    public double getNeutral() {
        return neutral;
    }

    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    public double getPositive() {
        return positive;
    }

    public void setPositive(double positive) {
        this.positive = positive;
    }

    public double getVeryPositive() {
        return veryPositive;
    }

    public void setVeryPositive(double veryPositive) {
        this.veryPositive = veryPositive;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "game='" + game + '\'' +
                ", reviewer='" + reviewer + '\'' +
                ", rating=" + rating +
                ", scoreStanford=" + scoreStanford +
                ", scoreStanfordLength=" + scoreStanfordLength +
                ", veryNegative=" + veryNegative +
                ", negative=" + negative +
                ", neutral=" + neutral +
                ", positive=" + positive +
                ", veryPositive=" + veryPositive +
                '}';
    }
}
