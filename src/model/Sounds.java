package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
// STORE ALL THE SOUNDS
	
	
	Sound sword;
	Sound arrow;
	Sound fireball;
	
	public Sounds(){
		
		try {
			sword=new Sound("music/sword.ogg");
			arrow=new Sound("music/arrow.ogg");
			fireball= new Sound("music/fireball.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	


	
	
}
