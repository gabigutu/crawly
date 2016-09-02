package com.crawler.akka.messages;

/**
 * Created by dorinela on 07.04.2016.
 */
public class ReviewMessage {

    private String review;
    private Integer counter;
    private Integer rate;

    public ReviewMessage(String review, Integer counter, Integer rate) {
        this.review = review;
        this.counter = counter;
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ReviewMessage{" +
                "review='" + review + '\'' +
                ", counter=" + counter +
                ", rate=" + rate +
                '}';
    }
}
