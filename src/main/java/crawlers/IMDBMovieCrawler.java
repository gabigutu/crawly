package main.java.crawlers;

import main.java.crawlers.Movie;
import main.java.crawlers.Review;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.sleepycat.asm.Attribute;
import java.io.FileNotFoundException;
import java.util.HashSet;

import scala.tools.nsc.interactive.Pickler.TildeDecorator;
import scala.xml.Elem;

public class IMDBMovieCrawler extends AbstractCrawler{
	
	public IMDBMovieCrawler() {
		super();
		setName("IMDB");
		setMaxScore(10.0);
	}
	
	@Override
	public ArrayList<String> crawl(String url) {
		return null;
	}

	@Override
	public ArrayList<Review> parse(String url) throws IOException {
		if (url == null || url.length() == 0) return null;
		
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements lista = doc.getElementsByAttributeValue("id", "tn15content");
		
		if(
				lista.size() > 0 &&
				lista.get(0).html() != null &&
				lista.get(0).html().length() > 0){
			Element CorpPrincipal = lista.get(0);

			Elements children = CorpPrincipal.children();
			
			Elements revs = new Elements();
			Elements auts = new Elements();
			
			for(int i=0; i < children.size()-1; i++){
				if(
						children.get(i).tagName().equals("div") &&
						children.get(i+1).tagName().equals("p")){
					auts.add(children.get(i));
					revs.add(children.get(i+1));
				}
			}
			//System.out.println(revs.size() + " " + auts.size());
			
			ArrayList<Review> result = new ArrayList<Review>();
			for(int i=0; i<revs.size(); i++){
				result.add(new Review());
			}
			
			for(int i=0; i<revs.size(); i++){
				if( 
						revs.get(i) != null && 
						revs.get(i).html().length() > 0 ){
					//System.out.println(revs.get(i).html());
					result.get(i).setFullReview(revs.get(i).html().replace("<br>", ""));
				}
				if(
						auts.get(i) != null &&
						auts.get(i).html().length() > 0){
					
					//Probleme la useful
//					Elements useful = auts.get(i).getElementsByTag("small");
//					//System.out.println(useful.get(0).html());
//					if(
//							useful.size() > 0 &&
//							useful.get(0) != null && 
//							useful.get(0).html().length() > 0){
//						StringTokenizer st = new StringTokenizer(useful.get(0).html(), " ");
//						int a = Integer.parseInt(st.nextToken());
//						st.nextToken();
//						st.nextToken();
//						int b = Integer.parseInt(st.nextToken());
//						result.get(i).setUseful(1.0 * a / b);
//						//System.out.println(result[i].getUseful());
//					}
					
					Elements title = auts.get(i).getElementsByTag("h2");
					if(
							title.size() > 0 &&
							title.get(0) != null && 
							title.get(0).html().length() > 0){
						//System.out.println(title.get(0).html());
						result.get(i).setShortDescription(title.get(0).html());
					}
					
					Elements imgScore = auts.get(i).getElementsByTag("img");
					if(
							imgScore.size() > 1 &&
							imgScore.get(1) != null){
						//System.out.println(imgScore.get(1).toString());
						Attributes atrb = imgScore.get(1).attributes();
						if(atrb.hasKey("alt")){
							//System.out.println(atrb.get("alt"));
							StringTokenizer st = new StringTokenizer(atrb.get("alt"), "/");
							int a = Integer.parseInt(st.nextToken());
							int b = Integer.parseInt(st.nextToken());
							result.get(i).setScore(1.0 * a / b);
							//System.out.println(result[i].getScore());
						}
					}
					
					//Autor
					Elements autor = auts.get(i).getElementsByTag("a");
					if(
							autor.size() > 1 &&
							autor.get(1) != null &&
							autor.get(1).html().length() > 0){
						//System.out.println(autor.get(1).html());
						result.get(i).setUsername(autor.get(1).html());
					}
					
					//Tara Probleme
//					Elements tara = auts.get(i).getElementsByTag("small");
//					if(
//							tara.size() > 1 &&
//							tara.get(1) != null &&
//							tara.get(1).html().length() > 0){
//						//System.out.println(tara.get(1).html().substring(5));
//						result.get(i).setCountry(tara.get(1).html().substring(5));
//					}
					
					//Data
					Elements datadata = auts.get(i).getElementsByTag("small");
					Element data = null;
					for(Element e : datadata){
						String text = e.html();
						boolean ok = text.contains("January") || text.contains("February") || text.contains("March") || text.contains("April") ||
								text.contains("May") || text.contains("June") || text.contains("July") || text.contains("August") ||
								text.contains("September") || text.contains("October") || text.contains("November") || text.contains("December");
						if(ok){
							data = e;
						}
					}
					if(
							data != null &&
							data.html().length() > 0){
						//System.out.println(data.get(2).html());
						String dateText = data.html();
						//System.out.println(dateText);
						StringTokenizer stdt = new StringTokenizer(dateText, " ,");
						
						String day = stdt.nextToken();
						String month = stdt.nextToken();
						String year = stdt.nextToken();
						
						String monthNumber = "";
						switch(month){
							case "January": monthNumber = "1";
										break;
							case "February": monthNumber = "2";
										break;	
							case "March": monthNumber = "3";
										break;
							case "April": monthNumber = "4";
										break;
							case "May": monthNumber = "5";
										break;
							case "June": monthNumber = "6";
										break;
							case "July": monthNumber = "7";
										break;	
							case "August": monthNumber = "8";
										break;
							case "September": monthNumber = "9";
										break;
							case "October": monthNumber = "10";
										break;
							case "November": monthNumber = "11";
										break;
							case "December": monthNumber = "12";
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
						
						result.get(i).setDate( new java.sql.Date(timeOfReview.getTime()));
						//System.out.println(result[i].getDate());
					}
				}
			}
			return result;
		}
		
		
		return new ArrayList<Review>();
	}
	
	//Buna dar am descoperit hardcodare!!!
//	public ArrayList<Review> selectReviewPage(String url) throws IOException{
//		if (url == null || url.length() == 0) return null;
//		
//		Document doc = Jsoup.connect(url)
//				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//			    .referrer("http://www.google.com")
//				.get();
//		
//		Elements navigation = doc.getElementsByClass("subnav");
//		
//		//System.out.println(navigation.size());
//		
//		if(
//				navigation.size() > 0 &&
//				navigation.get(0) != null &&
//				navigation.get(0).html().length() > 0){
//			Elements quickNav = navigation.get(0).getElementsByClass("quicklink");
//			//System.out.println(quickNav.size());
//			
//			Element userReviews = null;
//			for(Element e : quickNav){
//				if(e.html().equals("USER REVIEWS")){
//					userReviews = e;
//					break;
//				}
//			}
//			//System.out.println(userReviews.html());
//			
//			Attributes atrb = userReviews.attributes();
//			if(atrb.hasKey("href")){
//				//System.out.println(atrb.get("href"));
//				
//			}
//		}
//		
//		return null;
//	}
	
	public int getNumberOfReviews(String url) throws IOException{
		
		if (url == null || url.length() == 0) return 0;
		
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements region = doc.getElementsByAttributeValue("id", "tn15content");
		
		if(
				region.size() > 0 &&
				region.get(0).html() != null &&
				region.get(0).html().length() > 0){
			Elements candidates = region.get(0).getElementsByAttributeValue("border", "0");
			//System.out.println(candidates.size());
			
			Element found = null;
			for(Element e : candidates){
//				System.out.println(e.toString());
//				System.out.println();
				if(e.getElementsByTag("td").size() > 0){
					found = e;
				}
			}
			
			if(found == null) return 0;
			
			Elements number = found.getElementsByAttributeValue("align", "right");
			if(
					number.size() > 0 &&
					number.get(0) != null &&
					number.get(0).html().length() > 0){
				//System.out.println(number.get(0).toString());
				StringTokenizer st = new StringTokenizer(number.get(0).html(), " ");
				return Integer.parseInt(st.nextToken());
			}
		}
		
		return 0;
	}
	
	public String processTitle(String url){
		while(!url.endsWith("/") && url.length() > 0){
			url = url.substring(0, url.length()-1);
		}
		return url;
	}
	
	public ArrayList<Review> getMovieReviews(String urlToTitle) throws IOException, InterruptedException{
		String title = processTitle(urlToTitle);
		int totalNumber = getNumberOfReviews(title + "reviews?start=0");
		
                Thread.currentThread().sleep(1000);
                
		int limit = totalNumber / 10;
		
		ArrayList<Review> result = new ArrayList<Review>();
		
		for(int i=0; i<=limit; i++){
			result.addAll(parse(title + "reviews?start=" + i*10));
			System.out.println((i+1)*10 +" / "+ totalNumber);
			Thread.currentThread().sleep(1000);
		}
		
		//System.out.println(totalNumber);
		
		return result;
	}
	
	public Movie getMovieData(String urlToTitle) throws IOException, InterruptedException{
		Movie m = new Movie();
		m.setLink(urlToTitle);
		
		m.setReviews(getMovieReviews(urlToTitle));
		
                Thread.currentThread().sleep(1000);
                
		if (urlToTitle == null || urlToTitle.length() == 0) return m;
		
		Document doc = Jsoup.connect(urlToTitle)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com").header("Accept-Language", "en-US, en")
				.get();
		
		Elements titleWrapper = doc.getElementsByClass("title_wrapper");
		if(
				titleWrapper.size() > 0 &&
				titleWrapper.get(0) != null &&
				titleWrapper.get(0).html().length() > 0){
			Elements title = titleWrapper.get(0).getElementsByTag("h1");
			if(
					title.size() > 0 &&
					title.get(0) != null &&
					title.get(0).html().length() > 0){
				//System.out.println(title.get(0).html());
				StringTokenizer st = new StringTokenizer(title.get(0).html(), ";");
				//System.out.println(st.nextToken().replace("&nbsp", ""));
				m.setTitle(st.nextToken().replace("&nbsp", ""));
				
				Elements year = title.get(0).getElementsByTag("a");
				if(
						year.size() > 0 &&
						year.get(0) != null &&
						year.get(0).html().length() > 0){
					//System.out.println(year.get(0).html());
					m.setYear(Integer.parseInt(year.get(0).html()));
				}
			}
		}
		
		Elements ratingValue = doc.getElementsByClass("ratingValue");
		if(
				ratingValue.size() > 0 &&
				ratingValue.get(0) != null &&
				ratingValue.get(0).html().length() > 0){
			Elements strong = ratingValue.get(0).getElementsByTag("strong");
			if(
					strong.size() > 0 &&
					strong.get(0) != null &&
					strong.get(0).html().length() > 0){
				Elements score = strong.get(0).getElementsByTag("span");
				if(
						score.size() > 0 &&
						score.get(0) != null &&
						score.get(0).html().length() > 0){
					//System.out.println(score.get(0).html());
					m.setGeneralScore(Double.parseDouble(score.get(0).html()) / 10);
				}
			}
		}
		
		return m;
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		IMDBMovieCrawler crawler = new IMDBMovieCrawler();
		
		File radacina = new File("RemakesIMDB/Input");
		
		int contor = 0;
                
                ArrayList<String> errors = new ArrayList<String>();
                
                ArrayList<String> reDo = new ArrayList<String>();
                Scanner sca = new Scanner(new File("RemakesIMDB/Errors/errFiles.txt"));
                String addr = "";
                while(sca.hasNextLine() && (addr = sca.nextLine())!=null){
                    reDo.add(addr);
                }
                sca.close();
                
                for(File folder : radacina.listFiles()){
                    if(folder.isDirectory()){
                        for(File fisier : folder.listFiles()){
                            if(fisier.isHidden() || fisier.isDirectory()){
                                continue;
                            }
                            if(!reDo.contains(fisier.getAbsolutePath())){
                                contor++;
                                continue;
                            }
                            int movie = 0;
                            Scanner sc = new Scanner(fisier);
//                            File destinatie = new File("RemakesIMDB/Output/Group"+contor);
//                            destinatie.mkdir();
                            
                            String link = "";
                            while(sc.hasNextLine() && (link = sc.nextLine())!=null){
                                PrintWriter pw = new PrintWriter(new File("RemakesIMDB/Output/Group"+contor+"/Movie"+movie+".txt"));
                                movie++;
                                try{
                                    pw.println(crawler.getMovieData(link));
                                }catch(Exception e){
                                    pw.close();
                                    sc.close();
                                    errors.add(fisier.getAbsolutePath());
                                    System.out.println("Movie error: " + fisier.getAbsolutePath());
                                    break;
                                }
                                pw.close();
                            }
                            contor++;
                            sc.close();
                            System.out.println("Movie done: " + fisier.getAbsolutePath());
                        }
                        System.out.println("Folder done: " + folder.getAbsolutePath());
                    }
                }
                
                PrintWriter err = new PrintWriter(new File("RemakesIMDB/Errors/errFiles.txt"));
                for(String text : errors){
                    err.println(text);
                }
                err.close();  
	}

    @Override
    public ArrayList<Review> parseIndexedLinks() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}
