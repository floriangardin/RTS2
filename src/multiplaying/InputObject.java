package multiplaying;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Input;

import display.BottomBar;
import model.ActionObjet;
import model.Game;
import model.Player;

public class InputObject extends MultiObjetModel{

	public Player player;
	public int round;

	private Vector<Boolean> validated;

	public boolean rightClick;
	public boolean leftClick;

	public boolean pressedRightClick;
	public boolean pressedLeftClick;
	public int id;
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


	public InputObject (Game g, Player player, Input input){
		this.id= g.idInput;
		g.idInput ++;
		this.player = player;
		this.round = g.round;
		this.rightClick = input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
		this.pressedRightClick = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		this.leftClick = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		this.pressedLeftClick = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		this.xMouse = input.getAbsoluteMouseX()+(int)g.plateau.Xcam;
		this.yMouse = input.getAbsoluteMouseY()+(int)g.plateau.Ycam;
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
		for(ActionObjet c: g.plateau.selection.get(g.plateau.currentPlayer.id))
			selection.add(c.id);

		// Only for current player at the creation of the input
		BottomBar bb = g.plateau.currentPlayer.bottomBar;
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
		this.validated = new Vector<Boolean>();
		for(Player p:g.plateau.players)
			validated.add(false);
	}

	public InputObject(String im, Game game){
		HashMap<String,String> content = new HashMap<String, String>(); 
		String[] vaneau = im.split(",");
		selection = new Vector<Integer>();
		for(int i=0; i<vaneau.length-1; i++){
			content.put(vaneau[i].split(":")[0],vaneau[i].split(":")[1]);
		}
		this.id = Integer.parseInt(content.get("id"));
		this.player = game.getPlayerById(Integer.parseInt(content.get("idP")));
		this.round = Integer.parseInt(content.get("rnd"));
		this.xMouse = Integer.parseInt(content.get("xM"));
		this.yMouse = Integer.parseInt(content.get("yM"));
		if(content.containsKey("rC"))
			rightClick = true;
		if(content.containsKey("lC"))
			leftClick = true;
		if(content.containsKey("pRC")) pressedRightClick= true;
		if(content.containsKey("pLC")) pressedLeftClick= true;

		if(content.containsKey("ESC")) isPressedESC= true;
		if(content.containsKey("MAJ")) isPressedMAJ= true;
		if(content.containsKey("CTR")) isPressedCTRL= true;
		if(content.containsKey("BCK")) isPressedBACK= true;
		if(content.containsKey("DOT")) isPressedDOT= true;
		if(content.containsKey("ENT")) isPressedENTER= true;
		if(content.containsKey("TAB")) isPressedTAB= true;
		if(content.containsKey("LFT")) isPressedLEFT= true;
		if(content.containsKey("RGT")) isPressedRIGHT= true;
		if(content.containsKey("UP")) isPressedUP= true;
		if(content.containsKey("DWN")) isPressedDOWN= true;
		if(content.containsKey("A")) isPressedA= true;
		if(content.containsKey("B")) isPressedB= true;

		if(content.containsKey("P0")) isPressedProd0= true;
		if(content.containsKey("P1")) isPressedProd1= true;
		if(content.containsKey("P2")) isPressedProd2= true;
		if(content.containsKey("P3")) isPressedProd3= true;

		for(int i=0; i<10; i++){
			if(content.containsKey(i+"")) isPressedNumPad[i] = true;
		}

//		if(content.containsKey("sel")){
//			String[] sel = content.get("sel").split("_");
//			for(int i=0; i<sel.length;i++){
//				this.selection.addElement(Integer.parseInt(sel[i]));
//			}
//		}
		this.validated = new Vector<Boolean>();
		for(Player p:game.plateau.players)
			validated.add(false);
		if(Game.debugValidation)
			System.out.println("InputObject line 157 : New input from "+this.player.id+" coming from round "+this.round);
	}

	public String toString(){
		//Add header with type message, round , player;
		String s = "3I";
		s+="idP:" +player.id+ ",rnd:"+round+",xM:"+xMouse+",yM:"+yMouse;
		s+=",id:"+this.id;
		if(rightClick)
			s+=",rC: ";
		if(leftClick)
			s+=",lC: ";

		if(pressedRightClick) s+=",pRC: ";
		if(pressedLeftClick) s+=",pLC: ";

		if(isPressedESC) s+=",ESC: ";
		if(isPressedMAJ) s+=",MAJ: ";
		if(isPressedCTRL) s+=",CTR: ";
		if(isPressedBACK) s+=",BCK: ";
		if(isPressedDOT) s+=",DOT: ";
		if(isPressedENTER) s+=",ENT: ";
		if(isPressedTAB) s+=",TAB: ";
		if(isPressedLEFT) s+=",LFT: ";
		if(isPressedRIGHT) s+=",RGT: ";
		if(isPressedUP) s+=",UP: ";
		if(isPressedDOWN) s+=",DWN: ";

		if(isPressedProd0) s+=",P0: ";
		if(isPressedProd1) s+=",P1: ";
		if(isPressedProd2) s+=",P2: ";
		if(isPressedProd3) s+=",P3: ";
		if(isPressedA) s+=",A: ";
		if(isPressedB) s+=",B: ";

		for(int i=0; i<10; i++)
			s+=(isPressedNumPad[i] ? ","+i+": " : "");
		return s;
	}

	
	public void validate(Player player){
		if(validated.size()>player.id){
			validated.set(player.id-1,true);
			if(Game.debugValidation)
				System.out.println("InputObjet line 240, validation de player "+player.id+"round : "+this.round);
		}
		else{
			if(Game.debugValidation)
				System.out.println("InputObjet line 242, putain glandu");
		}
	}

	public void validate(){
		/** Validate everything (when it comes from other player)
		 * 
		 */
		for(int i=0; i<validated.size();i++){
			validated.set(i,true);
		}
	}

	public String getMessageValidationToSend() {
		
		return "3V|"+this.id+"|"+this.player.id+"|";
	}

	public boolean isValidated() {
		int n = 0;
		for(Boolean b: this.validated){
			if(b)
				n++;
		}
		return n>=this.validated.size()-2;
	}

}
