package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteDbAccessor {

	private Connection con;
	private Statement stmt;
	private PreparedStatement prepStmt;
	private ResultSet rs;
	
	private String connectionUrl;
	
	private String defaultConnUrl = "jdbc:sqlite:./data/sql/data.db";
	
	/**
	 * Constructor for SqlServerDbAccessor, sets connectionUrl to default value
	 */
	public SqliteDbAccessor() {
		connectionUrl = defaultConnUrl;
	}
	
	/**
	 * Constructor for SqlServerDbAccessor, sets connectionUrl with values passed in
	 * @param serverName the name of the SQL server
	 * @param user the username for the server
	 * @param pwd the password for the server
	 * @param dbName the name of the database to query
	 */
	public SqliteDbAccessor(String serverName, String user, String pwd, 
			String dbName) {
		connectionUrl = "jdbc:sqlserver://;";
		connectionUrl += "servername=" + serverName + ";"; 
		connectionUrl += "user=" + user + ";"; 
		connectionUrl += "password=" + pwd + ";"; 
		connectionUrl += "databaseName=" + dbName + ";"; 
	}
	
	/**
	 * Setter for setting the name of the database to query
	 * @param dbName the name of the database to query
	 */
	public void setDbName(String dbName) {
		connectionUrl += "databaseName=" + dbName;
	}
	
	/**
	 * Method for connecting to the specified database
	 */
	public void connectToDb() {
    	try {
    		// Establish the connection.
    		Class.forName("org.sqlite.JDBC");
        	con = DriverManager.getConnection(connectionUrl);
    	} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * getter for PreparedStatement
	 * @return prepStmt the PreparedStatement
	 */
	public PreparedStatement getPrepStmt() {
		// TODO Auto-generated method stub
		return prepStmt;
	}

	/**
	 * Getter for the database connection
	 * @return con the database connection
	 */
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return con;
	}

	/**
	 * Getter for database connection URL
	 * @return connectionUrl the database connection URL
	 */
	public String getUrl() {
		// TODO Auto-generated method stub
		return connectionUrl;
	}
}