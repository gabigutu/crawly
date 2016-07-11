package com.crawler.akka;

import akka.actor.UntypedActor;
import com.crawler.App;
import com.crawler.Constants;
import com.crawler.akka.messages.FinalMessage;
import com.crawler.processing.CalculateScores;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Created by dorinela on 07.04.2016.
 */
public class StatisticsActor extends UntypedActor {

    XSSFWorkbook workbook = null;
    CalculateScores calculateScores= new CalculateScores();
    public static Integer reviewNumber = 0;
    List<FinalMessage> finalMessages = new ArrayList<>();

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof FinalMessage) {

            System.out.println("Receive message for write in file ... ");
            FinalMessage finalMessage = (FinalMessage) o;

            finalMessages.add(finalMessage);
            if (finalMessages.size() == App.REVIEWS_NUMBER) {
                /*for (FinalMessage fm : finalMessages) {
                    writeFinalData(fm);
                }*/
                writeFinalDataList(finalMessages);
            }
        }
    }

    public void writeFinalDataList(List<FinalMessage> finalMessages) {
        try {
            XSSFSheet sheet;
            if (workbook == null) {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet();
            }
            sheet = workbook.getSheetAt(0);
            for(FinalMessage finalMessage : finalMessages) {
                int rows = sheet.getPhysicalNumberOfRows();
                Row row = sheet.createRow(rows);

                Cell cell = row.createCell(0);
                cell.setCellValue(finalMessage.getInitialMessage().getGame());

                cell = row.createCell(1);
                cell.setCellValue(finalMessage.getInitialMessage().getReview());

                cell = row.createCell(2);
                cell.setCellValue(finalMessage.getInitialMessage().getRate());

                cell = row.createCell(3);
                cell.setCellValue(finalMessage.getInitialMessage().getReviewer());

                cell = row.createCell(4);
                cell.setCellValue(calculateScores.getScoreStanford(App.STANFORD_CORE_NLP, finalMessage.getInitialMessage().getReview()));

                cell = row.createCell(5);
                cell.setCellValue(calculateScores.getScoreStanfordLength(App.STANFORD_CORE_NLP, finalMessage.getInitialMessage().getReview()));

                //ordoneaza map
                int column = 6;

                SortedSet<String> keys = new TreeSet<String>(finalMessage.getScores().keySet());
                for (String key : keys) {
                    cell = row.createCell(column);
                    cell.setCellValue(finalMessage.getScores().get(key));
                    column ++;
                }
                reviewNumber ++;
                System.out.println("Finish to create row .................for review + " + reviewNumber);
            }
            FileOutputStream outputStream = new FileOutputStream(Constants.STATISTICS_FILE);
            workbook.write(outputStream);
            System.out.println("Finish to write all data in file !!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public void writeFinalData(FinalMessage finalMessage) {
        try {
            XSSFSheet sheet = null;
            if (workbook == null) {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet();
            }
            sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rows);

            Cell cell = row.createCell(0);
            cell.setCellValue(finalMessage.getInitialMessage().getGame());

            cell = row.createCell(1);
            cell.setCellValue(finalMessage.getInitialMessage().getReview());

            cell = row.createCell(2);
            cell.setCellValue(finalMessage.getInitialMessage().getRate());

            cell = row.createCell(3);
            cell.setCellValue(finalMessage.getInitialMessage().getReviewer());

            cell = row.createCell(4);
            cell.setCellValue(calculateScores.getScoreStanford(App.STANFORD_CORE_NLP, finalMessage.getInitialMessage().getReview()));

            cell = row.createCell(5);
            cell.setCellValue(calculateScores.getScoreStanfordLength(App.STANFORD_CORE_NLP, finalMessage.getInitialMessage().getReview()));

            //ordoneaza map
            int column = 6;

            SortedSet<String> keys = new TreeSet<String>(finalMessage.getScores().keySet());
            for (String key : keys) {
                cell = row.createCell(column);
                cell.setCellValue(finalMessage.getScores().get(key));
                column ++;
            }
            FileOutputStream outputStream = new FileOutputStream(Constants.STATISTICS_FILE);
            workbook.write(outputStream);
            reviewNumber ++;
            System.out.println("Finish to write in file .................for review + " + reviewNumber);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }


}
