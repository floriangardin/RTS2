package multiplaying;

import java.util.Vector;

import org.newdawn.slick.Input;

import display.BottomBar;
import model.ActionObjet;
import model.Game;
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

	public boolean isPressedProd0;
	public boolean isPressedProd1;
	public boolean isPressedProd2;
	public boolean isPressedProd3;
	public boolean isPressedA;
	public boolean isPressedB;
	public boolean[] isPressedNumPad = new boolean[10];

	public int xMouse;
	public int yMouse;
	
	public Vector<Integer> selection;

	public InputModel (Game g, int time, int team, Input input, int Xcam,int Ycam, int resX, int resY){
		this.team = team;
		this.timeValue = time;
		this.resX = resX;
		this.resY = resY;
		this.Xcam = Xcam;
		this.Ycam = Ycam;

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
		this.isPressedProd0 = input.isKeyPressed(Input.KEY_W);
		this.isPressedProd1 = input.isKeyPressed(Input.KEY_X);
		this.isPressedProd2 = input.isKeyPressed(Input.KEY_C);
		this.isPressedProd3 = input.isKeyPressed(Input.KEY_V);
		this.isPressedA = input.isKeyDown(Input.KEY_A);
		this.isPressedB = input.isKeyDown(Input.KEY_B);

		selection = new Vector<Integer>();
		for(ActionObjet c: g.plateau.selection.get(g.currentPlayer))
			selection.add(c.id);
		
		// Only for current player at the creation of the input
		BottomBar bb = g.players.get(g.currentPlayer).bottomBar;
		float relativeXMouse = input.getAbsoluteMouseX();
		float relativeYMouse = input.getAbsoluteMouseY();
		if(relativeXMouse>bb.action.x && relativeXMouse<bb.action.x+bb.action.icoSizeX && relativeYMouse>bb.action.y && relativeYMouse<bb.action.y+bb.action.sizeY){
			int mouseOnItem = (int)((relativeYMouse-bb.action.y)/(bb.action.sizeY/bb.action.prodIconNb));
			//System.out.println(mouseOnItem);
			if(isPressedLeftClick){
				switch(mouseOnItem){
				case 0: isPressedProd0 = true;isPressedLeftClick = false; break;
				case 1: isPressedProd1 = true;isPressedLeftClick = false;break;
				case 2: isPressedProd2 = true;isPressedLeftClick = false;break;
				case 3: isPressedProd3 = true;isPressedLeftClick = false;break;
				default:
				} 
			}
		}
		for(int i=0; i<10;i++){
			this.isPressedNumPad[i] = input.isKeyPressed(i+2);
		}
	}

	public InputModel(String im){
		String[] vaneau = Utils.split(im, ' ');
		int intBuffer = 0;
		boolean boolBuffer = false;
		String[] sel = new String[0];;
		selection = new Vector<Integer>();
		for(int i=0; i<vaneau.length; i++){
			if(i<8){
				intBuffer = Integer.parseInt(vaneau[i]);
			} else if(i<8) {
				boolBuffer = (vaneau[i].equals("true"));
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
			case 19: this.isPressedProd0 = boolBuffer;break;
			case 20: this.isPressedProd1 = boolBuffer;break;
			case 21: this.isPressedProd2 = boolBuffer;break;
			case 22: this.isPressedProd3 = boolBuffer; break;
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:
			case 32:
				this.isPressedNumPad[i-23] = boolBuffer;break;
			case 33:
				sel = vaneau[i].split(",");
			}
		}
		for(int i=0; i<sel.length;i++){
			this.selection.addElement(i);
		}
	}

	public String toString(){
		String s = "0";
		s+=timeValue+" "+team+ " "+xMouse+" "+yMouse+" "+resX+" "+resY+" "+Xcam+" "+Ycam;
		s+=" "+leftClick + " " +rightClick+" "+isPressedLeftClick+" "+isPressedRightClick+" "+isPressedESC+" "+isPressedMAJ+" "+isPressedCTRL+
				" "+isPressedBACK+" "+isPressedDOT+" "+isPressedENTER+" "+isPressedTAB+" "+isPressedProd0+" "+isPressedProd1+" "+isPressedProd2+" "+isPressedProd3;
		for(int i=0; i<10;i++){
			s+= " " + this.isPressedNumPad[i];
		}
		s+=" ";
		for(Integer i : selection)
			s+=i+",";
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
		if(!this.isPressedProd0 && m2.isPressedProd0)
			this.isPressedProd0 = true;
		if(!this.isPressedProd1 && m2.isPressedProd1)
			this.isPressedProd1 = true;
		if(!this.isPressedProd2 && m2.isPressedProd2)
			this.isPressedProd2 = true;
		if(!this.isPressedProd3 && m2.isPressedProd3)
			this.isPressedProd3 = true;
		for(int i=0;i<10;i++){
			if(!this.isPressedNumPad[i]&& m2.isPressedNumPad[i])
				this.isPressedNumPad[i] = true;
		}
	}

}
