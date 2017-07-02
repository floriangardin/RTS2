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
import mybot.IAFlo;
import plateau.Plateau;
import render.SimpleRenderEngine;
import ressources.Map;

public class SimpleGame extends BasicGame {
	Vector<Player> players = new Vector<Player>();
	Plateau plateau ;
	Camera camera;
	Interface bottombar;
	final int currentPlayer;
	SimpleClient client;
	
	public SimpleGame(int currentPlayer) {
		super("RTS ULTRAMYTHE");
		this.currentPlayer = currentPlayer;
		
		// TODO Auto-generated constructor stub
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
		// 3 : Update interface
		bottombar.update(im);
		// 2: Update selection in im.selection
		p.selection.handleSelection(im, bottombar);
		// Multiplayer .. Send input
		client.send(im);
		Vector<InputObject> ims = client.getInputs();
		// 3 : Update plateau (singleplayer = Main.nDelay==0) FIXME : InputHandler
		//Vector<InputObject> inputs = InputHandler.getInputsForRound(plateau.round, Main.nDelay>0);
		plateau.update(ims, players);
		// 4 : Update the camera
		p.selection.plateau = plateau;
		camera.update(im, players.get(currentPlayer).hasRectangleSelection());
	}
	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		SimpleRenderEngine.render(g, plateau, camera, players.get(currentPlayer));
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		this.plateau = Map.createPlateau(Map.maps().get(0), "maps");
		this.players = new Vector<Player>();
		for(int i=0; i<2; i++){
			Player player = new Player(i, "Test"+i, this.plateau.teams.get(i+1), plateau);
			this.players.add(player);
			if(i!=currentPlayer){	
				//FIXME : Generic way to put ia ...
				player.initIA(new IAFlo(player, plateau));
			}
		}
		this.camera = new Camera(800, 600, 0, 0, (int)this.plateau.maxX, (int)this.plateau.maxY);
		this.bottombar = new Interface(plateau, players.get(currentPlayer));
		InputHandler.init(this.players.size());
		KeyMapper.init();
		this.client = new SimpleClient();
		client.connect();
		client.setPlateau(plateau);
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
