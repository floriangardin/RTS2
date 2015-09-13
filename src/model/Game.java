package model;
import java.net.InetAddress;
import java.util.Timer;
import java.util.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.util.Log;

import buildings.Building;
import bullets.Bullet;
import display.BottomBar;
import display.Message;
import display.TopBar;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuPause;
import multiplaying.*;
import multiplaying.ConnectionModel.*;
import nature.Tree;
import nature.Water;
import spells.SpellEffect;
import units.Character;

public class Game extends BasicGame 
{	

	public int idChar = 0;
	public int idBullet = 0;

	// Music and sounds
	public float soundVolume;
	public float volume;
	public Music mainMusic ;
	public Music musicStartGame;
	public Sounds sounds;
	public Images images;
	public boolean playStartMusic = true;
	// TIMEr
	public Timer timer ;


	// Constants
	public Map map;
	public Image background ;
	public Constants constants;

	// Bars
	public BottomBar bottomBars;
	public TopBar topBars;
	public float relativeHeightBottomBar = 1f/6f;
	public float relativeHeightTopBar = 1f/20f;

	// Selection
	public Rectangle selection;
	public boolean new_selection;
	public Vector<Objet> objets_selection=new Vector<Objet>();

	// Resolution : 
	public int resX;
	public int resY;

	// Plateau
	public Plateau plateau ;
	public AppGameContainer app;
	public Vector<Player> players = new Vector<Player>();
	public int currentPlayer = 1;


	Vector<Objet> undestroyable = new Vector<Objet>();
	
	// Network and multiplaying
	public boolean inMultiplayer;
	public long startTime;
	public int portConnexion = 6113;
	public int portInput = 6115;
	public int portOutput = 6115;
	public int portChat = 2347;
	// Host and client
	public InetAddress addressHost;
	public InetAddress addressClient;
	public Vector<InputModel> inputs = new Vector<InputModel>();
	public Vector<InputModel> toRemoveInputs = new Vector<InputModel>();
	public Vector<String> toSendInputs = new Vector<String>();
	public Vector<OutputModel> outputs = new Vector<OutputModel>();
	public Vector<OutputModel> toRemoveOutputs = new Vector<OutputModel>();
	public Vector<String> toSendOutputs = new Vector<String>();
	public Vector<String> connexions = new Vector<String>();
	public Vector<String> toSendConnexions = new Vector<String>();
	public int timeValue;
	// Sender and Receiver
	public MultiReceiver outputReceiver = new MultiReceiver(this,portInput);
	public MultiSender outputSender = new MultiSender(this.app,this,addressClient, portOutput, this.toSendOutputs);
	public MultiReceiver connexionReceiver = new MultiReceiver(this,portConnexion);
	public MultiSender connexionSender;
	public boolean isHost;
	//Debugging network
	public int toAdd = 0;
	public int toRemove = 0;

