package model;

import java.io.File;
import java.util.Vector;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import control.InputHandler;
import control.InputObject;
import control.KeyMapper;
import display.Camera;
import display.Interface;
import main.Main;
import multiplaying.Checksum;
import mybot.IAFlo;
import plateau.Plateau;
import render.SimpleRenderEngine;
import ressources.Map;

public class SimpleGame extends BasicGame {
	Vector<Player> players = new Vector<Player>();
	Camera camera;
	Interface bottombar;
	final int currentPlayer;
	
	public SimpleGame(int currentPlayer) {
		super("RTS ULTRAMYTHE");
		this.currentPlayer = currentPlayer;
		// TODO Auto-generated constructor stub
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
	}
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		SimpleRenderEngine.render(g, SimpleClient.getPlateau(), camera, players.get(currentPlayer));
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		SimpleClient.setPlateau(Map.createPlateau(Map.maps().get(0), "maps"));
		Plateau plateau = SimpleClient.getPlateau();
		this.players = new Vector<Player>();
		for(int i=0; i<2; i++){
			Player player = new Player(i, "Test"+i, plateau.teams.get(i+1), plateau);
			this.players.add(player);
			if(i!=currentPlayer){	
				//FIXME : Generic way to put ia ...
				player.initIA(new IAFlo(player, plateau));
			}
		}
		this.camera = new Camera(800, 600, 0, 0, (int)plateau.maxX, (int)plateau.maxY);
		this.bottombar = new Interface(plateau, players.get(currentPlayer));
		InputHandler.init(this.players.size());
		KeyMapper.init();
		// Launch server if it doesnt exist, otherwise continue, bind to host plateau !
		try{
			SimpleServer.init();
		}catch(Exception e){
			System.out.println("Server already exist !");
		}
		SimpleClient.init(plateau);
	}


	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX = 800;
		int resolutionY = 600;
		try {
			SimpleGame game = new SimpleGame(0);
			AppGameContainer app = new AppGameContainer(game);
			Game.app = app;
			app.setIcon("ressources/images/danger/iconeJeu.png");
//			app.setDisplayMode(resolutionX, resolutionY,true);
			app.setShowFPS(true);
			app.setDisplayMode(resolutionX, resolutionY,false);
			app.setAlwaysRender(false);
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
