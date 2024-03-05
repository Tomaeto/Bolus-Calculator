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
	

	
	private List<RatioBean> ICData;
	private List<RatioBean> CFData;
	private List<String[]> TargetData;

	LocalTime time;
	public CSVHandler() {
		try {
			Reader IC = Files.newBufferedReader(Paths.get("./data/csv/ICRatio.csv"));
			Reader CF = Files.newBufferedReader(Paths.get("./data/csv/CorrectionFactor.csv"));
			Reader Target = getTargetReader("./data/csv/Targets.csv");

			
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
	
	public BufferedReader getTargetReader(String filepath) throws IOException {

		//Variables needed to create new reader file if not found
		JTextField lowerField, upperField;
		JPanel optionPanel;
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

		//Attempt to return new BufferedReader for the filepath, if fail, print stack trace and exit program
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

		Writer write = new FileWriter("./data/csv/Bolus.csv", true);
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
