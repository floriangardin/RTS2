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

public class GameSimu extends Game	{
	
	public Simulator simu;

	public GameSimu(float resX, float resY) {
		super(resX, resY);
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void update(GameContainer gc, int t) throws SlickException 
	{}
	
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{}
	
	public void init(GameContainer gc) throws SlickException {	
		this.sounds = new Sounds();
		this.options = new Options();
		this.images = new Images();
		this.musics = new Musics();
		simu.run();

	}

}
