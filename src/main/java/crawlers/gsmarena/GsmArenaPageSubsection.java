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
class GsmArenaPageSubsection {
    private String title;
    private ArrayList<String> paragraphs;
    
    public GsmArenaPageSubsection(){
        paragraphs = new ArrayList<String>();
    }
    
    public String getTitle(){
        return title;
    }
    
    public ArrayList<String> getParagraphs(){
        return paragraphs;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setParagraphs(ArrayList<String> paragraphs){
        this.paragraphs = paragraphs;
    }
    
    public void addParagraph(String paragraph){
        paragraphs.add(paragraph);
    }
}
