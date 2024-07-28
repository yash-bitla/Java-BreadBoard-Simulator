# Gates.java
package tryout;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

class ICGate {

	Cordinate src;
	
	boolean isGnd() {
		Cordinate tmp = new Cordinate(1, 0, src.c+6);
		
		if(BreadBoard.getValue(tmp)==-2) {
			return true;
		}
		return false;
	}
	
	boolean check(int ch) {
		boolean flag = true;
		Attribute tmp[] = new Attribute[3];
		int i, s, r, c, t, k;
		t = (ch==0)?7408 : ((ch==1)?7432 : ((ch==3)?7400 : 7486));
		k = 0;
		if(BreadBoard.getValue(src)!=1) {
			display("Pin 14 should have Vcc connected of IC " + t);
			return false;
		}

		if(!isGnd()) {
			display("Pin 7 should have Ground connected of IC " + t);
			return false;
		}
		
		for(i=0;i<13;i+=3) {
			if(i==6) {
				i++;
			}

			s = (i<7)?1:0;
			r = (i<7)?0:4;
			
			for(int j=0;j<3;j++) {
				c = src.c + ((i+j<7)?i+j:(13-i-j));
				tmp[j] = BreadBoard.ofpin[s][r][c];
			}
			
			if(tmp[0].link!=-1 && tmp[1].link!=-1 && tmp[2].link!=-1){
				if(tmp[0].value!=-1 && tmp[1].value!=-1 && tmp[2].value!=-1) {
					calculate(i, ch);
				}
				k++;
			} else if(!(tmp[0].link==-1 && tmp[1].link==-1 && tmp[2].link==-1)) {
				display("Incomplete Connection at Gate " + (((i+1)/4)+1) + " of IC " + t);
				return false;
			}
		}
		
		if(k==0) {
			display("Redundant IC " + t);
			return false;
		}
		return flag;
	}
	
	void calculate(int pin, int ch) {
		Attribute ob;
		int s, r, c, input1, input2;
		s = (pin<7)?1:0;
		r = (pin<7)?0:4;
		c = src.c + ((pin<7)?pin:(13-pin));
		
		if(pin<7) {
			input1 = BreadBoard.getValue(new Cordinate(s, r, c));
			input2 = BreadBoard.getValue(new Cordinate(s, r, c+1));
			ob = BreadBoard.ofpin[s][r][c+2];
			ob.link = -2;
			ob.value = getOutput(input1, input2, ch);
			BreadBoard.addValue(new Cordinate(s, r, c+2), ob.value);
		} else {
			input1 = BreadBoard.getValue(new Cordinate(s, r, c-2));
			input2 = BreadBoard.getValue(new Cordinate(s, r, c-1));
			ob = BreadBoard.ofpin[s][r][c];
			ob.link = -2;
			ob.value = getOutput(input1, input2, ch);
			BreadBoard.addValue(new Cordinate(s, r, c), ob.value);
		}
	}
	
	int getOutput(int i1, int i2, int ch) {
		boolean input1 = (i1==1);
		boolean input2 = (i2==1);
		boolean output = false;
		
		switch(ch) {
		case 0:
			output = input1&&input2;
			break;
			
		case 1:
			output = input1||input2;
			break;
			
		case 3:
			output = !(input1&&input2);
			break;
			
		case 4:
			output = !(input1||input2);
			break;
			
		case 5:
			output = (input1!=input2);
		}
		
		return (output)?1:0;
	}
	
	int getOutput(int i) {
		boolean input = (i==1);
		boolean output = !input;
		
		return (output)?1:0;
	}

	void link(int pin) {
		int s, r, c, tmp;
		String url = "breadboard_pin";
		url += ((pin<7)?"up":"down") + ".png";
		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(url));
		s = (pin<7)?1:0;
		r = (pin<7)?0:4;
		c = src.c + ((pin<7)?pin:(13-pin));
		
		Attribute ob = null;
		BreadBoard.pin[s][r][c].setIcon(icon);
		
		for(int i=0;i<5;i++) {
			tmp = r + i*((pin<7)?1:-1);
			ob = BreadBoard.ofpin[s][tmp][c];
			
			if(ob.link!=-1) {
				break;
			}
		}
		
