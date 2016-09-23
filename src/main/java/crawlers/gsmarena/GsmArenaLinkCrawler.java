/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.gsmarena;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.sleepycat.asm.Attribute;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import scala.tools.nsc.interactive.Pickler.TildeDecorator;
import scala.xml.Elem;

/**
 *
 * @author ReaderBench
 */
public class GsmArenaLinkCrawler {
    
    public static ArrayList<String> crawlLinkPage(String urlToPage) throws IOException{
        ArrayList<String> links = new ArrayList<String>();
        
        if (urlToPage == null || urlToPage.length() == 0) return links;
		
        Document doc = Jsoup.connect(urlToPage)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
                    .get();
        
        Elements reviewItemTitle = doc.getElementsByClass("review-item-title");
        
        if(reviewItemTitle.size() > 0){
//            System.out.println(reviewItemTitle.size());
            reviewItemTitle.stream().map((e) -> e.getElementsByTag("a")).filter((link) -> (
                link.size() > 0 &&
                link.get(0) != null &&
                link.get(0).html().length() > 0 &&
                link.get(0).hasAttr("href"))).forEach((link) -> {
                    //System.out.println(link.get(0).attr("href"));
                    links.add("http://www.gsmarena.com/"+link.get(0).attr("href"));
            });
        }
        
        return links;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        PrintWriter pw = new PrintWriter(new File("GsmArenaReviewLinks.txt"));
        
        for(int i=1; i<51; i++){
            ArrayList<String> links = crawlLinkPage("http://www.gsmarena.com/reviews.php3?iPage="+i);
            links.stream().forEach((link) -> {
                pw.println(link);
            });
        }
        
        pw.close();
    }
    
}
