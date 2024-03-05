package gui;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public MainFrame() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Bolus Calculator");
        this.add(new MainPanel());

	}


    public static void main(String[] args) {
    	MainFrame frame = new MainFrame();

    	frame.setVisible(true);
    }
 
	
}