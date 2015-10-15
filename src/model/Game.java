package model;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import buildings.Building;
import bullets.Bullet;
import display.BottomBar;
import display.Message;
import display.TopBar;
import menu.Menu;
import menu.MenuIntro;
import multiplaying.InputModel;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;
import multiplaying.OutputModel;
import spells.SpellEffect;
import units.Character;

public class Game extends BasicGame 
{	

	public int idChar = 0;
	public int idBullet = 0;

	// Font 
	public TrueTypeFont font;
	// Music and sounds
	public Options options;
	public Sounds sounds;
	public Images images;
	public Musics musics;

	// Timer
	public Timer timer ;

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
	public float resX;
	public float resY;

	// Plateau
	public Plateau plateau ;
	public AppGameContainer app;
	public Vector<Player> players = new Vector<Player>();
	public int currentPlayer = 1;


	// Network and multiplaying
	public boolean inMultiplayer;
	public boolean host = false;
	public long startTime;
	public int portConnexion = 6113;
	public int portInput = 6114;
	public int portOutput = 6115;
	public int portChat = 2347;
	// Host and client
	private String addressHostString = "192.168.1.27";
	private String addressClientString = "192.168.1.31";
	public InetAddress addressHost;
	public InetAddress addressClient;
	public Vector<InputModel> inputs = new Vector<InputModel>();
	public Vector<InputModel> toRemoveInputs = new Vector<InputModel>();
	public Vector<String> toSendInputs = new Vector<String>();
	public Vector<String> outputs = new Vector<String>();
	public Vector<OutputModel> toRemoveOutputs = new Vector<OutputModel>();
	public Vector<String> toSendOutputs = new Vector<String>();
	public Vector<String> connexions = new Vector<String>();
	public Vector<String> toSendConnexions = new Vector<String>();
	public int timeValue;
	// Sender and Receiver
	public MultiReceiver inputReceiver;
	public MultiSender inputSender;
	public MultiReceiver outputReceiver;
	public MultiSender outputSender;
	public MultiReceiver connexionReceiver;
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
		g.setFont(this.font);
		// g repr�sente le pinceau
		//g.setColor(Color.black);


		if(isInMenu){
			this.menuCurrent.draw(g);
			return;
		} 
		g.translate(-plateau.Xcam,- plateau.Ycam);
		int i = 0;
		int j = 0;
		while(i<this.plateau.maxX+this.images.grassTexture.getWidth()){
			while(j<this.plateau.maxY+this.images.grassTexture.getHeight()){
				g.drawImage(this.images.grassTexture, i,j);
				j+=this.images.grassTexture.getHeight();
			}
			i+=this.images.grassTexture.getWidth();
			j= 0;
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
			if(this.plateau.rectangleSelection !=null){
				if(player==currentPlayer){
					g.setColor(Color.green);
					g.draw(this.plateau.rectangleSelection);
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
		Vector<InputModel> ims = new Vector<InputModel>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game
		if(isInMenu){
			this.menuCurrent.update(gc.getInput());
		} else {
			if(!host){
				// client mode
				InputModel im = new InputModel(this,0,currentPlayer,gc.getInput(),(int) plateau.Xcam,(int)Math.floor(plateau.Ycam),(int)resX,(int)resY);
				this.toSendInputs.addElement(im.toString());
				ims.add(im);
				if(outputs.size()>0){
					this.plateau.currentString = outputs.lastElement();
					outputs.clear();
				}
				this.plateau.update(ims);
			} else {
				ims.add(new InputModel(this,0,1,gc.getInput(),(int) plateau.Xcam,(int)Math.floor(plateau.Ycam),(int)resX,(int)resY));
				if(inMultiplayer){
					// host mode
					if(inputs.size()>0)
						ims.add(this.inputs.lastElement());
					this.plateau.update(ims);
					this.toSendOutputs.add(this.plateau.currentString);
				} else {
					// solo mode
					this.plateau.update(ims);
				}
			}
		}
	}

	public void newGame(){
		//Clean all variables

		Map.createMapEmpty(this);

		//System.out.println(this.plateau.mapGrid);
		//			Map.createMapEmpty(this);
		// Instantiate BottomBars for all players:
		for(int player=1; player<3; player++){
			new BottomBar(this.plateau,this.players.get(player),(int)this.resX,(int)this.resY);
			new TopBar(this.plateau,this.players.get(player),(int)this.resX,(int)this.resY);
		}
		this.bottomBars = this.players.get(currentPlayer).bottomBar;
		this.topBars = this.players.get(currentPlayer).topBar;
		selection = null;

	}

	@Override
	public void init(GameContainer gc) throws SlickException {	
		Image cursor = new Image("pics/cursor.png");
		java.awt.Font fe = new java.awt.Font("Candara",java.awt.Font.BOLD,28);
		this.font = new TrueTypeFont(fe, true);
		if(gc!=null)
			gc.setMouseCursor(cursor.getSubImage(0, 0, 24, 64),5,16);
		this.sounds = new Sounds();
		this.options = new Options();
		this.images = new Images(true);
		this.musics = new Musics();

		this.menuIntro = new MenuIntro(this);
		this.setMenu(menuIntro);
		this.connexionReceiver.start();
		this.connexionSender.start();
		if(!host){
			this.outputReceiver.start();
			this.inputSender.start();
		} else{
			this.outputSender.start();
			this.inputReceiver.start();
		}
	}


	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;
		this.images = new Images(false);
		try {
			addressHost = InetAddress.getByName(addressHostString);
			addressClient = InetAddress.getByName(addressClientString);
		} catch (UnknownHostException e) {
			System.out.println("unknown address");
		}
		try {
			host = (InetAddress.getLocalHost().getHostName()+".home").equals(addressHost.getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		inputReceiver = new MultiReceiver(this,portInput);
		inputSender = new MultiSender(addressHost,portInput,this.toSendInputs);
		outputReceiver = new MultiReceiver(this,portOutput);
		outputSender = new MultiSender(addressClient, portOutput, this.toSendOutputs);
		connexionReceiver = new MultiReceiver(this,portConnexion);
		//TODO: upgrading multiplaying
		connexionSender = new MultiSender(host ? addressClient : addressHost, portConnexion, this.toSendConnexions);

	}
}
