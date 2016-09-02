package com.crawler;

import com.crawler.akka.AkkaActorSystem;
import com.crawler.akka.messages.InitialMessage;
import com.crawler.elasticsearch.GetData;
import com.crawler.processing.ProcessingLists;
import com.crawler.processing.ReadData;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by dorinela on 07.04.2016.
 */
public class App {

    public static AkkaActorSystem appActorSystem;
    public static Properties properties = getProperties("stanford.properties");
    public static StanfordCoreNLP STANFORD_CORE_NLP = new StanfordCoreNLP(properties);

    public static Set<String> STOPWORDS = new HashSet<>();
    public static Set<String> DICTIONARY = new HashSet<>();

    public static Set<String> AFFECTIVE_LIST = new HashSet<>();
    public static Map<String, Set<String>>  ALL_AFFECTIVE_LIST = new HashMap<>();
    public static Set<String> INQUIRERBASIC = new HashSet<>();
    public static Set<String> NEGATIVE_WORDS = new HashSet<>();
    public static Set<String> POSITIVE_WORDS = new HashSet<>();
    public static Map<String, Set<String>>  ALL_INQUIRERBASIC = new HashMap<>();

    //affective norms
    public static Map<String, Double> AFFECTIVE_NORMS = new HashMap<>();
    public static Map<String, Double> AFFECTIVE_NORMS_POSITIVE = new HashMap<>();
    public static Map<String, Double> AFFECTIVE_NORMS_NEGATIVE = new HashMap<>();
    public static Map<String, Double> AFFECTIVE_NORMS_NEUTRAL = new HashMap<>();

    //LIWC
    public static Set<String> LIWC_AFFECT = new HashSet<>();
    public static Set<String> LIWC_ANGER = new HashSet<>();
    public static Set<String> LIWC_ANX = new HashSet<>();
    public static Set<String> LIWC_ASSENT = new HashSet<>();
    public static Set<String> LIWC_CAUSE = new HashSet<>();
    public static Set<String> LIWC_DEATH = new HashSet<>();
    public static Set<String> LIWC_FEEL = new HashSet<>();
    public static Set<String> LIWC_HEAR = new HashSet<>();
    public static Set<String> LIWC_INHIB = new HashSet<>();
    public static Set<String> LIWC_LEISURE = new HashSet<>();
    public static Set<String> LIWC_NEGEMO = new HashSet<>();
    public static Set<String> LIWC_PERCEPT = new HashSet<>();
    public static Set<String> LIWC_POSEMO = new HashSet<>();
    public static Set<String> LIWC_SAD = new HashSet<>();
    public static Set<String> LIWC_SEE = new HashSet<>();
    public static Set<String> LIWC_SPACE = new HashSet<>();
    public static Set<String> LIWC_TIME = new HashSet<>();

    //senticnet
    public static Map<String, Double> SENTICNET_APITUDE = new HashMap<>();
    public static Map<String, Double> SENTICNET_APTITUDE_NEGATIVE = new HashMap<>();
    public static Map<String, Double> SENTICNET_APTITUDE_POSITIVE = new HashMap<>();

    public static Map<String, Double> SENTICNET_ATTENTION = new HashMap<>();
    public static Map<String, Double> SENTICNET_ATTENTION_NEGATIVE = new HashMap<>();
    public static Map<String, Double> SENTICNET_ATTENTION_POSITIVE = new HashMap<>();

    public static Map<String, Double> SENTICNET_PLEASANTNESS = new HashMap<>();
    public static Map<String, Double> SENTICNET_PLEASANTNESS_NEGATIVE = new HashMap<>();
    public static Map<String, Double> SENTICNET_PLEASANTNESS_POSITIVE = new HashMap<>();

    public static Map<String, Double> SENTICNET_POLARITY = new HashMap<>();
    public static Map<String, Double> SENTICNET_POLARITY_NEGATIVE = new HashMap<>();
    public static Map<String, Double> SENTICNET_POLARITY_POSITIVE = new HashMap<>();

    public static Map<String, Double> SENTICNET_SENSITIVITY = new HashMap<>();
    public static Map<String, Double> SENTICNET_SENSITIVITY_NEGATIVE = new HashMap<>();
    public static Map<String, Double> SENTICNET_SENSITIVITY_POSITIVE = new HashMap<>();
    public static Integer REVIEWS_NUMBER = 0;

