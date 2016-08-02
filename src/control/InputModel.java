package control;


import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import control.KeyMapper.KeyEnum;
import display.BottomBar;
import model.Game;
import model.Objet;
import model.Player;

public class InputModel implements java.io.Serializable{

	/**
	 * Handle selection rectangle ( it is part of inputs at the end ...)
	 */
	
	public int id;
	public int round;
	public Player player;
	
	public Vector<Boolean> validated;
	public boolean toPlay;
	
	public boolean isOnMiniMap;
	
	public Vector<KeyEnum> down;
	public Vector<KeyEnum> pressed;
	public float x;
	public float y;
	
	

	
	public InputModel (Game g, Player player, Input input,boolean toPlay, KeyMapper km){
		this.id= g.idInput;
		g.idInput ++;
		this.player = player;
		this.round = g.round;
		down.clear();
		pressed.clear();
		x = input.getMouseX();
		y = input.getMouseY();
		// IL y a surement plus simple et moins coûteux
		for(Integer i : km.mapping.keySet()){
			if(input.isKeyPressed(i)){
				this.pressed.addElement(km.mapping.get(i));
			}
			if(input.isKeyDown(i)){
				this.down.addElement(km.mapping.get(i));
			}
		}
		// Mouse
		for(int i = 0; i<3; i++){
			if(input.isMousePressed(i)){
				pressed.addElement(km.mapping.get(i));
			}
			if(input.isMouseButtonDown(i)){
				down.addElement(km.mapping.get(i));
			}
		}
		this.toPlay = toPlay;

		
		this.x = input.getAbsoluteMouseX();
		this.y = input.getAbsoluteMouseY();
		
		this.isOnMiniMap = false;
		//TODO : check if on minimap
		if(player !=null && player.bottomBar!=null){
			// checking if on minimap or not
			this.isOnMiniMap = this.x>(1-player.bottomBar.ratioMinimapX)*g.resX && this.y>(g.resY-player.bottomBar.ratioMinimapX*g.resX) && this.x<g.resX-2f && this.y<g.resY-2f ;
			this.isOnMiniMap = this.isOnMiniMap && g.plateau.rectangleSelection.get(g.currentPlayer.id)==null;
			// checking for the prod button in the action bar
			if(pressed.contains(KeyEnum.LeftClick)){
				if(player.bottomBar.action.toDrawDescription[0]){
					this.pressed.addElement(KeyEnum.Prod0);
				}
				if(player.bottomBar.action.toDrawDescription[1]){
					this.pressed.addElement(KeyEnum.Prod0);
				}
				if(player.bottomBar.action.toDrawDescription[2]){
					this.pressed.addElement(KeyEnum.Prod0);
				}
				if(player.bottomBar.action.toDrawDescription[3]){
					this.pressed.addElement(KeyEnum.Prod0);
				}
			}
			boolean a = pressed.contains(KeyEnum.LeftClick);
			boolean b = down.contains(KeyEnum.LeftClick);
			if(a || b){
				for(int i=0; i<player.bottomBar.action.toDrawDescription.length; i++){
					if(player.bottomBar.action.toDrawDescription[i]){
						if(a){
							pressed.remove(KeyEnum.LeftClick);
						}
						if(b){
							pressed.remove(KeyEnum.LeftClick);
						}
					}
				}
			}
		}
		if(g.isInMenu || g.inEditor)
			return;
		
		this.x = input.getAbsoluteMouseX()+g.plateau.Xcam;
		this.y = input.getAbsoluteMouseY()+g.plateau.Ycam;
		
		if(isOnMiniMap){
//			System.out.println("miniMap");
			BottomBar b = player.bottomBar;
			this.x = (int) Math.floor((this.x-g.plateau.Xcam-b.minimap.startX)/b.minimap.rw);
			this.y = (int) Math.floor((this.y-g.plateau.Ycam-b.minimap.startY)/b.minimap.rh);
		}
		
		this.validated = new Vector<Boolean>();
		for(Player p:g.players)
			validated.add(false);
	}

	public static InputModel create(byte[]){
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
		if(content.containsKey("SPACE")) isPressedSpecial = true;
		
		toPlay= true;
		for(int i=0; i<10; i++){
			if(content.containsKey(i+"")){ 
				isPressedNumPad[i] = true;
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
		if(isPressedSpecial) s+=",SPACE: ";
		
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
