/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.sensCritique;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author ReaderBench
 */
public class SensCritiqueSondagesCrawler {
    
    public static ArrayList<String> crawlSondagesPage(String urlToPage) throws IOException{
        ArrayList<String> result = new ArrayList<String>();
        
        if (urlToPage == null || urlToPage.length() == 0) return result;
		
	Document doc = Jsoup.connect(urlToPage).timeout(90*1000)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
            .get();
        
        Elements links = doc.getElementsByClass("epca-title");
        
        if(links.size() > 0){
            links.stream().filter(e -> e.hasAttr("href")).forEach(e -> {
//                System.out.println(e.attr("href")); 
                result.add("http://www.senscritique.com"+e.attr("href"));
            });
        }
        
        return result;
    }
        
    public static void main(String[] args) throws IOException, InterruptedException{
//        crawlSondagesPage("http://www.senscritique.com/films/sondages/tous/tous/page-"+125);
        PrintWriter pw = new PrintWriter(new File("SensCritiqueFilmsMegaPackPart9.txt"));
        
        ArrayList<String> result = new ArrayList<String>();
        
        for(int i=81; i<=125; i++){
            result.addAll(crawlSondagesPage("http://www.senscritique.com/films/sondages/tous/tous/page-"+i));
            Thread.currentThread().sleep(1000);
            System.out.println(i);
        }
        
        result.stream().forEach(link -> {
            pw.println(link);
        });
        
        pw.close();
    }
    
}
