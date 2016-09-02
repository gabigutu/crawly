package com.crawler.akka.messages;

import java.util.Map;

/**
 * Created by dorinela on 14.04.2016.
 */
public class FinalMessage {

    private InitialMessage initialMessage;
    private Map<String, Double> scores;

    public FinalMessage(InitialMessage initialMessage, Map<String, Double> scores) {
        this.initialMessage = initialMessage;
        this.scores = scores;
    }

    public InitialMessage getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(InitialMessage initialMessage) {
        this.initialMessage = initialMessage;
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public void setScores(Map<String, Double> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "FinalMessage{" +
                "initialMessage=" + initialMessage +
                ", scores=" + scores +
                '}';
    }
}
