package crawlers;

import java.util.ArrayList;

public class Movie {
	
	private String title;
	private String link;
	private ArrayList<Review> reviews;
	private Integer year;
	private Double generalScore = -1.0;
	
	public Double getGeneralScore() {
		return generalScore;
	}
	public void setGeneralScore(Double generalScore) {
		this.generalScore = generalScore;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder("<movie>\n" + 
				 			"\t<title>"+title+"</title>\n" +
				 			"\t<link>"+link+"</link>\n"+
				 			"\t<year>"+year+"</year>\n"+
				 			"\t<generalScore>"+generalScore+"</generalScore>\n");
		
		if(reviews != null){
			for(Review r : reviews){
				result.append("\t<review>\n"+
								"\t\t<date>"+r.getDate()+"</date>\n"+
								"\t\t<username>"+r.getUsername()+"</username>\n"+
								"\t\t<score>"+r.getScore()+"</score>\n"+
								"\t\t<shortDescription>"+r.getShortDescription()+"</shortDescription>\n"+
								"\t\t<fullReview>"+r.getFullReview()+"</fullReview>\n"+
								"\t</review>\n");
			}
		}
		
		result.append("</movie>");
		
		return result.toString();
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
}
