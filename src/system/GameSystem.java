package system;

import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import control.InputObject;
import display.Camera;
import display.Interface;
import events.EventHandler;
import main.Main;
import menu.Lobby;
import menuutils.Menu_Player;
import model.Game;
import model.GameClient;
import model.GameServer;
import multiplaying.Checksum;
import mybot.IAFlo;
import plateau.Plateau;
import render.EndSystem;
import render.RenderEngine;
import render.SimpleRenderEngine;
import ressources.Map;

public class GameSystem extends ClassSystem{

	public Vector<Player> players;
	public Interface bottombar;
	public int currentPlayer;
	//public EventHandler events;
	public Camera camera;


	public GameSystem(Lobby lobby){
		GameClient.setPlateau(Map.createPlateau(lobby.idCurrentMap, "maps"));
		Plateau plateau = GameClient.getPlateau();
		currentPlayer = lobby.idCurrentPlayer;
		this.players = new Vector<Player>();
		for(Menu_Player mp : lobby.players){
			Player player = new Player(mp, plateau.teams.get(mp.team), plateau);
			this.players.add(player);
			if(mp.id!=lobby.idCurrentPlayer){	
				//FIXME : Generic way to put ia ...
				player.initIA(new IAFlo(player, plateau));
			}
		}
		this.camera = new Camera(Game.resX, Game.resY, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
		this.bottombar = new Interface(plateau, players.get(currentPlayer));
		KeyMapper.init();
		InputHandler.init(this.players.size());
//		EventHandler.init(plateau, camera);
		// Launch server if it doesnt exist, otherwise continue, bind to host plateau !
		try{
			GameServer.init();
		}catch(Exception e){
			System.out.println("Server already exist !");
		}
		GameClient.init(plateau);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub


	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {

		// 0 : Update ia's 
		for(Player p : players){
			if(p.ia!=null){
				p.ia.action(plateau);
			}
		}
		// 1 : Get Control
		InputObject im = new InputObject(gc.getInput(), camera, Player.getTeamId(), plateau.round);
		Player p = players.get(currentPlayer);
		// Get the plateau from client
		Plateau plateau = GameClient.getPlateau();
		// Get Control
		InputObject im = new InputObject(gc.getInput(), camera, p.getTeam(), GameClient.roundForInput());

		// 2: Update selection in im.selection
		Player.handleSelection(im, bottombar, plateau);
		// Multiplayer .. Send input
		// 3 : Update plateau (singleplayer = Main.nDelay==0)
		Vector<InputObject> inputs = InputHandler.getInputsForRound(plateau.round, Main.nDelay>0);
		plateau.update(inputs, players);
		// 4 : Update the camera

		camera.update(im, players.get(currentPlayer).hasRectangleSelection());
		// Checking end condition
		if(plateau.teamLooser>0){
			Game.endSystem = new EndSystem(plateau, camera, plateau.teamLooser);
			Game.system = Game.endSystem;
		}
	}

	public Player getCurrentPlayer(){
		return this.players.get(currentPlayer);
	}

	public int getCurrentTeam(){
		return this.players.get(currentPlayer).getGameTeam().id;
	}


}
