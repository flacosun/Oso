package indexing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Term {
	public Term(String name){
		this.name = name;
		this.indexEntryMap = new HashMap<Integer, IndexEntry>();
		this.df = 0.0;
		this.N = 0;
	}
	public void addIndex(int fileNumber, int position){
		if(indexEntryMap.containsKey(fileNumber)){
			IndexEntry entry = indexEntryMap.get(fileNumber);
			entry.addOccurance(position);
			indexEntryMap.put(fileNumber, entry);
		}
		else {
			IndexEntry entry = new IndexEntry(fileNumber);
			entry.addOccurance(position);
			indexEntryMap.put(fileNumber, entry);
		}
		df++;
		return;
	};
	public void print(int numOfPage){
		N = numOfPage;
		idf = Math.log10(N/df);
		PrintWriter outFile;
		PrintWriter catalogFile;
		try {
			outFile = new PrintWriter(new BufferedWriter(new FileWriter("terms/" + this.name + ".txt", false)));
			catalogFile = new PrintWriter(new BufferedWriter(new FileWriter("catalog.txt", true)));
			catalogFile.println(name);
			catalogFile.close();
			//outFile.println(name);
			outFile.println(idf);
			Object[] keys = indexEntryMap.keySet().toArray();
			Arrays.sort(keys);
			for(Object key : keys){
				IndexEntry entry = indexEntryMap.get((Integer) key);
				outFile.print(entry.getDocument() + " ");
			    outFile.print(entry.getTf() + " ");
			    for(int i = 0; i < entry.positionList.size(); i++){
			    	outFile.print(entry.positionList.get(i) + " ");
			    }
			    outFile.print(System.lineSeparator());
			}
			outFile.close();
			System.out.println("Done printing " + this.name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void load() {
		try {
			BufferedReader br=new BufferedReader(new FileReader("terms/" + this.name + ".txt"));
			idf = Double.parseDouble(br.readLine());
			String line;
			while((line = br.readLine()) != null){
				String[] indexArray = line.split(" ");
				int fileNumber = Integer.parseInt(indexArray[0]);
				for(int i = 2; i < indexArray.length; i++){
					int position = Integer.parseInt(indexArray[i]);
					this.addIndex(fileNumber, position);
					indexEntryMap.get(fileNumber).setTF(Double.parseDouble(indexArray[1]));
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String name;
	public int N;
	public Map<Integer, IndexEntry> indexEntryMap;
	public double df;
	public double idf;
}
