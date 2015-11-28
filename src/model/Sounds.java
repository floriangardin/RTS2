package model;

import java.util.Vector;

import org.newdawn.slick.Music;
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
	public Vector<Sound> orderSpearman;
	public Vector<Sound> attackSpearman;
	public Vector<Sound> deathSpearman;
	public Vector<Sound> selectionSpearman;
	public Vector<Sound> orderCrossbowman;
	public Vector<Sound> attackCrossbowman;
	public Vector<Sound> deathCrossbowman;
	public Vector<Sound> selectionCrossbowman;
	//public Sound lackRessources;

	// menu
	public Sound menuMouseOverItem;
	public Sound menuItemSelected;

	public Music soundDefeat;
	public Music soundVictory;

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

			// units sounds
			this.getSoundByName(attackSpearman, "attaqueSpearman");
			this.getSoundByName(deathSpearman, "mortSpearman");
			this.getSoundByName(selectionSpearman, "selectionSpearman");
			orderSpearman = attackSpearman;
			this.getSoundByName(attackCrossbowman, "attaqueCrossbowman");
			this.getSoundByName(deathCrossbowman, "mortCrossbowman");
			this.getSoundByName(selectionCrossbowman, "selectionCrossbowman");
			orderCrossbowman = attackCrossbowman;


			soundDefeat = new Music("music/music_defeat.ogg");
			soundVictory = new Music("music/music_victory.ogg");
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

	public void getSoundByName(Vector<Sound> v, String type){
		v = new Vector<Sound>();
		int number = 1;
		while(true){
			String s = type;
			if(number<10)
				s+="0"+number;
			else
				s+=number;
			try{
				v.addElement(new Sound("music/soundsUnit/"+s+".ogg"));
			} catch (SlickException e){
				break;
			}
		}
	}





}
