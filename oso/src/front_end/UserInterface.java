package front_end;

import indexing.IndexEntry;
import indexing.PorterStemmer;
import indexing.Term;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserInterface {
	public static void main(String[] args){
		String query;
		String[] queries;
		BufferedReader inputReader=new BufferedReader(new InputStreamReader(System.in));
		try {
			BufferedReader catalogReader = new BufferedReader(new FileReader("catalog.txt"));
			String line;
			while((line = catalogReader.readLine()) != null){
				catalog.add(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("OK");
		System.out.println("Please Enter Query:");
		try {
			while((query = inputReader.readLine()) != null){
				
				queries = query.split(" ");
				if(queries == null)
					continue;
				
				if(query.length() > 80){
					System.out.println("Query too long");
					continue;
				}
				queries = query.split(" ");
				if(queries.length > 8){
					System.out.println("Too many query words");
					continue;
				}
				boolean isQueryLengthValid = true;
				for(int i = 0; i < queries.length; i++){
					if(queries[i].length() > 20){
						isQueryLengthValid = false;
						break;
					}
				}
				if(!isQueryLengthValid){
					System.out.println("Too many query words");
					continue;
				}
				List<String> queryList = new ArrayList<String>(); 
				for(int i = 0; i < queries.length; i++){
					PorterStemmer tempStemmer = new PorterStemmer();
					tempStemmer.add(queries[i].toCharArray(), queries[i].length());
					tempStemmer.stem();
					String stemmedWord = tempStemmer.toString();
					queryList.add(stemmedWord);
				}
				List<String> result = new ArrayList<String>();
				if(queryList.size() > 1){
					List<String> res = searchWholeWords(queryList);
					result.addAll(res);
				}
				else if(queryList.size() == 1){
					result.addAll(searchSingleWord(queryList));
				}
				//if(result.size() < 20){
					//result.addAll(vectorSearch(queryList));
				//}
				System.out.println("Result:");
				for(int i = 0; i < Math.min(10, result.size()); i++){
					System.out.println(result.get(i));
				}
				System.out.println("Please Enter Query:");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> searchSingleWord(List<String> queryList) {
		Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
		Term t = new Term(queryList.get(0));
		t.load();
		Iterator it = t.indexEntryMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, IndexEntry> pairs = (Map.Entry<Integer, IndexEntry>) it.next();
			IndexEntry entry = pairs.getValue();
			resultMap.put(entry.getDocument(), entry.getTf());
		}
		List<Map.Entry<Integer, Double> > resultEntryList = new ArrayList<Map.Entry<Integer, Double> >(resultMap.entrySet());
		Collections.sort(resultEntryList, 
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2){
						return (int) ((o2.getValue() - o1.getValue())*10);
					}
				});
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < Math.min(20, resultEntryList.size()); i++){
			result.add(getUrl(resultEntryList.get(i).getKey()));
		}
		return result;
	}

	private List<String> vectorSearch(List<String> queryList) {
		
		return null;
	}

	private static List<String> searchWholeWords(List<String> queryList) {
		for (int i = 0; i < queryList.size(); i++){
			if(!isExistInCatalog(queryList.get(i)))
				return null;
		}
		Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
		List<Term> termList = loadTerm(queryList);
		Term fistTerm = termList.get(0);
		Iterator it = fistTerm.indexEntryMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, IndexEntry> pairs = (Map.Entry<Integer, IndexEntry>) it.next();
			boolean isInAllTerms = true;
			for (int i = 1; i < termList.size(); i++){
				if(! termList.get(i).indexEntryMap.containsKey(pairs.getKey())){
					isInAllTerms = false;
					break;
				}
			}
			if (!isInAllTerms)
				continue;
			List< List <Integer> > positionList = new ArrayList< List <Integer> >();
			for (int i = 0; i < termList.size(); i++){
				positionList.add(termList.get(i).indexEntryMap.get(pairs.getKey()).positionList);
			}
			int[] pointers = new int[positionList.size()];
			for(int i = 0; i < pointers.length; i++){
				pointers[i] = 0;
			}
			while(pointers[0] != positionList.get(0).size()){
				for(int i = 1; i < pointers.length; i++){
					while(pointers[i] != positionList.get(i).size() && positionList.get(i).get(pointers[i]) <= positionList.get(i-1).get(pointers[i-1]))
						pointers[i]++;
				}
				boolean isContinuous = true;
				for(int i = 1; i < pointers.length; i++){
					if(pointers[i-1] != pointers[i] - 1){
						isContinuous = false;
						break;
					}
				}
				if(isContinuous){
					int document = pairs.getValue().getDocument();
					if(resultMap.containsKey(document)){
						resultMap.put(document, resultMap.get(document)+1);
					}
					else{
						resultMap.put(document, 1);
					}
				}
				pointers[0]++;
			}
		}
		List<Map.Entry<Integer, Integer> > resultEntryList = new ArrayList<Map.Entry<Integer, Integer> >(resultMap.entrySet());
		Collections.sort(resultEntryList, 
				new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2){
						return (o2.getValue() - o1.getValue());
					}
				});
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < Math.min(20, resultEntryList.size()); i++){
			result.add(getUrl(resultEntryList.get(i).getKey()));
		}
		return result;
	}

	private static String getUrl(Integer name) {
		try {
			BufferedReader br=new BufferedReader(new FileReader("documents/" + name + ".txt"));
			String ret = br.readLine();
			br.close();
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<Term> loadTerm(List<String> queryList) {
		List<Term> termList = new ArrayList<Term>();
		for (int i = 0; i < queryList.size(); i++){
			Term t = new Term(queryList.get(i));
			t.load();
			termList.add(t);
		}
		return termList;
	}

	private static boolean isExistInCatalog(String word) {

		if(foundWordInCatalog(word, catalog, 0, 274621))
			return true;
		else 
			return false;
	}

	private static boolean foundWordInCatalog(String word, List<String> catalog, int low, int high) {
		if (low > high)
			return false;
		int mid = (low + high) / 2;
		String s = catalog.get(mid);
		int compareResult = word.compareTo(s);
		if (compareResult  == 0){
			return true;
		}
		else if (compareResult > 0 ){
			return foundWordInCatalog(word, catalog, mid + 1, high);
		}
		else {
			return foundWordInCatalog(word, catalog, low, mid - 1);
		}
	}
	private static List<String> catalog = new ArrayList<String> ();
}
