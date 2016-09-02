package com.crawler;

/**
 * Created by Dorinela on 11/14/2015.
 */
public class Constants {

    //CRAWLER
    public static final String CRAWLER_STORAGE_FOLDER = "src/main/resources/crawler4jStorage";
    public static final String DATAFILE = "src/main/resources/output/statistics.csv";
    public static final String STATISTICS_FILE = "src/main/resources/output/raport.xlsx";

    //Elasticsearch
    public static final String JSON_FILE = "src/main/resources/starWars.json";
    public static final String URL = "https://www.amazon.com/Lego-Star-Wars-Complete-Saga-PC/product-reviews/B002KIBT9G/ref=cm_cr_arp_d_paging_btm_3?ie=UTF8&showViewpoints=1&sortBy=recent&pageNumber=";
    public static final String GAME_NAME = "Elder Scrolls";

    //Delimiter used in CSV file
    public static final String COMMA_DELIMITER = ",";
    public static final String NEW_LINE_SEPARATOR = "\n";

    //files
    public static final String PATH = "src/main/resources/input/";

    public static final String STOP_WORDS = PATH + "stopwords_en.txt";
    public static final String DICTIONARY = PATH + "dict_en.txt";
    public static final String HEADER = PATH + "new_header.txt";

    public static final String NEGATIVE_WORDS = PATH + "negative_words.txt";
    public static final String POSITIVE_WORDS = PATH + "positive_words.txt";
    public static final String AFFECTIVE_LIST = PATH + "affective_list.txt";
    public static final String INQUIRERBASIC = PATH + "inquirerbasic.txt";

    //senticnet
    public static final String SENTICNET_APITUDE = PATH + "senticnet/senticnet_aptitude.xlsx";
    public static final String SENTICNET_APTITUDE_NEGATIVE = PATH + "senticnet/senticnet_aptitude_negative.xlsx";
    public static final String SENTICNET_APTITUDE_POSITIVE = PATH + "senticnet/senticnet_aptitude_positive.xlsx";

    public static final String SENTICNET_ATTENTION = PATH + "senticnet/senticnet_attention.xlsx";
    public static final String SENTICNET_ATTENTION_NEGATIVE = PATH + "senticnet/senticnet_attention_negative.xlsx";
    public static final String SENTICNET_ATTENTION_POSITIVE = PATH + "senticnet/senticnet_attention_positive.xlsx";

    public static final String SENTICNET_PLEASANTNESS = PATH + "senticnet/senticnet_pleasantness.xlsx";
    public static final String SENTICNET_PLEASANTNESS_NEGATIVE = PATH + "senticnet/senticnet_pleasantness_negative.xlsx";
    public static final String SENTICNET_PLEASANTNESS_POSITIVE = PATH + "senticnet/senticnet_pleasantness_positive.xlsx";

    public static final String SENTICNET_POLARITY = PATH + "senticnet/senticnet_polarity.xlsx";
    public static final String SENTICNET_POLARITY_NEGATIVE = PATH + "senticnet/senticnet_polarity_negative.xlsx";
    public static final String SENTICNET_POLARITY_POSITIVE = PATH + "senticnet/senticnet_polarity_positive.xlsx";

    public static final String SENTICNET_SENSITIVITY = PATH + "senticnet/senticnet_sensitivity.xlsx";
    public static final String SENTICNET_SENSITIVITY_NEGATIVE = PATH + "senticnet/senticnet_sensitivity_negative.xlsx";
    public static final String SENTICNET_SENSITIVITY_POSITIVE = PATH + "senticnet/senticnet_sensitivity_positive.xlsx";

    //LIWC
    public static final String LIWC_AFFECT = PATH + "LIWC/LIWC_Affect.xlsx";
    public static final String LIWC_ANGER = PATH + "LIWC/LIWC_Anger.xlsx";
    public static final String LIWC_ANX = PATH + "LIWC/LIWC_Anx.xlsx";
    public static final String LIWC_ASSENT = PATH + "LIWC/LIWC_Assent.xlsx";
    public static final String LIWC_CAUSE = PATH + "LIWC/LIWC_Cause.xlsx";
    public static final String LIWC_DEATH = PATH + "LIWC/LIWC_Death.xlsx";
    public static final String LIWC_FEEL = PATH + "LIWC/LIWC_Feel.xlsx";
    public static final String LIWC_HEAR = PATH + "LIWC/LIWC_Hear.xlsx";
    public static final String LIWC_INHIB = PATH + "LIWC/LIWC_Inhib.xlsx";
    public static final String LIWC_LEISURE = PATH + "LIWC/LIWC_Leisure.xlsx";
    public static final String LIWC_NEGEMO = PATH + "LIWC/LIWC_Negemo.xlsx";
    public static final String LIWC_PERCEPT = PATH + "LIWC/LIWC_Percept.xlsx";
    public static final String LIWC_POSEMO = PATH + "LIWC/LIWC_Posemo.xlsx";
    public static final String LIWC_SAD = PATH + "LIWC/LIWC_Sad.xlsx";
    public static final String LIWC_SEE = PATH + "LIWC/LIWC_See.xlsx";
    public static final String LIWC_SPACE = PATH + "LIWC/LIWC_Space.xlsx";
    public static final String LIWC_TIME = PATH + "LIWC/LIWC_Time.xlsx";

    //affective norms
    public static final String AFFECTIVE_NORMS = PATH + "affective-norms/affective_norms.xlsx";
    public static final String AFFECTIVE_NORMS_POSITIVE = PATH + "affective-norms/affective_norms_positive.xlsx";
    public static final String AFFECTIVE_NORMS_NEGATIVE = PATH + "affective-norms/affective_norms_negative.xlsx";
}
