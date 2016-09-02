package com.crawler.mycrawler;

import com.crawler.App;
import com.crawler.Constants;
import com.crawler.elasticsearch.AddData;
import com.crawler.entities.Statistic;
import com.crawler.processing.CalculateScores;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by Dorinela on 11/14/2015.
 */
public class MyCrawler extends WebCrawler {

    /**
     * @param referringPage - reference page
     * @param url - url
     * @return true if the page will be processed / false otherwise
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return href.startsWith(Constants.URL);
        //return href.equals(Constants.URL_TEST);
    }

    /**
     * Page processing
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        ArrayList<Map<String, Object>> allComments = new ArrayList<>();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            Document doc = Jsoup.parse(html);
            Element elementById = doc.getElementById("cm_cr-review_list");

            System.out.println("\nstart page processing ***************************************************");

            Elements everyComment = elementById.getElementsByClass("review");
            for (Element element : everyComment) {
                Map<String, Object> map = new HashMap<>();
                //get rate
                Elements rank = element.getElementsByClass("a-icon-alt");
                if (rank.size() == 0) {
                    map.put("rate", 0);
                } else {
                    String r = rank.get(0).text().substring(0,1);
                    map.put("rate", r);
                    /*for (Element e : rank) {
                        String r = e.text().substring(0, 1);
                        System.out.println("r: " + r);
                        Integer rate = Integer.valueOf(r);
                        map.put("rate", r);
                    }*/
                }
                //get author
                Elements author = element.getElementsByClass("author");
                if (author.size() == 0) {
                    map.put("reviewer", "");
                } else {
                    for (Element e : author) {
                        map.put("reviewer", e.text());
                    }
                }

                //get comment
                Elements commentsElement = element.getElementsByClass("review-text");
                if (commentsElement.size() == 0) {
                    map.put("comment", "");
                } else {
                    for (Element e : commentsElement) {
                        map.put("comment", e.text());
                    }
                }
                System.out.println(map);
                allComments.add(map);
            }

            System.out.println("end page processing ***************************************************\n");
            saveData(allComments);
        }

    }

    private void saveData(ArrayList<Map<String, Object>> data) {
        Properties properties = App.getProperties("stanford.properties");
        StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP(properties);

        ArrayList<Map> dataForElasticSearch = new ArrayList<>();
        List<Statistic> dates = new ArrayList<>();

        for (Map c : data) {
            //check words
            CalculateScores calculateScores = new CalculateScores();
            int numberOfWords = calculateScores.checkWords(stanfordCoreNLP, (String)c.get("comment"));
            if (numberOfWords > 50) {

                double scoreStanford = calculateScores.getScoreStanford(stanfordCoreNLP, (String)c.get("comment"));
                double scoreStanfordLength = calculateScores.getScoreStanfordLength(stanfordCoreNLP, (String)c.get("comment"));
                Map<String, Double> sentimentAnalisys = calculateScores.sentimentAnalysis(stanfordCoreNLP, (String)c.get("comment"));
                double veryNegative = sentimentAnalisys.get("Very Negative");
                double negative = sentimentAnalisys.get("Negative");
                double neutral =  sentimentAnalisys.get("Neutral");
                double positive = sentimentAnalisys.get("Positive");
                double veryPositive = sentimentAnalisys.get("Very Positive");

                Statistic statistic = new Statistic(Constants.GAME_NAME, (String)c.get("reviewer"), Integer.valueOf(c.get("rate").toString()), scoreStanford, scoreStanfordLength, veryNegative, negative, neutral, positive, veryPositive);
                System.out.println(statistic.toString());
                dates.add(statistic);
                dataForElasticSearch.add(c);
            }
        }

        //add to csv file
        CsvFileWriter csvFileWriter = new CsvFileWriter();
        csvFileWriter.writeCsvFile(Constants.DATAFILE, dates);

        //add data (Elasticsearch)
        AddData addData = new AddData();
        addData.addDataToElasticsearch(dataForElasticSearch);
    }

}
