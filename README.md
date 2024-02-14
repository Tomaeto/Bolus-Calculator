Program for caluclating bolus for Type 1 Diabetes

Proof of concept for eventual Android app

Stores IC ratio and Correction Factor in .csv files, formatted as Value,Start Time,End Time

Stores Bolus information in .csv as BG,Bolus,Carbs,Timestamp (YYYY-MM-dd HH:mm)

Uses OpenCSV for CSV parsing, reading and writing.

	src
		/csv - contains Classes for reading/writing .CSV files and relevant Bean classes
		
		/gui - contains Classes for building/displaying GUI for program
		
	
	data - contains all .CSV files
	
	
	lib - contains .JARs for OpenCSV and relevant dependencies
