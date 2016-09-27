package main.java.crawlers;

import java.io.IOException;
import java.util.ArrayList;

import helpers.*;

/**
 * @author Gabriel Gutu
 *
 */
public abstract class AbstractCrawler {

	private String name;
	protected ArrayList<Review> reviews;
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
	}
	
	/**
	 * 
	 */
	public AbstractCrawler() {
		this(Constants.CRAWLER_NAME_NULL);
	}
	
	public abstract ArrayList<Review> crawl();
	
	public abstract ArrayList<Review> parse(String url) throws IOException;
	
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
