/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.sensCritique;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author ReaderBench
 */
public class SensCritiqueEntity {
    private String nameEn;
    private String nameFr;
    private String typeEn;
    private String typeFr;
    private int year;
    private String link;
    private Double generalScore;
    private int[] scoreDistribution;
    private ArrayList<Review> reviews;

    public SensCritiqueEntity(){
        scoreDistribution = new int[10];
        reviews = new ArrayList<Review>();
    }

    /**
     * @return the type
     */
    public String getTypeEn() {
        return typeEn;
    }

    /**
     * @param typeEn the type to set
     */
    public void setTypeEn(String typeEn) {
        this.typeEn = typeEn;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the scoreDistribution
     */
    public int[] getScoreDistribution() {
        return scoreDistribution;
    }

    /**
     * @param scoreDistribution the scoreDistribution to set
     */
    public void setScoreDistribution(int[] scoreDistribution) {
        this.scoreDistribution = scoreDistribution;
    }
    
    /**
     * @return the generalScore
     */
    public Double getGeneralScore() {
        return generalScore;
    }

    /**
     * @param generalScore the generalScore to set
     */
    public void setGeneralScore(Double generalScore) {
        this.generalScore = generalScore;
    }

    /**
     * @return the reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews the reviews to set
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
    
    public void addReview(Review review){
        reviews.add(review);
    }
    
    public void addReviews(Collection<Review> reviews){
        this.reviews.addAll(reviews);
    }

    /**
     * @return the nameEn
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn the nameEn to set
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * @return the nameFr
     */
    public String getNameFr() {
        return nameFr;
    }

    /**
     * @param nameFr the nameFr to set
     */
    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        
        sb.append("<sensCritiqueEntity>\n");
        sb.append("\t<nameEn>"+nameEn+"</nameEn>\n");
        sb.append("\t<nameFr>"+nameFr+"</nameFr>\n");
        sb.append("\t<typeEn>"+typeEn+"</typeEn>\n");
        sb.append("\t<typeFr>"+getTypeFr()+"</typeFr>\n");
        sb.append("\t<year>"+year+"</year>\n");
        sb.append("\t<link>"+link+"</link>\n");
        sb.append("\t<generalScore>"+generalScore+"</generalScore>\n");
        
        sb.append("\t<scoreDistribution>\n");
        for(int i =0; i<10; i++){
            sb.append("\t\t<numberOfVoters>"+scoreDistribution[i]+"</numberOfVoters>\n");
        }
        sb.append("\t</scoreDistribution>\n");
        
        reviews.stream().forEach( r -> {
            sb.append("\t<review>\n");
            sb.append("\t\t<title>"+r.getTitle()+"</title>\n");
            sb.append("\t\t<author>"+r.getAuthor()+"</author>\n");
            sb.append("\t\t<date>"+r.getDate()+"</date>\n");
            sb.append("\t\t<score>"+r.getScore()+"</score>\n");
            sb.append("\t\t<content>"+r.getContent()+"</content>\n");
            sb.append("\t</review>\n");
        });
        
        sb.append("</sensCritiqueEntity>");
        
        return sb.toString();
    }

    /**
     * @return the typeFr
     */
    public String getTypeFr() {
        return typeFr;
    }

    /**
     * @param typeFr the typeFr to set
     */
    public void setTypeFr(String typeFr) {
        this.typeFr = typeFr;
    }
}
