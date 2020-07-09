package com.mvu.lottery.data;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Repository;

@Repository
public class AnalyzedDrawnNumberData {

	public AnalyzedDrawnNumberData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Return the data from the analyzed data file. The returned String is 
	 * a combined of all lines.
	 * @param dataPath
	 * @return
	 * @throws Exception
	 */
	public String getAnalyzedData(final Path dataPath) throws Exception {
		
		//Check to see if the file is up to date.
		
		return String.join("", Files.readAllLines(dataPath));
	}
	
}
