package control;


import java.util.Vector;

import org.newdawn.slick.Input;

import control.KeyMapper.KeyEnum;
import display.Camera;
import model.Game;
import plateau.Building;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;
import plateau.Character;

public strictfp class InputObject implements java.io.Serializable{
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

	public boolean toPlay;
	public boolean isOnMiniMap;


	public Vector<KeyEnum> down= new Vector<KeyEnum>();
	public Vector<KeyEnum> pressed= new Vector<KeyEnum>();
	public float x, xOnScreen;
	public float y, yOnScreen;
	

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
		
		this.team = team;
		this.round = round;
	}
	public InputObject(Input input){
		initInput(input);
		this.toPlay = false;
		this.isOnMiniMap = false;
		
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

	
	public void initInput(Input input){
		xOnScreen = input.getMouseX();
		yOnScreen = input.getMouseY();
		this.pressed = new Vector<KeyEnum>();
		this.down = new Vector<KeyEnum>();
//		for(int i=0; i<255; i++){
//			try{
//				if(input.isKeyDown(i)){
//					System.out.println(i);
//				}
//			} catch(Exception e){
//				
//			}
//		}
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
		

	public void eraseLetter(){
		this.pressed.clear();
		this.down.clear();
	}
	
	public void reset(){
		x = (Game.resX/2 + Camera.Xcam)/Game.ratioX;
		y = (Game.resY/2 + Camera.Ycam)/Game.ratioY;
		xOnScreen = Game.resX/2;
		yOnScreen = Game.resY/2;
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
	public void sanitizeInput(Plateau plateau) {
		Vector<Integer> toRemove = new Vector<Integer>();
		for(Integer i : this.selection){
			if(plateau.getById(i)==null || !(plateau.getById(i) instanceof Character || plateau.getById(i) instanceof Building )){
				toRemove.add(i);
			}
		}
		this.selection.removeAll(toRemove);
		
		if(plateau.getById(this.idObjetMouse)==null || !(plateau.getById(this.idObjetMouse) instanceof Character || plateau.getById(this.idObjetMouse) instanceof Building )){
			this.idObjetMouse = -1;
		}
		if(plateau.getById(this.idSpellLauncher)==null || !(plateau.getById(this.idSpellLauncher) instanceof Character || plateau.getById(this.idSpellLauncher) instanceof Building )){
			this.idSpellLauncher = -1;
		}
		
	}




}
