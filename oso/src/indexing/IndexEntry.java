package indexing;

import java.util.ArrayList;
import java.util.List;

public class IndexEntry {
	IndexEntry(int doc){
		document = doc;
		tf = 0;
		positionList = new ArrayList<Integer>();
	}
	public void addOccurance(int position) {
		positionList.add(position);
		tf++;
		return;
	}
	public double getTf(){
		logtf = Math.log10((double) tf);
		return logtf;
	}
	public int getDocument(){
		return document;
	}
	public void setTF(double tfToSet){
		logtf = tfToSet;
		return;
	}
	private int document;
	private int tf;
	private double logtf;
	public List<Integer> positionList;
	
}
