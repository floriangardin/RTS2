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
	
	public boolean isPressedQ;
	public boolean isPressedD;
	public boolean isPressedS;
	public boolean isPressedZ;

	public boolean isPressedProd0;
	public boolean isPressedProd1;
	public boolean isPressedProd2;
	public boolean isPressedProd3;
	public boolean isPressedMinimap;
	public boolean isPressedB;
	public boolean isPressedF;
	public boolean isPressedA;
	public boolean isPressedE;
	public boolean isPressedR;
	public boolean isPressedH;
	public boolean isPressedSuppr;
	public boolean isPressedPause;
	public boolean isPressedImmolation;
	public boolean isPressedF1 ;
	public boolean isPressedF2;
	public boolean isPressedF3;
	public boolean isPressedF4;
	public boolean isPressedRallyPoint;
	public int letterPressed;
	
	
	public boolean toPlay ;
	public boolean[] isPressedNumPad = new boolean[10];

	public int xMouse;
	public int yMouse;

	public Vector<Integer> selection;
	public boolean isOnMiniMap;


	public InputObject (Game g, Player player, Input input,boolean toPlay){
		this.id= g.idInput;
		g.idInput ++;
		this.player = player;
		this.round = g.round;
		this.rightClick = input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
		this.pressedRightClick = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		this.leftClick = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
		this.pressedLeftClick = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		this.isPressedESC = input.isKeyPressed(Input.KEY_ESCAPE);
		this.isPressedMAJ = input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyPressed(Input.KEY_RSHIFT);
		this.isPressedCTRL = input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL);
		this.isPressedBACK = input.isKeyPressed(Input.KEY_BACK);
		this.isPressedDOT = input.isKeyPressed(Input.KEY_COMMA);
		this.isPressedENTER = input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_NUMPADENTER);
		this.isPressedTAB = input.isKeyPressed(Input.KEY_TAB);
		
		this.isPressedUP = input.isKeyDown(Input.KEY_UP);
		this.isPressedLEFT = input.isKeyDown(Input.KEY_LEFT);
		this.isPressedRIGHT = input.isKeyDown(Input.KEY_RIGHT);
		this.isPressedDOWN = input.isKeyDown(Input.KEY_DOWN);
		
		this.isPressedZ = input.isKeyDown(Input.KEY_Z);
		this.isPressedQ = input.isKeyDown(Input.KEY_Q);
		this.isPressedD = input.isKeyDown(Input.KEY_D);
		this.isPressedS = input.isKeyDown(Input.KEY_S);
		
		this.isPressedProd0 = input.isKeyPressed(Input.KEY_W);
		this.isPressedProd1 = input.isKeyPressed(Input.KEY_X);
		this.isPressedProd2 = input.isKeyPressed(Input.KEY_C);
		this.isPressedProd3 = input.isKeyPressed(Input.KEY_V);
		
		this.isPressedMinimap = input.isKeyDown(Input.KEY_SPACE)||input.isKeyDown(Input.KEY_LMENU);
		this.isPressedB = input.isKeyDown(Input.KEY_B);
		this.isPressedF = input.isKeyDown(Input.KEY_F);
		this.isPressedA = input.isKeyPressed(Input.KEY_A);
		this.isPressedE = input.isKeyPressed(Input.KEY_E);
		this.isPressedR = input.isKeyPressed(Input.KEY_R);
		this.isPressedH = input.isKeyPressed(Input.KEY_H);
		this.isPressedF1 = input.isKeyPressed(Input.KEY_F1);
		this.isPressedF2 = input.isKeyPressed(Input.KEY_F2);
		this.isPressedF3 = input.isKeyPressed(Input.KEY_F3);
		this.isPressedF4 = input.isKeyPressed(Input.KEY_F4);
		this.isPressedRallyPoint = input.isKeyPressed(Input.KEY_R);
		
		this.isPressedSuppr = input.isKeyPressed(Input.KEY_U);
		this.isPressedPause = input.isKeyPressed(Input.KEY_F10);
		this.isPressedImmolation = input.isKeyPressed(Input.KEY_I);
		this.toPlay = toPlay;

		for(int i=0; i<10;i++){
			this.isPressedNumPad[i] = input.isKeyPressed(i+2);
		}
		this.xMouse = input.getAbsoluteMouseX();
		this.yMouse = input.getAbsoluteMouseY();
		
		this.isOnMiniMap = false;
		if(player !=null && player.bottomBar!=null){
			this.isOnMiniMap = this.xMouse>(1-player.bottomBar.ratioMinimapX)*g.resX && this.yMouse>(g.resY-player.bottomBar.ratioMinimapX*g.resX) && this.xMouse<g.resX-2f && this.yMouse<g.resY-2f ;
			this.isOnMiniMap = this.isOnMiniMap && g.plateau.rectangleSelection.get(g.currentPlayer.id)==null;
		}
		if(g.isInMenu || g.inEditor)
			return;
		
		this.xMouse = input.getAbsoluteMouseX()+g.plateau.Xcam;
		this.yMouse = input.getAbsoluteMouseY()+g.plateau.Ycam;
		
		if(isOnMiniMap){
//			System.out.println("miniMap");
			BottomBar b = player.bottomBar;
			this.xMouse = (int) Math.floor((this.xMouse-g.plateau.Xcam-b.minimap.startX)/b.minimap.rw);
			this.yMouse = (int) Math.floor((this.yMouse-g.plateau.Ycam-b.minimap.startY)/b.minimap.rh);
		} else {
//			System.out.println("pas MiniMap");
		}
		this.validated = new Vector<Boolean>();
		for(Player p:g.players)
			validated.add(false);
	}

	public InputObject(String im, Game game){
		HashMap<String,String> content = new HashMap<String, String>(); 
		String[] vaneau = im.split(",");
		//System.out.println("InputObjet line 112: " + im);
		selection = new Vector<Integer>();
		for(int i=0; i<vaneau.length; i++){
			content.put(vaneau[i].split(":")[0],vaneau[i].split(":")[1]);
		}
		this.id = Integer.parseInt(content.get("id"));
		this.player = game.getPlayerById(Integer.parseInt(content.get("idP")));
		this.round = Integer.parseInt(content.get("rnd"));
		this.xMouse = Integer.parseInt((content.get("xM")));
		this.yMouse = Integer.parseInt((content.get("yM")));
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
		if(content.containsKey("A")) isPressedMinimap= true;
		if(content.containsKey("B")) isPressedB= true;
		if(content.containsKey("F")) isPressedF= true;
		if(content.containsKey("T")) isPressedA= true;
		if(content.containsKey("E")) isPressedE= true;
		if(content.containsKey("R")) isPressedR= true;
		if(content.containsKey("H")) isPressedH= true;
		if(content.containsKey("U")) isPressedSuppr= true;
		if(content.containsKey("pause")) isPressedPause= true;
		
		if(content.containsKey("P0")) isPressedProd0= true;
		if(content.containsKey("P1")) isPressedProd1= true;
		if(content.containsKey("P2")) isPressedProd2= true;
		if(content.containsKey("P3")) isPressedProd3= true;
		if(content.containsKey("IMO")) isPressedImmolation = true;
		
		if(content.containsKey("F1")) isPressedF1 = true;
		if(content.containsKey("F2")) isPressedF2 = true;
		if(content.containsKey("F3")) isPressedF3 = true;
		if(content.containsKey("F4")) isPressedF4 = true;
		if(content.containsKey("R")) isPressedR = true;
		
		toPlay= true;
		System.out.println(im);
		for(int i=0; i<10; i++){
			if(content.containsKey(i+"")){ 
				isPressedNumPad[i] = true;
				System.out.println(i);
			}
		}
		//System.out.println("InputObjet line 152: " + this.toString());

//		if(content.containsKey("sel")){
//			String[] sel = content.get("sel").split("_");
//			for(int i=0; i<sel.length;i++){
//				this.selection.addElement(Integer.parseInt(sel[i]));
//			}
//		}
		this.validated = new Vector<Boolean>();
		for(Player p:game.players)
			validated.add(false);
		if(Game.debugValidation)
			System.out.println("InputObject line 157 : New input from "+this.player.id+" coming from round "+this.round);
	}

	public String toString(){
		//Add header with type message, round , player;
		String s = "";
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
		if(isPressedMinimap) s+=",A: ";
		if(isPressedB) s+=",B: ";
		if(isPressedF) s+=",F: ";
		if(isPressedA) s+=",T: ";
		if(isPressedE) s+=",E: ";
		if(isPressedR) s+=",R: ";
		if(isPressedH) s+=",H: ";
		if(isPressedSuppr) s+=",U: ";
		if(isPressedPause) s+=",pause: ";
		if(isPressedImmolation) s+=",IMO: ";
		if(isPressedF1) s+=",F1: ";
		if(isPressedF2) s+=",F2: ";
		if(isPressedF3) s+=",F3: ";
		if(isPressedF4) s+=",F4: ";
		if(isPressedR) s+=",R: ";
		
		if(toPlay) s+=",toPlay: ";
		for(int i=0; i<10; i++)
			s+=(isPressedNumPad[i] ? ","+i+": " : "");
		s+=",";
		return s;
	}

	
	public void validate(Player player){
		if(validated.size()>player.id){
			validated.set(player.id-1,true);
			if(Game.debugValidation)
				System.out.println("InputObjet line 240, validation de player "+player.id+"round : "+this.round);
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

	public String getMessageValidationToSend(Game g) {
		
		return ""+this.round+"|"+this.player.id+"|"+g.currentPlayer.id+"|";
	}

	public boolean isValidated() {
		int n = 0;
		for(Boolean b: this.validated){
			if(b)
				n++;
		}
		return n>=this.validated.size()-2;
	}
	
	public void eraseLetter(){
		this.isPressedUP = false;
		this.isPressedLEFT = false;
		this.isPressedRIGHT = false;
		this.isPressedDOWN = false;
		this.isPressedProd0 = false;
		this.isPressedProd1 = false;
		this.isPressedProd2 = false;
		this.isPressedProd3 = false;
		this.isPressedMinimap = false;
		this.isPressedB = false;
		this.isPressedF = false;
		this.isPressedA = false;
		this.isPressedE = false;
		this.isPressedR = false;
		this.isPressedImmolation = false;
		this.isPressedZ = false;
		this.isPressedS = false;
		this.isPressedQ = false;
		this.isPressedD = false;
		for(int i=0; i<10; i++)
			this.isPressedNumPad[i]=false;
	}

}
