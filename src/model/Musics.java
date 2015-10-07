package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Musics {
	public Sound menu;
	public Sound imperial;
	//public Sound lackRessources;
	
	public Musics(){
		
		try {
			menu=new Sound("music/menuTheme.ogg");
			imperial=new Sound("music/imperialTheme.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
