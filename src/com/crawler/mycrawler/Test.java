package com.crawler.mycrawler;

import com.crawler.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Dorinela on 7/10/2016.
 */
public class Test {

    public static Properties properties = getProperties("stanford.properties");

    public static void main(String[] args) {
        //Properties properties = getProperties("database.properties");
        System.out.println(properties.getProperty("annotators"));
    }

    public static Properties getProperties (String resourceName) {

        Properties properties = new Properties();
        InputStream input = null;
        try {

            input = new Test().getClass().getClassLoader().getResourceAsStream(resourceName);

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
