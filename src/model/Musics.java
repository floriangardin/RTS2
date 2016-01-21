package model;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Musics {
	public Music menu;
	public Music imperial;
	public Music multi;
	public Music editor;
	//public Sound lackRessources;
	
	public Musics(){
		
		try {
			menu=new Music("music/menuTheme.ogg");
			imperial=new Music("music/imperialTheme.ogg");
			multi=new Music("music/waitingMusic.ogg");
			editor=new Music("music/mapEditor.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
