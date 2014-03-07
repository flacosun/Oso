package analyser;

//import ir.assignments.one.a.Frequency;
//import ir.assignments.one.a.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Counts the total number of words and their frequencies in a text file.
 */

public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
		List<Frequency> frequencies = new ArrayList<Frequency>();
		if(words.size() == 0){
			Frequency f = new Frequency("*", -1);
			frequencies.add(f);
			return frequencies;
		}
		//final int hashTableSize = 21911;
		Map<String, Integer> frequencyMap = new HashMap<String, Integer>();
		for(int i = 0; i < words.size(); i++){
			String tempWord = words.get(i);
			if(frequencyMap.containsKey(tempWord))
				frequencyMap.put(tempWord, frequencyMap.get(tempWord) + 1);
			else
				frequencyMap.put(tempWord, 1);
		}
		
		for(String i : frequencyMap.keySet())
			frequencies.add(new Frequency(i, frequencyMap.get(i)));
		
		FrequencyComparator frequencyComparator = new FrequencyComparator();
		Collections.sort(frequencies, frequencyComparator);
		return frequencies;
	}
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		List<String> words = Utilities.makeList(file);
		List<Frequency> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
