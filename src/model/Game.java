package model;
import java.net.InetAddress;
import java.util.Timer;
import java.util.Vector;

import main.Main;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuOptions;
import multiplaying.Clock;
import multiplaying.InputHandler;
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
import display.Message;

public class Game extends BasicGame 
{	
	// DEBUG
	public static boolean debugTimeSteps = false;
	public static boolean debugPaquet = false;
	public static boolean debugValidation = false;
	public static boolean debugReceiver = false;
	public static boolean debugSender = false;
	public static boolean debugTourEnCours = false;

	// debugging tools
	long timeSteps = 0;
	public int round = 0;
	public int roundDebug = 0;
	public int idPaquetSend = 0;
	public int nbPaquetReceived = 0;
	public int idPaquetReceived = 0;
	public int idPaquetTreated = 0;

	//Increment de game

	public static float ratio = 60f/((float)Main.framerate);

	//Handle inputs from you and other players
	public InputHandler inputsHandler;

	//ID host
	public static int ID_HOST = 1;
	//Period of parse in frame
	public static int Tparsing = 27;

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
	//Clock
	public Clock clock;
	public boolean inMultiplayer;
	public boolean host = false;
	public long startTime;
	public int portConnexion = 6113;
	public int portInput = 6114;
	public int portOutput = 6115;
	public int portChat = 2347;
	// Host and client
	public InetAddress addressHost;
	//public Vector<InputObject> inputs = new Vector<InputObject>();
	public Vector<Vector<String>> toSendInputs = new Vector<Vector<String>>();
	public Vector<String> toSendConnexions = new Vector<String>();
	public Vector<String> connexions = new Vector<String>();
	public int timeValue;
	// Sender and Receiver
	public MultiReceiver inputReceiver;
	public Vector<MultiSender> inputSender = new Vector<MultiSender>();
	public MultiReceiver connexionReceiver;
	public MultiSender connexionSender;
	//Debugging network
	public int toAdd = 0;
	public int toRemove = 0;
	public Vector<Integer> vroundDropped=new Vector<Integer>();
	public Vector<Integer> vroundMissing = new Vector<Integer>();
	// To parse
	public String toParse= null;
	//Debug paquets dropped
	public int roundDropped=0;
	public int roundDroppedValidate = 0;
	public int roundDroppedMissing = 0;
	// Menus
	public Menu menuPause;
	public MenuIntro menuIntro;
	public MenuOptions menuOptions;
	public MenuMulti menuMulti;
	public MenuMapChoice menuMapChoice;
	public Menu menuCurrent = null;
	public boolean isInMenu = false;
	public int idInput;

