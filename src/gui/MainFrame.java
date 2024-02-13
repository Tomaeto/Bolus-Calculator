package gui;
import java.io.IOException;

import javax.swing.JFrame;

import com.opencsv.exceptions.CsvException;

import csv.DataReader;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public MainFrame() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Bolus Calculator");
        this.add(new MainPanel());

	}


    public static void main(String[] args) throws IOException, CsvException {
    	MainFrame frame = new MainFrame();
    	frame.setVisible(true);
    	
    	//Testing DataReader
		DataReader reader = new DataReader();

    }
}