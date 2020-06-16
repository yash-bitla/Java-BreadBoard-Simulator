# Display.java
package tryout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Display extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
	int linpt, lotpt;
	
	JLabel lblinpt[];
	JButton btninpt[];
	boolean flag[];
	JPanel pnlinpt[];
	JPanel inpt[];
	JPanel input;
	
	JLabel lblotpt[];
	JLabel tfotpt[];
	JPanel pnlotpt[];
	JPanel otpt[];
	JPanel output;
	
	private int Frame_Width;
	private int Frame_Height;
	private int Frame_X;
	private int Frame_Y;
	
	Display(int i, int o) {
		linpt = i;
		lotpt = o;

		lblinpt = new JLabel[linpt];
		btninpt = new JButton[linpt];
		flag = new boolean[linpt];
		inpt = new JPanel[linpt];
		pnlinpt = new JPanel[linpt];
		input = new JPanel();
		
		lblotpt = new JLabel[lotpt];
		tfotpt = new JLabel[lotpt];
		otpt = new JPanel[lotpt];
		pnlotpt = new JPanel[lotpt];
		output = new JPanel();
	}
	
	String getCordinate(Cordinate src) {
		char ch = (char)(src.s*5 + src.r + 65);
		String s = ch + "-" + src.c;
		return s;
	}
	
	public void init() {
		int wd = Math.max(linpt, lotpt);
		wd = (wd<=8)?wd:(wd-1)%8+1;
		int ht = ((linpt-1)/8) + ((lotpt-1)/8) + 2; 
		frame = new JFrame(); 
		Frame_Width = 175*wd; Frame_Height = 175*ht + 75;
		Frame_X = 960 - Frame_Width/2; Frame_Y = 150;

		FlowLayout fl = new FlowLayout();
		fl.setVgap(25);
		frame.setLayout(fl);
		input.setLayout(new FlowLayout());
		output.setLayout(new FlowLayout());
		
		for(int i=0;i<linpt;i++) {
			lblinpt[i] = new JLabel("INPUT at " + getCordinate(BreadBoard.inpt.get(i)) + ":");
			lblinpt[i].setFont(new Font("SansSerif", 0, 20));
			lblinpt[i].setHorizontalAlignment(JLabel.CENTER);
			lblinpt[i].setPreferredSize(new Dimension(150, 30));
			btninpt[i] = new JButton();
			btninpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("input_off.png")));
			btninpt[i].setPreferredSize(new Dimension(50, 50));
			btninpt[i].addActionListener(this);
			inpt[i] = new JPanel();
			inpt[i].setLayout(new FlowLayout());
			inpt[i].add(btninpt[i]);
			pnlinpt[i] = new JPanel();
			pnlinpt[i].setLayout(new GridLayout(2, 1));
			pnlinpt[i].add(lblinpt[i]);
			pnlinpt[i].add(inpt[i]);
			frame.add(pnlinpt[i]);
		}
		
		for(int i=0;i<lotpt;i++) {
			lblotpt[i] = new JLabel("OUTPUT at " + getCordinate(BreadBoard.otpt.get(i)) + ":");
			lblotpt[i].setFont(new Font("SansSerif", 0, 16));
			lblotpt[i].setHorizontalAlignment(JLabel.CENTER);
			lblotpt[i].setPreferredSize(new Dimension(150, 30));
			tfotpt[i] = new JLabel();
			tfotpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("output_off.png")));
			tfotpt[i].setPreferredSize(new Dimension(100, 122));
			otpt[i] = new JPanel();
			otpt[i].setLayout(new FlowLayout());
			otpt[i].add(tfotpt[i]);
			pnlotpt[i] = new JPanel();
			pnlotpt[i].setLayout(new FlowLayout());
			pnlotpt[i].setPreferredSize(new Dimension(150, 175));
			pnlotpt[i].add(lblotpt[i]);
			pnlotpt[i].add(otpt[i]);
			frame.add(pnlotpt[i]);
		}
		int i=0;
		for(Cordinate src : BreadBoard.otpt) {
			int tmp = BreadBoard.getValue(src);
			if(tmp==1) {
				tfotpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("output_on.png")));
			} else {
				tfotpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("output_off.png")));
			}
			i++;
		}
		
		jFrame(frame);
	}
	
	private void jFrame(final JFrame frame) {
		
		frame.setVisible(true);
		frame.setTitle("Pin Configuration");
		frame.setSize(Frame_Width, Frame_Height);
		frame.setLocation(Frame_X, Frame_Y);
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				System.out.println(frame.getLocationOnScreen());
				System.out.println(frame.getSize());
			}
		});
	
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
		Cordinate isrc;
		JButton prsd = (JButton) arg0.getSource();
		int i = 0, tmp;
		
		for(JButton btn : btninpt) {
			if(btn.equals(prsd)) {
				 if(!flag[i]) {
					 btninpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("input_on.png")));
					 isrc = BreadBoard.inpt.get(i);
					 BreadBoard.ofpin[isrc.s][isrc.r][isrc.c].value = 1;
					 flag[i] = true;

				 } else {
					 btninpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("input_off.png")));
					 isrc = BreadBoard.inpt.get(i);
					 BreadBoard.ofpin[isrc.s][isrc.r][isrc.c].value = 0;
					 flag[i] = false;
				 }
				 break;
			}
			i++;
		}
		BreadBoard.execute();
		
		i=0;
		for(Cordinate src : BreadBoard.otpt) {
			tmp = BreadBoard.getValue(src);
			if(tmp==1) {
				tfotpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("output_on.png")));
			} else {
				tfotpt[i].setIcon(new ImageIcon(ClassLoader.getSystemResource("output_off.png")));
			}
			i++;
		}
		
	}

}
