package model;

import java.io.File;
import java.util.Vector;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import control.InputObject;
import control.KeyMapper;
import control.Player;
import display.Camera;
import display.Interface;
import multiplaying.Checksum;
import mybot.IAFlo;
import plateau.Plateau;
import plateau.Team;
import render.SimpleRenderEngine;
import ressources.Map;

public class SimpleGame extends BasicGame {
	
	public SimpleGame() {
		super("RTS ULTRAMYTHE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		// Get the plateau from client	
		// Get Control
		if(GameClient.slowDown>0){// Make it generic for n players 
			System.out.println("Slowing down : "+GameClient.slowDown);
			gc.setMinimumLogicUpdateInterval((1+GameClient.slowDown)*16);
			gc.setMaximumLogicUpdateInterval((1+GameClient.slowDown)*16);
			GameClient.slowDown=0;
		}else{
			gc.setMinimumLogicUpdateInterval(16);
			gc.setMaximumLogicUpdateInterval(16);
		}
		GameClient.mutex.lock();
		try{
			final InputObject im = new InputObject(gc.getInput(), Player.getTeamId(), GameClient.roundForInput());
			// Update selection in im.selection
			Player.handleSelection(im, GameClient.getPlateau());
			// Update interface
			Interface.update(im, GameClient.getPlateau());
			// Send input for round
			GameClient.send(im);
			// Send checksum to server for checking synchro
			if(GameClient.getRound()>30 && GameClient.getRound()%100==0){			
				GameClient.send(new Checksum(GameClient.getPlateau()));
			}
			// Get new inputs for round
			Vector<InputObject> ims = GameClient.getInputForRound();
				GameClient.getPlateau().update(ims);
			// 4 : Update the camera given current input
			Camera.update(im);
		}finally{
			GameClient.mutex.unlock();
		}
		
		gc.sleep(1);

	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		GameClient.mutex.lock();
		try{
		SimpleRenderEngine.render(g, GameClient.getPlateau());
		}
		finally{
			GameClient.mutex.unlock();
		}
		gc.sleep(1);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		GameClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		Plateau plateau = GameClient.getPlateau();
		plateau.update(new Vector<InputObject>());
		Camera.init(800, 600, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
		Interface.init(plateau);
		KeyMapper.init();
		// Try to find a server else launch one ...
		if(GameClient.getExistingServerIP()==null){			
			GameServer.init(); // En vrai il faudra le lancer à part
		}
		Player.init(plateau.teams.get(GameServer.hasLaunched ? 1 : 2));
		GameClient.init(plateau);
		gc.setMaximumLogicUpdateInterval(16);
		gc.setMinimumLogicUpdateInterval(16);
	}

	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = 800;
		int resolutionY = 600;
		try {
			SimpleGame game = new SimpleGame();
			AppGameContainer app = new AppGameContainer(game);
			Game.app = app;
			app.setIcon("ressources/images/danger/iconeJeu.png");
//			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setShowFPS(true);
			app.setDisplayMode(resolutionX, resolutionY,false);
			app.setAlwaysRender(false);
			app.supportsMultiSample();
			app.setUpdateOnlyWhenVisible(false);
			app.setClearEachFrame(true);
			app.setVSync(true);
			//app.setTargetFrameRate(30);
			//app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
}
