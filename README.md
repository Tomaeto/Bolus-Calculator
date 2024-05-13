# Diabetes Bolus Calculator

Program for caluclating bolus for Type 1 Diabetes


Stores IC ratio and Correction Factor in .csv files, formatted as Value,Start Time,End Time.

Stores Bolus information in .csv as BG,Bolus,Carbs,Timestamp (YYYY-MM-dd HH:mm)

Uses OpenCSV for CSV parsing, reading and writing.

Uses SQLite for database reading/management.

	src
		/csv - contains Classes for reading/writing .CSV files and relevant Bean classes
		
		/gui - contains Classes for building/displaying GUI for program
		
	
	data - contains all .CSV/.db files
	
	
	lib - contains .JARs for OpenCSV/SQLite and relevant dependencies


 # To Run

 * Requires Java 17.0 or later

Open Terminal in root directory and run the following:

	java -jar BolusCalc.jar
