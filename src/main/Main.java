package main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import model.Game;

public class Main {
	// A REGLER \\
	public static float ratioSpace = 0.7f;
	public static int framerate = 60;
	public static int nDelay=5;
	///////\\\\\\\\\
	
	public static float increment = 0.1f*30/Main.framerate;
	public static boolean pleinEcran = true;
	
	public static void main(String[] args) {
		//Log.setLogSystem(new NullLogSystem()); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
//		System.out.println(LWJGLUtil.getPlatformName());
		int resolutionX;
		int resolutionY;
		if(pleinEcran){
			resolutionX = (int)screenSize.getWidth();		
			resolutionY = (int)screenSize.getHeight();
		} else {
			resolutionX = 1200;		
			resolutionY = 800;
		}
		try {
			Game game = new Game(resolutionX,resolutionY);
			AppGameContainer app = new AppGameContainer( game );
			game.app = app;
			app.setIcon("ressources/images/danger/iconeJeu.png");
//			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setShowFPS(true);
			app.setDisplayMode(resolutionX, resolutionY,pleinEcran);
			app.setAlwaysRender(false);
			app.setUpdateOnlyWhenVisible(false);
			app.setClearEachFrame(true);
			app.setVSync(true);
			//app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	

}
