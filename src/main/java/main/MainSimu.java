package main;
import java.io.File;
import java.util.Vector;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import bot.IA;
import control.KeyMapper;
import control.Player;
import model.Game;
import model.GameClient;
import model.GameServer;
import model.WholeGame;
import mybot.IAPython;

public strictfp class MainSimu {
	
	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = 800;
		int resolutionY = 600;
		try {
			Game game = new Game(resolutionX,resolutionY);
			// INIT SYSTEMS
			KeyMapper.init();
			GameServer.init();
			GameClient.init("localhost");
			Player.setTeam(2);
			Vector<IA> ias = new Vector<IA>();
			ias.add(new IAPython(2));
			ias.add(new IAPython(1));
			IA.init(ias);
			Game.system = new WholeGame(false);
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
