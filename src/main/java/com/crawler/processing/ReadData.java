package com.crawler.processing;
import com.crawler.App;
import com.crawler.Constants;
import com.crawler.entities.Review;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Dorinela on 4/6/2016.
 */
public class ReadData {

    public static Set<String> readDataFromFile(String fileName) {

        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
            list = stream
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> result = new HashSet<>();
        result.addAll(list);
        return result;
    }

    public static Map<String, Set<String>> getLists(Set<String> initial) {
        Map<String, Set<String>> result =new HashMap<>();

        for (String i : initial) {
            String[] words = i.split("\t");
            Set<String> wordsList = new HashSet<>(Arrays.asList(words));
            wordsList.remove(0);
            Set<String> wordListExpanded = expandList(wordsList);
            result.put(words[0], wordListExpanded);
        }

        return result;
    }

    public static Set<String> readDataFromExcel(String fileName) {
        Set<String> result = new HashSet<>();

        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();


            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (nextRow.getCell(0).getCellType() != XSSFCell.CELL_TYPE_BOOLEAN) {
                    String word = nextRow.getCell(0).getStringCellValue();
                    result.add(word);
                }
            }
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Exception on read from file: " + fileName);
        }

        return result;
    }

    public static Map<String, Double> readDataWithValenceFromExcel(String fileName) {
        Map<String, Double> result = new HashMap<>();
        int contor = 0;
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();


            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (nextRow.getCell(0).getCellType() != XSSFCell.CELL_TYPE_BOOLEAN) {
                    String word = nextRow.getCell(0).getStringCellValue().toString();
                    Double valence = nextRow.getCell(1).getNumericCellValue();
                    contor ++;
                    result.put(word, valence);
                }

            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Exception on read from file: " + fileName);
        }
        //System.out.println("contor: " + contor);
        return result;
    }

    public static Set<String> expandList(Set<String> list) {
        Set<String> result = new HashSet<>();

        for (String l : list) {
            if (l.endsWith("*")) {
                //search all words in dictionary
               for (String d : App.DICTIONARY) {
                   if (Pattern.matches(l.substring(0, l.length()-1) + ".+", d)) {
                       result.add(d);
                   }
               }
            } else {
                result.add(l);
            }
        }

        return result;
    }

    public static void writeHeaders(List<String> headers) {
        try {
            XSSFSheet sheet = null;
            XSSFWorkbook workbook = new XSSFWorkbook();

            sheet = workbook.createSheet();
            int rows = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rows);

            //ordoneaza map
            int column = 0;

            for (String header : headers) {
                Cell cell = row.createCell(column);
                cell.setCellValue(header);
                column ++;
            }
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/headers.xlsx");
            workbook.write(outputStream);
            System.out.println("Finish to write in file ............");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static List<String> readHeaderFromFile(String fileName) {

        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
            list = stream
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Review> readDataFromExcelGameRate(String fileName) {
        List<Review> result = new ArrayList<>();
        int contor = 0;
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();


            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                String game = nextRow.getCell(0).getStringCellValue().toString();
                Double rate = nextRow.getCell(1).getNumericCellValue();
                Review review = new Review(game, rate);
                contor ++;
                result.add(review);

            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Exception on read from file: " + fileName);
        }
        //System.out.println("contor: " + contor);
        return result;
    }

    public static void main(String[] args) {
        ReadData readData = new ReadData();
        Map<String, Double> data = readData.readDataWithValenceFromExcel(Constants.AFFECTIVE_NORMS);
        System.out.println(data);
    }
}
