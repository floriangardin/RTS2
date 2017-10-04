package main;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import control.KeyMapper;
import control.Player;
import model.Game;
import model.GameClient;
import model.GameServer;
import model.ReplayGame;

public strictfp class MainReplay {
	
	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = 800;
		int resolutionY = 600;
		try {
			Game game = new Game(resolutionX,resolutionY);
			// INIT SYSTEMS
			KeyMapper.init();
			Game.system = new ReplayGame();
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
