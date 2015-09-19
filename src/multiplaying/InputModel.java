package multiplaying;

import java.util.Vector;

import org.newdawn.slick.Input;

import model.ActionObjet;
import model.Plateau;
import model.Utils;

public class InputModel extends MultiObjetModel{

	public int team;

	public int resX;
	public int resY;
	public int Xcam;
	public int Ycam;

	public boolean rightClick;
	public boolean leftClick;

	public boolean isPressedRightClick;
	public boolean isPressedLeftClick;

	public boolean isPressedESC;
	public boolean isPressedMAJ;
	public boolean isPressedCTRL;
	public boolean isPressedBACK;
	public boolean isPressedDOT;
	public boolean isPressedENTER;
	public boolean isPressedTAB;
	public boolean isPressedLEFT;
	public boolean isPressedRIGHT;
	public boolean isPressedUP;
	public boolean isPressedDOWN;

	public boolean isPressedW;
	public boolean isPressedX;
	public boolean isPressedC;
	public boolean isPressedV;

	public boolean[] isPressedNumPad = new boolean[10];

	public Vector<Integer> selection;

	public int xMouse;
	public int yMouse;

	public InputModel (int time, int team, Input input, Plateau p){
		this.team = team;
		this.timeValue = time;
		this.resX = (int)p.g.resX;
		this.resY = (int)p.g.resY;
		this.Xcam = (int)p.Xcam;
		this.Ycam = (int)p.Ycam;

		this.rightClick = input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
		this.isPressedRightClick = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		this.leftClick = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		this.isPressedLeftClick = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		this.xMouse = input.getAbsoluteMouseX()+Xcam;
		this.yMouse = input.getAbsoluteMouseY()+Ycam;
		this.isPressedESC = input.isKeyPressed(Input.KEY_ESCAPE);
		this.isPressedMAJ = input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyPressed(Input.KEY_RSHIFT);
		this.isPressedCTRL = input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL);
		this.isPressedBACK = input.isKeyPressed(Input.KEY_BACK);
		this.isPressedDOT = input.isKeyPressed(Input.KEY_COMMA);
		this.isPressedENTER = input.isKeyPressed(Input.KEY_RETURN);
		this.isPressedTAB = input.isKeyPressed(Input.KEY_TAB);
		this.isPressedUP = input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_Z);
		this.isPressedLEFT = input.isKeyDown(Input.KEY_LEFT)|| input.isKeyDown(Input.KEY_Q);
		this.isPressedRIGHT = input.isKeyDown(Input.KEY_RIGHT)|| input.isKeyDown(Input.KEY_D);
		this.isPressedDOWN = input.isKeyDown(Input.KEY_DOWN)|| input.isKeyDown(Input.KEY_S);
		this.isPressedW = input.isKeyPressed(Input.KEY_W);
		this.isPressedX = input.isKeyPressed(Input.KEY_X);
		this.isPressedC = input.isKeyPressed(Input.KEY_C);
		this.isPressedV = input.isKeyPressed(Input.KEY_V);

		this.selection = new Vector<Integer>();
		for(ActionObjet c: p.selection.get(team)){
			this.selection.addElement(c.id);
		}
		for(int i=0; i<10;i++){
			this.isPressedNumPad[i] = input.isKeyPressed(i+2);
		}
	}

	public InputModel(String im){
		//System.out.println(im);
		String[] vaneau = Utils.split(im, ' ');
		this.selection = new Vector<Integer>();
		int intBuffer = 0;
		boolean boolBuffer = false;
		for(int i=0; i<vaneau.length; i++){
			if(i!=23){
				if(i<8){
					intBuffer = Integer.parseInt(vaneau[i]);
				} else {
					boolBuffer = (vaneau[i].equals("true"));
				}
			}
			switch(i){
			case 0: this.timeValue = intBuffer;break;
			case 1: this.team = intBuffer;break;
			case 2: this.xMouse = intBuffer;break;
			case 3: this.yMouse = intBuffer;break;
			case 4: this.resX = intBuffer;break;
			case 5: this.resY = intBuffer;break;
			case 6: this.Xcam = intBuffer;break;
			case 7: this.Ycam = intBuffer;break;
			case 8: this.leftClick = boolBuffer;break;
			case 9: this.rightClick = boolBuffer;break;
			case 10: this.isPressedLeftClick = boolBuffer;break;
			case 11: this.isPressedRightClick = boolBuffer;break;
			case 12: this.isPressedESC = boolBuffer;break;
			case 13: this.isPressedMAJ = boolBuffer;break;
			case 14: this.isPressedCTRL = boolBuffer;break;
			case 15: this.isPressedBACK = boolBuffer;break;
			case 16: this.isPressedDOT = boolBuffer;break;
			case 17: this.isPressedENTER = boolBuffer; break;
			case 18: this.isPressedTAB = boolBuffer; break;
			case 19: this.isPressedW = boolBuffer;break;
			case 20: this.isPressedX = boolBuffer;break;
			case 21: this.isPressedC = boolBuffer;break;
			case 22: this.isPressedV = boolBuffer; break;
			case 23: 
				String[] v = Utils.split(vaneau[23],'|');
				//System.out.println(v);
				//System.out.println(vaneau[23]);
				for(String s:v)
					this.selection.addElement(Integer.parseInt(s));
				break;
			default:
				this.isPressedNumPad[i-24] = boolBuffer;break;
			}
		}
	}

	public String toString(){
		String s = "0";
		s+=timeValue+" "+team+ " "+xMouse+" "+yMouse+" "+resX+" "+resY+" "+Xcam+" "+Ycam;
		s+=" "+leftClick + " " +rightClick+" "+isPressedLeftClick+" "+isPressedRightClick+" "+isPressedESC+" "+isPressedMAJ+" "+isPressedCTRL+
				" "+isPressedBACK+" "+isPressedDOT+" "+isPressedENTER+" "+isPressedTAB+" "+isPressedW+" "+isPressedX+" "+isPressedC+" "+isPressedV+" ";
		for(Integer i: this.selection)
			s+=i+"|";
		for(int i=0; i<10;i++){
			s+= " " + this.isPressedNumPad[i];
		}
		return s;
	}

	public void mix(InputModel m2){
		if(!this.isPressedCTRL && m2.isPressedCTRL)
			this.isPressedCTRL = true;
		if(!this.isPressedESC && m2.isPressedESC)
			this.isPressedESC = true;
		if(!this.isPressedMAJ && m2.isPressedMAJ)
			this.isPressedMAJ = true;
		if(!this.isPressedTAB && m2.isPressedTAB)
			this.isPressedTAB = true;
		if(!this.isPressedENTER && m2.isPressedENTER)
			this.isPressedENTER = true;
		if(!this.isPressedDOT && m2.isPressedDOT)
			this.isPressedDOT = true;
		if(!this.isPressedLeftClick && m2.isPressedLeftClick)
			this.isPressedLeftClick = true;
		if(!this.isPressedRightClick && m2.isPressedRightClick)
			this.isPressedRightClick = true;
		if(!this.leftClick && m2.leftClick)
			this.leftClick = true;
		if(!this.rightClick && m2.rightClick)
			this.rightClick = true;
		if(!this.isPressedW && m2.isPressedW)
			this.isPressedW = true;
		if(!this.isPressedX && m2.isPressedX)
			this.isPressedX = true;
		if(!this.isPressedC && m2.isPressedC)
			this.isPressedC = true;
		if(!this.isPressedV && m2.isPressedV)
			this.isPressedV = true;
		for(int i=0;i<10;i++){
			if(!this.isPressedNumPad[i]&& m2.isPressedNumPad[i])
				this.isPressedNumPad[i] = true;
		}
	}

}
