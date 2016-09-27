package main.java.crawlers;

import java.io.IOException;
import java.util.ArrayList;

import helpers.*;
import org.jsoup.Connection;

/**
 * @author Gabriel Gutu
 *
 */
public abstract class AbstractCrawler {

	private String name;
	protected ArrayList<Review> reviews;
    protected ArrayList<String> indexedLinks;
	protected Double maxScore;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

    public ArrayList<String> getLinks() {
        return indexedLinks;
    }

    public void setLinks(ArrayList<String> links) {
        this.indexedLinks = links;
    }
    
    public Double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * @param name
	 */
	public AbstractCrawler(String name) {
		this.name = name;
		reviews = new ArrayList<>();
        indexedLinks = new ArrayList<>();
	}
	
	/**
	 * 
	 */
	public AbstractCrawler() {
		this(Constants.CRAWLER_NAME_NULL);
	}
    
    public Connection setMozillaHeaders(Connection connection) {
        connection
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; "
                        + "rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com");
        return connection;
    }
	
	public abstract ArrayList<String> crawl(String url) throws IOException;
	
	public abstract ArrayList<Review> parse(String url) throws IOException;
    
    public abstract ArrayList<Review> parseIndexedLinks() throws IOException;
	
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Crawler " + name + "\n");
//		for (Review review : reviews) {
//			sb.append("\t==========\n");
//			
//			sb.append("\t");
//			sb.append("Score: " + review.getScore());
//			sb.append("\n");
//			
//			sb.append("\t");
//			sb.append("Text: " + review.getText());
//			sb.append("\n");
//		}
//		return sb.toString();
//	}
	
}
