package analyser;

//import ir.assignments.one.a.Frequency;

import java.util.Comparator;

public class FrequencyComparator implements Comparator<Frequency>{
	public int compare(Frequency fa, Frequency fb){
		if (fa.getFrequency() == fb.getFrequency())
			return fa.getText().compareTo(fb.getText());
		else
			if(fa.getFrequency()<fb.getFrequency())
				return 1;
			else
				return -1;
	}
}