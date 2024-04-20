package csv;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CSVHandler {
	

	//Lists for storing user data from CSV files
	private List<RatioBean> ICData;
	private List<RatioBean> CFData;
	private List<String[]> TargetData;

	LocalTime time;
	public CSVHandler() {
		
		//Getting user data for IC/CF ratios and target values
		try {
			Reader IC = getRatioReader("ICRatio.csv");
			Reader CF = getRatioReader("CorrectionFactor.csv");
			Reader Target = getTargetReader("Targets.csv");

			
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
	

	private BufferedReader getTargetReader(String filename) throws IOException {

		//Variables needed to create new reader file if not found
		JTextField lowerField, upperField;
		JPanel optionPanel;
		String filepath = "./data/csv/" + filename;
		File reader = new File(filepath);
		CSVWriter writer;
		
		//If file isn't found, show message dialog, prompt for entering values, and build new file
		if (!reader.isFile()) {
			
			//Showing message dialog and building panel for entering values
			JOptionPane.showMessageDialog(null,"Target data not found, please enter values."); 
			lowerField = new JTextField(3);
			upperField = new JTextField(3);
			optionPanel = new JPanel(new GridLayout(2,2));
			
			optionPanel.add(new JLabel("Enter lower target: "));
			optionPanel.add(lowerField);
			optionPanel.add(new JLabel("Enter upper target: "));
			optionPanel.add(upperField);
			
			//Showing dialog for entering upper and lower targets
			JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
			
			//Check if entered values are valid integers via regex and if lower < upper, repeatedly prompt user to reenter if invalid
			while (!upperField.getText().matches("-?\\d+") || !lowerField.getText().matches("-?\\d+") ||
					Integer.parseInt(lowerField.getText()) >= Integer.parseInt(upperField.getText()) ) {

				JOptionPane.showMessageDialog(null, "Invalid targets, please enter valid upper and lower targets.");
				JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
			}
			
			//Creating new Targets.csv file
			reader.createNewFile();

			//Writing given targets to file using CSVWriter
			writer = new CSVWriter(new FileWriter(reader));
			String[] headers = {"Upper Target", "Lower Target"};
			String[] values = {upperField.getText(), lowerField.getText()};
			writer.writeNext(headers);
			writer.writeNext(values);
			writer.close();
			
		}

		//Attempt to return new BufferedReader for the filepath, if it fails, print stack trace and exit program
		try {
			return Files.newBufferedReader(Paths.get(filepath));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);

		}
		return null;
	}
	

	private BufferedReader getRatioReader(String filename) throws IOException {
		
		//Variables needed for building ratio file if not found
		JTextField startField, endField, valueField;
		LocalTime start = LocalTime.parse("00:00"), end = LocalTime.parse("00:00");
		JPanel optionPanel;
		CSVWriter writer;
		String filepath = "./data/csv/" + filename;
		File reader = new File(filepath);
		
		//If entered filename doesn't exist, show relevant error message, build option panel for entering value and time range
		if (!reader.isFile()) {
			String message = "Correction Factor data not found, please enter values and time ranges in 24-hour format.";
			if (filename.equals("ICRatio.csv")) {
				message = "IC Ratio data not found, please enter values and time ranges in 24-hour format.";
			}
			JOptionPane.showMessageDialog(null, message);
			startField = new JTextField("00:00");
			endField = new JTextField();
			valueField = new JTextField();
			startField.setEditable(false);
			optionPanel = new JPanel(new GridLayout(3,2));
			optionPanel.add(new JLabel("Enter start time: "));
			optionPanel.add(startField);
			optionPanel.add(new JLabel("Enter end time: "));
			optionPanel.add(endField);
			optionPanel.add(new JLabel("Enter ratio/factor: "));
			optionPanel.add(valueField);
			
			//Creating new ratio file, entering headers
			reader.createNewFile();
			writer = new CSVWriter(new FileWriter(reader));
			String[] headers = {"value", "start", "end"};
			writer.writeNext(headers);


			//While the previous end time is not 23:59, continue taking entries for ratio and time range
			while (!startField.getText().equals("23:59")) {

				//Getting value and range
				JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
				boolean validInput = false;
				
				//Testing if inputs are valid
				while (!validInput) {
					
					//Try to parse given values and check if start is before end
					try {
						start = LocalTime.parse(startField.getText());
						end = LocalTime.parse(endField.getText());
						Integer.parseInt(valueField.getText());
						validInput = start.isBefore(end);
					}
					//Catch error, show error message and reprompt for input
					catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Invalid inputs, enter time range in HH:MM format and a valid integer ratio.");
						JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
						continue;
					}
				
					//If values are valid but start is not before end, show error message dn reprompt for input
					if (!validInput) {
						JOptionPane.showMessageDialog(null, "Invalid time range, enter time range with start before end.");
						JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
					}
				}
				
				//If values are valid, build String array as value,start,end and write to file
				String[] next = {valueField.getText(), startField.getText(), endField.getText()};
				writer.writeNext(next);
				
				//Set next start time to previous end time and reset end and ratio fields for next pass
				startField.setText(endField.getText());
				endField.setText("");
				valueField.setText("");
			}
			
			//Closing writer after all values are entered for entire day range
			writer.close();
		}
		
		//Attempt to return BufferedReader for the filepath, if it fails, print stack trace for error and exit
		try {
			return Files.newBufferedReader(Paths.get(filepath));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	

	public void writeBolus(int bg, double bolus, int carbs, LocalDateTime timestamp) throws IOException {

		//Creating BeanToCSV for writing BolusBean to file and building BolusBean from given data
		Writer write = new FileWriter("./data/csv/Bolus.csv", true);
		StatefulBeanToCsv<BolusBean> sbc = new StatefulBeanToCsvBuilder<BolusBean>(write).build();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		BolusBean bean = new BolusBean(bg, bolus, carbs, timestamp.format(format));
		
		//Writing to Bolus.csv
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
			time = LocalTime.now();
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
			time = LocalTime.now();
			if (time.isAfter(start) && time.isBefore(end)) {
				return entry.getRatio();
			}
		}
		return -1;
	}

	public int getUpperTarget() {
		return Integer.parseInt(TargetData.get(1)[0]);
	}
	
	public int getLowerTarget() {
		return Integer.parseInt(TargetData.get(1)[1]);
	}
	
	
}
