/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.gsmarena;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ReaderBench
 */
public class GsmArenaReviewCrawler {
    
    public static String cleanParagraph(String paragraph){
        StringBuilder sb = new StringBuilder("");
        
        boolean link = false;
        
        for(int i=0; i<paragraph.length(); i++){
            if(paragraph.charAt(i) == '<'){
                link = true;
                continue;
            }
            if(paragraph.charAt(i) == '>'){
                link = false;
                continue;
            }
            if(!link){
                sb.append(paragraph.charAt(i));
                continue;
            }
            if(link){
                continue;
            }
        }
        
        return sb.toString();
    }
    
    public static GsmArenaReviewPage getReviewPage(String urlToPage) throws IOException{
        GsmArenaReviewPage page = new GsmArenaReviewPage();
        
        if (urlToPage == null || urlToPage.length() == 0) return page;
		
        Document doc = Jsoup.connect(urlToPage)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
                    .get();
        
        Elements reviewBody = doc.getElementsByClass("review-body");
        if(
                reviewBody.size() > 0 &&
                reviewBody.get(0) != null &&
                reviewBody.get(0).html().length() > 0){
//            System.out.println(reviewBody.get(0).html());

            //Box Elements
            Elements articleBlurbTitle = reviewBody.get(0).getElementsByClass("article-blurb-title");
            Elements articleBlurb = reviewBody.get(0).getElementsByClass("article-blurb");
            if(
                    articleBlurbTitle.size() > 0 &&
                    articleBlurb.size() > 0){
                for(int i=0; i<articleBlurbTitle.size(); i++){
                    GsmArenaPageSubsection sec = new GsmArenaPageSubsection();
                    sec.setTitle(cleanParagraph(articleBlurbTitle.get(i).html()));
//                    System.out.println(sec.getTitle());
                    Elements bullets = articleBlurb.get(i).getElementsByTag("li");
                    for(Element e : bullets){
//                        System.out.println(e.html());
                        sec.addParagraph(cleanParagraph(e.html()));
                    }
                    page.addSubsection(sec);
                }
            }
            
            GsmArenaPageSubsection subsec = null;
            
            for(Element e : reviewBody.get(0).children()){
                if(e.hasAttr("class")){
                    continue;
                }
                if(e.tagName().equals("h3")){
                    if(subsec != null){
                        page.addSubsection(subsec);
                    }
                    subsec = new GsmArenaPageSubsection();
                    subsec.setTitle(cleanParagraph(e.html()));
//                    System.out.println("H3 "+e.html());
                }
                if(e.tagName().equals("p")){
//                    System.out.println("P "+cleanParagraph(e.html()));
                    if(subsec == null){
                        subsec = new GsmArenaPageSubsection();
                        subsec.setTitle("noTitleParsed");
                    }
                    subsec.addParagraph(cleanParagraph(e.html()));
                }
            }
            if(subsec != null) page.addSubsection(subsec);
        }
        
        return page;
    }
    
    public static GsmArenaReview getReview(String urlToPage) throws IOException{
        GsmArenaReview review = new GsmArenaReview();
        
        review.setLink(urlToPage);
        
        if (urlToPage == null || urlToPage.length() == 0) return review;
		
        Document doc = Jsoup.connect(urlToPage)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
                    .get();
        
        Elements articleInfoSubtitle = doc.getElementsByClass("article-info-subtitle");
        if(
                articleInfoSubtitle.size() > 0 &&
                articleInfoSubtitle.get(0) != null &&
                articleInfoSubtitle.get(0).html().length() > 0){
            //System.out.println(articleInfoSubtitle.get(0).html());
            review.setDescription(cleanParagraph(articleInfoSubtitle.get(0).html()));
        }
        
        Elements articleInfoName = doc.getElementsByClass("article-info-name");
        if(
                articleInfoName.size() > 0 &&
                articleInfoName.get(0) != null &&
                articleInfoName.get(0).html().length() > 0){
//            System.out.println(articleInfoName.get(0).html());
            review.setTitle(cleanParagraph(articleInfoName.get(0).html()));
        }
        
        Elements pageOptions = doc.getElementsByClass("page-options");
        if(
                pageOptions.size() > 0 &&
                pageOptions.get(0) != null &&
                pageOptions.get(0).html().length() > 0){
            int numberOfPages = 2;
            
            int slashIndex = urlToPage.lastIndexOf("/");
            String relativeLink = urlToPage.substring(slashIndex+1,urlToPage.length()-4);
            
            //System.out.println(relativeLink);
            
            while(pageOptions.get(0).html().contains(relativeLink+"p"+numberOfPages+".php")){
                numberOfPages++;
            }
            numberOfPages--;
            
//            System.out.println(numberOfPages);
            for(int i=1; i<=numberOfPages; i++){
                //System.out.println(urlToPage.substring(0,urlToPage.length()-4)+"p"+i+".php");
                review.addPage(getReviewPage(urlToPage.substring(0,urlToPage.length()-4)+"p"+i+".php"));
                
                //break;
            }
        }
       
        return review;
    }
        
    public static void main(String[] args) throws IOException, InterruptedException{
//        System.out.println(getReview("http://www.gsmarena.com/huawei_nova_plus-review-1494.php"));
//        System.out.println(getReview("http://www.gsmarena.com/lg_v20_hands_on-review-1490.php"));
//        System.out.println(getReview("http://www.gsmarena.com/android_v70_nougat-review-1491.php"));
            
//          System.out.println(getReview("http://www.gsmarena.com/asus_padfone_x-review-1105.php"));
//          System.out.println(getReview("http://www.gsmarena.com/motorola_moto_x_play-review-1325.php"));
//           PrintWriter err = new PrintWriter(new File("GsmArenaLinkErrors2.txt"));
//           Scanner sc = new Scanner(new File("GsmArenaTimeSaverLinks.txt"));
//           
//           String link = "";
//           
//           int contor = 3000;
//           
//           while(sc.hasNextLine() && (link = sc.nextLine())!=null){
//               PrintWriter pw = new PrintWriter(new File("GsmArenaReviews/Review"+contor+".txt"));
//               contor++;
//               try{
//                   pw.println(getReview(link));
//               }catch(Exception e){
//                   System.out.println("Error at review "+ (contor-1)+".");
//                   err.println(link);
//                   pw.close();
//                   continue;
//               }
//               System.out.println("Succes at review "+ (contor-1)+".");
//               pw.close();
//               Thread.currentThread().sleep(2000);
//           }
//           sc.close();
//           err.close();

        PrintWriter pw = new PrintWriter(new File("Review1455.txt"));
        pw.println(getReview("http://www.gsmarena.com/apple_iphone_7-review-1497.php"));
        pw.close();
    }
    
}
