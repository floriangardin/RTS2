package main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import model.Constants;
import model.Game;

import org.lwjgl.LWJGLUtil;

import IA.Simulation;
public class MainSimu {

	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
//		double width = 600;
//		double height = 400;
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = (int)width;		
		int resolutionY = (int)height;
		Game game = new Game();
		game.setParams(new Constants(60),(float) resolutionX,(float) resolutionY);
		// GENERATE SIMUS HERE
		Simulation simu = new Simulation(game);
		simu.simulate();
		System.out.println(simu.report.toString());
	}

}
