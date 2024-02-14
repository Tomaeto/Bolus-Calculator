package csv;

import java.time.LocalTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;

public class RatioBean {

	@CsvBindByName
	private int value;
	
	@CsvCustomBindByName(converter = LocalTimeConverter.class)
	private LocalTime start;
	
	@CsvCustomBindByName(converter = LocalTimeConverter.class)
	private LocalTime end;
	
	public RatioBean() {
	}
	
	public int getRatio() {
		return value;
	}
	
	public LocalTime getStart() {
		return start;
	}
	
	public LocalTime getEnd() {
		return end;
	}
	
	public String toString() {
		return value + "|" + start + "|" + end;
	}
}
