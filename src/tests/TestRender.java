package tests;

import java.io.File;
import java.util.Vector;

import org.junit.Test;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


import control.InputObject;
import display.Camera;
import model.Game;
import plateau.Plateau;
import render.RenderEngine;
import ressources.Map;
import ressources.Images;

public class TestRender extends BasicGame{
	public TestRender(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	Plateau plateau ;
	RenderEngine renderEngine ;
	Camera camera ; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
			System.out.println(new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
			TestRender game = new TestRender("test Rendering");
			AppGameContainer app = new AppGameContainer(game);
			Game.app = app;
			app.setIcon("ressources/images/danger/iconeJeu.png");
//			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setShowFPS(true);
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

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		RenderEngine.render(g, plateau, camera);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		Images.init();
		camera = new Camera(800, 600, 0, 0);	
		RenderEngine.init(camera);
		plateau = Map.createPlateau("test01", "maptests");
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		// TODO Auto-generated method stub
		plateau.update(new Vector<InputObject>());
	}

}
