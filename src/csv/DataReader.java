package csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class DataReader {
	
	private CSVReader ICReader;
	private CSVReader CFReader;
	
	private List<String[]> ICData;
	private List<String[]> CFData;
	
	public DataReader() throws IOException, CsvException {
		Reader IC = Files.newBufferedReader(Paths.get("./data/ICRatio.csv"));
		Reader CF = Files.newBufferedReader(Paths.get("./data/CorrectionFactor.csv"));
		
		ICReader = new CSVReader(IC);
		CFReader = new CSVReader(CF);
		
		ICData = ICReader.readAll();
		CFData = CFReader.readAll();
		
		ICReader.close();
		CFReader.close();
	}
	
	public List<String[]> getICData() {
		return ICData;
	}
	
	public List<String[]> getCFData() {
		return CFData;
	}
	
	
	
}
