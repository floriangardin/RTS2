
package main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.openal.AL;

import IA.Simulator;
import model.Constants;
import model.Game;

public class MainSimu {
	int framerate = 60;

	Constants constants;
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = (int)width;		
		int resolutionY = (int)height;
//		Game game = new Game();
//		game.setParams(new Constants(Main.framerate),(float) resolutionX,(float) resolutionY);
//		Simulation simu = new Simulation(game);
//		simu.simulate();
//		System.out.println(simu.report.toString());

		new Simulator(1);
		AL.destroy();
	}

}

