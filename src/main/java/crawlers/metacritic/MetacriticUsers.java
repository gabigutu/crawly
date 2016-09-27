package main.java.crawlers.metacritic;

import main.java.crawlers.Review;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import main.java.crawlers.AbstractCrawler;
import main.java.crawlers.Review;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * @author Gabriel Gutu
 *
 */
public class MetacriticUsers extends AbstractCrawler {
	
	public MetacriticUsers() {
		super();
		setName("Metacritic");
		setMaxScore(10.0);
	}

	@Override
	public ArrayList<String> crawl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Review> parse(String url) throws IOException {		
		if (url == null || url.length() == 0) return null;
		
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com")
				.get();
		Elements reviewsElements = doc.select(".review");
		
		for(Element reviewElement : reviewsElements) {
			Review review = new Review();
			
			// get user's rating
			Elements scoreElements = reviewElement.getElementsByClass("metascore_w");
			Double rating = 0.0;
			if (
					scoreElements.size() > 0 &&
					scoreElements.get(0).html() != null &&
					scoreElements.get(0).html().length() > 0) {
				rating = Double.parseDouble(scoreElements.get(0).html()) / maxScore;
			}
			review.setScore(rating);
			//System.out.println(rating);
			
			// get user's review
			Elements textElements = reviewElement.getElementsByClass("review_body");
			String text = "";
			if (textElements.size() > 0) {
				text = textElements.get(0).html();
				//System.out.println(text);
			}
			//review.setText(Jsoup.clean(text, Whitelist.simpleText()));
			
			
			// get review's date
			Elements dateElements = reviewElement.getElementsByClass("date");
			// TODO: convert string to date
			
			if(
					dateElements.size() > 0 &&
					dateElements.get(0).html() != null &&
					dateElements.get(0).html().length() > 0){
				String dateText = dateElements.get(0).html();
				//System.out.println(dateText);
				StringTokenizer stdt = new StringTokenizer(dateText, " ,");
				
				String month = stdt.nextToken();
				String day = stdt.nextToken();
				String year = stdt.nextToken();
				
				String monthNumber = "";
				switch(month){
					case "Jan": monthNumber = "1";
								break;
					case "Feb": monthNumber = "2";
								break;	
					case "Mar": monthNumber = "3";
								break;
					case "Apr": monthNumber = "4";
								break;
					case "May": monthNumber = "5";
								break;
					case "Jun": monthNumber = "6";
								break;
					case "Jul": monthNumber = "7";
								break;	
					case "Aug": monthNumber = "8";
								break;
					case "Sep": monthNumber = "9";
								break;
					case "Oct": monthNumber = "10";
								break;
					case "Nov": monthNumber = "11";
								break;
					case "Dec": monthNumber = "12";
								break;
					default: 	monthNumber = "Invalid month";
								break;
				}
				
				String date = monthNumber + "." + day + "." + year;
				SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
				java.util.Date timeOfReview = null;
				
				try {
					timeOfReview = sdf.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				review.setDate( new java.sql.Date(timeOfReview.getTime()));
				//System.out.println(review.getDate());
			}
			
			// TODO: parse username (or website)
			Elements sourceElements = reviewElement.getElementsByClass("name");
			if(
					sourceElements.size() > 0 &&
					sourceElements.get(0).html() != null &&
					sourceElements.get(0).html().length() > 0){
				Elements userElements = sourceElements.get(0).getElementsByTag("a");
				if(
						userElements.size() > 0 &&
						userElements.get(0).html() != null &&
						userElements.get(0).html().length() > 0){
					System.out.println("Autor: "+userElements.get(0).html());
					String user = userElements.get(0).html();
					review.setUsername(user);
				}
			}
			
			Elements descriptionElements = reviewElement.getElementsByTag("span");
			if(
					descriptionElements.size() > 0 &&
					descriptionElements.get(0).html() != null &&
					descriptionElements.get(0).html().length() > 0){
				System.out.println(descriptionElements.get(0).childNodeSize());
				if(descriptionElements.get(0).childNodeSize() > 1){
					//If comment needs to be expanded.
					Elements descriptionContentElements = descriptionElements.get(0).getElementsByClass("blurb_expanded");
					//System.out.println(descriptionContentElements.size());
					if(
							descriptionContentElements.size() > 0 &&
							descriptionContentElements.get(0).html() != null &&
							descriptionContentElements.get(0).html().length() > 0){
						System.out.println("Text: "+descriptionContentElements.get(0).html());
						review.setFullReview(descriptionContentElements.get(0).html());
					}
				}else{
					String description = descriptionElements.get(0).html();
					System.out.println("Text: "+description);
					review.setFullReview(description);
				}
			}
			
			review.setOfficial(false);			
			
			reviews.add(review);
		}
		
		return reviews;
	}
	
	public static void main(String[] args){
		MetacriticUsers m = new MetacriticUsers();
		try {
			m.parse("http://www.metacritic.com/tv/house-of-cards-2013/user-reviews");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void setName(String metacritic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setMaxScore(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Review> parseIndexedLinks() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
