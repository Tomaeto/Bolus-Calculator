package csv;

import java.time.LocalTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RatioBean {

	@CsvBindByName
	private int value;
	
	@CsvBindByName
	private String start;
	
	@CsvBindByName
	private String end;
	
	public RatioBean() {
		
	}
	public int getRatio() {
		return value;
	}
	
	public void setRatio(int value) {
		this.value = value;
	}
	
	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	
	public String getEnd() {
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
}
