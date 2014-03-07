package analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens. Returns
	 * an ArrayList of these tokens, ordered according to their occurrence in
	 * the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 * 
	 * Words are also normalized to lower case.
	 * 
	 * Example:
	 * 
	 * Given this input string "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be ["an", "input", "string", "this",
	 * "is", "or", "is", "it"]
	 * 
	 * @param input
	 *            The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by
	 *         occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(input));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> tokenList = new ArrayList<String>();
		boolean isReading = false;
		int tempchar;
		StringBuffer tempToken = new StringBuffer();
		try {
			while((tempchar = reader.read()) != -1){
				
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tokenList;
	}
	
	/**
	 * Construct a tokenList without tokenization
	 * @param input
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<String> makeList(File input) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(input));
		ArrayList<String> tokenList = new ArrayList<String>();
		/*Scanner sc = null;
		Scanner sc2 = null;
		try {
	        sc = new Scanner(input);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
		
		while (sc.hasNextLine()) {
			sc2 = new Scanner(sc.nextLine());
			while(sc2.hasNext()){
				String s = sc2.next();
				//tokenList.add(s);
				System.out.println(s);
				System.out.println(++counter);
			}
			
        }
		sc2.close();
		sc.close();
		*/
		String temp = new String();
		int counter=0;
		try {
			while ((temp = br.readLine())!=null) {
				/*sc2 = new Scanner(sc.nextLine());
				while(sc2.hasNext()){
					String s = sc2.next();
					//tokenList.add(s);
					System.out.println(s);
					System.out.println(++counter);
				}*/
				
				//System.out.println(temp);
				if(temp.length()>1)
					tokenList.add(temp);
				++counter;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(counter);
		return tokenList;
	}

	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique
	 * items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies ["sentence:2", "the:1",
	 * "this:1", "repeats:1", "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6 Unique item count: 5
	 * 
	 * sentence 2 the 1 this 1 repeats 1 word 1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies ["you think:2", "how you:1",
	 * "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6 Unique 2-gram count: 5
	 * 
	 * you think 2 how you 1 know how 1 think you 1 you know 1
	 * 
	 * @param frequencies
	 *            A list of frequencies.
	 * @throws IOException 
	 */
	public static void printFrequencies(List<Frequency> frequencies) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("twogramfrequency.txt")); 
		if(frequencies.size() == 0){
			osw.close();
			return;
		}
		String tempWord;
		int tempFrequency;
		
		boolean is2Gram = false;
		if(frequencies.size()>0){
			tempWord = frequencies.get(0).getText();
			is2Gram = false;
			for(char j = 0; j < tempWord.length(); j++){
				if (j == ' '){
					is2Gram = true;
					break;
				}
			}
		}
		
		int totalCount = 0;
		for (int i = 0; i < frequencies.size(); i++){
			tempFrequency = frequencies.get(i).getFrequency();
			totalCount += tempFrequency;
		}
		
		String content;
		int count;
		if(is2Gram || frequencies.get(0).getFrequency() == -1 && frequencies.get(0).getText().equals("#")) 
			content = "2-gram";
		else 
			content = "item";
		if(frequencies.get(0).getFrequency() == -1){
			count = 0;
			totalCount = 0;
		}
		else
			count = frequencies.size();
		String outFormat = "Total " + content + " count: " + totalCount + " Unique " + content + " count: " + count;
		try {
			osw.write(outFormat, 0, outFormat.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(outFormat);
		//System.out.println();
		for (int i = 0; i < 20; i++){
			tempWord = frequencies.get(i).getText();
			tempFrequency = frequencies.get(i).getFrequency();
			if (tempFrequency < 0) break;
			outFormat = tempFrequency + "\t" + tempWord + "\r\n";
			System.out.println(outFormat);
			try {
				osw.write(outFormat, 0, outFormat.length());
				osw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if(i == frequencies.size()-1) ;
			//else System.out.print(' ');
		}
		osw.close();
		return;
	}
}
