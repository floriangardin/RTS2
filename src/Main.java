import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;




public class Main {
	int framerate = 200;
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = 1280;		
		int resolutionY = 720;
		try {
			Main main = new Main();
			Game game = new Game();
			game.setParams(main.framerate,(float) resolutionX,(float) resolutionY);
			AppGameContainer app = new AppGameContainer( game );
			app.setDisplayMode(resolutionX, resolutionY, false);
			//app.setFullscreen(true);
			app.setTargetFrameRate(main.framerate);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
