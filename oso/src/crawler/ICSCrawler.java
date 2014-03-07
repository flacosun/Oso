package crawler;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ICSCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                      + "|png|tiff?|mid|mp2|mp3|mp4"
                                                      + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                      + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private final static Pattern FILTERS2 = Pattern.compile(".*(\\.ics\\.uci\\.edu).*");
    
    /**
     * You should implement this function to specify whether
     * the given url should be crawled or not (based on your
     * crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
            String href = url.getURL().toLowerCase();
            return !FILTERS.matcher(href).matches() && FILTERS2.matcher(href).matches() && (!url.getSubDomain().equals("archive.ics")) &&
            		(!url.getSubDomain().equals("calendar.ics")) &&
            		(!url.getSubDomain().equals("drzaius.ics")) &&
            		(!url.getSubDomain().equals("djp3-pc2.ics"));
            		
    }

    /**
     * This function is called when a page is fetched and ready 
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {          
            String url = page.getWebURL().getURL();
            System.out.println("URL: " + url);
            //String[] splittedUrl = url.split("\\");
            //String encodingFormat = page.getContentEncoding();
            //System.out.println("Encoding: " + encodingFormat);

            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    System.out.println("Charset: " + page.getContentCharset());
                    System.out.println("Content Data: " + page.getContentData().toString());
                    System.out.println("Encoding: " + page.getContentEncoding());
                    System.out.println("Content type: " + page.getContentType());
                    System.out.println("Fetch Response Headers: " + page.getFetchResponseHeaders());
                    System.out.println("Html: " + htmlParseData.getHtml());
                    System.out.println("OutgoingUrl: " + htmlParseData.getOutgoingUrls().get(0));
                    System.out.println("Title: " + htmlParseData.getTitle());
                    String text = htmlParseData.getText();
                    String html = htmlParseData.getHtml();
                    List<WebURL> links = htmlParseData.getOutgoingUrls();
                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    System.out.println("Number of outgoing links: " + links.size());
                    
            }
    }
}