package model;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Vector;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
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
import system.ClassSystem;

public class WholeGame extends ClassSystem{
	
	public WholeGame() {
		// TODO Auto-generated method stub
				GameClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
				Plateau plateau = GameClient.getPlateau();
				plateau.update();
				
				Camera.init(Game.resX, Game.resY, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
				Interface.init(plateau);
				// Try to find a server else launch one ...
				if(GameClient.getExistingServerIP()==null){			
					GameServer.init(); // En vrai il faudra le lancer à part
				}
				Player.init(plateau.teams.get(GameServer.hasLaunched ? 1 : 2));
				GameClient.init(plateau); 
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

	
	
}
