package com.basic.core.main;

import com.basic.core.StopWords;
import com.basic.core.model.DataStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;


public class CountMinSketchAnalyzer {
	private static final Logger logger = LoggerFactory.getLogger(CountMinSketchAnalyzer.class);

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double epsilon = 0.0001;
		double delta = 0.001;
		double support = 0.001;

		long startTimeSystemTime = System.currentTimeMillis();
		DataStructure sketch = new DataStructure(epsilon, delta, support);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\dataresult\\resultTweets.txt")));
		String str;
		StopWords sw = new StopWords();
		sw.addWords("/stopwords.txt");
		while ((str = br.readLine()) != null) {
			if (sw.isIn(str))
				continue;
			sketch.add(str, 1L);
		}
		//sketch.show();
		long endTimeSystemTime = System.currentTimeMillis();
		logger.info("startTime:" + new Timestamp(startTimeSystemTime));
		logger.info("endTime:" + new Timestamp(endTimeSystemTime));
		long timelong = (endTimeSystemTime - startTimeSystemTime) / 1000;
		logger.info("totalTime:" + timelong + " s" + "------or------" + timelong / 60 + " min");

		logger.info("love:" + sketch.estimateCount("love"));
		logger.info("why:" + sketch.estimateCount("why"));
		br.close();
	}

}