		BreadBoard.ofpin[s][r][c] = ob;
	}
	
	public void display(String s) {
		JOptionPane.showMessageDialog(null, s, "BREADBOARD", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
}

class AND extends ICGate{
	
	AND(Cordinate src) {
		this.src = src;
	}
	
	void init() {
		for(int i=0;i<14;i++) {
			link(i);
		}
	}

	void start() {
		init();
		BreadBoard.canDisplay = BreadBoard.canDisplay && check(0);
	}

}

class OR extends ICGate{
	
	OR(Cordinate src) {
		this.src = src;
	}

	void init() {
		for(int i=0;i<14;i++) {
			link(i);
		}
	}

	void start() {
		init();
		BreadBoard.canDisplay = BreadBoard.canDisplay && check(1);
	}

}

class NOT extends ICGate{
	
	NOT(Cordinate src) {
		this.src = src;
	}

	void init() {
		for(int i=0;i<14;i++) {
			link(i);
		}
	}

	void start() {
		init();
		BreadBoard.canDisplay = BreadBoard.canDisplay && check();
	}

	boolean check() {
		boolean flag = true;
		Attribute tmp[] = new Attribute[2];
		int i, s, r, c, k=0;
		
		if(BreadBoard.getValue(src)!=1) {
			display("Pin 14 should have Vcc connected of IC 7404");
			return false;
		}

		if(!isGnd()) {
			display("Pin 7 should have Ground connected of IC 7404");
			return false;
		}
		
		for(i=0;i<13;i+=2) {
			if(i==6) {
				i++;
			}

			s = (i<7)?1:0;
			r = (i<7)?0:4;
			
			for(int j=0;j<2;j++) {
				c = src.c + ((i+j<7)?i+j:(13-i-j));
				tmp[j] = BreadBoard.ofpin[s][r][c];
			}
			
			if(tmp[0].link!=-1 && tmp[1].link!=-1) {
				if(tmp[0].value!=-1 && tmp[1].value!=-1) {
					calculate(i);
				}
				k++;
			} else if(!(tmp[0].link==-1 && tmp[1].link==-1)) {
				display("Incomplete Connection at Gate " + ((i/2)+1) + " of IC 7404");
				return false;
			} else {
				tmp[(i<7)?1 : 0].value = 3;
			}
		}
		if(k==0) {
			display("Redundant IC 7404");
			return false;
		}
		return flag;
	}

	void calculate(int pin) {
		Attribute ob;
		int s, r, c, input;
		s = (pin<7)?1:0;
		r = (pin<7)?0:4;
		c = src.c + ((pin<7)?pin:(13-pin));
		
		if(pin<7) {
			input = BreadBoard.getValue(new Cordinate(s, r, c));
			ob = BreadBoard.ofpin[s][r][c+1];
			ob.link = -2;
			ob.value = getOutput(input);
			BreadBoard.addValue(new Cordinate(s, r, c+1), ob.value);
		} else {
			input = BreadBoard.getValue(new Cordinate(s, r, c-1));
			ob = BreadBoard.ofpin[s][r][c];
			ob.link = -2;
			ob.value = getOutput(input);
			BreadBoard.addValue(new Cordinate(s, r, c), ob.value);
		}
	}
	
}

class NAND extends ICGate{
	
	NAND(Cordinate src) {
		this.src = src;
	}
	
	void init() {
		for(int i=0;i<14;i++) {
			link(i);
		}
	}

	void start() {
		init();
		BreadBoard.canDisplay = BreadBoard.canDisplay && check(3);
	}

}

class NOR extends ICGate{
	
	NOR(Cordinate src) {
		this.src = src;
	}

	void init() {
		for(int i=0;i<14;i++) {
			link(i);
		}
	}

	void start() {
		init();
		BreadBoard.canDisplay = BreadBoard.canDisplay && check();
	}

	boolean check() {
		boolean flag = true;
		Attribute tmp[] = new Attribute[3];
		int i, s, r, c, k=0;
		
		if(BreadBoard.getValue(src)!=1) {
			display("Pin 14 should have Vcc connected of IC 7402");
			return false;
		}

		if(!isGnd()) {
			display("Pin 7 should have Ground connected of IC 7402");
			return false;
		}
		
		for(i=0;i<13;i+=3) {
			if(i==6) {
				i++;
			}

			s = (i<7)?1:0;
			r = (i<7)?0:4;
			
			for(int j=0;j<3;j++) {
				c = src.c + ((i+j<7)?i+j:(13-i-j));
				tmp[j] = BreadBoard.ofpin[s][r][c];
			}
			
			if(tmp[0].link!=-1 && tmp[1].link!=-1 && tmp[2].link!=-1){
				if(tmp[0].value!=-1 && tmp[1].value!=-1 && tmp[2].value!=-1) {
					calculate(i);
				}
				k++;
			} else if(!(tmp[0].link==-1 && tmp[1].link==-1 && tmp[2].link==-1)) {
				display("Incomplete Connection at Gate " + (((i+1)/4)+1) + " of IC 7402");
				return false;
			} else {
				tmp[(i<7)?0 : 2].value = 3;
			}
		}
		if(k==0) {
			display("Redundant IC 7402");
			return false;
		}
		
		return flag;
	}

	void calculate(int pin) {
		Attribute ob;
		int s, r, c, input1, input2;
		s = (pin<7)?1:0;
		r = (pin<7)?0:4;
		c = src.c + ((pin<7)?pin:(13-pin));
		
		if(pin<7) {
			input1 = BreadBoard.getValue(new Cordinate(s, r, c-2));
			input2 = BreadBoard.getValue(new Cordinate(s, r, c-1));
			ob = BreadBoard.ofpin[s][r][c];
			ob.link = -2;
			ob.value = getOutput(input1, input2, 4);
			BreadBoard.addValue(new Cordinate(s, r, c), ob.value);
		} else {
			input1 = BreadBoard.getValue(new Cordinate(s, r, c));
			input2 = BreadBoard.getValue(new Cordinate(s, r, c+1));
			ob = BreadBoard.ofpin[s][r][c+2];
			ob.link = -2;
			ob.value = getOutput(input1, input2, 4);
			BreadBoard.addValue(new Cordinate(s, r, c+2), ob.value);
		}
	}
	
}

class XOR extends ICGate{
	
	XOR(Cordinate src) {
		this.src = src;
	}
	
	void init() {
		for(int i=0;i<14;i++) {
			link(i);
		}
	}

	void start() {
		init();
		BreadBoard.canDisplay = BreadBoard.canDisplay && check(5);
	}

}

