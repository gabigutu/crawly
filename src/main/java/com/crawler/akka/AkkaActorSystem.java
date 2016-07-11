package com.crawler.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.crawler.Constants;
import com.crawler.processing.ReadData;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dorinela on 07.04.2016.
 */
public class AkkaActorSystem {

    public static ActorSystem actorSystem;

    public ActorRef statisticsActor;
    public ActorRef processReviewActor;
    //public List<ActorRef> checkListActors = new ArrayList<ActorRef>();
    public List<ActorRef> processReviewActors = new ArrayList<ActorRef>();

    //ReadData readData = new ReadData();

    //List<String> negativeWordsResult = readData.readDataFromFile(Constants.NEGATIVE_WORDS);
    //List<String> positiveWordsResult = readData.readDataFromFile(Constants.POSITIVE_WORDS);


    public void init() {
        System.out.println("Akka actor system create");
        actorSystem = ActorSystem.create("standfordProcessingActorSystem", ConfigFactory.load("akka.conf"));

        System.out.println("Init actors");
        statisticsActor = this.actorSystem.actorOf(Props.create(StatisticsActor.class), "statistics-actor");

        processReviewActor = this.actorSystem.actorOf(Props.create(ProcessReviewActor.class).withDispatcher("akka.actor.review-dispatcher"), "process-review-actor");
        /*for(int i = 0; i < Constants.REVIEWS; i++) {
            ActorRef processReviewActor = this.actorSystem.actorOf(Props.create(ProcessReviewActor.class).withDispatcher("akka.actor.review-dispatcher"), "process-review-actor" + i);
            processReviewActors.add(processReviewActor);
        }*/
    }

    public static void stop() {
        actorSystem.shutdown();
        actorSystem.awaitTermination();
        actorSystem = null;

        System.out.println("Akka actor system shutdown");
    }
}
