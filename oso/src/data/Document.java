package data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class Document {
	
	public Document(int ID){
		this.ID = ID;
		this.path = "data/documents/" + ID + ".txt";
	}
	
	public Document(int ID, String url, List<String> text){
		this(ID);
		this.url = url;
		this.text = text;
	}
	
	public void print(){
		StringBuilder sb = new StringBuilder();
		for(String tempString: this.text){
			sb.append(tempString).append(System.lineSeparator());
		}
		try {
			PrintWriter outFile = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter (
									new FileOutputStream(this.path,true)
							, "UTF-8")
					)
				);
			outFile.println(this.url);
			outFile.println(this.title);
			outFile.println(this.anchorText.size());
			for(String s : this.anchorText){
				outFile.println(s);
			}
			outFile.print(sb);
			outFile.flush();
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Page: " + url + " ID: " + this.ID);
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String Url) {
		this.url = Url;
	}
	
	private String title;
	private List<String> anchorText;
	private String path;
	private int ID;
	private String url;
	private List<String> text;
}
