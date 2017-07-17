package model;

import java.io.FileNotFoundException;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import control.InputObject;
import control.Player;
import display.Camera;
import display.Interface;
import menu.Lobby;
import multiplaying.ChatHandler;
import multiplaying.Checksum;
import plateau.Objet;
import plateau.Plateau;
import render.EndSystem;
import render.RenderEngine;
import render.SimpleRenderEngine;
import ressources.Map;
import system.ClassSystem;

public class WholeGame extends ClassSystem{
	private Replay replay= new Replay();
	
	public WholeGame() {
		// TODO Auto-generated method stub
		if(Lobby.isInit()){			
			GameClient.setPlateau(Map.createPlateau(Lobby.idCurrentMap, "maps"));
		}else{
			GameClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		}
		Plateau plateau = GameClient.getPlateau();
		plateau.update();
		// Put camera at the center of headquarter
		Objet hq = plateau.getById(Player.getTeam(plateau).hq);
		int xHQ =(int) hq.x;
		int yHQ = (int)hq.y;
		Camera.init(Game.resX, Game.resY, xHQ-Game.resX/2, yHQ-Game.resY/2, (int)plateau.maxX, (int)plateau.maxY);
		Interface.init(plateau);
		
	}
	
	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		if(Game.endSystem != null){
			Game.endSystem.update(gc, arg1);
			return;
		}
		handleSlowDown(gc);
		GameClient.mutex.lock();
		try{
			Input in = gc.getInput();
			final InputObject im = new InputObject(in, Player.getTeamId(), GameClient.roundForInput());
			ChatHandler.action(in, im);
			// Update interface
			Interface.update(im, GameClient.getPlateau());
			// Update selection in im.selection
			Player.handleSelection(im, GameClient.getPlateau());
			// Send input for round
			GameClient.send(im);
			// Send checksum to server for checking synchro
			if(GameClient.getRound()>30 && GameClient.getRound()%100==0){			
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
		}finally{
			GameClient.mutex.unlock();
		}
		if(GameClient.getPlateau().teamLooser>0){
			try {
				replay.save();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Game.endSystem = new EndSystem(GameClient.getPlateau());
			Game.system = Game.endSystem;
		}
	}
	
	private void handleSlowDown(GameContainer gc) {
		if(GameClient.slowDown>0){
			System.out.println("Slowing down : "+GameClient.slowDown);
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
		}
		finally{
			GameClient.mutex.unlock();
			if(RenderEngine.isReady()){
				RenderEngine.render(g, p);
				
			} else {
				SimpleRenderEngine.render(g, p);
				
			}
		}
		
	}

	
	
}
