package control;


import java.util.Vector;

import org.newdawn.slick.Input;

import control.KeyMapper.KeyEnum;
import data.Attributs;
import display.BottomBar;
import model.Game;
import model.Objet;
import model.Player;
import utils.ObjetsList;

public class InputObject implements java.io.Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 371933210691238615L;
	public int id;
	public int round;
	public int idplayer;

	// Spells
	public int idObjetMouse;
	public int idSpellLauncher;
	public ObjetsList spell;
	
	// Selection
	public Vector<Integer> selection = new Vector<Integer>();
	public Vector<Boolean> validated;
	public boolean toPlay;

	public boolean isOnMiniMap;

	private transient final  static  boolean debugTouche = false;

	public Vector<KeyEnum> down;
	public Vector<KeyEnum> pressed;
	public float x;
	public float y;

	public InputObject (){
		this.id= 0;
		this.idplayer = -1;
		this.round = 0;
		
		down = new Vector<KeyEnum>();
		pressed = new Vector<KeyEnum>();
		
		
		
		x = 0;
		y = 0;
		this.toPlay = false;
		this.isOnMiniMap = false;
		this.validated = new Vector<Boolean>();
	}
	
	public Vector<Objet> getSelection(){
		Vector<Objet> res = new Vector<Objet>();
		for(Integer i : selection){
			res.add(Game.g.plateau.getById(i));
		}
		return res;
	}

	public InputObject (int idplayer, Input input,boolean toPlay, KeyMapper km){
		this.id= Game.g.idInput;
		Game.g.idInput ++;
		this.idplayer = idplayer;
		this.round = Game.g.round;
		down = new Vector<KeyEnum>();
		pressed = new Vector<KeyEnum>();
		x = input.getMouseX();
		y = input.getMouseY();
		// IL y a surement plus simple et moins coûteux
		if(debugTouche){
			for(int i=0; i<250; i++){
				if(input.isKeyPressed(i)){
					System.out.println(i);
				}
			}
		}
		


		
		for(KeyEnum ke : km.mapping.keySet()){
			for(Integer i : km.mapping.get(ke)){
				
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
				pressed.addElement(km.mouseMapping.get(i));
			}
			if(input.isMouseButtonDown(i)){
				down.addElement(km.mouseMapping.get(i));
			}
		}
		// Spells
		if(pressed.size()>0){
			if(pressed.contains(KeyEnum.LeftClick) && Game.g.spellCurrent!=null){
				int i = Game.g.spellLauncher.getSpellsName().indexOf(Game.g.spellCurrent);
				if(Game.g.spellLauncher.getSpellState(i)>=Game.g.data.spells.get(Game.g.spellCurrent).getAttribut(Attributs.chargeTime)){
					this.spell = Game.g.spellCurrent;
					this.idSpellLauncher = Game.g.spellLauncher.id;
					if(Game.g.spellTarget!=null){
						this.idObjetMouse = Game.g.spellTarget.id;
					} else {
						this.idObjetMouse = -1;
					}
					Game.g.spellCurrent = null;
					Game.g.spellLauncher = null;
					Game.g.spellTarget = null;
				}
				pressed.remove(KeyEnum.LeftClick);
				down.remove(KeyEnum.LeftClick);
			} else {
				Game.g.spellCurrent = null;
				Game.g.spellLauncher = null;
				Game.g.spellTarget = null;
			}
			
		
			// ATTACK CLICK
			if(pressed.contains(KeyEnum.DeplacementOffensif) && !Game.g.attackClick){
				Game.g.attackClick = true;
				pressed.remove(KeyEnum.DeplacementOffensif);
			}
			else if(pressed.contains(KeyEnum.LeftClick) && Game.g.attackClick){
				pressed.remove(KeyEnum.LeftClick);
				Game.g.attackClick = false;
				pressed.add(KeyEnum.DeplacementOffensif);
			} else {
				Game.g.attackClick = false;
			}
			
			
		}
		this.toPlay = toPlay;

		this.x = input.getAbsoluteMouseX();
		this.y = input.getAbsoluteMouseY();

		this.isOnMiniMap = false;
		//TODO : check if on minimap
		
		
		Player player = null;
		if(idplayer>0 && idplayer<Game.g.players.size()){
			player = Game.g.players.get(idplayer);
		}
		if(player!=null && Game.g.bottomBar!=null){
			// checking if on minimap or not
			this.isOnMiniMap = this.x>(1-Game.g.bottomBar.ratioMinimapX)*Game.g.resX && this.y>(Game.g.resY-Game.g.bottomBar.ratioMinimapX*Game.g.resX) && this.x<Game.g.resX-2f && this.y<Game.g.resY-2f ;
			/// In case not in game, in "MENUMAPCHOICE"
			if(!Game.g.isInMenu){				
				this.isOnMiniMap = this.isOnMiniMap && Game.g.inputsHandler.getSelection(Game.g.currentPlayer.id).rectangleSelection==null;
			}

			// checking for the prod button in the action bar
			if(pressed.contains(KeyEnum.LeftClick) ){
				if(Game.g.bottomBar.action.toDrawDescription[0][0]){
					this.pressed.addElement(KeyEnum.Prod0);
				}
				if(Game.g.bottomBar.action.toDrawDescription[1][0]){
					this.pressed.addElement(KeyEnum.Prod1);
				}
				if(Game.g.bottomBar.action.toDrawDescription[2][0]){
					this.pressed.addElement(KeyEnum.Prod2);
				}
				if(Game.g.bottomBar.action.toDrawDescription[3][0]){
					this.pressed.addElement(KeyEnum.Prod3);
				}
				if(Game.g.bottomBar.action.toDrawDescription[0][1]){
					this.pressed.addElement(KeyEnum.Tech0);
				}
				if(Game.g.bottomBar.action.toDrawDescription[1][1]){
					this.pressed.addElement(KeyEnum.Tech1);
				}
				if(Game.g.bottomBar.action.toDrawDescription[2][1]){
					this.pressed.addElement(KeyEnum.Tech2);
				}
				if(Game.g.bottomBar.action.toDrawDescription[3][1]){
					this.pressed.addElement(KeyEnum.Tech3);
				}
			}
			boolean a = pressed.contains(KeyEnum.LeftClick);
			boolean b = down.contains(KeyEnum.LeftClick);
			if(a || b){
				for(int i=0; i<Game.g.bottomBar.action.toDrawDescription.length; i++){
					if(Game.g.bottomBar.action.toDrawDescription[i][0]){
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
		if(Game.g.isInMenu || Game.g.inEditor)
			return;

		this.x = input.getAbsoluteMouseX()+Game.g.Xcam;
		this.y = input.getAbsoluteMouseY()+Game.g.Ycam;

		if(isOnMiniMap){
			//			System.out.println("miniMap");
			BottomBar b = Game.g.bottomBar;
			this.x = (int) Math.floor((this.x-Game.g.Xcam-b.minimap.startX)/b.minimap.rw);
			this.y = (int) Math.floor((this.y-Game.g.Ycam-b.minimap.startY)/b.minimap.rh);
		}

		this.validated = new Vector<Boolean>();
		for(Player p : Game.g.players)
			validated.add(false);
		
		
		// Handle selection at the very end
		// Handle Selection
		Game.g.inputsHandler.updateSelection(this);
		
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

		return ""+this.round+"|"+this.idplayer+"|"+g.currentPlayer.id+"|";
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
		return this.pressed.contains(KeyEnum.valueOf("Prod"+i));
	}



}
