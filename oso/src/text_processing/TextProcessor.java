package text_processing;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TextProcessor {
	public static List<String> tokenize(String text){
		ArrayList<String> tokenList = new ArrayList<String>();
		boolean isReading = false;
		StringBuffer tempToken = new StringBuffer();
		int length = text.length();
		for (int i = 0; i < length; i++){
			char readingChar = text.charAt(i);
			if (Character.isLetterOrDigit(readingChar)){
				tempToken.append(readingChar);
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
		if(tempToken.length() != 0){
			tokenList.add(tempToken.toString().toLowerCase());
		}
		return tokenList;
	}
}
