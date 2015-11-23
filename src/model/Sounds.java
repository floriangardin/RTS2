package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
// STORE ALL THE SOUNDS
	
	
	public Sound sword;
	public Sound arrow;
	public Sound fireball;
	public Sound death;
	public Sound buzz;
	public Sound bonus;
	public Sound order;
	//public Sound lackRessources;
	
	// menu
	public Sound menuMouseOverItem;
	public Sound menuItemSelected;
	
	public Sounds(){
		
		try {
			sword=new Sound("music/sword.ogg");
			arrow=new Sound("music/arrow.ogg");
			fireball= new Sound("music/fireball.ogg");
			death=new Sound("music/death.ogg");
			menuMouseOverItem = new Sound("music/menuMouseOverItem.ogg");
			menuItemSelected = new Sound("music/menuItemSelected.ogg");
			buzz = new Sound("music/menuItemSelected.ogg");
			bonus = new Sound("music/bonusound.ogg");
			order = new Sound("music/yes.ogg");
			//lackRessources = new Sound("music/lackRessources.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Sound getByName(String weapon) {
		switch(weapon){
		case("sword"):
		case("spear"):
			return this.sword;
		case("bow"):
			return this.arrow;
		case("wand"):
			return this.fireball;
		}
		return null;
	}
	


	
	
}
