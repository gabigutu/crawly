package com.crawler.elasticsearch;

import com.crawler.Constants;

import com.crawler.processing.ProcessingLists;
import com.crawler.processing.ReadData;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.util.*;

/**
 * Created by Dorinela on 1/14/2016.
 */
public class GetData {

    public ArrayList<Map> searchByType(String index, String category) {
        ArrayList<Map> result = new ArrayList<Map>();
        try {
            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", "elasticsearch").build();

            Client client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            SearchResponse response = client.prepareSearch(index)
                    .setSize(1000)
                    .setTypes(category)
                    .execute()
                    .actionGet();
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                Map data = searchHit.getSource();
                result.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int getScorePerGame(String gameName){
        GetData getData = new GetData();
        ArrayList<Map> result = getData.searchByType("games", gameName);

        ArrayList<Integer> stars = new ArrayList<Integer>();
        int totalValue = 0;
        for (Map map : result) {
            totalValue += Integer.valueOf(map.get("rate").toString());
        }
        return totalValue/result.size();

    }

    public ArrayList<Map> numberOfRate(String index, String type, Integer rate) {
        ArrayList<Map> result = new ArrayList<Map>();
        try {
            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", "elasticsearch").build();

            Client client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            SearchResponse response = client.prepareSearch(index)
                    .setSize(500)
                    .setTypes(type)
                    .setQuery(QueryBuilders.matchQuery("rate", rate))         // Query
                    .execute()
                    .actionGet();
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                Map data = searchHit.getSource();
                data.put("id", searchHit.getId());
                result.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int checkWordsFromList(String review) {
        Set<String> stopwords = ReadData.readDataFromFile(Constants.STOP_WORDS);

        String[] words = review.split(" ");
        ProcessingLists processingLists = new ProcessingLists();
        int sum = 0;

        for (String word : words) {
            if (stopwords.contains(word)) {
                sum ++;
            }
        }

        return words.length - sum;
    }

    public int checkWords(StanfordCoreNLP pipeline, String text) {
        int result = 0;
        Set<String> stopwords = ReadData.readDataFromFile(Constants.STOP_WORDS);
        Set<String> dictionary = ReadData.readDataFromFile(Constants.DICTIONARY);
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (pos.startsWith("NN") || pos.startsWith("VB") || pos.startsWith("JJ") ||pos.startsWith("RB")) {
                    if (!stopwords.contains(word.toLowerCase()) && dictionary.contains(word.toLowerCase())) {
                        result ++;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        GetData getData = new GetData();
       /* String text = "So.... it seems I have to make the same mistake a few times before learning a relatively easy lesson. Never, ever, ever pre-order. The game is generally disappointing. As I'm sure many other reviews have stated, no single player campaign. Very limited in scope (maps), Extremely limited single player options. I'm not going to review multi-player as I've no interest in it. I'm certain there will be additional maps and content for multi-player (for additional cost) as EA is wont to do. I'd be willing buy a Battlefront II port over this. Yes, graphics are great but a lot of games have great graphics. If you're only interested in a multiplayer run and gun style game, then this may be the game for you. If you have any interest in enjoying, even a rudimentary, single player experience. Then avoid this. It will only lead to disrepair and disappointment.";
        String review = "The game looks really good however lacks content as part of the base game. There is not and will not be a campaign. But luckily";
        int nr = getData.checkWords(App.STANFORD_CORE_NLP, review);
        System.out.println("nr: " + nr);
        ProcessingLists processingLists = new ProcessingLists();
        double p = processingLists.calculateProcentWithValence(App.AFFECTIVE_NORMS, review, nr);
        System.out.printf("procent: " + p);*/

        Set<String> stop = ReadData.readDataFromFile(Constants.STOP_WORDS);
        Set<String> in = ReadData.readDataFromFile(Constants.LIWC_AFFECT);
        Map<String, Set<String>> all = ReadData.getLists(in);
        SortedSet<String> result = new TreeSet<String>();

        SortedSet<String> lists = new TreeSet<String>();
       // for (String key : all.keySet()) {
            for (String s : in) {
                if (stop.contains(s)) {
                    result.add(s);
            }
            }
       // }
       for (String s: result) {
           System.out.println(s);
       }


        //System.out.println(getData.checkWordsFromList(text));

        /*SentimentModel model = SentimentModel.loadSerialized("edu/stanford/nlp/models/sentiment/sentiment.ser.gz");
        boolean knownWord = model.wordVectors.containsKey("peace");
        System.out.println(knownWord);*/

        //list.forEach(System.out::println);

//        Map<Integer, Integer> statistic = new HashMap<Integer, Integer>();
//        statistic.put(1, 0);
//        statistic.put(2, 0);
//        statistic.put(3, 0);
//        statistic.put(4, 0);
//        statistic.put(5, 0);
//
//        String gameName = "maxPayne";
//        ArrayList<Map> data = getData.searchByType("games", gameName);
//        for (Map map : data) {
//            Integer rate = Integer.valueOf(map.get("rate").toString());
//            statistic.put(rate, statistic.get(rate) + 1);
//        }
//
//        System.out.println(statistic);
//        //add to csv file
//        String fileName = "C:\\Users\\Dorinela\\Desktop\\rates.csv";
//        CsvFileWriter.writeRateInCsvFile(fileName, gameName, statistic);
//
//        System.out.println(getData.numberOfRate("games", gameName, 1).size());
//        System.out.println(getData.numberOfRate("games", gameName, 2).size());
//        System.out.println(getData.numberOfRate("games", gameName, 3).size());
//        System.out.println(getData.numberOfRate("games", gameName, 4).size());
//        System.out.println(getData.numberOfRate("games", gameName, 5).size());

    }
}
