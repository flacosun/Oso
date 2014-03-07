package analyser;

//import ir.assignments.one.a.Frequency;
//import ir.assignments.one.a.Utilities;
//import ir.assignments.one.b.FrequencyComparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Count the total number of 2-grams and their frequencies in a text file.
 */
public final class TwoGramFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private TwoGramFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique 2-gram in the original list. The frequency of each 2-grams
	 * is equal to the number of times that two-gram occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied 2-grams sorted
	 * alphabetically. 
	 * 
	 * 
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["you", "think", "you", "know", "how", "you", "think"]
	 * 
	 * The output list of 2-gram frequencies should be 
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of two gram frequencies, ordered by decreasing frequency.
	 */
	private static List<Frequency> computeTwoGramFrequencies(ArrayList<String> words) {
		List<Frequency> frequencies = new ArrayList<Frequency>();
		//final int hashTableSize = 21911;
		if(words.size() == 0){
			Frequency f = new Frequency("#", -1);
			frequencies.add(f);
			return frequencies;
		}
		Map<String, Integer> frequencyMap = new HashMap<String, Integer>();
		for(int i = 0; i < words.size()-1; i++){
			String tempWord = words.get(i) + ' ' + words.get(i+1);
			if(frequencyMap.containsKey(tempWord))
				frequencyMap.put(tempWord, frequencyMap.get(tempWord) + 1);
			else
				frequencyMap.put(tempWord, 1);
		}
		words=null;
		System.out.println("OK");
		for(String i : frequencyMap.keySet())
			frequencies.add(new Frequency(i, frequencyMap.get(i)));
		System.out.println("Sort");
		FrequencyComparator frequencyComparator = new FrequencyComparator();
		
		Collections.sort(frequencies, frequencyComparator);
		return frequencies;
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		ArrayList<String> words = Utilities.makeList(file);
		List<Frequency> frequencies = computeTwoGramFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
	
}
