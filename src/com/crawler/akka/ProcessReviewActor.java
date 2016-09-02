package com.crawler.akka;

import akka.actor.UntypedActor;
import com.crawler.App;
import com.crawler.akka.messages.FinalMessage;
import com.crawler.akka.messages.InitialMessage;
import com.crawler.processing.ProcessingLists;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by dorinela on 07.04.2016.
 */
public class ProcessReviewActor extends UntypedActor{

    ProcessingLists processingLists = new ProcessingLists();

    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitialMessage) {
            //System.out.println("Receive message for process ....");
            InitialMessage initialMessage = (InitialMessage) o;
            //int numberOfNonStopWords = checkWords(App.STANFORD_CORE_NLP, initialMessage.getReview());
            ///System.out.println("Number of non stop words: " + numberOfNonStopWords);
            System.out.println("Start process review: " + initialMessage.getReview());
            Map<String, Integer> lemmas = processingLists.checkWords(App.STANFORD_CORE_NLP, initialMessage.getReview());

            if (lemmas != null && lemmas.size() > 0) {
                Map<String, Double> scores = new HashMap<>();
                //calculate and send to StatisticActor result

                //simple lists
                //System.out.println(App.NEGATIVE_WORDS);

                double procent = processingLists.percentageCalculation(App.NEGATIVE_WORDS, lemmas);
                scores.put("NEGATIVE_WORDS", procent);
                // System.out.println("Procent for negative_words: " + procent);

                procent = processingLists.percentageCalculation(App.POSITIVE_WORDS, lemmas);
                scores.put("POSITIVE_WORDS", procent);
                //System.out.println("Procent for positive_words: " + procent);

                for (String key : App.ALL_AFFECTIVE_LIST.keySet()) {
                    procent = processingLists.percentageCalculation(App.ALL_AFFECTIVE_LIST.get(key), lemmas);
                    scores.put(key, procent);
                }
                for (String key : App.ALL_INQUIRERBASIC.keySet()) {
                    procent = processingLists.percentageCalculation(App.ALL_INQUIRERBASIC.get(key), lemmas);
                    scores.put(key, procent);
                }
                //System.out.println("Finish for simple lists");

                //LIWC
                procent = processingLists.percentageCalculation(App.LIWC_AFFECT, lemmas);
                scores.put("LIWC_AFFECT", procent);
                procent = processingLists.percentageCalculation(App.LIWC_ANGER, lemmas);
                scores.put("LIWC_ANGER", procent);
                procent = processingLists.percentageCalculation(App.LIWC_ANX, lemmas);
                scores.put("LIWC_ANX", procent);
                procent = processingLists.percentageCalculation(App.LIWC_ASSENT, lemmas);
                scores.put("LIWC_ASSENT", procent);
                procent = processingLists.percentageCalculation(App.LIWC_CAUSE, lemmas);
                scores.put("LIWC_CAUSE", procent);
                procent = processingLists.percentageCalculation(App.LIWC_DEATH, lemmas);
                scores.put("LIWC_DEATH", procent);
                procent = processingLists.percentageCalculation(App.LIWC_FEEL, lemmas);
                scores.put("LIWC_FEEL", procent);
                procent = processingLists.percentageCalculation(App.LIWC_HEAR, lemmas);
                scores.put("LIWC_HEAR", procent);
                procent = processingLists.percentageCalculation(App.LIWC_INHIB, lemmas);
                scores.put("LIWC_INHIB", procent);
                procent = processingLists.percentageCalculation(App.LIWC_LEISURE, lemmas);
                scores.put("LIWC_LEISURE", procent);
                procent = processingLists.percentageCalculation(App.LIWC_NEGEMO, lemmas);
                scores.put("LIWC_NEGEMO", procent);
                procent = processingLists.percentageCalculation(App.LIWC_PERCEPT, lemmas);
                scores.put("LIWC_PERCEPT", procent);
                procent = processingLists.percentageCalculation(App.LIWC_POSEMO, lemmas);
                scores.put("LIWC_POSEMO", procent);
                procent = processingLists.percentageCalculation(App.LIWC_SAD, lemmas);
                scores.put("LIWC_SAD", procent);
                procent = processingLists.percentageCalculation(App.LIWC_SEE, lemmas);
                scores.put("LIWC_SEE", procent);
                procent = processingLists.percentageCalculation(App.LIWC_SPACE, lemmas);
                scores.put("LIWC_SPACE", procent);
                procent = processingLists.percentageCalculation(App.LIWC_TIME, lemmas);
                scores.put("LIWC_TIME", procent);
                // System.out.println("Finish for LIWC");

                //affective norms
                procent = processingLists.percentageCalculationWithValence(App.AFFECTIVE_NORMS, lemmas);
                scores.put("AFFECTIVE_NORMS", procent);
                procent = processingLists.percentageCalculationWithValence(App.AFFECTIVE_NORMS_POSITIVE, lemmas);
                scores.put("AFFECTIVE_NORMS_POSITIVE", procent);
                procent = processingLists.percentageCalculationWithValence(App.AFFECTIVE_NORMS_NEGATIVE, lemmas);
                scores.put("AFFECTIVE_NORMS_NEGATIVE", procent);
                //System.out.println("Finish for affective norms");

                //SENTICNET
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_APITUDE, lemmas);
                scores.put("SENTICNET_APITUDE", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_APTITUDE_NEGATIVE, lemmas);
                scores.put("SENTICNET_APTITUDE_NEGATIVE", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_APTITUDE_POSITIVE, lemmas);
                scores.put("SENTICNET_APTITUDE_POSITIVE", procent);

                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_ATTENTION, lemmas);
                scores.put("SENTICNET_ATTENTION", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_ATTENTION_NEGATIVE, lemmas);
                scores.put("SENTICNET_ATTENTION_NEGATIVE", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_ATTENTION_POSITIVE, lemmas);
                scores.put("SENTICNET_ATTENTION_POSITIVE", procent);

                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_PLEASANTNESS, lemmas);
                scores.put("SENTICNET_PLEASANTNESS", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_PLEASANTNESS_NEGATIVE, lemmas);
                scores.put("SENTICNET_PLEASANTNESS_NEGATIVE", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_PLEASANTNESS_POSITIVE, lemmas);
                scores.put("SENTICNET_PLEASANTNESS_POSITIVE", procent);

                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_POLARITY, lemmas);
                scores.put("SENTICNET_POLARITY", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_POLARITY_NEGATIVE, lemmas);
                scores.put("SENTICNET_POLARITY_NEGATIVE", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_POLARITY_POSITIVE, lemmas);
                scores.put("SENTICNET_POLARITY_POSITIVE", procent);

                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_SENSITIVITY, lemmas);
                scores.put("SENTICNET_SENSITIVITY", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_SENSITIVITY_NEGATIVE, lemmas);
                scores.put("SENTICNET_SENSITIVITY_NEGATIVE", procent);
                procent = processingLists.percentageCalculationWithValence(App.SENTICNET_SENSITIVITY_POSITIVE, lemmas);
                scores.put("SENTICNET_SENSITIVITY_POSITIVE", procent);
                //System.out.println("Finish for senticnet");


                //compute final message
                FinalMessage finalMessage = new FinalMessage(initialMessage, scores);
                // System.out.println("Send message to StatisticActor ......");
                App.appActorSystem.statisticsActor.tell(finalMessage, self());
            } else {
                System.out.println("\n---------------------------------------------");
                System.out.println("Skip review: " + initialMessage.getReview());
                System.out.println("----------------------------------------------\n");
                App.REVIEWS_NUMBER --;
            }

        }
    }


}
