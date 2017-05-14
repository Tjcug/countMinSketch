package com.basic.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

public class StopWords 
{
    private static final Logger logger = LoggerFactory.getLogger(StopWords.class);
	private HashSet uglyWords;
	
	public StopWords() {
		uglyWords = new HashSet();
	}
	
	public HashSet addWords(String txtFile) throws IOException {
	// this method add words to the uglyWords set reading each word from the file txtFile
	// the txtFile should contain one word per line
		BufferedReader in;
		try {
			InputStream  inputStream= Object. class .getResourceAsStream( txtFile );
			in = new BufferedReader(new InputStreamReader(inputStream));
            logger.debug("stopwords.txt filter file detected.\n");
		} catch (Exception e) {
			e.printStackTrace();
            logger.error("The program will procede without a stopwords.txt filter file.\n");
			return uglyWords;
		}
		String word;
		while ((word = in.readLine()) != null) {
			uglyWords.add(word.toLowerCase());
		}
		return uglyWords;
	}
	
	public boolean isIn(String word) {
		return uglyWords.contains(word);
	}
	
	public void displayStopWords(){
		Iterator set = uglyWords.iterator();
		while (set.hasNext())
            logger.info(String.valueOf(set.next()));
	}
	
	
}
