package model;

import java.io.FileNotFoundException;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import bot.IA;
import control.InputObject;
import control.Player;
import display.Camera;
import display.Interface;
import events.EventHandler;
import menu.Lobby;
import multiplaying.ChatHandler;
import multiplaying.Checksum;
import plateau.Building;
import plateau.Plateau;
import render.EndSystem;
import render.RenderEngine;
import render.SimpleRenderEngine;
import ressources.Map;
import ressources.MusicManager;
import ressources.SoundManager;
import ressources.Taunts;
import system.ClassSystem;

public class WholeGame extends ClassSystem{
	private Replay replay= new Replay();
	// Pour conditions de victoire
	private boolean repeat = false;
	private int repeatNumber = 100;
	private String currentMap = null;
	public static final float nbRoundStart = 200f;
	
	public WholeGame(boolean repeat) {
		// TODO Auto-generated method stub
		this.repeat = true;
		if(Lobby.isInit()){
			currentMap = Lobby.idCurrentMap;
			GameClient.setPlateau(Map.createPlateau(Lobby.idCurrentMap, "maps"));
		}else{
			currentMap = Map.maps().get(0);
			GameClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		}
		Plateau plateau = GameClient.getPlateau();
		plateau.update();
		// Put camera at the center of headquarter if exists
		Building hq = plateau.getHQ(Player.getTeam(plateau));
		if(hq!=null){
			int xHQ =(int) hq.x;
			int yHQ = (int)hq.y;
			Camera.init(Game.resX, Game.resY, xHQ-Game.resX/2, yHQ-Game.resY/2, (int)plateau.maxX, (int)plateau.maxY);
		}else{
			Camera.init(Game.resX, Game.resY, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
		}
		
		Interface.init(plateau);
	}
	
	public WholeGame() {
		// TODO Auto-generated method stub
		EventHandler.init();
		if(Lobby.isInit()){			
			GameClient.setPlateau(Map.createPlateau(Lobby.idCurrentMap, "maps"));
		}else{
			GameClient.setPlateau(Map.createPlateau("testcollision2", "maps"));
//			GameClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		}
		Game.endSystem = null;
		Plateau plateau = GameClient.getPlateau();
		plateau.update();
		RenderEngine.init(plateau);
		// Put camera at the center of headquarter
		Building hq =plateau.getHQ(Player.getTeam(plateau));
		if(hq!=null){
			int xHQ =(int) hq.x;
			int yHQ = (int)hq.y;
			Camera.init(Game.resX, Game.resY, (int)(xHQ*Game.ratioX)-Game.resX/2, (int)(yHQ*Game.ratioY)-Game.resY/2, (int)plateau.maxX, (int)plateau.maxY);	
		}else{
			Camera.init(Game.resX, Game.resY, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
		}
		Interface.init(plateau);
	}
	
	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		if(SimpleRenderEngine.ux!=0){
//			return;
		}
		if(Game.endSystem != null){
			Game.endSystem.update(gc, arg1);
			return;
		}
		handleSlowDown(gc);
		GameClient.mutex.lock();
		try{
			Input in = gc.getInput();
			Vector<InputObject> iaIms = new Vector<InputObject>();
			final InputObject im = new InputObject(in, Player.getTeamId(), GameClient.roundForInput());
			// handling start
			if(GameClient.getPlateau().round<nbRoundStart){
				im.reset();
			} else {
				iaIms = IA.play(GameClient.getPlateau(), GameClient.roundForInput());
			}
			ChatHandler.action(in, im);
			// Update interface
			Interface.update(im, GameClient.getPlateau());
			// Update selection in im.selection
			Player.handleSelection(im, GameClient.getPlateau());
			// Send input for round
			GameClient.send(im);
			// Send IA Inputs
			GameClient.send(iaIms);
			// Update sound and music
			MusicManager.update(GameClient.getPlateau());
			SoundManager.update(GameClient.getPlateau());
			
			// Send checksum to server for checking synchro
			if(GameClient.getRound()>30 && GameClient.getRound()%(GameClient.delay*2)==0){			
				GameClient.send(new Checksum(GameClient.getPlateau()));
			}
			// Get new inputs for round
			Vector<InputObject> ims = GameClient.getInputForRound();
			// Update replay
			Replay.handleReplay(replay, ims, GameClient.getPlateau() );
			//Update Plateau
			GameClient.getPlateau().update(ims);
			// 4 : Update the camera given current input
			Camera.update(im);
			RenderEngine.xmouse = im.x;
			RenderEngine.ymouse = im.y;
		}finally{
			GameClient.mutex.unlock();
		}
		if(GameClient.getPlateau().teamLooser>0){

			if(this.repeat){
				if(this.repeatNumber--<0){
					this.repeatNumber = 100;
					GameClient.setPlateau(Map.createPlateau(this.currentMap, "maps"));
				}
				
			}else{
				Game.endSystem = new EndSystem(GameClient.getPlateau());
				Game.system = Game.endSystem;
				try {
					replay.save();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(Taunts.isInit()){
			Taunts.update();
		}
	}
	
	private void handleSlowDown(GameContainer gc) {
		if(GameClient.slowDown>0){
			//System.out.println("Slowing down : "+GameClient.slowDown);
			gc.setMinimumLogicUpdateInterval(32);
			gc.setMaximumLogicUpdateInterval(32);
			GameClient.slowDown=0;
		}else{
			gc.setMinimumLogicUpdateInterval(16);
			gc.setMaximumLogicUpdateInterval(16);
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
			if(RenderEngine.isReady()){
				RenderEngine.render(g, p);
				
			} else {
				SimpleRenderEngine.render(g, p);
				
			}
		}
		finally{
			GameClient.mutex.unlock();
		}
		
	}

}