	public Vector<String> checksum = new Vector<String>();
	public Vector<String> clockSynchro= new Vector<String>();
	public boolean processSynchro=false;

	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
		app.setClearEachFrame(true);
		this.plateau.Xcam = (int) (this.plateau.maxX/2 - this.resX/2);
		this.plateau.Ycam = (int) (this.plateau.maxY/2 -this.resY/2);
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
		// Draw the buildings
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
				if(player==plateau.currentPlayer.id){
					g.setColor(Color.green);
					g.draw(this.plateau.rectangleSelection.get(player));
				}
			}
		}
		// Draw bottom bar
		g.translate(plateau.Xcam, plateau.Ycam);
		if(this.plateau.currentPlayer.bottomBar!=null)
			this.plateau.currentPlayer.bottomBar.draw(g);
		if(this.plateau.currentPlayer.bottomBar.topBar!=null)
			this.plateau.currentPlayer.bottomBar.topBar.draw(g);
		// Draw messages
		Message m;
		if(this.plateau.messages.size()>2){
			for(int k=0; k<this.plateau.messages.get(plateau.currentPlayer.id).size();k++){
				m = this.plateau.messages.get(plateau.currentPlayer.id).get(k);
				g.setColor(m.color);
				Font f = g.getFont();
				float height = f.getHeight(m.message);
				g.drawString(m.message, 20f, this.plateau.currentPlayer.bottomBar.topBar.sizeY+20f+2f*height*k);
			}
		}

		if(debugTimeSteps)
			System.out.println("fin du render : "+(System.currentTimeMillis()-timeSteps));
	}
	// Do our logic 
	@Override
	public void update(GameContainer gc, int t) throws SlickException {	
		Vector<InputObject> ims = new Vector<InputObject>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game	
		if(isInMenu){
			this.menuCurrent.update(gc.getInput());
		} else {

			if(debugTimeSteps)
				System.out.println("fin du tour - temps total : "+(System.currentTimeMillis()-timeSteps));
			if(debugTimeSteps)
				timeSteps = System.currentTimeMillis();	
			//Update of current round
			this.clock.setRoundFromTime();

			//Testing clock syncrho



			this.clockSynchro.addElement("3H|"+this.round+"|"+this.clock.getCurrentTime()+"|");
			this.sendInputToAllPlayer(this.clockSynchro.lastElement());
			if(this.clockSynchro.size()>20){
				this.clockSynchro.remove(0);
			}
			//System.out.println("line 270 : " +this.round+ " " + Long.toString(this.clock.getCurrentTime()).substring(2, 5));
			this.roundDebug++;
			if(Game.debugValidation)
				System.out.println("Game line 252: rounds jou�s:"+roundDebug+" round actuel: "+round);
			InputObject im = new InputObject(this,plateau.currentPlayer,gc.getInput());
			if(debugTimeSteps)
				System.out.println("-- NOUVEAU TOUR --");
			if(debugTimeSteps)
				System.out.println("calcul de l'input : "+(System.currentTimeMillis()-timeSteps));
			if(inMultiplayer){
				if(Game.debugValidation || Game.debugTourEnCours)
					System.out.println("=== = = =Game line 258 : now in round "+this.round);
				//Utils.printCurrentState(this.plateau);
				// On envoie l'input du tour courant
				this.sendInputToAllPlayer(im.toString());



				//Checksum for testing synchro
				if(this.plateau.characters.size()>0){
					//Compute checksum
					//this.checksum.addElement("3C|"+this.round+"|"+this.plateau.characters.get(0).x+"-"+this.plateau.characters.get(0).y+"-"+this.plateau.characters.size()+"|");

					String checksum = "3C|"+this.round+"|";
					int i = 0;

					while(i<this.plateau.characters.size()){
						checksum+=Integer.toString(((int)(10*this.plateau.characters.get(i).x))%10);
						checksum+=Integer.toString(((int)(10*this.plateau.characters.get(i).y))%10);
						i++;
					}
					checksum+="|";
					this.checksum.addElement(checksum);
					this.sendInputToAllPlayer(this.checksum.lastElement());
					if(this.checksum.size()>5){
						this.checksum.remove(0);
					}
				}

				//Si Desynchro on envoie un process de synchro
				if(this.host && this.processSynchro){
					this.sendInputToAllPlayer(this.plateau.toStringArray(16000).get(0));
				}
				//To string du plateau tous les n_turns
				//				if(this.host && this.round%Game.Tparsing==0){
				//
				//					Vector<String> plateauState = this.plateau.toStringArray(256);
				//					for(String s : plateauState){
				//						this.sendInputToAllPlayer(s);
				//					}
				//
				//				}

				// On ajoute l'input du tour courant � l'inputhandler				
				this.inputsHandler.addToInputs(im);
				if(debugTimeSteps)
					System.out.println("update du plateau client: "+(System.currentTimeMillis()-timeSteps));
				if(Game.debugValidation){
					System.out.println("Game line 266 : Paquets dropped missing: "+this.roundDroppedMissing);
					System.out.println("Game line 266 : Paquets dropped validate : "+this.roundDroppedValidate);
					System.out.println("Game line 266 : Paquets dropped : "+this.roundDropped);
					System.out.println("Game J'AI PAS RECU LA VALIDATION POUR JOUER MES TRUCS: ");
					for(Integer i : this.vroundDropped){
						System.out.print(i+",");
					}
					System.out.println();
					System.out.println("Game J'AI PAS RECU L'INPUT DE L'AUTRE : ");
					for(Integer i : this.vroundMissing){
						System.out.print(i+",");
					}
					System.out.println();
				}
				this.plateau.handleView(im, this.plateau.currentPlayer.id);
				ims = this.inputsHandler.getInputsForRound(this.round);

				//Parse the plateau if informations are received.
				int i = 0;
				//				while(i<this.toParse.size()){
				//					this.plateau.parse(this.toParse.get(i));
				//					i++;
				//
				//					//Clear the parser at each global parsing
				//					this.toParse.clear();
				//
				//				}
				//				// On joue tout le temps les tours mais on peut annuler des inputs ( par soucis de fluidité)

				boolean successSynchro = false;
				if(processSynchro){
					//Si round+2
					String[] u = this.toParse.split("!");
					if(Integer.parseInt(u[1])==(this.round-InputHandler.nDelay)){
						this.plateau.parse(this.toParse);
						this.processSynchro = false;
					}

				}
				
				if(!successSynchro){
					this.plateau.update(ims);

					this.plateau.updatePlateauState();
				}
				if(debugTimeSteps)
					System.out.println("update du plateau serveur: "+(System.currentTimeMillis()-timeSteps));

			} else {
				ims.add(im);
				this.plateau.handleView(im, this.plateau.currentPlayer.id);
				// solo mode, update du joueur courant
				this.plateau.update(ims);
				//Update des ordres de l'IA
				this.plateau.updateIAOrders();

				// Maintenant l'update effectif du plateau est séparé ..
				this.plateau.updatePlateauState();
				//Update IA orders
				if(debugTimeSteps)
					System.out.println("update du plateau single player: "+(System.currentTimeMillis()-timeSteps));

			}
		}
		if(debugPaquet){
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
		this.plateau.Xcam =(int)( this.plateau.currentPlayer.getGameTeam().hq.getX()-this.resX/2);
		this.plateau.Ycam = (int)(this.plateau.currentPlayer.getGameTeam().hq.getY()-this.resY/2);
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
		this.inputsHandler = new InputHandler(this);
		//System.out.println(this.plateau.mapGrid);
		//			Map.createMapEmpty(this);
		// Instantiate BottomBars for all players:
		selection = null;
		this.clock = new Clock(this);

	}

	public void sendInputToPlayer(Player player, String s){
		this.toSendInputs.get(player.id).add(s);
	}
	public void sendInputToAllPlayer(String s){
		for(int i=0; i<this.toSendInputs.size();i++)
			if(i!=0 && i!=plateau.currentPlayer.id){
				toSendInputs.get(i).add(s);
			}
		if(Game.debugValidation){
			if(s.charAt(1)=='I')
				System.out.println("Game line 351 : Sending inputs to all players for round "+this.round);
			else if(s.charAt(1)=='V')
				System.out.println("Game line 351 : Sending a validation to all players");
			else if(s.charAt(1)=='P')
				System.out.println("Game line 351 : Sending a plateau parse to all players");
		}
	}

	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;



		connexionReceiver = new MultiReceiver(this,portConnexion);
		connexionSender = new MultiSender(null, portConnexion, this.toSendConnexions,this);

	}

}
