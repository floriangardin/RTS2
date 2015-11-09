package model;
import java.net.InetAddress;
import java.util.Timer;
import java.util.Vector;

import menu.Menu;
import menu.MenuIntro;
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuOptions;
import multiplaying.InputMailBox;
import multiplaying.InputModel;
import multiplaying.InputObject;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

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

import spells.SpellEffect;
import units.Character;
import buildings.Building;
import bullets.Bullet;
import display.BottomBar;
import display.Message;
import display.TopBar;

public class Game extends BasicGame 
{	

	// DEBUG
	public boolean debugTimeSteps = false;
	long timeSteps = 0;
	public boolean debugPaquet = true;
	public int round = 0;
	public int idPaquetSend = 0;
	public int nbPaquetReceived = 0;
	public int idPaquetReceived = 0;
	public int idPaquetTreated = 0;
	
	//NEW FLO
	//Handle inputs from you and other players
	public InputMailBox inputsBox;
	

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
	public Vector<Objet> objets_selection= new Vector<Objet>();

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
		g.fillRect(this.plateau.maxX, 0, 2*this.plateau.maxX, 2*this.plateau.maxY);
		g.fillRect(0, this.plateau.maxY, 2*this.plateau.maxX, 2*this.plateau.maxY);
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

		if(debugTimeSteps)
			System.out.println("fin du render : "+(System.currentTimeMillis()-timeSteps));
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
			if(debugTimeSteps)
				System.out.println("fin du tour - temps total : "+(System.currentTimeMillis()-timeSteps));
			if(debugTimeSteps)
				timeSteps = System.currentTimeMillis();	
			InputModel im = new InputModel(this,plateau.currentPlayer.id,round,gc.getInput(),(int) plateau.Xcam,(int)Math.floor(plateau.Ycam),(int)resX,(int)resY);
			ims.add(im);
			if(debugTimeSteps)
				System.out.println("-- NOUVEAU TOUR --");
			if(debugTimeSteps)
				System.out.println("calcul de l'input : "+(System.currentTimeMillis()-timeSteps));
			if(inMultiplayer){
				// Plus de host et de client ...
				//
				//Utils.printCurrentState(this.plateau);

				// client mode
				// On envoie ses inputs 
				this.toSendInputs.addElement(im.toString());
				// On ajoute ses inputs dans sa mailbox ( qui se charge automatiquement de mettre l'input 
				// dans la boîte d'envoie
				
				this.inputsBox.addInput(new InputObject(this,im,true));
				if(outputs.size()>0){
					this.idPaquetTreated++;
					this.plateau.currentString = outputs.lastElement();
					outputs.clear();
				}
				
				if(debugTimeSteps)
					System.out.println("update du plateau client: "+(System.currentTimeMillis()-timeSteps));

				// host mode
				//On reçoit ses inputs
				// si on a des inputs en attente on les passe en argument � update de plateau
				while(inputs.size()>0){
					//TODO : Message de validation à envoyer
					
					// Message de validation à envoyer ...
					this.idPaquetTreated++;
					//inputs contient directement les inputs déja formatés.
					ims.add(this.inputs.lastElement());
					this.idPaquetReceived = inputs.get(0).time;
					inputs.remove(0);
					//System.out.println(ims.lastElement());
				}
				//ICI on met bout à bout tous les inputs validés
				Vector<InputModel> imss= new Vector<InputModel>();
				for(Player p : this.plateau.players){
					imss.addAll(p.inputs);
				}
				
				this.plateau.update(imss);
				if(debugTimeSteps)
					System.out.println("update du plateau serveur: "+(System.currentTimeMillis()-timeSteps));
				for(Vector<String> v : this.toSendOutputs){
					v.add(this.plateau.currentString);
				}
				//System.out.println(this.toSendOutputs.size());

				
				
				
				
			} else {
				// solo mode
				this.plateau.update(ims);
				if(debugTimeSteps)
					System.out.println("update du plateau single player: "+(System.currentTimeMillis()-timeSteps));

			}
		}
		if(debugPaquet){
			round ++ ;
			System.out.println("tour de jeu: " + round);
			System.out.println("nb paquets envoy�s: " + idPaquetSend);
			System.out.println("nb paquets re�us: " + nbPaquetReceived);
			System.out.println("-- difference: " + (idPaquetSend - idPaquetReceived));
			System.out.println("nb paquets trait�s: " + idPaquetTreated);
		}

	}

	public void launchGame(){

		this.musics.imperial.loop();
		this.musics.imperial.setVolume(options.musicVolume);
		//this.game.newGame();
		this.quitMenu();
		this.plateau.Xcam = this.plateau.currentPlayer.getGameTeam().hq.getX()-this.resX/2;
		this.plateau.Ycam = this.plateau.currentPlayer.getGameTeam().hq.getY()-this.resY/2;
		this.startTime = System.currentTimeMillis();
		this.nbPaquetReceived = 0;
		this.idPaquetSend = 0;
		this.idPaquetTreated = 0;
		this.round = 0;
	}

	
	public Player getPlayerById(int id){
		return this.plateau.players.get(id);
		
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
		
		//FLO INPUTS
		this.inputsBox = new InputMailBox(this);
		//System.out.println(this.plateau.mapGrid);
		//			Map.createMapEmpty(this);
		// Instantiate BottomBars for all players:
		this.bottomBars = new BottomBar(this.plateau,(int)this.resX,(int)this.resY);
		this.topBars = new TopBar(this.plateau,(int)this.resX,(int)this.resY);
		selection = null;

	}

	
	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;


		connexionReceiver = new MultiReceiver(this,portConnexion);
		connexionSender = new MultiSender(null, portConnexion, this.toSendConnexions,this);

	}
}
