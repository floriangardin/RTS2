package system;

import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import control.InputHandler;
import control.InputObject;
import control.KeyMapper;
import display.Camera;
import display.Interface;
import events.EventHandler;
import main.Main;
import menu.Lobby;
import menuutils.Menu_Player;
import model.Game;
import model.Player;
import model.SimpleClient;
import model.SimpleServer;
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
		SimpleClient.setPlateau(Map.createPlateau(lobby.idCurrentMap, "maps"));
		Plateau plateau = SimpleClient.getPlateau();
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
			SimpleServer.init();
		}catch(Exception e){
			System.out.println("Server already exist !");
		}
		SimpleClient.init(plateau);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		if(RenderEngine.isReady()){
			RenderEngine.render(g, SimpleClient.getPlateau(), camera, players.get(currentPlayer), bottombar);	
		} else {
			SimpleRenderEngine.render(g, SimpleClient.getPlateau(), camera, players.get(currentPlayer));
		}
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Player p = players.get(currentPlayer);
		// Get the plateau from client
		Plateau plateau = SimpleClient.getPlateau();
		// Get Control
		InputObject im = new InputObject(gc.getInput(), camera, p.getTeam(), SimpleClient.roundForInput());
		InputHandler.addToInputs(im, true);
		//		byte[] ser = Serializer.serialize(plateau);
		//		System.out.println("Size of plateau : "+ser.length);
		// Update interface
		bottombar.update(im, plateau);
		// Update selection in im.selection
		p.selection.handleSelection(im, bottombar, plateau);
		// Send input for round
		SimpleClient.send(im);
		// Send checksum to server for checking synchro
		SimpleClient.send(new Checksum(plateau.round, plateau.toString()));
		// Get new inputs for round
		Vector<InputObject> ims = SimpleClient.getInputs();
		//		// Update IA from plateau 
		//		for(Player player : players){
		//			if(player.ia!=null){
		//				player.ia.action(plateau);
		//			}
		//		}
		plateau.update(ims, players);
		// 4 : Update the camera given current input
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
