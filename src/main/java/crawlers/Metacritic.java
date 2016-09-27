package main.java.crawlers;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public ArrayList<Review> parse(String url) throws IOException {
        if (url == null || url.length() == 0) {
            return null;
        }

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get();
        Elements reviewsElements = doc.select(".review");

        for (Element reviewElement : reviewsElements) {
            Review review = new Review();

            // get user's rating
            Elements scoreElements = reviewElement.getElementsByClass("metascore_w");
            Double rating = 0.0;
            if (scoreElements.size() > 0
                    && scoreElements.get(0).html() != null
                    && scoreElements.get(0).html().length() > 0) {
                rating = Double.parseDouble(scoreElements.get(0).html()) / maxScore;
            }
            review.setScore(rating);

            // get user's review
            Elements textElements = reviewElement.getElementsByClass("review_body");
            String text = "";
            if (textElements.size() > 0) {
                text = textElements.get(0).html();
            }
            review.setShortDescription(Jsoup.clean(text, Whitelist.simpleText()));

            // get review's date
            Elements dateElements = reviewElement.getElementsByClass("date");
            // TODO: convert string to date

            // TODO: parse username (or website)
            reviews.add(review);
        }

        return reviews;
    }
}
