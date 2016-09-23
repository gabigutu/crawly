/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.gsmarena;

import java.util.ArrayList;

/**
 *
 * @author ReaderBench
 */
public class GsmArenaReview {
    private String title;
    private String description;
    private ArrayList<GsmArenaReviewPage> pages;
    private String link;
    
    public String getLink(){
        return link;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public GsmArenaReview(){
        this.pages = new ArrayList<GsmArenaReviewPage>();
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the pages
     */
    public ArrayList<GsmArenaReviewPage> getPages() {
        return pages;
    }

    /**
     * @param pages the pages to set
     */
    public void setPages(ArrayList<GsmArenaReviewPage> pages) {
        this.pages = pages;
    }
    
    public void addPage(GsmArenaReviewPage page){
        pages.add(page);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        
        sb.append("<review>\n");
        
        sb.append("\t<title>"+title+"</title>\n");
        
        sb.append("\t<description>"+description+"</description>\n");
        
        sb.append("\t<link>"+link+"</link>\n");
        
        for(GsmArenaReviewPage page : pages){
            sb.append("\t<page>\n");
            
            for(GsmArenaPageSubsection sec : page.getSubsections()){
                sb.append("\t\t<subsection>\n");
                
                sb.append("\t\t\t<subsectionTitle>"+sec.getTitle()+"<subsectionTitle>\n");
                
                for(String text : sec.getParagraphs()){
                    sb.append("\t\t\t<paragraph>"+text+"</paragraph>\n");
                }
                
                sb.append("\t\t</subsection>\n");
            }
            
            sb.append("\t</page>\n");
        }
        
        sb.append("</review>");
        
        return sb.toString();
    }
}
