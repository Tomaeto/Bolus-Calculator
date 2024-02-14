package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import csv.DataHandler;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	private DataHandler reader = new DataHandler();
	private int bloodGlucose = 0;
	private int carbs = 0;
	private double bolus;
	private int upperTarget;
	private int correctionFactor;
	private int ICRatio;
	
	public MainPanel() {
		correctionFactor = reader.getCurrentCF();
		ICRatio = reader.getCurrentIC();
		upperTarget = reader.getUpperTarget();
		this.setLayout(new BorderLayout());
		this.add(buildTopPanel(), BorderLayout.NORTH);
		this.add(buildMidPanel(), BorderLayout.CENTER);
		this.add(buildBotPanel(), BorderLayout.SOUTH);
	}

	
	private JPanel buildTopPanel() {
		JPanel panel = new JPanel();
		JLabel BGLabel = new JLabel("BG: ");
		JLabel carbLabel = new JLabel("Carbs: ");
		
        // Create a NumberFormatter to allow only integer input w/o grouping
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        
        // Create a JFormattedTextField with the NumberFormatter
        JFormattedTextField BGEntryField = new JFormattedTextField(formatter);
        BGEntryField.setColumns(3);
        JFormattedTextField carbEntryField = new JFormattedTextField(formatter);
        carbEntryField.setColumns(3);
        
        BGEntryField.addFocusListener(new FocusListener() {
        	public void focusGained(FocusEvent e) {
        		//Do Nothing
        	}
        	
        	public void focusLost(FocusEvent e) {
        		try {
        		bloodGlucose = Integer.parseInt(BGEntryField.getText());
        		} catch (NumberFormatException ex) {
        			System.err.println("Incorrect Format");
        		}
        	}
        });
        
        carbEntryField.addFocusListener(new FocusListener() {
        	public void focusGained(FocusEvent e) {
        		//Do Nothing
        	}
        	
        	public void focusLost(FocusEvent e) {
        		try {
        		carbs = Integer.parseInt(carbEntryField.getText());
        		} catch (NumberFormatException ex) {
        			System.err.println("Incorrect Format");
        		}
        	}
        });
        
        JPanel BGPanel = new JPanel();
        BGPanel.add(BGLabel);
        BGPanel.add(BGEntryField);
        
        JPanel carbPanel = new JPanel();
        carbPanel.add(carbLabel);
        carbPanel.add(carbEntryField);
        
        panel.add(BGPanel);
        panel.add(carbPanel);
        
        return panel;
	}
	
	private JPanel buildMidPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel ICLabel = new JLabel("Current IC Ratio: " + ICRatio);
		JLabel CFLabel = new JLabel("Current Correction Factor: " + correctionFactor);
		panel.add(ICLabel, BorderLayout.NORTH);
		panel.add(CFLabel, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel buildBotPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel bolusText = new JLabel("Bolus = " + carbs + " / " + ICRatio);

		JButton calcButton = new JButton("Generate");
		calcButton.addActionListener((ActionEvent e) -> {
			bolus = (double)carbs / (double)ICRatio;
			bolus = roundToHalf(bolus);
			bolusText.setText("Bolus = " + carbs + " / " + ICRatio);

			if (bloodGlucose > upperTarget) { 
				bolus += (bloodGlucose - upperTarget) / (double)correctionFactor;
				bolus = roundToHalf(bolus);
				bolusText.setText(bolusText.getText() + " + (" + bloodGlucose + " - " + upperTarget + ") / " + correctionFactor);
			}
			calcButton.setText(Double.toString(bolus));
			
			try {
				reader.writeBolus(bloodGlucose, bolus, carbs, LocalDateTime.now());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			super.update(getGraphics());
		});
		
		panel.add(bolusText, BorderLayout.CENTER);
		panel.add(calcButton, BorderLayout.SOUTH);

		return panel;
	}
	
	private static double roundToHalf(double f) {
		return Math.round(f * 2) / 2.0;
	}
	
	
}
