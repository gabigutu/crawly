package com.crawler.akka;

import akka.actor.UntypedActor;
import com.crawler.App;
import com.crawler.akka.messages.CheckListResponseMessage;
import com.crawler.akka.messages.ReviewMessage;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by dorinela on 07.04.2016.
 */
public class CheckListActor extends UntypedActor{

    List<String> list;
    String listName;

    public CheckListActor(String listName, List<String> list) {
        this.listName = listName;
        this.list = list;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof ReviewMessage) { // this is a review
            ReviewMessage reviewMessage = (ReviewMessage)o;
            double checkResult = checkWordsFromList(list, reviewMessage.getReview(), reviewMessage.getCounter());
            CheckListResponseMessage responseMessage = new CheckListResponseMessage(checkResult, listName, reviewMessage.getReview(), reviewMessage.getRate());
            App.appActorSystem.statisticsActor.tell(responseMessage, self());
        }
    }

    private double checkWordsFromList(List<String> list, String review, int nonStopWordsFromReview) {

        //System.out.println("Review-ul are " + nonStopWordsFromReview + " cuvinte");
        int contor = 0;
        for (String l : list) {
            if (l.endsWith("*")) {
                l = l.substring(0,l.length()-1);
                boolean contains = review.matches(".*\\b" + l + ".*");
                if (contains) {
                    contor ++;
                }
            } else {
                boolean contains = review.matches(".*\\b" + l + "\\b.*");
                if (contains) {
                    contor ++;
                }
            }
        }
        double result = 0;
        //System.out.println("In review se gasesc " + contor + " cuvinte din lista");
        result = (double)(100*contor)/nonStopWordsFromReview;

        DecimalFormat newFormat = new DecimalFormat("#.###");
        double threeDecimal =  Double.valueOf(newFormat.format(result));

        return threeDecimal;
    }

}