    static {
        STOPWORDS = ReadData.readDataFromFile(Constants.STOP_WORDS);
        DICTIONARY = ReadData.readDataFromFile(Constants.DICTIONARY);

        AFFECTIVE_LIST = ReadData.readDataFromFile(Constants.AFFECTIVE_LIST);
        ALL_AFFECTIVE_LIST = ReadData.getLists(AFFECTIVE_LIST);

        INQUIRERBASIC = ReadData.readDataFromFile(Constants.INQUIRERBASIC);
        ALL_INQUIRERBASIC = ReadData.getLists(INQUIRERBASIC);
        NEGATIVE_WORDS = ReadData.readDataFromFile(Constants.NEGATIVE_WORDS);
        POSITIVE_WORDS = ReadData.readDataFromFile(Constants.POSITIVE_WORDS);

        //affective norms
        AFFECTIVE_NORMS = ReadData.readDataWithValenceFromExcel(Constants.AFFECTIVE_NORMS);
        AFFECTIVE_NORMS_POSITIVE = ReadData.readDataWithValenceFromExcel(Constants.AFFECTIVE_NORMS_POSITIVE);
        AFFECTIVE_NORMS_NEGATIVE = ReadData.readDataWithValenceFromExcel(Constants.AFFECTIVE_NORMS_NEGATIVE);

        //senticnet
        SENTICNET_APITUDE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_APITUDE);
        SENTICNET_APTITUDE_NEGATIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_APTITUDE_NEGATIVE);
        SENTICNET_APTITUDE_POSITIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_APTITUDE_POSITIVE);

        SENTICNET_ATTENTION = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_ATTENTION);
        SENTICNET_ATTENTION_NEGATIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_ATTENTION_NEGATIVE);
        SENTICNET_ATTENTION_POSITIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_ATTENTION_POSITIVE);

        SENTICNET_PLEASANTNESS = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_PLEASANTNESS);
        SENTICNET_PLEASANTNESS_NEGATIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_PLEASANTNESS_NEGATIVE);
        SENTICNET_PLEASANTNESS_POSITIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_PLEASANTNESS_POSITIVE);

        SENTICNET_POLARITY = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_POLARITY);
        SENTICNET_POLARITY_NEGATIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_POLARITY_NEGATIVE);
        SENTICNET_POLARITY_POSITIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_POLARITY_POSITIVE);

        SENTICNET_SENSITIVITY = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_SENSITIVITY);
        SENTICNET_SENSITIVITY_NEGATIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_SENSITIVITY_NEGATIVE);
        SENTICNET_SENSITIVITY_POSITIVE = ReadData.readDataWithValenceFromExcel(Constants.SENTICNET_SENSITIVITY_POSITIVE);

        //LIWC
        LIWC_AFFECT = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_AFFECT));
        LIWC_ANGER = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_ANGER));
        LIWC_ANX = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_ANX));
        LIWC_ASSENT = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_ASSENT));
        LIWC_CAUSE = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_CAUSE));
        LIWC_DEATH = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_DEATH));
        LIWC_FEEL =  ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_FEEL));
        LIWC_HEAR = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_HEAR));
        LIWC_INHIB = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_INHIB));
        LIWC_LEISURE = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_LEISURE));
        LIWC_NEGEMO = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_NEGEMO));
        LIWC_PERCEPT =  ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_PERCEPT));
        LIWC_POSEMO = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_POSEMO));
        LIWC_SAD = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_SAD));
        LIWC_SEE = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_SEE));
        LIWC_SPACE = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_SPACE));
        LIWC_TIME = ReadData.expandList(ReadData.readDataFromExcel(Constants.LIWC_TIME));
    }

    public static void main(String[] args) {
        appActorSystem = new AkkaActorSystem();
        appActorSystem.init();

        GetData accessDatabase = new GetData();
        Properties databaseProperties = getProperties("database.properties");
        //String index = "watchDogs";
        //String gameName = "Watch Dogs";
        List<Map> data = accessDatabase.searchByType(databaseProperties.getProperty("index"), databaseProperties.getProperty("type"));
        REVIEWS_NUMBER = data.size();
        System.out.println(REVIEWS_NUMBER + " reviews for processing\n");

        for (int i = 0; i < data.size(); i ++) {
            InitialMessage initialMessage = new InitialMessage(databaseProperties.getProperty("game.name") ,data.get(i).get("comment").toString(), Integer.valueOf(data.get(i).get("rate").toString()),data.get(i).get("reviewer").toString(), i);
            //appActorSystem.processReviewActors.get(i).tell(initialMessage, null);
            appActorSystem.processReviewActor.tell(initialMessage, null);
        }
    }

    public static Properties getProperties (String resourceName) {

        Properties properties = new Properties();
        InputStream input = null;
        try {

            input = new App().getClass().getClassLoader().getResourceAsStream(resourceName);

            if (input == null) {
                throw new RuntimeException("Configuration file missing: " + resourceName);
            }
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return properties;
    }
}
