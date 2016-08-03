package control;


import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;
import org.newdawn.slick.Input;

import control.KeyMapper.KeyEnum;
import display.BottomBar;
import model.Game;
import model.Player;

public class InputObject implements java.io.Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 371933210691238615L;
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

	public InputObject (){
		this.id= 0;
		this.player = null;
		this.round = 0;
		down = new Vector<KeyEnum>();
		pressed = new Vector<KeyEnum>();
		x = 0;
		y = 0;
		this.toPlay = false;
		this.isOnMiniMap = false;
		this.validated = new Vector<Boolean>();
	}

	public InputObject (Game g, Player player, Input input,boolean toPlay, KeyMapper km){
		this.id= g.idInput;
		g.idInput ++;
		this.player = player;
		this.round = g.round;
		down = new Vector<KeyEnum>();
		pressed = new Vector<KeyEnum>();
		x = input.getMouseX();
		y = input.getMouseY();
		// IL y a surement plus simple et moins coûteux
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
