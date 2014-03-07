package indexing;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Document {
	public Document(int fileNumber, String url, List<String> text){
		StringBuilder sb = new StringBuilder();
		for(String tempString: text){
			sb.append(tempString).append(System.lineSeparator());
		}
		try {
			BufferedOutputStream outFile = new BufferedOutputStream(new FileOutputStream("./documents/" + fileNumber + ".txt"));
			outFile.write(url.getBytes("UTF-8"), 0, url.length());
			outFile.write(System.lineSeparator().getBytes("UTF-8"), 0, System.lineSeparator().length());
			outFile.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			outFile.flush();
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("Page: " + url + " Count: " + fileNumber);
	}
	int fileNumber;
	String url;
}
