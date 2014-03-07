package indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCatalog {
	public static void main(String[] arg){
		try {
			BufferedReader br = new BufferedReader(new FileReader(arg[0]));
			List<String> catalogList = new ArrayList<String>();
			String word;
			while((word = br.readLine()) != null){
				catalogList.add(word);
			}
			br.close();
			Collections.sort(catalogList);
			PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter("catalog1.txt", false)));
			for(int i =0; i < catalogList.size(); i++){
				outFile.println(catalogList.get(i));
			}
			System.out.print(catalogList.size());
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