	// Menus
	public Menu menuPause;
	public Menu menuIntro;
	public Menu menuCurrent = null;
	public boolean isInMenu = false;

	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
		app.setClearEachFrame(true);
		if(!this.musicStartGame.playing()){
			this.musicStartGame.play();
			this.musicStartGame.setVolume(this.volume);
		}
		this.plateau.Xcam = this.plateau.maxX/2 - this.resX/2;
		this.plateau.Ycam = this.plateau.maxY/2 -this.resY/2;
	}
	public void setMenu(Menu m){
		this.menuCurrent = m;
		this.isInMenu = true;
		app.setClearEachFrame(false);
	}


	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		g.translate(-plateau.Xcam,- plateau.Ycam);
		// g repr�sente le pinceau
		//g.setColor(Color.black);
		int i = 0;
		int j = 0;
		while(i<this.plateau.maxX+this.background.getWidth()){
			while(j<this.plateau.maxY+this.background.getHeight()){
				g.drawImage(this.background, i,j);
				j+=this.background.getHeight();
			}
			i+=this.background.getWidth();
			j= 0;
		}
		if(isInMenu){
			g.translate(plateau.Xcam, plateau.Ycam);
			this.menuCurrent.draw(g);
			return;
		}

		g.setColor(Color.black);
		g.fillRect(this.plateau.maxX, 0, this.plateau.maxX, this.plateau.maxY);
		g.fillRect(0, this.plateau.maxY, this.plateau.maxX, this.plateau.maxY);
		//g.fillRect(0,0,gc.getScreenWidth(),gc.getScreenHeight());


		// Draw the selection of your team 
		for(ActionObjet o: plateau.selection.get(currentPlayer)){
			o.drawIsSelected(g);
		}
		//Creation of the drawing Vector
		Vector<Objet> toDraw = new Vector<Objet>();
		Vector<Objet> toDrawAfter = new Vector<Objet>();
		// Draw the Action Objets
		for(Character o : plateau.characters){
			//o.draw(g);
			if(o.visibleByCurrentPlayer)
				toDrawAfter.add(o);
		}
		for(ActionObjet o : plateau.equipments){
			//o.draw(g);
			toDraw.add(o);
		}
		// Draw the natural Objets

		for(NaturalObjet o : this.plateau.naturalObjets){
			o.draw(g);
		}
		// Draw the enemy generators
		for(Building e : this.plateau.buildings){
			if(e.visibleByCurrentPlayer)
				toDrawAfter.add(e);
			else
				toDraw.add(e);
		}
		Utils.triY(toDraw);
		Utils.triY(toDrawAfter);
		// determine visible objets
		for(Objet o: toDraw)
			o.draw(g);
		plateau.drawFogOfWar(g);
		for(Objet o: toDrawAfter)
			o.draw(g);
		for(Bullet o : plateau.bullets){
			if(o.visibleByCurrentPlayer)
				o.draw(g);
		}
		for(SpellEffect a: plateau.spells){
			if(a.visibleByCurrentPlayer)
				a.draw(g);
		}
		// Draw the selection :
		for(int player=1; player<3; player++){
			if(this.plateau.rectangleSelection.get(player) !=null){
				if(player==currentPlayer){
					g.setColor(Color.green);
					g.draw(this.plateau.rectangleSelection.get(player));
				}
			}
		}
		// Draw bottom bar
		g.translate(plateau.Xcam, plateau.Ycam);
		if(this.bottomBars!=null)
			this.bottomBars.draw(g);
		if(this.topBars!=null)
			this.topBars.draw(g);
		// Draw messages
		Message m;
		if(this.plateau.messages.size()>2){
			for(int k=0; k<this.plateau.messages.get(currentPlayer).size();k++){
				m = this.plateau.messages.get(currentPlayer).get(k);
				g.setColor(m.color);
				Font f = g.getFont();
				float height = f.getHeight(m.message);
				g.drawString(m.message, 20f, this.topBars.sizeY+20f+2f*height*k);
			}
		}
	}
	// Do our logic 
	@Override
	public synchronized void update(GameContainer gc, int t) throws SlickException 
	{	
		InputModel im=null;
		//System.out.println(this.plateau.characters);
		if(inMultiplayer){
			/* Multiplaying Host Pipeline
			 * 1 - take the input of t-5 client and host
			 * 2 - perform action() and update()
			 * 3 - send the output of the action and update step
			 */

			this.outputReceiver.lock = false;
			// Defining the clock
			timeValue = (int)(System.currentTimeMillis() - startTime)/(this.constants.FRAMERATE);

			OutputModel om = null;

			undestroyable = new Vector<Objet>();

			// 1 - perform action() and update()
			im = new InputModel(timeValue,this.currentPlayer,gc.getInput(),this.plateau.Xcam,this.plateau.Ycam,this.resX,this.resY);

			if(isInMenu){
				this.menuCurrent.update(im);
			} else {
				om = this.plateau.update(im);
			}

			// 2 - send the output of the action and update step;
			this.toSendOutputs.addElement(om.toString());

			// 3 - receive the output of the action of the other player;
			if(this.outputs.size()>0){
				om = this.outputs.get(this.outputs.size()-1);
				this.outputs.clear();
				this.plateau.updateFromOutput(om);
			}
			System.out.println(undestroyable);

		} else if (!inMultiplayer){
			// If not in multiplayer mode, dealing with the common input
			im = new InputModel(0,1,gc.getInput(),(int) plateau.Xcam,(int)Math.floor(plateau.Ycam),(int)resX,(int)resY);
			// updating the game
			if(isInMenu){
				this.menuCurrent.update(im);
			} else {
				this.plateau.update(im);
			}

		}

	}

	public void newGame(boolean host){
		//Clean all variables
		this.plateau = new Plateau(this.constants,this.plateau.maxX,this.plateau.maxY,2,this);
		this.players = new Vector<Player>();
		this.players.add(new Player(this.plateau,0,0));
		this.players.add(new Player(this.plateau,1,0));
		this.players.add(new Player(this.plateau,2,0));

		if(host)
			this.map.createMapPhillipe(plateau,this.players);
		else
			this.map.createMapPhillipe(plateau,this.players);
		// Instantiate BottomBars for all players:
		for(int player=1; player<3; player++){
			new BottomBar(this.plateau,this.players.get(player),(int)this.resX,(int)this.resY);
			new TopBar(this.plateau,this.players.get(player),(int)this.resX,(int)this.resY);
		}

		this.bottomBars = this.players.get(currentPlayer).bottomBar;
		this.topBars = this.players.get(currentPlayer).topBar;
		selection = null;
	}
	public void newGame(ConnectionModel cm){
		//Clean all variables
		this.plateau.maxX = 3000;
		this.plateau.maxY = 2000;
		newGame(false);
		this.addressHost = cm.ia;
		this.currentPlayer = 2;
		this.bottomBars = this.players.get(currentPlayer).bottomBar;
		this.topBars = this.players.get(currentPlayer).topBar;
		this.bottomBars.update((int)resX, (int)resY);
	}
	// Init our Game objects
	@Override
	public void init(GameContainer gc) throws SlickException 
	{	

		Image cursor = new Image("pics/cursor.png");
		this.volume = 0.2f;
		this.soundVolume = 0.2f;
		gc.setMouseCursor(cursor.getSubImage(0, 0, 24, 64),5,16);
		mainMusic = new Music("music/ambiance.ogg");
		//mainMusic.setVolume(0.1f);
		//mainMusic.loop();
		this.sounds = new Sounds();
		this.images = new Images();

		this.timer = new Timer();

		this.plateau = new Plateau(this.constants,3000,3000,3,this);

		this.musicStartGame = new Music("music/nazi_start.ogg");
		this.players.add(new Player(this.plateau,0,0));
		this.players.add(new Player(this.plateau,1,0));
		this.players.add(new Player(this.plateau,2,0));




		this.background =  new Image("pics/grass1.jpg").getScaledCopy(0.6f);
		this.menuIntro = new MenuIntro(this);
		this.menuPause = new MenuPause(this);
		this.map = new Map();
		this.setMenu(menuIntro);
		this.startTime = System.currentTimeMillis();
		//		Thread t1 = new Thread(new InputListener(this, this.app, 0));
		//		t1.start();

		// Create background image manually


		try{
			Thread.sleep(10);
		} catch(InterruptedException e) {}
	}
	public Game ()
	{
		super("Ultra Mythe RTS 3.0");
	}
	public void setParams(Constants constants,int resX, int resY){
		this.constants = constants;
		this.resX = resX;
		this.resY = resY;
		//
	}
}
