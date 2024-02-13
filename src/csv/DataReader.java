package csv;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class DataReader {
	

	
	private List<String[]> ICData;
	private List<String[]> CFData;
	private List<String[]> TargetData;

	LocalTime time = LocalTime.now();
	public DataReader() {
		try {
			Reader IC = Files.newBufferedReader(Paths.get("./data/ICRatio.csv"));
			Reader CF = Files.newBufferedReader(Paths.get("./data/CorrectionFactor.csv"));
			Reader Target = Files.newBufferedReader(Paths.get("./data/Targets.csv"));
			
			CSVReader ICReader = new CSVReader(IC);
			CSVReader CFReader = new CSVReader(CF);
			CSVReader TargetReader = new CSVReader(Target);

			ICData = ICReader.readAll();
			CFData = CFReader.readAll();
			TargetData = TargetReader.readAll();

			ICReader.close();
			CFReader.close();
			TargetReader.close();
			

		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
	
	public List<String[]> getICData() {
		return ICData;
	}
	
	public List<String[]> getCFData() {
		return CFData;
	}
	
	public int getCurrentIC() {
		LocalTime start, end;
		for (String[] entry : ICData.subList(1,ICData.size()-1)) {
			start = LocalTime.parse(entry[1]);
			end = LocalTime.parse(entry[2]);
			if (time.isBefore(end) && time.isAfter(start)) {
				return Integer.parseInt(entry[0]);
			}
		}
		return -1;
	}
	
	public int getCurrentCF() {
		LocalTime start, end;
		for (String[] entry : CFData.subList(1, CFData.size()-1)) {
			start = LocalTime.parse(entry[1]);
			end = LocalTime.parse(entry[2]);
			if (time.isAfter(start) && time.isBefore(end)) {
				return Integer.parseInt(entry[0]);
			}
		}
		System.out.println(time);
		return -1;
	}
	
	public int getUpperTarget() {
		return Integer.parseInt(TargetData.get(1)[0]);
	}
	
	public int getLowerTarget() {
		return Integer.parseInt(TargetData.get(1)[1]);
	}
	
	
}
