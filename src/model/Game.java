package model;
import java.net.InetAddress;
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
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuOptions;
import multiplaying.InputModel;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;
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


	// Network and multiplaying
	public boolean inMultiplayer;
	public boolean host = false;
	public long startTime;
	public int portConnexion = 6113;
	public int portInput = 6114;
	public int portOutput = 6115;
	public int portChat = 2347;
	// Host and client
	public InetAddress addressHost;
	public InetAddress addressClient;
	public Vector<InputModel> inputs = new Vector<InputModel>();
	public Vector<InputModel> toRemoveInputs = new Vector<InputModel>();
	public Vector<String> toSendInputs = new Vector<String>();
	public Vector<String> outputs = new Vector<String>();
	public Vector<String> toSendConnexions = new Vector<String>();
	public Vector<String> connexions = new Vector<String>();
	public Vector<Vector<String>> toSendOutputs = new Vector<Vector<String>>();
	public int timeValue;
	// Sender and Receiver
	public MultiReceiver inputReceiver;
	public MultiSender inputSender;
	public MultiReceiver outputReceiver;
	public Vector<MultiSender> outputSender;
	public MultiReceiver connexionReceiver;
	public MultiSender connexionSender;
	public boolean isHost;
	//Debugging network
	public int toAdd = 0;
	public int toRemove = 0;

	// Menus
	public Menu menuPause;
	public MenuIntro menuIntro;
	public MenuOptions menuOptions;
	public MenuMulti menuMulti;
	public MenuMapChoice menuMapChoice;
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
		for(ActionObjet o: plateau.selection.get(plateau.currentPlayer.id)){
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
				if(player==plateau.currentPlayer.id){
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
			for(int k=0; k<this.plateau.messages.get(plateau.currentPlayer.id).size();k++){
				m = this.plateau.messages.get(plateau.currentPlayer.id).get(k);
				g.setColor(m.color);
				Font f = g.getFont();
				float height = f.getHeight(m.message);
				g.drawString(m.message, 20f, this.topBars.sizeY+20f+2f*height*k);
			}
		}
	}
	// Do our logic 
	@Override
	public synchronized void update(GameContainer gc, int t) throws SlickException {	
		Vector<InputModel> ims = new Vector<InputModel>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game
		if(isInMenu){
			this.menuCurrent.update(gc.getInput());
		} else {
			InputModel im = new InputModel(this,0,plateau.currentPlayer.id,gc.getInput(),(int) plateau.Xcam,(int)Math.floor(plateau.Ycam),(int)resX,(int)resY);
			ims.add(im);
			if(inMultiplayer){
				//Utils.printCurrentState(this.plateau);
				if(!host){
					// client mode
					this.toSendInputs.addElement(im.toString());
					if(outputs.size()>0){
						this.plateau.currentString = outputs.lastElement();
						outputs.clear();
						//System.out.println("paquets perdus:" +(outputs.size()-1));
						//outputs.clear();
					}
					this.plateau.update(ims);
				} else {
					// host mode
					if(inputs.size()>0){
						ims.add(this.inputs.lastElement());	
						inputs.clear();
						//System.out.println(ims.lastElement());
					}
					this.plateau.update(ims);
					for(Vector<String> v : this.toSendOutputs){
						v.add(this.plateau.currentString);
					}
					System.out.println(this.toSendOutputs.size());
				}
			} else {
				// solo mode
				this.plateau.update(ims);
			}


		}
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
		this.images = new Images();
		this.musics = new Musics();

		this.menuIntro = new MenuIntro(this);
		this.menuOptions = new MenuOptions(this);
		this.menuMulti = new MenuMulti(this);
		this.menuMapChoice = new MenuMapChoice(this);
		this.setMenu(menuIntro);
		this.connexionReceiver.start();
		Map.initializePlateau(this, 1f, 1f);

		//System.out.println(this.plateau.mapGrid);
		//			Map.createMapEmpty(this);
		// Instantiate BottomBars for all players:
		for(int player=1; player<3; player++){
			new BottomBar(this.plateau,this.plateau.players.get(player),(int)this.resX,(int)this.resY);
			new TopBar(this.plateau,(int)this.resX,(int)this.resY);
		}
		this.bottomBars = this.plateau.currentPlayer.bottomBar;
		this.topBars = this.plateau.currentPlayer.topBar;
		selection = null;
		
	}


	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;


		connexionReceiver = new MultiReceiver(this,portConnexion);
		//TODO: upgrading multiplaying
		connexionSender = new MultiSender(null, portConnexion, this.toSendConnexions,this);

	}
}
