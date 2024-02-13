package csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class DataReader {
	

	
	private List<RatioBean> ICData;
	private List<RatioBean> CFData;
	private List<String[]> TargetData;

	LocalTime time = LocalTime.now();
	public DataReader() {
		try {
			Reader IC = Files.newBufferedReader(Paths.get("./data/ICRatio.csv"));
			Reader CF = Files.newBufferedReader(Paths.get("./data/CorrectionFactor.csv"));
			Reader Target = Files.newBufferedReader(Paths.get("./data/Targets.csv"));

			CsvToBean<RatioBean> ICReader = new CsvToBeanBuilder<RatioBean>(IC).withType(RatioBean.class).build();
			CsvToBean<RatioBean> CFReader = new CsvToBeanBuilder<RatioBean>(CF).withType(RatioBean.class).build();
			CSVReader TargetReader = new CSVReader(Target);
			

			ICData = ICReader.parse();
			CFData = CFReader.parse();
			TargetData = TargetReader.readAll();
			TargetReader.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
	
	public void test() {
		try {
			Reader read = Files.newBufferedReader(Paths.get("./data/CorrectionFactor.csv"));
			CsvToBean<RatioBean> cb = new CsvToBeanBuilder<RatioBean>(read).withType(RatioBean.class).build();
			System.out.println(cb.parse().get(0).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<RatioBean> getICData() {
		return ICData;
	}
	
	public List<RatioBean> getCFData() {
		return CFData;
	}
	
	public int getCurrentIC() {
		LocalTime start, end;
		for (RatioBean entry : ICData) {
			start = entry.getStart();
			end = entry.getEnd();
			if (time.isBefore(end) && time.isAfter(start)) {
				return entry.getRatio();
			}
		}
		return -1;
	}
	
	public int getCurrentCF() {
		LocalTime start, end;
		for (RatioBean entry : CFData) {
			start = entry.getStart();
			end = entry.getEnd();
			if (time.isAfter(start) && time.isBefore(end)) {
				return entry.getRatio();
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
