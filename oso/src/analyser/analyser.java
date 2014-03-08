package analyser;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

public class analyser {
	public static void main(String[] args) throws Exception {
		//System.out.print(args[0]);
		//countPages(args[0]);
		//countSubDomains("url2.txt");
		//findLongestPage(args[0]);
		//removeStopWords("words3.txt", "stopwords.txt");
		String[] arg = {"wordsWithoutStop.txt"};
		//WordFrequencyCounter.main(arg);
		TwoGramFrequencyCounter.main(arg);
		//find2Grams(f);
	}

	private static void removeStopWords(String wordFileName, String stopWordFileName) throws IOException {
		File wordFile = new File(wordFileName);
		File stopWordFile = new File(stopWordFileName);
		Set<String> stopWords = new HashSet<String>();
		if(!wordFile.exists()||wordFile.isDirectory())
			throw new FileNotFoundException();
		BufferedReader br=new BufferedReader(new FileReader(stopWordFile));
		String line;
		while((line=br.readLine())!=null){
			/*Matcher wordMatcher = wordPattern.matcher(line);
			if(!wordMatcher.matches()){
				System.out.println(line);
				while(1==1);
			}
			else{
				for(int i = 1; i < wordMatcher.groupCount(); i++){
					stopWords.add(wordMatcher.group(i));
				}
			}*/
			stopWords.add(line);
			
		}
		
		/*for (String str : stopWords){
			System.out.println(str);
		}*/
		//System.out.println(stopWords.size());
		br.close();
		//br=new BufferedReader(new FileReader(wordFile));
		BufferedOutputStream outFile=new BufferedOutputStream(new FileOutputStream("wordsWithoutStop.txt"));
		//Pattern wordPattern = Pattern.compile(".*");
		/*while((line=br.readLine())!=null){
			Matcher wordMatcher = wordPattern.matcher(line);
			if(!wordMatcher.matches()){
				System.out.println('#');
				while(1==1);
			}
			else{
				for(int i = 1; i < wordMatcher.groupCount(); i++){
					if(!stopWords.contains(wordMatcher.group(i))){
						System.out.println(wordMatcher.group(i));
						//outFile.write(wordMatcher.group(i).getBytes(),0,wordMatcher.group(i).length());
					}
				}
			}
		}*/
		//br.close();
		Scanner sc = null;
		try {
	        sc = new Scanner(wordFile);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
		while (sc.hasNext()) {
            String s = sc.next();
            if(!stopWords.contains(s)){
            	System.out.println(s);
            	outFile.write(s.getBytes(), 0, s.length());
            	outFile.write('\n');
            }
        }
		sc.close();
		outFile.close();
	}

	public static List<String> tokenize(String input) {
		List<String> tokenList = new LinkedList<String>();
		boolean isReading = false;
		char tempchar;
		StringBuffer tempToken = new StringBuffer();
		for(int i=0;i<input.length();i++){
			tempchar=input.charAt(i);
			if (Character.isLetterOrDigit((char) tempchar)){
				tempToken.append((char)tempchar);
				if(!isReading){
					isReading = true;
				}
			}
			else{
				if(isReading){
					tokenList.add(tempToken.toString().toLowerCase());
					tempToken = new StringBuffer();
					isReading = false;
				}

			}
		}
		return tokenList;
	}

	

	private static void countSubDomains(String urlFileName) throws IOException {
		File urlFile = new File(urlFileName);
		if(!urlFile.exists()||urlFile.isDirectory())
			throw new FileNotFoundException();
		BufferedReader br=new BufferedReader(new FileReader(urlFile));
		String temp=null;
		int counter = 0;
	    Pattern P = Pattern.compile("(^http\\w{0,1}://)(.*?)(\\.ics\\.uci\\.edu)(.*)");
	    Map<String, Integer> subdomainMap = new HashMap<String, Integer>();
	    while((temp=br.readLine())!=null){
	    	Matcher M = P.matcher(temp);
	    	if(!M.matches()){
	    		System.out.println("No Matches for "+temp);
	    		while(1==1);
	    	}
	    	else{
	    		counter+=1;
	    		if (subdomainMap.containsKey(M.group(2))){
	    			subdomainMap.put(M.group(2), subdomainMap.get(M.group(2))+1);
	    		}
	    		else {
	    			subdomainMap.put(M.group(2), 1);
	    		}
	    		System.out.println("ADDED "+temp+" FROM SUB DOMAIN " + M.group(2)+".ics.edu");
	    	}
	    	
	    }
	    //ValueComparator comp =  new ValueComparator(subdomainMap);
	    //TreeMap<String,Integer> sortedSubdomain = new TreeMap<String,Integer>(comp);
	    File outFile = new File("subdomain2.txt");
	    BufferedWriter output = new BufferedWriter(new FileWriter(outFile));
	    Map<String, Integer> sortedSubdomain = sortByComparator(subdomainMap);
	    int tempdbg=0;
	    for (Iterator it = sortedSubdomain.entrySet().iterator();it.hasNext();){
	    	//System.out.println(++tempdbg);
	    	++tempdbg;
	    	Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)it.next();
	    	output.write(entry.getKey()+".ics.uci.edu, ");
	    	output.write(entry.getValue().toString());
	    	output.write("\r\n");
	    }
	    br.close();
	    output.close();
	    System.out.println(tempdbg);
	    System.out.println(counter);
	}
	
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap)
    {

        List<Entry<String, Integer> > list = new LinkedList<Entry<String, Integer> >(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2){
            	return o1.getKey().compareTo(o2.getKey());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

	private static void findLongestPage(String filePath) throws IOException {
		File f = new File(filePath);
		if(!f.exists()||f.isDirectory())
			throw new FileNotFoundException();
		BufferedOutputStream outFile=new BufferedOutputStream(new FileOutputStream("words3.txt"));
	    BufferedReader br=new BufferedReader(new FileReader(f));
	    Pattern P = Pattern.compile("(<<<)(.*)(>>>)");
	    String tempLine = null;
	    String url = null;
	    //List<String> words = new LinkedList<String>();
	    List<String> wordsInPage = new LinkedList<String>();
	    PageWords max = new PageWords(null, 0);
	    while((tempLine=br.readLine())!=null){
	    	Matcher M = P.matcher(tempLine);
	    	if(M.matches()){
	    		if(!wordsInPage.isEmpty()){
	    			StringBuilder sb = new StringBuilder();
	    			if(wordsInPage.size() > max.numberOfWords){
	    				max.numberOfWords=wordsInPage.size();
	    				max.nameOfPage=url;
	    			}
	    			for(String tempString: wordsInPage){
	    				sb.append(tempString).append('\n');
	    			}
	    			outFile.write(sb.toString().getBytes(), 0, sb.length());
	    			outFile.flush();
	    			System.out.println("Page: " + url + " Words: " + wordsInPage.size());
	    			wordsInPage = new LinkedList<String>();
	    		}
	    		url=M.group(2);
	    		
	    		
	    	}
	    	else{
	    		wordsInPage.addAll(tokenize(tempLine));
	    	}
	    }
	    System.out.println(max.nameOfPage + ' ' + max.numberOfWords);
	    br.close();
	    outFile.close();
	}

	private static void countPages(String filePath) throws IOException {
		File f = new File(filePath);
		if(!f.exists()||f.isDirectory())
			throw new FileNotFoundException();
		BufferedOutputStream outFile=new BufferedOutputStream(new FileOutputStream("url2.txt"));
	    BufferedReader br=new BufferedReader(new FileReader(f));
	    String temp=null;
	    int counter = 0;
	    Pattern P = Pattern.compile("(<<<)(.*?)(>>>)");
	    HashSet<String> urlSet = new HashSet<String>();
	    while((temp=br.readLine())!=null){
	    	Matcher M = P.matcher(temp);
	    	if (M.matches()){
	    		String url = M.group(2);
	    		if(!urlSet.contains(url)){
	    			System.out.println(url);
	    			counter++;
	    			System.out.println(counter);
	    			urlSet.add(url);
	    			url += "\r\n";
	    			outFile.write(url.getBytes(), 0, url.length());
	    		}
	    	}
	    }
	    br.close();
	    outFile.close();
	    return;
	}
}
