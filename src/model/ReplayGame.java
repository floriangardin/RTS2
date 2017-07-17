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

public class ReplayGame extends ClassSystem{
	private Replay replay= new Replay();
	
	
	public ReplayGame(){
		init("lastReplay");
	}
	public ReplayGame(String file) {
		init(file);
		
	}
	
	public void init(String file){
		// Look for replay
		try {
			replay = Replay.load(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Plateau plateau = replay.getCurrentPlateau();
		Camera.init(Game.resX, Game.resY, Game.resX/2, Game.resY/2, (int)plateau.maxX, (int)plateau.maxY);
		Interface.init(plateau);
	}
	
	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		if(Game.endSystem != null){
			Game.endSystem.update(gc, arg1);
			return;
		}
		Input in = gc.getInput();
		final InputObject im = new InputObject(in, Player.getTeamId(), replay.getCurrentPlateau().getRound());
		// Update interface
		Interface.update(im, replay.getCurrentPlateau());
		// Update selection in im.selection
		Player.handleSelection(im, replay.getCurrentPlateau());
		replay.update();
		// 4 : Update the camera given current input
		Camera.update(im);
		if(replay.getCurrentPlateau().teamLooser>0){
			Game.endSystem = new EndSystem(replay.getCurrentPlateau());
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
			p = replay.getCurrentPlateau();
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
