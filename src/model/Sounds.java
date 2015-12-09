package model;

import java.io.File;
import java.io.FileNotFoundException;
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
	public Sound fire;
	
	//Messages
	public Sound chatMessage;
	public Sound buildingTaken;
	public Sound barrackSound;
	public Sound headQuartersSound;
	public Sound millSound;
	public Sound mineSound;
	public Sound stableSound;
	public Sound academySound;
	public Sound universitySound;
	public Sound towerSound;
	
	
	public Sound messageWrong;
	public Sound noRoomSound;
	public Sound noRessourcesSound;
	public Sound unitCreated;
	public Sound techDiscovered;
	public Sound underAttack;
	
	
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
			fire = new Sound("music/fire.ogg");



			soundDefeat = new Music("music/music_defeat.ogg");
			soundVictory = new Music("music/music_victory.ogg");
			
			//BUILDING
			this.headQuartersSound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.stableSound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.barrackSound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.academySound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.universitySound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.millSound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.mineSound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			this.towerSound = new Sound("music/soundsBuildings/selectionHeadQuarters.ogg");
			
			//MESSAGES
			this.messageWrong = new Sound("music/messages/messageWrong.ogg");
			this.techDiscovered = new Sound("music/messages/techDiscovered.ogg");
			this.buildingTaken = new Sound("music/messages/techDiscovered.ogg");
			//lackRessources = new Sound("music/lackRessources.ogg");
		} catch (SlickException e) {

		}

		attackSpearman = new Vector<Sound>();
		deathSpearman = new Vector<Sound>();
		selectionSpearman = new Vector<Sound>();
		attackCrossbowman = new Vector<Sound>();
		deathCrossbowman = new Vector<Sound>();
		selectionCrossbowman = new Vector<Sound>();

		// units sounds
		this.getSoundByName(attackSpearman, "attaqueSpearman");
		this.getSoundByName(deathSpearman, "mortSpearman");
		this.getSoundByName(selectionSpearman, "selectionSpearman");
		orderSpearman = attackSpearman;
		this.getSoundByName(attackCrossbowman, "attaqueCrossbowman");
		this.getSoundByName(deathCrossbowman, "mortCrossbowman");
		this.getSoundByName(selectionCrossbowman, "selectionCrossbowman");
		orderCrossbowman = attackCrossbowman;
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
		int number = 1;
		File f;
		while(true){
			String s = type;
			if(number<10)
				s+="0"+number;
			else
				s+=number;
			f = new File("music/soundsUnit/"+s+".ogg");
			if(f.exists()){
				try {
					v.addElement(new Sound("music/soundsUnit/"+s+".ogg"));
				} catch (SlickException e) {
				}
			} else {
				break;
			}
			number++;
		}
	}





}
