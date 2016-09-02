package crawlers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

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
public class Metacritic extends AbstractCrawler {
	
	public Metacritic() {
		super();
		setName("Metacritic");
		setMaxScore(100.0);
	}

	@Override
	public ArrayList<Review> crawl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Review parse(String url) throws IOException {		
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
			review.setShortDescription(Jsoup.clean(text, Whitelist.simpleText()));
			
			
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
			Elements sourceElements = reviewElement.getElementsByClass("source");
			if(
					sourceElements.size() > 0 &&
					sourceElements.get(0).html() != null &&
					sourceElements.get(0).html().length() > 0){
				//System.out.println(sourceElements.get(0).html());
				String source = sourceElements.get(0).html();
				if(!source.contains("a rel=\"popup:external\"")){
					review.setUsername(source);
				}else{
					StringTokenizer sts = new StringTokenizer(source,"\" ");
					String link = "";
					while(sts.hasMoreTokens()){
						String token = sts.nextToken();
						if(token.startsWith("http")){
							link = token;
							break;
						}
					}
					//System.out.println(link);
					review.setLink(link);
					
					Elements publicationElements = sourceElements.get(0).getElementsByTag("a");
					if(
							publicationElements.size() > 0 &&
							publicationElements.get(0).html() != null &&
							publicationElements.get(0).html().length() > 0){
						//System.out.println(publicationElements.get(0).html());
						review.setUsername(publicationElements.get(0).html());
					}
				}
			
			}
			
			//fullReview
			
			review.setOfficial(true);
			
			reviews.add(review);
		}
		
		return null;
	}
	
	public static void main(String[] args){
		Metacritic m = new Metacritic();
		try {
			m.parse("http://www.metacritic.com/tv/house-of-cards-2013/critic-reviews");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
