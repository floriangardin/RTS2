package multiplaying;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Input;

import display.BottomBar;
import model.ActionObjet;
import model.Game;
import model.Utils;

public class InputModel extends MultiObjetModel{

	public int team;

	public boolean rightClick;
	public boolean leftClick;

	public boolean pressedRightClick;
	public boolean pressedLeftClick;

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

		this.rightClick = input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
		this.pressedRightClick = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		this.leftClick = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		this.pressedLeftClick = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
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
			//.println(mouseOnItem);
			if(pressedLeftClick){
				switch(mouseOnItem){
				case 0: isPressedProd0 = true;pressedLeftClick = false; break;
				case 1: isPressedProd1 = true;pressedLeftClick = false;break;
				case 2: isPressedProd2 = true;pressedLeftClick = false;break;
				case 3: isPressedProd3 = true;pressedLeftClick = false;break;
				default:
				} 
			}
		}
		for(int i=0; i<10;i++){
			this.isPressedNumPad[i] = input.isKeyPressed(i+2);
		}
	}

	public InputModel(String im){
		HashMap<String,String> content = new HashMap<String, String>(); 
		String[] vaneau = im.split(",");
		selection = new Vector<Integer>();
		for(int i=0; i<vaneau.length-1; i++){
			content.put(vaneau[i].split(":")[0],vaneau[i].split(":")[1]);
		}
		this.team = Integer.parseInt(content.get("team"));
		this.xMouse = Integer.parseInt(content.get("xMouse"));
		this.yMouse = Integer.parseInt(content.get("yMouse"));
		if(content.containsKey("rightClick"))
			rightClick = true;
		if(content.containsKey("leftClick"))
			leftClick = true;
		if(content.containsKey("pressedRightClick")) pressedRightClick= true;
		if(content.containsKey("pressedLeftClick")) pressedLeftClick= true;

		if(content.containsKey("ESC")) isPressedESC= true;
		if(content.containsKey("MAJ")) isPressedMAJ= true;
		if(content.containsKey("CTRL")) isPressedCTRL= true;
		if(content.containsKey("BACK")) isPressedBACK= true;
		if(content.containsKey("DOT")) isPressedDOT= true;
		if(content.containsKey("ENTER")) isPressedENTER= true;
		if(content.containsKey("TAB")) isPressedTAB= true;
		if(content.containsKey("LEFT")) isPressedLEFT= true;
		if(content.containsKey("RIGHT")) isPressedRIGHT= true;
		if(content.containsKey("UP")) isPressedUP= true;
		if(content.containsKey("DOWN")) isPressedDOWN= true;
		if(content.containsKey("A")) isPressedA= true;
		if(content.containsKey("B")) isPressedB= true;

		if(content.containsKey("Prod0")) isPressedProd0= true;
		if(content.containsKey("Prod1")) isPressedProd1= true;
		if(content.containsKey("Prod2")) isPressedProd2= true;
		if(content.containsKey("Prod3")) isPressedProd3= true;

		for(int i=0; i<10; i++){
			if(content.containsKey(i+"")) isPressedNumPad[i] = true;
		}

		if(content.containsKey("selection")){
			String[] sel = content.get("selection").split("_");
			for(int i=0; i<sel.length;i++){
				this.selection.addElement(Integer.parseInt(sel[i]));
			}
		}
	}

	public String toString(){
		String s = "";
		s+="0team:" + team+ ",xMouse:"+xMouse+",yMouse:"+yMouse;

		if(rightClick)
			s+=",rightClick: ";
		if(leftClick)
			s+=",leftClick: ";

		if(pressedRightClick) s+=",pressedRightClick: ";
		if(pressedLeftClick) s+=",pressedLeftClick: ";

		if(isPressedESC) s+=",ESC: ";
		if(isPressedMAJ) s+=",MAJ: ";
		if(isPressedCTRL) s+=",CTRL: ";
		if(isPressedBACK) s+=",BACK: ";
		if(isPressedDOT) s+=",DOT: ";
		if(isPressedENTER) s+=",ENTER: ";
		if(isPressedTAB) s+=",TAB: ";
		if(isPressedLEFT) s+=",LEFT: ";
		if(isPressedRIGHT) s+=",RIGHT: ";
		if(isPressedUP) s+=",UP: ";
		if(isPressedDOWN) s+=",DOWN: ";

		if(isPressedProd0) s+=",Prod0: ";
		if(isPressedProd1) s+=",Prod1: ";
		if(isPressedProd2) s+=",Prod2: ";
		if(isPressedProd3) s+=",Prod3: ";
		if(isPressedA) s+=",A: ";
		if(isPressedB) s+=",B: ";

		for(int i=0; i<10; i++)
			s+=(isPressedNumPad[i] ? ","+i+": " : "");


		if(this.selection.size()>0){
			s+=",selection:";
			for(Integer i : this.selection)
				s+=i+"_";
		}
		s+=",";
		return s;
	}



}
