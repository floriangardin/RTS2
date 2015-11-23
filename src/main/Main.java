package main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import model.Game;

public class Main {
	public static int framerate = 60;
	public static float increment = 0.1f*30/Main.framerate;
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		double width = 600;
//		double height = 400;
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = (int)screenSize.getWidth();		
		int resolutionY = (int)screenSize.getHeight();
//		int resolutionX = 800;		
//		int resolutionY = 600;
		try {
			Game game = new Game(resolutionX,resolutionY);
			AppGameContainer app = new AppGameContainer( game );
			game.app = app;
//			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setShowFPS(false);
			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setAlwaysRender(false);
			app.setClearEachFrame(true);
			//app.setTargetFrameRate(Main.framerate);
			app.setVSync(false);
			//app.setSmoothDeltas(true);
			app.setMinimumLogicUpdateInterval(1000/framerate);
			app.setMaximumLogicUpdateInterval(1000/framerate);
			//app.setMaximumLogicUpdateInterval(20);
			//app.setMaximumLogicUpdateInterval(16);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
