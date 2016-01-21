package IAProject;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import main.Main;
import model.Game;

public class MainSimuProject {
	public static int framerate = 1000;
	public static float increment = 0.1f*30/Main.framerate;
	public static boolean pleinEcran = false;
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());

		int resolutionX;
		int resolutionY;
		if(pleinEcran){
			resolutionX = (int)screenSize.getWidth();		
			resolutionY = (int)screenSize.getHeight();
		} else {
			resolutionX = 120;		
			resolutionY = 80;
		}
		try {
			Game game = new GameSimuProject(resolutionX,resolutionY);
			AppGameContainer app = new AppGameContainer( game );
			game.app = app;

//			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setShowFPS(false);
			app.setDisplayMode(resolutionX, resolutionY,pleinEcran);
			app.setAlwaysRender(false);
			app.setUpdateOnlyWhenVisible(false);
			app.setClearEachFrame(true);
			//app.setTargetFrameRate(Main.framerate);
			app.setVSync(true);
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
