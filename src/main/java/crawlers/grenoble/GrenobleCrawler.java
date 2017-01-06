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
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    
    private boolean saveTxtFiles;

    public GrenobleCrawler() {
        super();
        setName("Grenoble");
        setMaxScore(100.0);
    }

    public void setSaveTxtFiles(boolean saveTxtFiles) {
        this.saveTxtFiles = saveTxtFiles;
    }
    
    @Override
    public ArrayList<String> crawl(String url) throws IOException {
        if (url == null || url.length() == 0) {
            return null;
        }

        LOGGER.log(Level.INFO, "Crawling {0}.", url);

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
        PrintWriter writer, writerTxt;
                
        LOGGER.log(Level.INFO, "There are {0} links to be processed.", indexedLinks.size());
        for (String link : indexedLinks) {
            LOGGER.log(Level.INFO, "Processing link: {0}", link);

            Connection connection = Jsoup.connect(link);
            connection = setMozillaHeaders(connection);
            Document doc = connection.get();
            Response response = connection.execute();
            // TODO: Didn't find any way to get text from Document
            // Two requests are sent. Needs improvement!
            String urlLink = new URL(link).getPath();
            File file = new File(Constants.GRENOBLE_OUTPUT_PATH + urlLink);
            writer = new PrintWriter(file);
            writer.write(response.body());
            writer.close();
            
            if (saveTxtFiles) {
                LOGGER.log(Level.INFO, "Creating Txt file: {0}{1}.txt", new Object[]{Constants.GRENOBLE_OUTPUT_PATH, urlLink});
                File fileTxt = new File(Constants.GRENOBLE_OUTPUT_PATH + urlLink + ".txt");
                writerTxt = new PrintWriter(fileTxt);
            }
            else {
                writerTxt = null;
            }
            LOGGER.log(Level.INFO, "Printed HTML file to: {0}{1}.html", new Object[]{Constants.GRENOBLE_OUTPUT_PATH, urlLink});

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

            LOGGER.log(Level.INFO, "Adding title of document: {0}", h1.text());
            LOGGER.log(Level.INFO, "Adding author of document: {0}", author);
            Directives directivesDocument = new Directives();
            directivesDocument
                    .add("document")
                    .attr("language", "en")
                    .add("title").set(h1.text()).up()
                    .add("authors").add("author").set(author).up().up();
            if (saveTxtFiles && writerTxt != null && h1 != null) writerTxt.write(h1.text() + "\n");
            if (saveTxtFiles && writerTxt != null && author != "") writerTxt.write(author + "\n");
            
            //Xembler xmlBuilder = new Xembler();
            String htmlToTxt = buildSection(content, directivesDocument, false);
            if (saveTxtFiles && writerTxt != null) writerTxt.write(htmlToTxt);
            LOGGER.log(Level.INFO, "There are {0} sections.", sections.size());
            for (Element section : sections) {
                htmlToTxt = buildSection(section, directivesDocument, true);
                if (saveTxtFiles && writerTxt != null) writerTxt.write(htmlToTxt);
                directivesDocument.up();
            }
            if (saveTxtFiles && writerTxt != null)  writerTxt.close();

            Xembler xmlBuilder = new Xembler(directivesDocument);
            printXmlFile(urlLink, xmlBuilder);
        }

        return null;

    }

    private static String buildSection(Element element, Directives directivesDocument, boolean includeParagraphs) {
        Elements innerElements = element.children();
        if (innerElements.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        LOGGER.log(Level.INFO, "There are {0} inner elements.", innerElements.size());

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
        LOGGER.log(Level.INFO, "Discovered new section: {0}", sectionTitle);
        //sb.append(sectionTitle).append("\n");

        if (innerElements.get(0).tag().toString().compareTo("h2") == 0) {
            innerElements.remove(0);
        }

        LOGGER.log(Level.INFO, "Inner inner elements size: {0}", innerElements.size());
        for (Element innerElement : innerElements) {
            if (!includeParagraphs && innerElement.tag().toString().compareTo("p") == 0) continue;
            LOGGER.log(Level.INFO, "Inner element: {0}", innerElement.text());
            if (innerElement.tag().toString().compareTo("div") == 0
                    && innerElement.attr("class").compareTo("section") == 0) {
                //LOGGER.info("Will call again build section for " + innerElement);
                //sectionDirective = buildSection(innerElement, sectionDirective);
            } else {
                LOGGER.log(Level.INFO, "Found inner elements: {0}", innerElement.tag());
                directivesDocument.add(innerElement.tag()).set(innerElement.text()).up();
                sb.append(innerElement.text()).append("\n");
            }
        }
        return sb.toString();
    }

    private static void printXmlFile(String fileName, Xembler xmlBuilder) {
        PrintWriter writer;
        File file = new File(Constants.GRENOBLE_OUTPUT_PATH + fileName + ".xml");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            writer = new PrintWriter(file);
            LOGGER.log(Level.INFO, "Printing to file {0}:", file.getName());
            LOGGER.info(xmlBuilder.toString());
            writer.print(xmlBuilder.xml());
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "Couldn''t print print to: {0}", file.getAbsolutePath());
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ImpossibleModificationException ex) {
            Logger.getLogger(GrenobleCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
