package com.crawler.elasticsearch;

import com.crawler.App;
import com.crawler.Constants;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.util.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by Dorinela on 11/14/2015.
 */
public class AddData {

    private Properties properties = App.getProperties("database.properties");

    /**
     * add data into Elasticsearch
     * @param data
     */
    public void addDataToElasticsearch(ArrayList<Map> data) {
        try {
            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", properties.getProperty("cluster.name")).build();

            Client client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(properties.getProperty("hostname")), Integer.valueOf(properties.getProperty("port"))));

            //create json for every review and add in db
            for (Map map : data) {
                //Map<String, Object> json = map;
                IndexResponse response = client.prepareIndex(properties.getProperty("index"), properties.getProperty("type")).setSource(map).execute().actionGet();
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * read data from JSON for adding into Elasticsearch
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<Map> readDataFromJSON() throws FileNotFoundException {
        ArrayList<Map> result = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(Constants.JSON_FILE));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object jsonObj : jsonArray) {
                JSONObject jsonObject = (JSONObject) jsonObj;
                JSONObject source = (JSONObject) jsonObject.get("_source");

                String rate = (String)source.get("rate");
                String comment = (String)source.get("comment");
                String reviewer = (String)source.get("reviewer");

                Map<String, String> map = new HashMap<>();
                map.put("rate", rate);
                map.put("comment", comment);
                map.put("reviewer", reviewer);

                result.add(map);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException{
        AddData addData = new AddData();
        ArrayList<Map> data = addData.readDataFromJSON();
        System.out.println(data.size());
        System.out.println(data);
        addData.addDataToElasticsearch(data);
    }
}
