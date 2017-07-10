package main;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import menu.Lobby;
import model.Game;
import system.GameSystem;

public class MainSimu {
	
	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = 800;
		int resolutionY = 600;
		try {
			Game game = new Game(resolutionX,resolutionY);
			Lobby lobby = new Lobby();
			lobby.initSingle();
			Game.system = new GameSystem(lobby);
			AppGameContainer app = new AppGameContainer(game);
			Game.app = app;
			app.setShowFPS(true);
			app.setDisplayMode(resolutionX, resolutionY,false);
			app.setAlwaysRender(false);
			app.setUpdateOnlyWhenVisible(false);
			app.setMinimumLogicUpdateInterval(1000/Main.framerate);
			app.setMaximumLogicUpdateInterval(1000/Main.framerate);
			app.setTargetFrameRate(Main.framerate);
			//app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
