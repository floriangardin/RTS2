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
		SimpleClient.mutex.lock();
		try{
			InputObject im = new InputObject(gc.getInput(), Player.getTeamId(), SimpleClient.roundForInput());
			// Update selection in im.selection
			Player.handleSelection(im, SimpleClient.getPlateau());
			// Update interface
			Interface.update(im, SimpleClient.getPlateau());
			// Send input for round
			SimpleClient.send(im);
			// Send checksum to server for checking synchro
			if(SimpleClient.getRound()>30 && SimpleClient.getRound()%100==0){			
				SimpleClient.send(new Checksum(SimpleClient.getPlateau()));
			}
			// Get new inputs for round
			Vector<InputObject> ims = SimpleClient.getInputForRound();
				SimpleClient.getPlateau().update(ims);
			// 4 : Update the camera given current input
			Camera.update(im);
		}finally{
			SimpleClient.mutex.unlock();
		}
		gc.sleep(1);

	}
	
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		SimpleClient.mutex.lock();
		try{
		SimpleRenderEngine.render(g, SimpleClient.getPlateau());
		}
		finally{
			SimpleClient.mutex.unlock();
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		SimpleClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		Plateau plateau = SimpleClient.getPlateau();
		plateau.update(new Vector<InputObject>());
		Camera.init(800, 600, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
		Interface.init(plateau);
		KeyMapper.init();
		// Try to find a server else launch one ...
		if(SimpleClient.getIP()==null){			
			SimpleServer.init(); // En vrai il faudra le lancer à part
		}
		Player.init(plateau.teams.get(SimpleServer.hasLaunched ? 1 : 2));
		SimpleClient.init(plateau);
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
			//app.setSmoothDeltas(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
}
