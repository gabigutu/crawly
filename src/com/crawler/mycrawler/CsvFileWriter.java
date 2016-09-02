package com.crawler.mycrawler;

import com.crawler.App;
import com.crawler.Constants;
import com.crawler.entities.Statistic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Dorinela on 1/4/2016.
 */
public class CsvFileWriter {

    private Properties properties = App.getProperties("file.properties");

    //CSV file header
    private String HEADER = "REVIEW,LIWC_AH,LIWC_AI,LIWC_AJ,LIWC_AK,LIWC_AL,LIWC_AM,AFFECTIVE_NORMS,NEGATIVE_WORDS,POSITIVE_WORDS," +
            "SENTICNET_DATA,VADER_SENTIMENT_LEXICON,HAPPINESS_GALC,ENJOYMENT_GALC,DISAPPOINTMENT_GALC,POSITIV_GI,NEGATIV_GI,STRONG_GI,STANFORD_AVERAGE,STANFORD_AVERAGE_L,RATE";

    public void writeCsvFile(String fileName, List<Statistic> dates) {

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName, true);

            //Write the CSV file header
            fileWriter.append(properties.getProperty("header"));

            //Add a new line separator after the header
            fileWriter.append(Constants.NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (Statistic d : dates) {
                fileWriter.append(String.valueOf(d.getGame()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(d.getReviewer());
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getRating()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getScoreStanford()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getScoreStanfordLength()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getVeryNegative()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getNegative()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getNeutral()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getPositive()));
                fileWriter.append(Constants.COMMA_DELIMITER);
                fileWriter.append(String.valueOf(d.getVeryPositive()));
                fileWriter.append(Constants.NEW_LINE_SEPARATOR);
            }
            System.out.println("Data was added successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

}
