package gui;
import java.io.IOException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	//Setting up frame and adding Main Panel
	public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Bolus Calculator");
        this.add(new MainPanel());

	}



    public static void main(String[] args) throws IOException {
    	MainFrame frame = new MainFrame();
    	frame.setVisible(true);

    	
    }
 
	

	
}