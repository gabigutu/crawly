package crawlers;

import java.sql.Date;

/**
 * @author Gabriel Gutu
 *
 */
public class Review {

	private Double score;
	private String text;	
	private String username;
	private Date date;
	private boolean official;
	
	public Review() {
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
	
	
	
}
