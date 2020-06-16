# ICOption.java
package tryout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ICOption implements ActionListener {
	
	JFrame frame;
	JLabel ptr[];
	JLabel spc[];
	JButton btn[];
	JPanel pnl[];
	
	private int Frame_Width;
	private int Frame_Height;
	private int Frame_X;
	private int Frame_Y;
	Cordinate src;
	
	public ICOption(Cordinate src) {
		this.src = src;
	}

	public void init() {
		frame = new JFrame(); 
		Frame_Width = 480; Frame_Height = 450;
		Frame_X = 635; Frame_Y = 200;
		frame.setLayout(new FlowLayout());
		String s[] = {"AND", "OR", "NOT", "NAND", "NOR", "X-OR"};
		
		spc = new JLabel[6];
		for(int i=0;i<6;i++) {
			spc[i] = new JLabel("   ");
			spc[i].setFont(new Font("SansSerif", 0, 30));
		}
		
		ptr = new JLabel[6];
		for(int i=0;i<6;i++) {
			ptr[i] = new JLabel("~~>");
			ptr[i].setFont(new Font("SansSerif", 0, 30));
		}
		
		btn = new JButton[6];
		for(int i=0;i<6;i++) {
			btn[i] = new JButton(s[i]);
			btn[i].setPreferredSize(new Dimension(300, 50));
			btn[i].setFont(new Font("SansSerif", 0, 20));
			btn[i].addActionListener(this);
		}
		
		pnl = new JPanel[6];
		for(int i=0;i<6;i++) {
			pnl[i] = new JPanel();
			pnl[i].setLayout(new FlowLayout());
			pnl[i].add(ptr[i]);
			pnl[i].add(btn[i]);
			pnl[i].add(spc[i]);
			frame.add(pnl[i]);
		}
		
		jFrame(frame);
	}
	
	private void jFrame(final JFrame frame) {
		
		frame.setVisible(true);
		frame.setTitle("Pin Configuration");
		frame.setSize(Frame_Width, Frame_Height);
		frame.setLocation(Frame_X, Frame_Y);
		frame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {
				BreadBoard.board.setEnabled(true);
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
			}
			public void windowOpened(WindowEvent arg0) {
			}
			
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton prsd = (JButton)arg0.getSource();
		if(prsd.equals(btn[0])) {
			AND ic = new AND(src);
			ic.init();
			BreadBoard.gate.add(ic);
			BreadBoard.addICGate(src, "IC - 7408");
		} else if(prsd.equals(btn[1])) {
			OR ic = new OR(src);
			ic.init();
			BreadBoard.gate.add(ic);
			BreadBoard.addICGate(src, "IC - 7432");
		} else if(prsd.equals(btn[2])) {
			NOT ic = new NOT(src);
			ic.init();
			BreadBoard.gate.add(ic);
			BreadBoard.addICGate(src, "IC - 7404");
		} else if(prsd.equals(btn[3])) {
			NAND ic = new NAND(src);
			ic.init();
			BreadBoard.gate.add(ic);
			BreadBoard.addICGate(src, "IC - 7400");
		} else if(prsd.equals(btn[4])) {
			NOR ic = new NOR(src);
			ic.init();
			BreadBoard.gate.add(ic);
			BreadBoard.addICGate(src, "IC - 7402");
		} else {
			XOR ic = new XOR(src);
			ic.init();
			BreadBoard.gate.add(ic);
			BreadBoard.addICGate(src, "IC - 7486");
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

}
