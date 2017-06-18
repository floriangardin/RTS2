package system;

import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import control.InputHandler;
import control.InputObject;
import control.Selection;
import display.Camera;
import display.DisplayHandler;
import display.Interface;
import events.EventNames;
import menu.Lobby;
import menuutils.Menu_Player;
import model.Game;
import model.Player;
import plateau.Objet;
import plateau.Plateau;
import render.RenderEngine;
import ressources.Map;

public class GameSystem extends ClassSystem{
	
	public Vector<Player> players;
	public Plateau plateau;
	public Interface bottombar;
	public int currentPlayer;
	public DisplayHandler events;
	public Camera camera;
	

	
	public GameSystem(Lobby lobby){
		this.plateau = Map.createPlateau(lobby.idCurrentMap, "maps");
		this.players = new Vector<Player>();
		for(Menu_Player mp : lobby.players){
			this.players.add(new Player(mp, this.plateau.teams.get(mp.team)));
		}
		this.camera = new Camera(Game.resX, Game.resY, 0, 0, (int)this.plateau.maxX, (int)this.plateau.maxY);
		InputHandler.init(this.players.size());
		RenderEngine.init(plateau);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		RenderEngine.render(g, plateau, camera, players.get(currentPlayer));
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		// 1 : Get Control
		InputObject im = new InputObject(gc.getInput(), camera);
		InputHandler.addToInputs(im, true);
		Player p = players.get(currentPlayer);

		// 2: Update selection in im.selection
		p.selection.handleSelection(im);
		
		// 3 : Update plateau
		plateau.update(InputHandler.getInputsForRound(plateau.round));
		
		// 4 : Update the camera
		camera.update(im, players.get(currentPlayer).hasRectangleSelection());
//		
		
//		// TODO Auto-generated method stub
//
//		this.handleInterface(im);
//
//
//		this.handleMinimap(im, player);
//		
//
//		InputHandler.updateSelection(ims);
//		
//
//		// 3 - handling visibility
//		this.updateVisibility();
	}
	
	public void setPlateau(Plateau plateau){
		this.plateau = plateau;
		this.bottombar = new Interface(plateau);
	}
	
	public Player getCurrentPlayer(){
		return this.players.get(currentPlayer);
	}
	
	public int getCurrentTeam(){
		return this.players.get(currentPlayer).getGameTeam().id;
	}
	
	public void triggerEvent(EventNames name,Objet o){
		events.addEvent(name, o);
	}
	public DisplayHandler getEvents(){
		return events;
	}

}
