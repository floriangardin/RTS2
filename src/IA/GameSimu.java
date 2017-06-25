package IA;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import menu.MenuIntro;
import model.Game;
import model.Options;
import ressources.Images;
import ressources.Musics;
import ressources.Sounds;

public class GameSimu 	{
	
	public Simulator simu;

	public GameSimu() {
		
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void update(GameContainer gc, int t) throws SlickException 
	{}
	
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{}
	
	public void init(GameContainer gc) throws SlickException {	

		simu.run();

	}

}
