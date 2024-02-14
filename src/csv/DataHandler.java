package csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class DataHandler {
	

	
	private List<RatioBean> ICData;
	private List<RatioBean> CFData;
	private List<String[]> TargetData;

	LocalTime time = LocalTime.now();
	public DataHandler() {
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
	
	public void writeBolus(int bg, double bolus, int carbs, LocalDateTime timestamp) throws IOException {

		Writer write = new FileWriter("./data/Bolus.csv", true);
		StatefulBeanToCsv<BolusBean> sbc = new StatefulBeanToCsvBuilder<BolusBean>(write).build();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		BolusBean bean = new BolusBean(bg, bolus, carbs, timestamp.format(format));
		try {
			sbc.write(bean);
			write.close();
		} catch (CsvDataTypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
