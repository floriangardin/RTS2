package system;

import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import control.InputHandler;
import control.InputObject;
import display.Camera;
import display.Interface;
import events.EventHandler;
import events.EventNames;
import main.Main;
import menu.Lobby;
import menuutils.Menu_Player;
import model.Game;
import model.Player;
import mybot.IAFlo;
import plateau.Objet;
import plateau.Plateau;
import render.RenderEngine;
import ressources.GraphicElements;
import ressources.Map;

public class GameSystem extends ClassSystem{
	
	public Vector<Player> players;
	public Plateau plateau;
	public Interface bottombar;
	public int currentPlayer;
	public EventHandler events;
	public Camera camera;
	

	
	public GameSystem(Lobby lobby){
		currentPlayer = lobby.idCurrentPlayer;
		this.plateau = Map.createPlateau(lobby.idCurrentMap, "maps");
		this.players = new Vector<Player>();
		for(Menu_Player mp : lobby.players){
			Player player = new Player(mp, this.plateau.teams.get(mp.team));
			this.players.add(player);
			if(mp.id!=lobby.idCurrentPlayer){	
				player.initIA(new IAFlo(player, plateau));
			}
		}
		this.camera = new Camera(Game.resX, Game.resY, 0, 0, (int)this.plateau.maxX, (int)this.plateau.maxY);
		this.bottombar = new Interface(plateau, players.get(currentPlayer));
		InputHandler.init(this.players.size());
		RenderEngine.init(plateau);
		EventHandler.init(plateau, camera);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		RenderEngine.render(g, plateau, camera, players.get(currentPlayer), bottombar);
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		// 0 : Update ia's 
		for(Player p : players){
			if(p.ia!=null){
				p.ia.action();
			}
		}
		// 1 : Get Control
		Player p = players.get(currentPlayer);
		InputObject im = new InputObject(gc.getInput(), camera, p.getTeam());
		InputHandler.addToInputs(im, true);
		// 2: Update selection in im.selection
		p.selection.handleSelection(im);
		// 3 : Update interface
		bottombar.update(im);
		// 3 : Update plateau (singleplayer = Main.nDelay==0)
		Vector<InputObject> inputs = InputHandler.getInputsForRound(plateau.round, Main.nDelay>0);
		plateau.update(inputs);
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
	
	public Player getCurrentPlayer(){
		return this.players.get(currentPlayer);
	}
	
	public int getCurrentTeam(){
		return this.players.get(currentPlayer).getGameTeam().id;
	}
	
	public void triggerEvent(EventNames name,Objet o){
		events.addEvent(name, o);
	}
	public EventHandler getEvents(){
		return events;
	}

}