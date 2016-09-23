/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.sensCritique;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ReaderBench
 */
public class SensCritiqueCrawler {
    
    public static Review crawlOpinion(String urlToPage) throws IOException{
       Review result = new Review();
       
       if (urlToPage == null || urlToPage.length() == 0) return result;
		
	Document doc = Jsoup.connect(urlToPage)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
            .get();
       
        Elements title = doc.getElementsByClass("rvi-cover-title");
        if(
                title.size() > 0 &&
                title.get(0) != null &&
                title.get(0).html().length() > 0){
//            System.out.println(title.get(0).html());
            result.setTitle(title.get(0).html());
        }
        
        Elements author = doc.getElementsByClass("rvi-review-author");
        if(
                author.size() > 0 &&
                author.get(0) != null &&
                author.get(0).html().length() > 0){
//            System.out.println(author.get(0).html());
            result.setAuthor(author.get(0).html());
        }//De verificat!!!
        
        Elements score = doc.getElementsByAttributeValue("itemprop", "ratingValue");
        if(
                score.size() > 0 &&
                score.get(0) != null &&
                score.get(0).html().length() > 0){
//            System.out.println(score.get(0).html());
          result.setScore(Double.parseDouble(score.get(0).html()) / 10);
        }
        
        Elements date = doc.getElementsByTag("time");
//        System.out.println(date.size());
        if(
                date.size() > 0 &&
                date.get(date.size()-1) != null &&
                date.get(date.size()-1).html().length() > 0 &&
                date.get(date.size()-1).hasAttr("datetime")){
//            System.out.println(date.get(date.size()-1).attr("datetime"));
            StringTokenizer st = new StringTokenizer(date.get(date.size()-1).attr("datetime"), " ");
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), "-");
            int year = Integer.parseInt(st2.nextToken());
            int month = Integer.parseInt(st2.nextToken());
            int day = Integer.parseInt(st2.nextToken());
//            System.out.println(year + " " + month + " " + day);
            String text = month + "." + day + "." + year;
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
            java.util.Date timeOfReview = null;
            
