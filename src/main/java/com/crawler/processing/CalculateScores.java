package com.crawler.processing;

import com.crawler.App;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Dorinela on 11/14/2015.
 */
public class CalculateScores {

    private Properties properties = App.getProperties("stanford.properties");

    public int checkWords(StanfordCoreNLP pipeline, String text) {
        String posTags = properties.getProperty("pos.tags");
        String [] splitPosTags = posTags.split(",");
        int result = 0;
        ArrayList<String> contentWords = new ArrayList<>(Arrays.asList(splitPosTags));
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if (contentWords.contains(pos)) {
                    //System.out.println(token);
                    result ++;
                }
            }
        }
        return result;
    }

    public double getScoreStanfordLength(StanfordCoreNLP pipeline, String line) {
        Long textLength = 0L;
        int sumOfValues = 0;

        if (line != null && line.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);

                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    textLength += partText.length();
                    sumOfValues = sumOfValues + sentiment * partText.length();
                }
            }
        }

        double result = 0;
        if (textLength != 0) {
            result = (double)sumOfValues/textLength;
        }

        DecimalFormat newFormat = new DecimalFormat("#.###");
        double threeDecimal =  Double.valueOf(newFormat.format(result));

        return threeDecimal;
    }

    public double getScoreStanford( StanfordCoreNLP pipeline, String line){
        List<Integer> scores = new ArrayList<Integer>();
        int totalScore = 0;

        Annotation document = pipeline.process(line);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree annotatedTree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int score = RNNCoreAnnotations.getPredictedClass(annotatedTree);
            scores.add(score);
            totalScore += score;
        }
        double result = (double)totalScore/scores.size();

        DecimalFormat newFormat = new DecimalFormat("#.###");
        double threeDecimal =  Double.valueOf(newFormat.format(result));
        return threeDecimal;
    }

    public Map<String, Double> sentimentAnalysis( StanfordCoreNLP pipeline, String line) {
        Map<String, Double> finalResult = new HashMap<String, Double>();

        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("Very Negative", 0);
        result.put("Negative", 0);
        result.put("Neutral", 0);
        result.put("Positive", 0);
        result.put("Very Positive", 0);
        int sentenceNumber = 0;

        Annotation annotation = pipeline.process(line);
        String[] sentimentText = { "Very Negative","Negative", "Neutral", "Positive", "Very Positive"};
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int score = RNNCoreAnnotations.getPredictedClass(tree);
            result.put(sentimentText[score], result.get(sentimentText[score]) + 1);
            sentenceNumber ++;
        }

        for (Map.Entry<String,Integer> entry : result.entrySet()) {
            double r = (double)(entry.getValue()*100)/sentenceNumber;
            DecimalFormat newFormat = new DecimalFormat("#.###");
            double threeDecimal =  Double.valueOf(newFormat.format(r));
            finalResult.put(entry.getKey(), threeDecimal);
        }
        return finalResult;
    }

}