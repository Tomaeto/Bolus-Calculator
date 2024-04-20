package sqlite;

import java.awt.GridLayout;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SqliteHandler {

	private ResultSet userData; //ID, Name, Upper, Lower, CorrectAbove
	private ResultSet ICData;  //Value, Start, End
	private ResultSet CFData;  //Value, Start, End
	private ResultSet tableSizes; //Rows in user, rows of IC, rows of CF, rows in bolus
	private SqliteDbAccessor db = new SqliteDbAccessor();

	public SqliteHandler() throws SQLException {
		db.connectToDb();
		String userSql = "CREATE TABLE IF NOT EXISTS user("
				+"id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL,"
				+"upper INTEGER, lower INTEGER, correct INTEGER);";
		
		String factorSql = "CREATE TABLE IF NOT EXISTS factors("
				+"type INTEGER, value INTEGER NOT NULL,"
				+"start TEXT, end TEXT, id INTEGER, FOREIGN KEY (id) REFERENCES user(id));";
		
		String bolusSql = "CREATE TABLE IF NOT EXISTS bolus("
				+"id INTEGER, carbCount INTEGER, BG INTEGER,"
				+"bolusAmt INTEGER, timestamp TEXT, FOREIGN KEY (id) REFERENCES user(id));";
		db.getConnection().prepareStatement(userSql).execute();
		db.getConnection().prepareStatement(factorSql).execute();
		db.getConnection().prepareStatement(bolusSql).execute();

		String checkSize = "SELECT (SELECT COUNT(*) FROM user), (SELECT COUNT(*) FROM factors WHERE type=0),"
						 + "(SELECT COUNT(*) FROM factors WHERE type=1), (SELECT COUNT(*) from bolus);";
		tableSizes = db.getConnection().prepareStatement(checkSize).executeQuery();

		
		JPanel optionPanel;
		LocalTime start = LocalTime.parse("00:00"), end = LocalTime.parse("00:00");
		if (tableSizes.getInt(1) == 0) {
			//If user table is empty
			JTextField nameField = new JTextField(3);
			JTextField upperField = new JTextField(3);
			JTextField lowerField = new JTextField(3);
			JTextField correctField = new JTextField(3);
			optionPanel = new JPanel(new GridLayout(4,2));
			optionPanel.add(new JLabel("Enter name: "));
			optionPanel.add(nameField);
			optionPanel.add(new JLabel("Enter upper target: "));
			optionPanel.add(upperField);
			optionPanel.add(new JLabel("Enter lower target: "));
			optionPanel.add(lowerField);
			optionPanel.add(new JLabel("Enter correct above value: "));
			optionPanel.add(correctField);
			JOptionPane.showMessageDialog(null, "User data not found, please enter values.");
			JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
			
			//Check if upperField, lowerField, and correctField are valid integers and if lower < upper, repeatedly prompt for input if invalid
			while (!upperField.getText().matches("-?\\d+") || !lowerField.getText().matches("-?\\d+") ||
					Integer.parseInt(lowerField.getText()) >= Integer.parseInt(upperField.getText()) ||
					!correctField.getText().matches("-?\\d+")) {
				
				JOptionPane.showMessageDialog(null, "Invalid input, please enter valid integers with lower target less than upper target.");
				JOptionPane.showConfirmDialog(null, optionPanel, null, JOptionPane.DEFAULT_OPTION);
			}
			
			String entryQuery = "INSERT INTO user(id, name, upper, lower, correct) VALUES (1, \""
					+ nameField.getText() + "\"," + Integer.parseInt(upperField.getText()) + ","
					+ Integer.parseInt(lowerField.getText()) + "," + Integer.parseInt(correctField.getText()) + ");";
			db.getConnection().prepareStatement(entryQuery).executeUpdate();
		}
		
		if (tableSizes.getInt(2) == 0) {
			writeFactors(0);
		}
		
		if (tableSizes.getInt(3) == 0) {
			writeFactors(1);
		}

		String userQuery = "SELECT * FROM user;";
		userData = db.getConnection().prepareStatement(userQuery).executeQuery();
		
		String ICQuery = "SELECT value, start, end FROM factors WHERE type=0;";
		ICData = db.getConnection().prepareStatement(ICQuery).executeQuery();

		String CFQuery = "SELECT value, start, end FROM factors WHERE type=1";
		CFData = db.getConnection().prepareStatement(CFQuery).executeQuery();
	}

	private void writeFactors(int type) throws SQLException {
		LocalTime start, end;
		JPanel optionPanel = new JPanel(new GridLayout(3,2));
		String message = "No IC data found, please enter values and time ranges in 24-hour format, ending at 23:59.";
		if (type == 1) message = "No CF data found, please enter values and time ranges in 24-hour format, ending at 23:59.";
		JOptionPane.showMessageDialog(null, message);
		JTextField startField = new JTextField("00:00");
		JTextField endField = new JTextField();
		JTextField valueField = new JTextField();
		startField.setEditable(false);
		optionPanel.add(new JLabel("Enter start time: "));
		optionPanel.add(startField);
		optionPanel.add(new JLabel("Enter end time: "));
		optionPanel.add(endField);
		optionPanel.add(new JLabel("Enter factor/ratio: "));
		optionPanel.add(valueField);
		
		String query;
		while (!startField.getText().equals("23:59")) {
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

			//If all values are valid, insert row into factors table
			query = "INSERT INTO factors(id, type, value, start, end) VALUES(1, "
					+ type + ", " + valueField.getText() + ", \"" + startField.getText()
					+ "\", \"" + endField.getText() + "\");";
			db.getConnection().prepareStatement(query).executeUpdate();
			
			//Set next start time to previous end time and reset end and ratio fields for next pass
			startField.setText(endField.getText());
			endField.setText("");
			valueField.setText("");
		}
	}
	
	public void writeBolus() throws SQLException {
		
	}
	
	public int getCurrentCF() throws SQLException {
		LocalTime start, end;
		while (CFData.next()) {
			start = LocalTime.parse(CFData.getString(2));
			end = LocalTime.parse(CFData.getString(3));
			if (LocalTime.now().isBefore(end) && LocalTime.now().isAfter(start)) {
				return CFData.getInt(1);
			}
		};
		return -1;
	}

	public int getCurrentIC() throws SQLException {
		LocalTime start, end;
		while (ICData.next()) {
			start = LocalTime.parse(ICData.getString(2));
			end = LocalTime.parse(ICData.getString(3));
			if (LocalTime.now().isBefore(end) && LocalTime.now().isAfter(start)) {
				return ICData.getInt(1);
			}
		};
		return -1;
	}

	public int getUpperTarget() throws SQLException {
		return userData.getInt(3);
	}

	public int getLowerTarget() throws SQLException {
		return userData.getInt(4);
	}
}
