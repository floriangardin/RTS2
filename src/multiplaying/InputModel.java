package multiplaying;

import org.newdawn.slick.Input;

import model.Utils;

public class InputModel extends MultiObjetModel{

	public int team;

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
	public boolean isPressedLEFT;
	public boolean isPressedRIGHT;
	public boolean isPressedUP;
	public boolean isPressedDOWN;
	public boolean[] isPressedNumPad = new boolean[10];

	public int xMouse;
	public int yMouse;

	public InputModel (int time, int team, Input input, int Xcam,int Ycam){
		this.team = team;
		this.timeValue = time;


		this.rightClick = input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
		this.isPressedRightClick = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		this.leftClick = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		this.isPressedLeftClick = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		this.xMouse = input.getAbsoluteMouseX()-Xcam;
		this.yMouse = input.getAbsoluteMouseY()-Ycam;
		this.isPressedESC = input.isKeyPressed(Input.KEY_ESCAPE);
		this.isPressedMAJ = input.isKeyPressed(Input.KEY_LSHIFT) || input.isKeyPressed(Input.KEY_RSHIFT);
		this.isPressedCTRL = input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL);
		this.isPressedBACK = input.isKeyPressed(Input.KEY_BACK);
		this.isPressedDOT = input.isKeyPressed(Input.KEY_COMMA);
		this.isPressedENTER = input.isKeyPressed(Input.KEY_RETURN);
		this.isPressedUP = input.isKeyDown(Input.KEY_UP);
		this.isPressedLEFT = input.isKeyDown(Input.KEY_LEFT);
		this.isPressedRIGHT = input.isKeyDown(Input.KEY_RIGHT);
		this.isPressedDOWN = input.isKeyDown(Input.KEY_DOWN);
		
		
		
		for(int i=0; i<10;i++){
			this.isPressedNumPad[i] = input.isKeyPressed(i+2);
		}
	}
	
	public InputModel(String im){
		String[] vaneau = Utils.split(im, ' ');
		int intBuffer = 0;
		boolean boolBuffer = false;
		for(int i=0; i<vaneau.length; i++){
			if(i<4){
				intBuffer = Integer.parseInt(vaneau[i]);
			} else {
				boolBuffer = (vaneau[i].equals("true"));
			}
			switch(i){
			case 0: this.timeValue = intBuffer;break;
			case 1: this.team = intBuffer;break;
			case 2: this.xMouse = intBuffer;break;
			case 3: this.yMouse = intBuffer;break;
			case 4: this.leftClick = boolBuffer;break;
			case 5: this.rightClick = boolBuffer;break;
			case 6: this.isPressedLeftClick = boolBuffer;break;
			case 7: this.isPressedRightClick = boolBuffer;break;
			case 8: this.isPressedESC = boolBuffer;break;
			case 9: this.isPressedMAJ = boolBuffer;break;
			case 10: this.isPressedCTRL = boolBuffer;break;
			case 11: this.isPressedBACK = boolBuffer;break;
			case 12: this.isPressedDOT = boolBuffer;break;
			case 13: this.isPressedENTER = boolBuffer; break;
			default:
				this.isPressedNumPad[i-14] = boolBuffer;break;
			}
		}
	}

	public String toString(){
		String s = "0";
		s+=timeValue+" "+team+ " "+xMouse+" "+yMouse;
		s+=" "+leftClick + " " +rightClick+" "+isPressedLeftClick+" "+isPressedRightClick+" "+isPressedESC+" "+isPressedMAJ+" "+isPressedCTRL+
				" "+isPressedBACK+" "+isPressedDOT+" "+isPressedENTER;
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
		for(int i=0;i<10;i++){
			if(!this.isPressedNumPad[i]&& m2.isPressedNumPad[i])
				this.isPressedNumPad[i] = true;
		}
		this.xMouse = (this.xMouse+m2.xMouse)/2;
		this.yMouse = (this.yMouse+m2.yMouse)/2;
	}

}
