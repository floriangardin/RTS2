package control;


import java.util.Vector;

import org.newdawn.slick.Input;

import control.KeyMapper.KeyEnum;
import display.Camera;
import model.Game;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public class InputObject implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 371933210691238615L;
	public int id;
	public int round;
	public int team;
	// Spells
	public int idObjetMouse=-1;
	public int idSpellLauncher;
	public ObjetsList spell;
	
	// Selection
	public Vector<Integer> selection = new Vector<Integer>();
	public Vector<Boolean> validated= new Vector<Boolean>();
	public boolean toPlay;
	public boolean isOnMiniMap;


	public Vector<KeyEnum> down= new Vector<KeyEnum>();
	public Vector<KeyEnum> pressed= new Vector<KeyEnum>();
	public float x, xOnScreen;
	public float y, yOnScreen;
	public long time;

	public InputObject (){

	}
	public InputObject(int team, int round){
		this.team = team;
		this.round = round;
	}
	
	public InputObject(Input input, int team, int round){
		initInput(input);
		this.toPlay = false;
		this.isOnMiniMap = false;
		this.validated = new Vector<Boolean>();
		this.team = team;
		this.round = round;
	}
	public InputObject(Input input){
		initInput(input);
		this.toPlay = false;
		this.isOnMiniMap = false;
		this.validated = new Vector<Boolean>();
	}
	
	public Vector<Objet> getSelection(Plateau plateau){
		Vector<Objet> res = new Vector<Objet>();
		for(Integer i : selection){
			res.add(plateau.getById(i));
		}
		return res;
	}
	
	public void rightClick(float x, float y){
		this.x = x;
		this.y = y;
		if(!this.pressed.contains(KeyEnum.RightClick)){			
			this.pressed.addElement(KeyEnum.RightClick);
		}
	}
	public void attack(float x2, float y2) {
		this.x = x2;
		this.y = y2;
		if(!this.pressed.contains(KeyEnum.DeplacementOffensif)){			
			this.pressed.addElement(KeyEnum.DeplacementOffensif);
		}
	}