            try {
		timeOfReview = sdf.parse(text);
            } catch (ParseException e) {
            	e.printStackTrace();
            }
            result.setDate(new Date(timeOfReview.getTime()));
//            System.out.println(result.getDate());
        }
        
        Elements reviewContent = doc.getElementsByClass("rvi-review-content");
        if(
                reviewContent.size() > 0 &&
                reviewContent.get(0) != null &&
                reviewContent.get(0).html().length() > 0){
//            System.err.println(reviewContent.get(0).text());
            result.setContent(reviewContent.get(0).text());
        }
       return result;
    }
    
    public static ArrayList<Review> crawlPage(String urlToPage) throws IOException{
        ArrayList<Review> result = new ArrayList<Review>();
        
        if (urlToPage == null || urlToPage.length() == 0) return result;
		
	Document doc = Jsoup.connect(urlToPage)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
            .get();
        
        Elements linksToOpinions = doc.getElementsByClass("ere-review-anchor");
        for(Element e : linksToOpinions){
            if(e.hasAttr("href")){
                result.add(crawlOpinion("http://www.senscritique.com"+e.attr("href")));
            }
        }
        
        return result;
    }
    
    public static SensCritiqueEntity crawlEntity(String urlToPage) throws IOException{
        SensCritiqueEntity sce = new SensCritiqueEntity();
        sce.setTypeEn("Comics");
        sce.setTypeFr("BD");
        sce.setLink(urlToPage);
        
        if (urlToPage == null || urlToPage.length() == 0) return sce;
		
	Document doc = Jsoup.connect(urlToPage)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
            .get();
        
//        PrintWriter pw = new PrintWriter(new File("test.txt"));
        
        Elements titleFr = doc.getElementsByClass("pvi-product-title");
        if(
                titleFr.size() > 0 &&
                titleFr.get(0) != null &&
                titleFr.get(0).html().length() > 0){
//            pw.println(titleFr.get(0).html());
            sce.setNameFr(titleFr.get(0).html());
        }
        
        Elements titleEn = doc.getElementsByClass("pvi-product-originaltitle");
        if(
                titleEn.size() > 0 &&
                titleEn.get(0) != null &&
                titleEn.get(0).html().length() > 0){
//            pw.println(titleEn.get(0).html());
            sce.setNameEn(titleEn.get(0).html());
        }
        
        Elements year = doc.getElementsByClass("pvi-product-year");
        if(
                year.size() > 0 &&
                year.get(0) != null &&
                year.get(0).html().length() > 0){
//            pw.println(year.get(0).html());
            String number = year.get(0).html();
            number = number.substring(1,number.length()-1);
            sce.setYear(Integer.parseInt(number));
        }      
        
        Elements generalScore = doc.getElementsByClass("pvi-scrating-value");
        if(
                generalScore.size() > 0 &&
                generalScore.get(0) != null &&
                generalScore.get(0).html().length() > 0){
//            pw.println(generalScore.get(0).html());
            sce.setGeneralScore(Double.parseDouble(generalScore.get(0).html()));
        }
        
        Elements graph = doc.getElementsByAttributeValueContaining("data-rel", "sc-graph-mini");
        if(
                graph.size() > 0 && 
                graph.get(0) != null &&
                graph.get(0).html().length() > 0){
            Elements divs = graph.get(0).getElementsByTag("div");
//            System.out.println(divs.size());
            if(divs.size()==10){
                int[] votes = new int[10];
                for(int i=0; i<10; i++){
                    votes[i] = Integer.parseInt(divs.get(i).html());
//                    System.out.println(divs.get(i));
                }
                sce.setScoreDistribution(votes);
            }
        }
        
        //NumberOfPages
        int number = getNumberOfPages(urlToPage);
        
        urlToPage = "http://www.senscritique.com/sc2" + urlToPage.substring(urlToPage.lastIndexOf('/')) + "/critiques/all.ajax";
        
        for(int i=1; i<=number; i++){
            String link = urlToPage + "#page-" + i + "/order-appreciation/filter-all/";
            sce.addReviews(crawlPage(link)); //break;
            System.err.println("Page " + i + "/" + number + ".");
            try{
                Thread.currentThread().sleep(2000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
//        pw.close();
        return sce;
    }
    
    private static int getNumberOfPages(String urlToPage) throws IOException {
        int number = 1;
        
        urlToPage = "http://www.senscritique.com/sc2" + urlToPage.substring(urlToPage.lastIndexOf('/')) + "/critiques/all.ajax";
//        System.out.println(urlToPage);
       
        if (urlToPage == null || urlToPage.length() == 0) return number;
		
	Document doc = Jsoup.connect(urlToPage)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
            .get();
        
        Elements eipaPage = doc.getElementsByClass("eipa-page");
        if(
                eipaPage.size() > 0 &&
                eipaPage.get(eipaPage.size()-1) != null &&
                eipaPage.get(eipaPage.size()-1).html().length() > 0){
            Elements lastPage = eipaPage.get(eipaPage.size()-1).getElementsByTag("a");
            if(
                    lastPage.size() > 0 &&
                    lastPage.get(0) != null &&
                    lastPage.get(0).html().length() > 0){
//                System.out.println(lastPage.get(0).html());
                String text = lastPage.get(0).html();
                try{
                    number = Integer.parseInt(text.substring(3));
                }catch(Exception e){
                    number = Integer.parseInt(text);//Fara 3 puncte.
                }
//                System.out.println(number);
            }
        }
        
        return number;
    }
    
    public static void main(String[] args) throws IOException{
//        SensCritiqueEntity sce = crawlEntity("http://www.senscritique.com/film/L_Homme_qui_voulut_etre_roi/461527");
//        System.out.println(sce.toString());
        Scanner sc = new Scanner(new File("SensCritique/BDErrors1.txt"));
        
        String link = "";
        
        int contor = 300;
        
        PrintWriter err = new PrintWriter(new File("SensCritique/BDErrors2.txt"));
        
        while(sc.hasNextLine() && (link = sc.nextLine())!=null){
            PrintWriter pw = new PrintWriter(new File("SensCritique/BD/SensCritiqueBD"+contor+".txt"));
            
            try{
                pw.println(crawlEntity(link));
                System.err.println("Succes at BD " + contor + ".");
                Thread.currentThread().sleep(2000);
            }catch(Exception e){
                System.out.println("Error at BD " + contor + ".");
                err.println(link);
            }
            pw.close();
            contor++;
        }
        
        err.close();
        sc.close();
    }
}
