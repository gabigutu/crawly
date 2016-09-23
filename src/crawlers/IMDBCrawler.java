package crawlers;

import main.java.crawlers.Movie;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IMDBCrawler {
	
	public static ArrayList<Movie> getTop250IMDB() throws IOException{
		Document doc = Jsoup.connect("http://www.imdb.com/chart/top")
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		Elements listerList = doc.getElementsByClass("lister-list");
		
		if(
				listerList.size() > 0 &&
				listerList.get(0) != null &&
				listerList.get(0).html().length() > 0 &&
				listerList.get(0).children().size() == 250){
			
			for(Element e : listerList.get(0).children()){
				Movie m = new Movie();
				//System.out.println(e.toString());
				result.add(m);
				
				Elements title = e.getElementsByClass("titleColumn");
				if(
						title.size() > 0 &&
						title.get(0) != null &&
						title.get(0).html().length() > 0){
					//System.out.println(title.get(0).html());
					
					//Title & Link
					Elements titleElement = title.get(0).getElementsByTag("a");
					if(
							titleElement.size() > 0 &&
							titleElement.get(0) != null &&
							titleElement.get(0).html().length() > 0){
						//System.out.println(titleElement.get(0).html());
						m.setTitle(titleElement.get(0).html());
						
						//Link
						if(titleElement.get(0).hasAttr("href")){
							//System.out.println(titleElement.get(0).attr("href"));
							m.setLink("http://www.imdb.com"+titleElement.get(0).attr("href"));
						}
					}
					
					//Year
					Elements yearElement = title.get(0).getElementsByClass("secondaryInfo");
					if(
							yearElement.size() > 0 &&
							yearElement.get(0) != null &&
							yearElement.get(0).html().length() > 0){
						//System.out.println(yearElement.get(0).html());
						String year = yearElement.get(0).html();
						
						//De luat de la genre page
						//m.setYear(Integer.parseInt(year.substring(1, year.length()-1)));
					}
					
					//IMDBMovieCrawler crawler = new IMDBMovieCrawler();
					
					//CULEGERE REVIEW-URI!
					//m.setReviews(crawler.getMovieReviews(m.getLink()));
				}
				
				Elements generalScore = e.getElementsByClass("imdbRating");
				if(
						generalScore.size() > 0 &&
						generalScore.get(0) != null &&
						generalScore.get(0).html().length() > 0){
					//System.out.println(statistics.get(0).html());
					Elements score = generalScore.get(0).getElementsByTag("strong");
					if(
							score.size() > 0 &&
							score.get(0) != null &&
							score.get(0).html().length() > 0){
						//System.out.println(score.get(0).html());
						m.setGeneralScore(Double.parseDouble(score.get(0).html()) / 10);
					}
				}
				
				//break;
			}
		}
		
		return result;
	}
	
	public static ArrayList<Movie> getLamePage(String url) throws IOException{
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements listerList = doc.getElementsByClass("lister-list");
		
		if(
				listerList.size() > 0 &&
				listerList.get(0) != null &&
				listerList.get(0).html().length() > 0 &&
				listerList.get(0).children().size() >=49){
			
			for(Element e : listerList.get(0).children()){
				//System.out.println(e.toString());
				Movie m = new Movie();
				result.add(m);
				
				Elements titleYearLink = e.getElementsByTag("h3");
				if(
						titleYearLink.size() > 0 &&
						titleYearLink.get(0) != null &&
						titleYearLink.get(0).html().length() > 0){
					//System.out.println(titleYearLink.get(0).html());
					Elements title = titleYearLink.get(0).getElementsByTag("a");
					if(
							title.size() > 0 &&
							title.get(0) != null &&
							title.get(0).html().length() > 0){
						//System.out.println(title.get(0).html());
						m.setTitle(title.get(0).html());
						
						if(title.get(0).hasAttr("href")){
							//System.out.println(title.get(0).attr("href"));
							m.setLink("http://www.imdb.com"+title.get(0).attr("href"));
						}
					}
					
					Elements year = titleYearLink.get(0).getElementsByClass("text-muted");
					if(
							year.size() > 0 &&
							year.get(0) != null &&
							year.get(0).html().length() > 0){
						//System.out.println(year.get(0).html());
						String yearNumber = year.get(0).html();
						
						//De luat de la genre page
						//m.setYear(Integer.parseInt(yearNumber.substring(1, yearNumber.length()-1)));
					}
				}
				
				Elements generalScore = e.getElementsByClass("ratings-imdb-rating");
				if(
						generalScore.size() > 0 &&
						generalScore.get(0) != null &&
						generalScore.get(0).hasAttr("data-value")){
					//System.out.println(generalScore.get(0).attr("data-value"));
					m.setGeneralScore(Double.parseDouble(generalScore.get(0).attr("data-value")) / 10);
				}
				
				//IMDBMovieCrawler crawler = new IMDBMovieCrawler();
				
				//CULEGERE REVIEW-URI!
				//m.setReviews(crawler.getMovieReviews(m.getLink()));
				
				//break;
			}
			
		}
		
		return result;
	}
	
	public static ArrayList<Movie> getLame250IMDB() throws IOException{
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		result.addAll(getLamePage("http://www.imdb.com/search/title?groups=bottom_250&lists=!watchlist&sort=user_rating,asc"));
		result.addAll(getLamePage("http://www.imdb.com/search/title?groups=bottom_250&lists=%21watchlist&sort=user_rating,asc&page=2&ref_=adv_nxt"));
		result.addAll(getLamePage("http://www.imdb.com/search/title?groups=bottom_250&lists=%21watchlist&sort=user_rating,asc&page=3&ref_=adv_nxt"));
		result.addAll(getLamePage("http://www.imdb.com/search/title?groups=bottom_250&lists=%21watchlist&sort=user_rating,asc&page=4&ref_=adv_nxt"));
		result.addAll(getLamePage("http://www.imdb.com/search/title?groups=bottom_250&lists=%21watchlist&sort=user_rating,asc&page=5&ref_=adv_nxt"));
		
		return result;
	}
	
	public static ArrayList<Movie> crawlGenrePage(String url) throws IOException{
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements listerList = doc.getElementsByClass("lister-list");
		
		if(
				listerList.size() > 0 &&
				listerList.get(0) != null &&
				listerList.get(0).html().length() > 0){
			//System.out.println(listerList.get(0).children().size());
			for(Element e : listerList.get(0).children()){
				Movie m = new Movie();
				result.add(m);
				
				//Name, Link, Year
				Elements listerItemHeader = e.getElementsByClass("lister-item-header");
				if(
						listerItemHeader.size() > 0 &&
						listerItemHeader.get(0) != null &&
						listerItemHeader.get(0).html().length() > 0){
					
					//Name & Link of movie
					Elements link = listerItemHeader.get(0).getElementsByTag("a");
					if(
							link.size() > 0 &&
							link.get(0) != null && 
							link.get(0).html().length() > 0){
						m.setTitle(link.get(0).html());
						//System.out.println(m.getTitle());
						if(link.get(0).hasAttr("href")){
							m.setLink("http://www.imdb.com"+link.get(0).attr("href"));
							//System.out.println(m.getLink());
						}
					}
					
					Elements year = listerItemHeader.get(0).getElementsByClass("lister-item-year");
					if(
							year.size() > 0 &&
							year.get(0) != null &&
							year.get(0).html().length() > 0){
						String text = "";
						StringTokenizer st = new StringTokenizer(year.get(0).html(), "() ",false);
						while(st.hasMoreTokens()){
							String chunk = st.nextToken(); 
							if(Character.isDigit(chunk.charAt(0)) && Character.isDigit(chunk.charAt(chunk.length()-1))){
								text = chunk;
							}
						}
						m.setYear(Integer.parseInt(text));
						//System.out.println(m.getYear());
					}
				}
				
				//General Score
				Elements generalScore = e.getElementsByClass("ratings-imdb-rating");
				if(
						generalScore.size() > 0 &&
						generalScore.get(0) != null){
					if(generalScore.get(0).hasAttr("data-value")){
						//System.out.println(generalScore.get(0).attr("data-value"));
						m.setGeneralScore(Double.parseDouble(generalScore.get(0).attr("data-value")) / 10);
					}
				}
				
				//CRAWL!!!
//				IMDBMovieCrawler crawler = new IMDBMovieCrawler();
//				m.setReviews(crawler.getMovieReviews(m.getLink()));
			
			}
		}
		
		return result;
	}
	
	public static ArrayList<Movie> getGenrePageExtended(String url) throws IOException{
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements article = doc.getElementsByClass("article");
		if(
				article.size() > 0 &&
				article.get(0) != null &&
				article.get(0).html().length() > 0){
			Elements seeMore = article.get(0).getElementsByClass("see-more");
			if(
					seeMore.size() > 0 &&
					seeMore.get(0) != null &&
					seeMore.get(0).html().length() > 0){
				Elements link = seeMore.get(0).getElementsByTag("a");
				if(
						link.size() > 0 &&
						link.get(0) != null &&
						link.get(0).html().length() > 0){
					//System.out.println(link.get(0).html());
					if(link.get(0).hasAttr("href")){
						//System.out.println(link.get(0).attr("href"));
						result.addAll(crawlGenrePage("http://www.imdb.com"+link.get(0).attr("href").replace("moviemeter,asc", "num_votes,desc")));
					}
				}
			}
		}
		
		return result;
	}
	
	public static ArrayList<Movie> getMostPopularByGenre() throws IOException{
		ArrayList<Movie> result = new ArrayList<Movie>();
		
		Document doc = Jsoup.connect("http://www.imdb.com/genre/?ref_=nv_ch_gr_3")
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements genreTabel = doc.getElementsByClass("genre-table");
		
		if(
				genreTabel.size() > 0 &&
				genreTabel.get(0) != null &&
				genreTabel.get(0).html().length() > 0){
			Elements headers = genreTabel.get(0).getElementsByTag("h3");
			for(Element e : headers){
				Elements link = e.getElementsByTag("a");
				if(
						link.size() > 0 &&
						link.get(0) != null){
					if(link.get(0).hasAttr("href")){
						//System.out.println(link.get(0).attr("href"));
						result.addAll(getGenrePageExtended(link.get(0).attr("href")));
					}
				}
				
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException{
//		ArrayList<Movie> movies1 = getTop250IMDB(); //DONE
//		ArrayList<Movie> movies2 = getLame250IMDB(); //DONE
		ArrayList<Movie> movies3 = getMostPopularByGenre();
		
//		PrintWriter pw1 = new PrintWriter(new File("Top250IMDB.txt"));
//		PrintWriter pw2 = new PrintWriter(new File("Lame249IMDB.txt"));
		PrintWriter pw3 = new PrintWriter(new File("MostReviewed50ByEachGenreIMDB.txt"));
		
//		for(Movie m : movies1){
//			pw1.println(m.getLink());
//		}
		
//		for(Movie m : movies2){
//			pw2.println(m.getLink());
//		}
		
		for(Movie m : movies3){
			pw3.println(m.getLink());
		}
		
//		pw1.close();
//		pw2.close();
		pw3.close();
	}
	
}
