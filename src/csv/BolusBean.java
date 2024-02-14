package csv;

import com.opencsv.bean.CsvBindByPosition;

public class BolusBean {

	@CsvBindByPosition(position = 0)
	private int bloodGlucose;
	
	@CsvBindByPosition(position = 1)
	private double bolus;
	
	@CsvBindByPosition(position = 2)
	private int carbs;
	
	@CsvBindByPosition(position = 3)
	private String timestamp;
	
	public BolusBean(int bloodGlucose, double bolus, int carbs, String timestamp) {
		this.bloodGlucose = bloodGlucose;
		this.bolus = bolus;
		this.carbs = carbs;
		this.timestamp = timestamp;
	}
	
	public double getBolus() {
		return bolus;
	}
	
	public int getCarbs() {
		return carbs;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public String toString() {
		return bolus + "|" + "carbs" + "|" + timestamp;
	}
}
