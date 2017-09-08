
package model;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import ressources.GraphicElements;
import stats.StatsSystem;
import system.ClassSystem;
import system.EndSystem;
import system.IntroSystem;
import system.MenuSystem;


public strictfp class Game extends BasicGame 
{
	
	public static int resX, resY;
	public static float ratioX, ratioY;
	public static float ratioResolution;
	
	public static AppGameContainer app; 

	public static ClassSystem system;
	public static ClassSystem gameSystem;
	public static MenuSystem menuSystem;
	public static StatsSystem statsSystem;
	public static EndSystem endSystem;

	public Game(int resX, int resY) {
		super("RTS Ultramythe");
		Game.resX = resX;
		Game.resY = resY;
		Game.ratioX = resX/1920f;
		Game.ratioY = resY/1080f;
		Game.ratioResolution = 1f*resX/1920f;
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(system!=null){
			if(GraphicElements.isInit()){				
				g.setFont(GraphicElements.font_main);
			}
			system.render(gc, g);
		} else {
			throw new SlickException("Game - System missing error");
		}
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		if(system==null){
			system = new IntroSystem();
		}
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		if(system!=null){
			system.update(arg0, arg1);
		} else {
			throw new SlickException("Game - System missing error");
		}
	}
	
	public static boolean isInGame(){
		return system==gameSystem;
	}
	
	public static boolean isInMenu(){
		return system==menuSystem;
	}
	
}
