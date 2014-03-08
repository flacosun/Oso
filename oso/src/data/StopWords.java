package data;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StopWords {
	public void removeStopWords(String wordFileName, String stopWordFileName) throws IOException {
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
}
