package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
// STORE ALL THE SOUNDS
	
	
	public Sound sword;
	public Sound arrow;
	public Sound fireball;
	//public Sound lackRessources;
	
	public Sounds(){
		
		try {
			sword=new Sound("music/sword.ogg");
			arrow=new Sound("music/arrow.ogg");
			fireball= new Sound("music/fireball.ogg");
			//lackRessources = new Sound("music/lackRessources.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	


	
	
}
