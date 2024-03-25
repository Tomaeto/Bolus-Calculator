package sqlite;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqliteHandler {

	private ResultSet userData; //ID, Name, Upper, Lower, CorrectAbove
	private ResultSet ICData;
	private ResultSet CFData;
	private SqliteDbAccessor db = new SqliteDbAccessor();

	public SqliteHandler() throws SQLException {
		try {
			File dbFile = new File("./data/sql/data.db");
			if (!dbFile.isFile()) {
				dbFile.createNewFile();
				//TODO:Generate message dialogs for getting user data (upper/lower/ratios/factors) and create db tables (copy from CSVHandler)
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.connectToDb();
		String userQuery = "SELECT * FROM user;";
		userData = db.getConnection().prepareStatement(userQuery).executeQuery();
		
	}

	public int getCurrentCF() {
		return 0;
	}

	public int getCurrentIC() {
		return 0;
	}

	public int getUpperTarget() {
		return 0;
	}

	public int getLowerTarget() {
		return 0;
	}
}
