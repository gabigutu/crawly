package main.java.crawlers;

import java.sql.Date;

/**
 * @author Gabriel Gutu
 *
 */
public class Review {

	private Double score = -1.0;
	private String shortDescription;	
	private String username;
	private Date date;
	private boolean official;
	private String link;
	private String fullReview;
	private Double useful;
	private String country;
	
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isOfficial() {
		return official;
	}
	public void setOfficial(boolean official) {
		this.official = official;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getFullReview() {
		return fullReview;
	}
	public void setFullReview(String fullReview) {
		this.fullReview = fullReview;
	}
	public Double getUseful() {
		return useful;
	}
	public void setUseful(Double useful) {
		this.useful = useful;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public String toString() {
		return "Review [score=" + score + ",\n shortDescription=" + shortDescription + ",\n username=" + username
				+ ",\n date=" + date + ",\n official=" + official + ",\n link=" + link + ",\n fullReview=" + fullReview
				+ ",\n useful=" + useful + ",\n country=" + country + "]\n";
	}
	
}
