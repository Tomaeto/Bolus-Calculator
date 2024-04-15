package sqlite;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class SqliteHandler {

	private ResultSet userData; //ID, Name, Upper, Lower, CorrectAbove
	private ResultSet ICData;  //Value, Start, End
	private ResultSet CFData;  //Value, Start, End
	private SqliteDbAccessor db = new SqliteDbAccessor();

	public SqliteHandler() throws SQLException {
		try {
			File dbFile = new File("./data/sql/data.db");
			if (!dbFile.isFile()) {
				dbFile.createNewFile();
				//TODO:Generate message dialogs for getting user data (upper/lower/ratios/factors) and create db tables (copy from CSVHandler)
				//CREATE TABLE IF NOT EXISTS user(
				//	id INTEGER PRIMARY KEY,
				//	name TEXT NOT NULL,
				//	upper INTEGER,
				//	lower INTEGER,
				//	correct INTEGER);
				
				//CREATE TABLE IF NOT EXISTS factors(
				//	id INTEGER, 
				//	type INTEGER, (0=IC, 1=CF)
				//	value INTEGER NOT NULL,
				//	start TEXT,
				//	end TEXT,
				//	FOREIGN KEY (id) REFRENCES user(id));
				
				//CREATE TABLE IF NOT EXISTS bolus(
				//	id INTEGER,
				//	carbCount INTEGER,
				//	BG INTEGER,
				//	bolusAmt INTEGER,
				//	timestamp TEXT,
				//	FOREIGN KEY (id) REFERENCES user(id));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		db.connectToDb();
		String userQuery = "SELECT * FROM user;";
		userData = db.getConnection().prepareStatement(userQuery).executeQuery();
		
		String ICQuery = "SELECT value, start, end FROM factors WHERE type=0 AND id=" + userData.getString(1) + ";";
		ICData = db.getConnection().prepareStatement(ICQuery).executeQuery();
		
		String CFQuery = "SELECT value, start, end FROM factors WHERE type=1 AND id=" + userData.getString(1) + ";";
		CFData = db.getConnection().prepareStatement(CFQuery).executeQuery();
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
