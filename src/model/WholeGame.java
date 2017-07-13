package model;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Vector;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import control.InputObject;
import control.KeyMapper;
import control.Player;
import display.Camera;
import display.Interface;
import multiplaying.Checksum;
import multiplaying.Communications;
import plateau.Plateau;
import render.EndSystem;
import render.RenderEngine;
import ressources.GraphicElements;
import ressources.Images;
import ressources.Map;
import ressources.Musics;
import ressources.Sounds;
import ressources.Taunts;

public class WholeGame extends BasicGame {
	
	public WholeGame() {
		super("RTS ULTRAMYTHE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		if(Game.endSystem != null){
			Game.endSystem.update(gc, arg1);
			return;
		}
		// Get the plateau from client	
		// Get Control
		if(GameClient.slowDown>0){// Make it generic for n players 
			System.out.println("Slowing down : "+GameClient.slowDown);
			gc.setMinimumLogicUpdateInterval((1+1)*16);
			gc.setMaximumLogicUpdateInterval((1+1)*16);
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
		if(GameClient.getPlateau().teamLooser>0){
			Game.endSystem = new EndSystem(GameClient.getPlateau());
			Game.system = Game.endSystem;
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(Game.endSystem != null){
			Game.endSystem.render(gc, g);
			return;
		}
		Plateau p = null;
		GameClient.mutex.lock();
		try{
			p = GameClient.getPlateau();
		}
		finally{
			GameClient.mutex.unlock();
			RenderEngine.render(g, p);
		}
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		// Classic init
		LoadingList.setDeferredLoading(false);
		Musics.init();
		Sounds.init();
		GraphicElements.init();
		Images.init();
		Communications.init();
		Options.init();
		KeyMapper.init();
		Taunts.init();
		
		// TODO Auto-generated method stub
		GameClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		Plateau plateau = GameClient.getPlateau();
		plateau.update(new Vector<InputObject>());
		Camera.init(Game.resX, Game.resY, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.out.println(new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = (int)screenSize.getWidth();
		int resolutionY = (int)screenSize.getHeight();
		Game.resX = resolutionX;
		Game.resY = resolutionY;
		try {
			WholeGame game = new WholeGame();
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
