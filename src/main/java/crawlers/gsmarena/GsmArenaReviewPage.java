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
public class GsmArenaReviewPage {
    private ArrayList<GsmArenaPageSubsection> subsections;
    
    public GsmArenaReviewPage(){
        this.subsections = new ArrayList<GsmArenaPageSubsection>();
    }
    
    /**
     * @return the subsections
     */
    public ArrayList<GsmArenaPageSubsection> getSubsections() {
        return subsections;
    }

    /**
     * @param subsections the subsections to set
     */
    public void setSubsections(ArrayList<GsmArenaPageSubsection> subsections) {
        this.subsections = subsections;
    }
    
    public void addSubsection(GsmArenaPageSubsection subsection){
        subsections.add(subsection);
    }
}
