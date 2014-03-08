package crawler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import text_processing.TextProcessor;
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
            return !FILTERS.matcher(href).matches() 
            		&& FILTERS2.matcher(href).matches() 
            		//&& (!url.getSubDomain().equals("archive.ics"))
            		//&& (!url.getSubDomain().equals("calendar.ics")) 
            		//&& (!url.getSubDomain().equals("drzaius.ics")) 
            		//&& (!url.getSubDomain().equals("djp3-pc2.ics"))
            		;
            		
    }

    /**
     * This function is called when a page is fetched and ready 
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {          
            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    try {
                    	System.out.println(page.getWebURL().getURL());
                    	String text = htmlParseData.getText();
                        String html = htmlParseData.getHtml();
                        List<WebURL> outgoingLinks = htmlParseData.getOutgoingUrls();
                    	PrintWriter outFile = new PrintWriter(
                    							new BufferedWriter(
                    									new OutputStreamWriter (
                    											new FileOutputStream("data/rawText.txt",true)
                    									, "UTF-8")
                    							)
                    						);
                    	outFile.println( "<<<" + page.getWebURL().getDocid() + ">>>");
                    	outFile.println(page.getWebURL().getURL());
                    	List<String> title = TextProcessor.tokenize(htmlParseData.getTitle());
                    	System.out.print("Title: ");
                    	for(String word : title){
                    		outFile.print(word + " ");
                    		System.out.print(word + " ");
                    		
                    	}
                    	outFile.print(System.lineSeparator());
        				List<WebURL> validOutgoingLinks = new LinkedList<WebURL>();
        				for(WebURL link : outgoingLinks) {
        					if (link.getDocid() >= 0){
        						validOutgoingLinks.add(link);
        					}
        				}
        				outFile.println(validOutgoingLinks.size());
        				for(WebURL link : validOutgoingLinks){
        					String anchorText = link.getAnchor();
        					if(anchorText == null){
        						anchorText = "NULL_ANCHOR_TEXT";
        					}
        					else{
        						List<String> words = TextProcessor.tokenize(anchorText);
        						anchorText = "";
        						for(String word : words)
        							anchorText += word + " ";
        						if(anchorText == "")
        							anchorText = "NULL_ANCHOR_TEXT";
        					}
        					outFile.println(link.getDocid() + " " + anchorText);
        				}
        				outFile.println(text);
        				outFile.close();
        				
        				
        				System.out.println("Html length: " + html.length());
        				
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
                    
                    /*
                    System.out.println("Charset: " + page.getContentCharset());
                    System.out.println("Content Data: " + page.getContentData().toString());
                    System.out.println("Encoding: " + page.getContentEncoding());
                    System.out.println("Content type: " + page.getContentType());
                    System.out.println("Fetch Response Headers: " + page.getFetchResponseHeaders());
                    System.out.println("Html: " + htmlParseData.getHtml());
                    System.out.println("OutgoingUrl: " + htmlParseData.getOutgoingUrls().get(0));
                    System.out.println("Title: " + htmlParseData.getTitle());
                   
                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    System.out.println("Number of outgoing links: " + links.size());
                    */
                    
            }
    }
}