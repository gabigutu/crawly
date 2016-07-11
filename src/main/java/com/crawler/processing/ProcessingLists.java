package com.crawler.processing;


import com.crawler.App;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dorinela on 4/7/2016.
 */
public class ProcessingLists {

    public int numberOfOccurrences(String word, String review) {
        int occurenceNumber = 0;
        Pattern p = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(review);
        while(m.find()) {
            occurenceNumber ++;
        }
        return occurenceNumber;
    }

    public int numberOfOccurrencesWithStar(String word, String review) {
        int occurenceNumber = 0;
        Pattern p = Pattern.compile("\\b" + word,  Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(review);
        while(m.find()) {
            occurenceNumber ++;
        }
        return occurenceNumber;
    }

    /**
     *
     * @param list - list of words with valences
     * @param lemmas - lemmas - map containing as keys the content words and as values the nr of appearances of that words
     * @return - percentage of the list occurring inside the review
     */
    public double percentageCalculationWithValence(Map<String, Double> list, Map<String, Integer> lemmas) {

        double total = 0;
        double sumList = 0;

        for(Map.Entry<String, Integer> e : lemmas.entrySet()) {

            if (list.containsKey(e.getKey())) {
                sumList += e.getValue() * list.get(e.getKey());
            }

            total += e.getValue();
        }

        DecimalFormat newFormat = new DecimalFormat("#.###");
        return Double.valueOf(newFormat.format(sumList / total));

    }

    /**
     *
     * @param list - list of words with valences
     * @param lemmas - map containing as keys the content words and as values the nr of appearances of that words
     * @return - percentage of the list occurring inside the review
     */
    public double normalizedPercentageCalculationWithValence(Map<String, Double> list, Map<String, Integer> lemmas) {

        double total = 0;
        double sumList = 0;

        for(Map.Entry<String, Integer> e : lemmas.entrySet()) {

            if (list.containsKey(e.getKey())) {
                sumList += (1 + Math.log(e.getValue())) * list.get(e.getKey());
            }
            total += (1 + Math.log( e.getValue()));
        }

        DecimalFormat newFormat = new DecimalFormat("#.###");
        return Double.valueOf(newFormat.format(sumList / total));

    }

    /**
     *
     * @param list - list of words
     * @param lemmas - lemmas - map containing as keys the content words and as values the nr of appearances of that words
     * @return - percentage of the list occurring inside the review
     */
    public double percentageCalculation(Set<String> list, Map<String, Integer> lemmas) {

        double total = 0;
        double sumList = 0;

        for(Map.Entry<String, Integer> e : lemmas.entrySet()) {

            if (list.contains(e.getKey())) {
                sumList += e.getValue();
            }
            total += e.getValue();
        }

        DecimalFormat newFormat = new DecimalFormat("#.###");
        return Double.valueOf(newFormat.format(sumList / total));
    }

    /**
     *
     * @param list - list of words
     * @param lemmas - lemmas - map containing as keys the content words and as values the nr of appearances of that words
     * @return - percentage of the list occurring inside the review
     */
    public double normalizedPercentageCalculation(Set<String> list, Map<String, Integer> lemmas) {

        double total = 0;
        double sumList = 0;

        for(Map.Entry<String, Integer> e : lemmas.entrySet()) {

            if (list.contains(e.getKey())) {
                sumList += (1 + Math.log(e.getValue()));
            }
            total += (1 + Math.log(e.getValue()));
        }

        DecimalFormat newFormat = new DecimalFormat("#.###");
        return Double.valueOf(newFormat.format(sumList / total));

    }

    public Map<String, Integer> checkWords(StanfordCoreNLP pipeline, String text) {
        Map<String, Integer> resultMAP = new HashMap<>();
        String[] split = text.split("\\.");
        if (split.length > 0) {
            Annotation document = new Annotation(text);
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            for(CoreMap sentence: sentences) {
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String word = token.get(CoreAnnotations.TextAnnotation.class).toLowerCase();
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    if (pos.startsWith("NN") || pos.startsWith("VB") || pos.startsWith("JJ") ||pos.startsWith("RB")) {
                        if (!App.STOPWORDS.contains(word) && App.DICTIONARY.contains(word)) {
                            String lemma = Morphology.lemmaStatic(word, pos, true);
                            if (!resultMAP.containsKey(lemma)) {
                                resultMAP.put(lemma, 0);
                            }
                            resultMAP.put(lemma, resultMAP.get(lemma) + 1);
                        }
                    }
                }
            }
        }
        return resultMAP;
    }

    public static void main(String[] args) {
        ProcessingLists processingLists = new ProcessingLists();
        String word = "loves";
        String review = "My daughter Loves this game. It covers each of the original movies loves as well as the prequels. I admit that the game is better than the prequel movies! " +
                "I'm not a big fan of the loves later releases.";
        //System.out.println(processingLists.numberOfOccurrences(word, review));
        //System.out.println(processingLists.calculateProcent(App.NEGATIVE_WORDS, review, 20));

    }
}
