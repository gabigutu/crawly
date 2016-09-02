package com.crawler.akka.messages;

/**
 * Created by dorinela on 07.04.2016.
 */
public class CheckListResponseMessage {

    private double percent;
    private String listName;
    private String review;
    private Integer rate;

    public CheckListResponseMessage(double percent, String listName, String review, Integer rate) {
        this.percent = percent;
        this.listName = listName;
        this.review = review;
        this.rate = rate;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
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

    @Override
    public String toString() {
        return "CheckListResponseMessage{" +
                "percent=" + percent +
                ", listName='" + listName + '\'' +
                ", review='" + review + '\'' +
                ", rate=" + rate +
                '}';
    }
}
