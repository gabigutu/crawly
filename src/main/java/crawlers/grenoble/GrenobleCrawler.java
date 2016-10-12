/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.crawlers.grenoble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.crawlers.AbstractCrawler;
import main.java.crawlers.Review;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

/**
 * This class crawls and parses course pages of University of Grenoble
 *
 * @author Gabriel Gutu <gabriel.gutu at cs.pub.ro>
 */
public class GrenobleCrawler extends AbstractCrawler {

    private static final Logger LOGGER = Logger.getLogger(GrenobleCrawler.class.getName());

    public GrenobleCrawler() {
        super();
        setName("Grenoble");
        setMaxScore(100.0);
    }

    @Override
    public ArrayList<String> crawl(String url) throws IOException {
        if (url == null || url.length() == 0) {
            return null;
        }

        LOGGER.info("Crawling " + url + ".");

        Connection connection = Jsoup.connect(url);
        connection = setMozillaHeaders(connection);
        Document doc = connection.get();
        Elements aElements = doc.select("li.toctree-l1 > a");

        for (Element aElement : aElements) {
            StringBuilder sbLink = new StringBuilder();
            sbLink.append(url);
            sbLink.append(aElement.attr("href"));
            indexedLinks.add(sbLink.toString());

            StringBuilder sbLogger = new StringBuilder();
            sbLogger.append(this.getClass().getName());
            sbLogger.append(" discovered new link: ");
            sbLogger.append(sbLink.toString());

            LOGGER.log(Level.INFO, sbLogger.toString());
        }

        return indexedLinks;
    }

    @Override
    public ArrayList<Review> parse(String url) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Review> parseIndexedLinks() throws IOException {

        LOGGER.info("There are " + indexedLinks.size() + " links to be processed.");

        for (String link : indexedLinks) {

            LOGGER.info("Processing link: " + link);

            Connection connection = Jsoup.connect(link);
            connection = setMozillaHeaders(connection);
            Document doc = connection.get();

            Element content = doc.select("div.body > div.section").get(0);

            // document title
            Element h1 = content.select("h1").get(0);

            // document author
            Elements p = content.select("p");
            String author = "";
            if (!p.get(0).select("a").isEmpty()) {
                author = p.get(0).select("a").get(0).text();
            }

            // other meta information are ignored (stored in <p> elements)
            // sections
            Elements sections = content.select("> div.section");

            LOGGER.info("Adding title of document: " + h1.text());
            LOGGER.info("Adding author of document: " + author);
            Directives directivesDocument = new Directives();
            directivesDocument
                    .add("document")
                    .attr("language", "en")
                    .add("title").set(h1.text()).up()
                    .add("authors").add("author").set(author).up().up();

            //Xembler xmlBuilder = new Xembler();
            buildSection(content, directivesDocument, false);
            LOGGER.info("There are " + sections.size() + " sections.");
            for (Element section : sections) {
                buildSection(section, directivesDocument, true);
                directivesDocument.up();
            }

            Xembler xmlBuilder = new Xembler(directivesDocument);
            String urlLink = new URL(link).getPath();
            printXmlFile(urlLink, xmlBuilder);
        }

        return null;

    }

    private static void buildSection(Element element, Directives directivesDocument, boolean includeParagraphs) {
        Elements innerElements = element.children();
        if (innerElements.isEmpty()) {
            return;
        }
        LOGGER.info("There are " + innerElements.size() + " inner elements.");

        directivesDocument = directivesDocument.add("section")
                .attr("type", "document");
        String sectionTitle = "";
        if (!element.select("h1").isEmpty()) {
            sectionTitle = element.select("h1").get(0).text();
        }
        else if (!element.select("h2").isEmpty()) {
            sectionTitle = element.select("h2").get(0).text();
        }
        directivesDocument
                .attr("title", sectionTitle);
        LOGGER.info("Discovered new section: " + sectionTitle);

        if (innerElements.get(0).tag().toString().compareTo("h2") == 0) {
            innerElements.remove(0);
        }

        LOGGER.info("Inner inner elements size: " + innerElements.size());
        for (Element innerElement : innerElements) {
            if (!includeParagraphs && innerElement.tag().toString().compareTo("p") == 0) continue;
            LOGGER.info("Inner element: " + innerElement.text());
            if (innerElement.tag().toString().compareTo("div") == 0
                    && innerElement.attr("class").compareTo("section") == 0) {
                //LOGGER.info("Will call again build section for " + innerElement);
                //sectionDirective = buildSection(innerElement, sectionDirective);
            } else {
                LOGGER.info("Found inner elements: " + innerElement.tag());
                directivesDocument.add(innerElement.tag()).set(innerElement.text()).up();
            }
        }
    }

    private static void printXmlFile(String fileName, Xembler xmlBuilder) {
        PrintWriter writer;
        File file = new File(Constants.GRENOBLE_OUTPUT_PATH + fileName + ".xml");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            writer = new PrintWriter(file);
            LOGGER.info("Printing to file " + file.getName() + ":");
            LOGGER.info(xmlBuilder.toString());
            writer.print(xmlBuilder.xml());
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.info("Couldn't print print to: " + file.getAbsolutePath());
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ImpossibleModificationException ex) {
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
