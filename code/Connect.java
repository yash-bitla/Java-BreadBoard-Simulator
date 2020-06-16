# Connect.java
package tryout;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class Connect extends JFrame implements ActionListener {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	JFrame frame;
	JLabel ptr[];
	JLabel spc[];
	JButton btn[];
	JPanel pnl[];
	
	private int Frame_Width;
	private int Frame_Height;
	private int Frame_X;
	private int Frame_Y;
	boolean flag;
	Cordinate src;
	
	Connect(Cordinate src) {
		this.src = src;
		flag = true;
	}

	public void init() {
		frame = new JFrame(); 
		Frame_Width = 480; Frame_Height = 450;
		Frame_X = 635; Frame_Y = 204;
		frame.setLayout(new FlowLayout());
		String s[] = {"Add IC", "Add Input", "Add Output", "Add Vcc", "Add Ground", "Connect another Pin"};
		
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
	
	//Create a generalized Frame:
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
				if(flag) {
					BreadBoard.board.setEnabled(true);
				}
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton prsd = (JButton)arg0.getSource();
		
		for(int i=0;i<6;i++) {
			if(prsd.equals(btn[i])) {
				call(i);
				break;
			}
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	Cordinate isFree() {
		Cordinate flag = null, tmp;
		int s, r, c;
		for(int pin=0;pin<14;pin++) {
			s = (pin<7)?1:0;
			r = (pin<7)?0:4;
			c = src.c + ((pin<7)?pin:(13-pin));
			tmp = new Cordinate(s, r, c);
			
			if(!BreadBoard.getPin(tmp).getIcon().toString().endsWith("pin.png")) {
				flag = tmp;
				break;
			}
		}
		
		return flag;
	}
	
	void call(int o) {
		JButton tmp;
		Icon icon;
		Cordinate pin;
		char ch;
		int v;
		
		try {
			switch(o) {
			case 0:
				if(src.s!=0 || src.r!=4) {
					display("An IC can only be added on the E Row.");
				} else if(o==0 && src.c>57) {
					display("An IC has 7 pin. Select a column less than 58.");
				} else {
					pin = isFree();
					if (pin!=null) {
						ch = (char)(65+pin.r+5*pin.s);
						display("Cannot place IC. Connection found at " + ch + "-" + pin.c + ".");
					} else {
						ICOption ob = new ICOption(src);
						flag = false;
						ob.init();
					}
				}
				break;
				
			case 1:
				v = 0;
				if(BreadBoard.checkValue(src, v)) {
					icon = new ImageIcon(ClassLoader.getSystemResource("breadboard_inpt.png"));
					BreadBoard.inpt.add(src);
					tmp = BreadBoard.getPin(src);
					tmp.setIcon(icon);
					BreadBoard.ofpin[src.s][src.r][src.c] = new Attribute(-2, v);
				} else {
					display("Error! Another Connection already Exists!");
				}
				
				break;
				
			case 2:
				v = 2;
				BreadBoard.checkValue(src, v);
				icon = new ImageIcon(ClassLoader.getSystemResource("breadboard_otpt.png"));
				BreadBoard.otpt.add(src);
				tmp = BreadBoard.getPin(src);
				tmp.setIcon(icon);
				BreadBoard.ofpin[src.s][src.r][src.c] = new Attribute(-2, v);
								
				break;
				
			case 3:
				v = 1;
				if(BreadBoard.checkValue(src, v)) {
					icon = new ImageIcon(ClassLoader.getSystemResource("breadboard_vcc.png"));
					BreadBoard.vcc.add(src);
					tmp = BreadBoard.getPin(src);
					tmp.setIcon(icon);
					BreadBoard.ofpin[src.s][src.r][src.c] = new Attribute(-2, v);
					
				} else {
					display("Error! Another Connection already Exists!");
				}
				break;
				
			case 4:
				v = -2;
				if(BreadBoard.checkValue(src, v)) {
					icon = new ImageIcon(ClassLoader.getSystemResource("breadboard_gnd.png"));
					BreadBoard.gnd.add(src);
					tmp = BreadBoard.getPin(src);
					tmp.setIcon(icon);
					BreadBoard.ofpin[src.s][src.r][src.c] = new Attribute(-2, v);
				} else {
					display("Error! Another Connection already Exists!");
				}
				break;
				
			case 5:
				flag = false;
				PinOption ob = new PinOption(src);
				ob.init();
			}
			
		} catch (Exception e) {
			System.out.println(o);
		}
		
	}

	public void display(String s) {
		JOptionPane.showMessageDialog(frame, s, "BREADBOARD", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
}
