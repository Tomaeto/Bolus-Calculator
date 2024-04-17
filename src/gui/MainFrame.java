package gui;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import sqlite.SqliteHandler;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	//Setting up frame and adding Main Panel
	public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Bolus Calculator");
        this.add(new MainPanel());

	}



    public static void main(String[] args) throws IOException, SQLException {
 //   	MainFrame frame = new MainFrame();
 //   	frame.setVisible(true);
    	SqliteHandler h = new SqliteHandler();
    	
    }

}