//	public InputObject (int idplayer, Input input, boolean toPlay){
//		this.id= Communications.idInput;
//		Communications.idInput ++;
//		this.idplayer = idplayer;
//		this.round = Game.gameSystem.round;
//		down = new Vector<KeyEnum>();
//		pressed = new Vector<KeyEnum>();
//		x = input.getMouseX();
//		y = input.getMouseY();
//		// Il y a surement plus simple et moins coûteux
//		if(debugTouche){
//			for(int i=0; i<250; i++){
//				if(input.isKeyPressed(i)){
//					System.out.println(i);
//				}
//			}
//		}
//		
//		initInput(input);
//		// Spells
//		if(pressed.size()>0){
//			if(pressed.contains(KeyEnum.LeftClick) && Game.g.spellCurrent!=null){
//				int i = Game.g.spellLauncher.getSpellsName().indexOf(Game.g.spellCurrent);
//				if(Game.g.spellLauncher.getSpellState(i)>=Game.g.data.spells.get(Game.g.spellCurrent).getAttribut(Attributs.chargeTime)){
//					this.spell = Game.g.spellCurrent;
//					this.idSpellLauncher = Game.g.spellLauncher.id;
//					if(Game.g.spellTarget!=null){
//						this.idObjetMouse = Game.g.spellTarget.id;
//					} else {
//						this.idObjetMouse = -1;
//					}
//					Game.g.spellCurrent = null;
//					Game.g.spellLauncher = null;
//					Game.g.spellTarget = null;
//				}
//				pressed.remove(KeyEnum.LeftClick);
//				down.remove(KeyEnum.LeftClick);
//			} else {
//				Game.g.spellCurrent = null;
//				Game.g.spellLauncher = null;
//				Game.g.spellTarget = null;
//			}
//			
//		
//			// ATTACK CLICK
//			if(pressed.contains(KeyEnum.DeplacementOffensif) && !Game.g.attackClick){
//				Game.g.attackClick = true;
//				pressed.remove(KeyEnum.DeplacementOffensif);
//			}
//			else if(pressed.contains(KeyEnum.LeftClick) && Game.g.attackClick){
//				pressed.remove(KeyEnum.LeftClick);
//				Game.g.attackClick = false;
//				pressed.add(KeyEnum.DeplacementOffensif);
//			} else {
//				Game.g.attackClick = false;
//			}
//			
//			
//		}
//		this.toPlay = toPlay;
//
//		this.x = input.getAbsoluteMouseX();
//		this.y = input.getAbsoluteMouseY();
//
//		this.isOnMiniMap = false;
//		
//		Player player = null;
//		if(idplayer>0 && idplayer<Game.gameSystem.players.size()){
//			player = Game.gameSystem.players.get(idplayer);
//		}
//		if(player!=null && Game.g.bottomBar!=null){
//			// checking if on minimap or not
//			this.isOnMiniMap = this.x>(1-Game.g.bottomBar.ratioMinimapX)*Game.g.resX && this.y>(Game.g.resY-Game.g.bottomBar.ratioMinimapX*Game.g.resX) && this.x<Game.g.resX-2f && this.y<Game.g.resY-2f ;
//			/// In case not in game, in "MENUMAPCHOICE"
//			if(!Game.g.isInMenu){				
//				this.isOnMiniMap = this.isOnMiniMap && Game.g.inputsHandler.getSelection(Game.g.currentPlayer.id).rectangleSelection==null;
//			}
//
//			// checking for the prod button in the action bar
//			boolean a = pressed.contains(KeyEnum.LeftClick);
//			boolean b = down.contains(KeyEnum.LeftClick);
//			if(pressed.contains(KeyEnum.LeftClick) ){
//				// action bar
//				String s = "";
//				for(int i=0; i<2; i++){
//					s = (i==0 ? "Prod" : "Tech");
//					for(int j=0;j<Game.g.bottomBar.prodIconNbY;j++){
//						if(Game.g.bottomBar.toDrawDescription[j][i]){
//							this.pressed.addElement(KeyEnum.valueOf(s+j));
//							if(a){
//								pressed.remove(KeyEnum.LeftClick);
//							}
//							if(b){
//								pressed.remove(KeyEnum.LeftClick);
//							}
//						}
//					}
//				}
//				// topbar
//				if(Game.g.bottomBar.iconChoice!=null){
//					int i = 0;
//					for(Icon icon : Game.g.bottomBar.iconChoice){
//						if(icon.isMouseOnIt){
//							pressed.add(KeyEnum.valueOf("ActCard"+i));
//							if(a){
//								pressed.remove(KeyEnum.LeftClick);
//							}
//							if(b){
//								pressed.remove(KeyEnum.LeftClick);
//							}
//						}
//						i+=1;
//					}
//				}
//			}
//		}
//		if(Game.isInMenu() || Game.isInEditor())
//			return;
//
//		this.x = input.getAbsoluteMouseX()+Game.g.Xcam;
//		this.y = input.getAbsoluteMouseY()+Game.g.Ycam;
//
//		if(isOnMiniMap){
//			//			System.out.println("miniMap");
//			BottomBar b = Game.g.bottomBar;
//			this.x = (int) Math.floor((this.x-Game.g.Xcam-b.startXMiniMap)/b.ratioWidthMiniMap);
//			this.y = (int) Math.floor((this.y-Game.g.Ycam-b.startYMiniMap)/b.ratioHeightMiniMap);
//		}
//
//		this.validated = new Vector<Boolean>();
//		for(Player p : Game.g.players)
//			validated.add(false);
//		
//		// Handle selection at the very end
//		// Handle Selection
//		Game.g.inputsHandler.updateSelection(this);
//	}
	
	public void initInput(Input input){
		time = System.nanoTime();
		xOnScreen = input.getMouseX();
		yOnScreen = input.getMouseY();
		this.pressed = new Vector<KeyEnum>();
		this.down = new Vector<KeyEnum>();
		// Keyboard
		for(KeyEnum ke : KeyMapper.mapping.keySet()){
			for(Integer i : KeyMapper.mapping.get(ke)){
				if(input.isKeyPressed(i)){
					this.pressed.addElement(ke);
				}
				if(input.isKeyDown(i)){
					this.down.addElement(ke);
				}
			}
		}
		// Mouse
		for(int i = 0; i<3; i++){
			if(input.isMousePressed(i)){
				pressed.addElement(KeyMapper.mouseMapping.get(i));
			}
			if(input.isMouseButtonDown(i)){
				down.addElement(KeyMapper.mouseMapping.get(i));
			}
		}
		x = (input.getMouseX() + Camera.Xcam)/Game.ratioX;
		y = (input.getMouseY() + Camera.Ycam)/Game.ratioY;
	}
	

	public void validate(int player){
		if(validated.size()>player){
			validated.set(player,true);
//			if(Game.debugValidation)
//				System.out.println("InputObjet line 240, validation de player "+player.id+"round : "+this.round);
		}
	}

//	public void validate(){
//		/** Validate everything (when it comes from other player)
//		 * 
//		 */
//		for(int i=0; i<validated.size();i++){
//			validated.set(i,true);
//		}
//	}
//
//	public String getMessageValidationToSend(Game g) {
//
//		return ""+this.round+"|"+this.idplayer+"|"+g.currentPlayer.id+"|";
//	}

	public boolean isValidated() {
		int n = 0;
		for(Boolean b: this.validated){
			if(b)
				n++;
		}
		return n>=this.validated.size();
	}

	public void eraseLetter(){
		this.pressed.clear();
		this.down.clear();
	}
	
	public void reset(){
		x = (Game.resX/2 + Camera.Xcam)/Game.ratioX;
		y = (Game.resY/2 + Camera.Ycam)/Game.ratioY;
		this.pressed.clear();
		this.down.clear();
	}


	public boolean isPressed(KeyEnum ke){
		return this.pressed.contains(ke);
	}

	public boolean isDown(KeyEnum ke){
		return this.down.contains(ke);
	}

	public boolean isPressedProd(int i){
		try{
			return this.pressed.contains(KeyEnum.valueOf("Prod"+i));
		} catch(IllegalArgumentException a){
			return false;
		}
	}
	
	public void pressProd(int i){
		if(!this.pressed.contains(KeyEnum.valueOf("Prod"+i))){
			this.pressed.add(KeyEnum.valueOf("Prod"+i));
		}
	}
	
	public boolean isPressedTech(int i){
		try{
			return this.pressed.contains(KeyEnum.valueOf("Tech"+i));
		} catch(IllegalArgumentException a){
			return false;
		}
	}
	
	public void pressTech(int i){
		if(!this.pressed.contains(KeyEnum.valueOf("Tech"+i))){
			this.pressed.add(KeyEnum.valueOf("Tech"+i));
		}
	}




}
