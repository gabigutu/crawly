/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.sensCritique;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ReaderBench
 */
public class SensCritiqueLinkCrawler {
    
    public static ArrayList<String> crawlPage(String urlToPage) throws IOException{
        ArrayList<String> result = new ArrayList<String>();
        
        if (urlToPage == null || urlToPage.length() == 0) return null;
		
	Document doc = Jsoup.connect(urlToPage).timeout(10*1000)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
            .get();
        
        Elements elcoTitle = doc.getElementsByClass("elco-title");
        
//        System.out.println(elcoTitle.size());
        
        elcoTitle.stream().map(new Function<Element, Elements>() {
            @Override
            public Elements apply(Element e) {
                return e.getElementsByTag("a");
            }
        }).filter(e -> e.hasAttr("href")).forEach(e -> {
//            System.out.println(e.attr("href"));
              result.add("http://www.senscritique.com"+e.attr("href"));
        });
                
        return result;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{      
//        crawlPage("http://www.senscritique.com/films/tops/top111");
        
//        crawlPage("http://www.senscritique.com/top/resultats/Les_meilleurs_albums_de_2016/1176005/page-1.ajax?limit=1000").forEach((String link) -> {
//            pw.println(link);
//        });
        
        Scanner sc = new Scanner(new File("SensCritique/SensCritiqueMegaPack/Musique/MusiqueMegaPack.txt"));
        PrintWriter err = new PrintWriter(new File("SensCritique/SensCritiqueMegaPack/Musique/Musique_MG_LinkErrors1.txt"));
        
        //DE Facut erorile!!! ->
        
        String link = "";
        int contor = 0;
        while(sc.hasNextLine() && (link = sc.nextLine())!=null){
            PrintWriter pw = new PrintWriter(new File("SensCritique/SensCritiqueMegaPack/Musique/MusiqueMegaPackLinksPart"+contor+".txt"));
            contor++;
            try{
                crawlPage(link+"/page-1.ajax?limit=1000").forEach((String line) -> {
                    pw.println(line);
                });
                crawlPage(link+"/page-2.ajax?limit=1000").forEach((String line) -> {
                    pw.println(line);
                });
                System.out.println("Succes at page " + contor + ".");
                Thread.currentThread().sleep(2000);
            }catch(Exception e){
                System.out.println("Error at page " + contor + ".");
                err.println(link);
            }
            pw.close();
        }
        
        sc.close();
        err.close();
    }
    
}
