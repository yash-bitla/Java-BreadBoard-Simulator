# PinOption.java
package tryout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class PinOption extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame frame;
	JLabel intr;
	JLabel tmp1, tmp2, tmp3, tmp4;
	JLabel rlbl, clbl;
	JTextArea rin, cin;
	JButton sbmt, btn1, btn2;
	JPanel tpnl, rpnl, cpnl, bpnl;
	private int Frame_Width;
	private int Frame_Height;
	private int Frame_X;
	private int Frame_Y;
	Cordinate src, src1;
	
	PinOption(Cordinate src) {
		this.src = src;
		btn1 = BreadBoard.getPin(src);
		src1 = new Cordinate(0, 0, 0);
	}

	public void init() {
		frame = new JFrame();
		Frame_Width = 540; Frame_Height = 280;
		Frame_X = 600; Frame_Y = 280;
		frame.setLayout(new FlowLayout());
		
		intr = new JLabel("   Enter the co-ordinates of the breadboard:");
		intr.setFont(new Font("SansSerif", 0, 20));
		intr.setPreferredSize(new Dimension(450, 50));
		rlbl = new JLabel("  ROW");
		rlbl.setFont(new Font("SansSerif", 0, 18));
		rlbl.setPreferredSize(new Dimension(100, 25));
		clbl = new JLabel("COLUMN");
		clbl.setFont(new Font("SansSerif", 0, 18));
		clbl.setPreferredSize(new Dimension(100, 25));
		
		tmp1 = new JLabel("");
		tmp1.setPreferredSize(new Dimension(75, 25));
		tmp2 = new JLabel("");
		tmp2.setPreferredSize(new Dimension(75, 25));
		tmp3 = new JLabel("");
		tmp3.setPreferredSize(new Dimension(75, 25));
		tmp4 = new JLabel("");
		tmp4.setPreferredSize(new Dimension(75, 25));
		
		rin = new JTextArea();
		rin.setFont(new Font("SansSerif", 0, 18));
		rin.setPreferredSize(new Dimension(100, 25));
		cin = new JTextArea();
		cin.setFont(new Font("SansSerif", 0, 18));
		cin.setPreferredSize(new Dimension(100, 25));
		
		tpnl = new JPanel();
		rpnl = new JPanel();
		cpnl = new JPanel();
		bpnl = new JPanel();
		
		sbmt = new JButton("Submit");
		sbmt.setFont(new Font("SansSerif", 0, 21));
		sbmt.setPreferredSize(new Dimension(125, 30));
		sbmt.addActionListener(this);
		
		rpnl.setLayout(new FlowLayout());
		rpnl.setPreferredSize(new Dimension(250, 50));
		rpnl.add(rlbl);
		rpnl.add(rin);
		rpnl.setBorder(new EtchedBorder());
		
		cpnl.setLayout(new FlowLayout());
		cpnl.setPreferredSize(new Dimension(250, 50));
		cpnl.add(clbl);
		cpnl.add(cin);
		cpnl.setBorder(new EtchedBorder());
		
		bpnl.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		bpnl.setPreferredSize(new Dimension(250, 50));
		bpnl.add(sbmt, gbc);
		
		tpnl.setLayout(new FlowLayout());
		tpnl.setPreferredSize(new Dimension(450, 250));
		tpnl.add(intr);
		tpnl.add(tmp1);
		tpnl.add(rpnl);
		tpnl.add(tmp2);
		tpnl.add(tmp3);
		tpnl.add(cpnl);
		tpnl.add(tmp4);
		tpnl.add(bpnl);
		
		frame.add(tpnl);
		jFrame(frame);
	}
	
	//Create a generalized Frame:
	private void jFrame(final JFrame frame) {
		
		frame.setVisible(true);
		frame.setTitle("Connect Configuration");
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String rs, cs;
		rs = rin.getText();
		cs = cin.getText();
		boolean flag = false;
		
		try {
			src1.c = Integer.valueOf(cs);
		} catch(Exception e) {
			flag = true;
		}
		if(rs.length()==0) {
			display("Row should not be empty!");
			frame.requestFocus();
		} else if(rs.length()>1) {
			display("Row should be a single character!");
			frame.requestFocus();
		} else if(rs.charAt(0)<'A' || rs.charAt(0)>'J') {
			display("Row should be between A and J!");
			frame.requestFocus();
		} else if(flag) {
			display("Column should be a number!");
			frame.requestFocus();
		} else if(src1.c<0 || src1.c>63) {
			display("Column should be between 0-63");
			frame.requestFocus();
		} else {
			src1.r = (rs).charAt(0) - 65;
			if(src1.r>=5) {
				src1.s = 1;
				src1.r -= 5;
			}
			if((BreadBoard.getPin(src1)).getIcon()!=BreadBoard.icon) {
				display("Pin already connected.");
				frame.requestFocus();
			} else if(isRedundant()) {
				display("Redundant connection. Select another column!");
				frame.requestFocus();
			} else {
				addPins();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
	
	boolean isRedundant() {
		Attribute ob1, ob2;
		ob1 = getAttr(src);
		ob2 = getAttr(src1);
		
		return (ob1.link==ob2.link && ob1.link!=-1);
	}
	
	Attribute getAttr(Cordinate src) {
		Attribute ob = null;
		for(int i=0;i<5;i++) {
			ob = BreadBoard.ofpin[src.s][i][src.c];
			if(ob.link!=-1) {
				break;
			}
		}
		
		return ob;
	}
	
	void addPins() {
		int l = BreadBoard.wire.size();
		Attribute ob1 = getAttr(src), ob2 = getAttr(src1);
		Pins p1, p2;
		p1 = new Pins(src);
		p2 = new Pins(src1);
		p1.add(p2);
		if(ob1.link==-2 && ob2.link==-2) {
			display("Connection Failed! There exists different connections on both sides.");
		} else if(ob1.link==-2) {
			ob1.link = l;
			ob1.color = BreadBoard.getColor();
			addTo(ob1, p1, 0);
		} else if(ob2.link==-2) {
			ob2.link = l;
			ob2.color = BreadBoard.getColor();
			addTo(ob2, p1, 0);
		} else if(ob1.link==-1 && ob2.link==-1) {
			ob1 = new Attribute(l, BreadBoard.getColor());
			addTo(ob1, p1, 0);
		} else if(ob1.link==-1) {
			addTo(ob2, p1, 1);
		} else if(ob2.link==-1) {
			addTo(ob1, p1, 1);
		} else {
			l = ob2.link;
			p1.add(BreadBoard.wire.get(l));
			addTo(ob1, p1, 1);
			BreadBoard.wire.remove(l);
		}
	}
	
	void addTo(Attribute ob, Pins p, int k) {
		int l = ob.link;
		p.ob = ob;
		updateButton(p);
		BreadBoard.ofpin[src.s][src.r][src.c] = ob;
		BreadBoard.ofpin[src1.s][src1.r][src1.c] = ob;
		if(k==0) {
			BreadBoard.wire.add(p);
		} else {
			BreadBoard.wire.get(l).add(p);
		}
	}
	
	void updateButton(Pins p) {
		Color color = p.ob.color;
		JButton btn;
		while(p!=null) {
			btn = BreadBoard.getPin(p.src);
			btn.setBackground(color);
			btn.setContentAreaFilled(false);
			btn.setOpaque(true);
			btn.setIcon(null);
			p = p.next;
		}
	}
			
	public void display(String s) {
		JOptionPane.showMessageDialog(frame, s, "BREADBOARD", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
}
