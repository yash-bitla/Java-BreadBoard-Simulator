# Simulator.java
package tryout;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Simulator {

	static JFrame frame;
	
	public static void main(String[] args) {
		BreadBoard board = new BreadBoard();
		frame = new JFrame("BreadBoard");
		frame.setSize(1530, 310);
		frame.setLocation(230, 180);
		frame.setVisible(true);
		frame.add(board);
		board.init();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.COMPONENT_RESIZED));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
