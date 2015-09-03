package model;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	int framerate = 60;
	
	Constants constants;
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = (int)width;		
		int resolutionY = (int)height;
		
		try {
			Main main = new Main();
			Game game = new Game();
			game.setParams(new Constants(main.framerate),(float) resolutionX,(float) resolutionY);
			AppGameContainer app = new AppGameContainer( game );
			game.app = app;
			app.setDisplayMode(resolutionX, resolutionY,true);
			//app.setFullscreen(true);
			app.setUpdateOnlyWhenVisible(false);
			app.setTargetFrameRate(main.framerate);
			app.setVSync(true);
			app.setClearEachFrame(true);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
