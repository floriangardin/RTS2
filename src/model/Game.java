
package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.Main;
import mapeditor.MapEditor;
import menu.Credits;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuOptions;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import multiplaying.Clock;
import multiplaying.InputHandler;
import multiplaying.InputObject;
import multiplaying.MultiMessage;
import multiplaying.MultiReceiver;
import multiplaying.MultiReceiver.Checksum;
import ressources.Images;
import ressources.Map;
import ressources.Musics;
import ressources.Sounds;
import ressources.Taunts;
import multiplaying.MultiSender;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

import spells.SpellEffect;
import tests.FatalGillesError;
import tests.Test;
import units.Character;
import buildings.Bonus;
import buildings.Building;
import buildings.BuildingProduction;
import buildings.BuildingTech;
import bullets.Bullet;
import display.DisplayRessources;
public class Game extends BasicGame 
{
	/////////////
	/// DEBUG ///
	/////////////

	public static boolean tests = false;
	public static Game g;
	public static boolean debugInputs = false;
	public static boolean debugTimeSteps = false;
	public static boolean debugPaquet = false;
	public static boolean debugValidation = false;
	public static boolean debugReceiver = false;
	public static boolean debugSender = false;
	public static boolean debugTourEnCours = false;
	public static boolean debugThread = false;
	public static boolean debugDisplayDebug = false;
	public static boolean debugMemory = false;

	public static boolean deplacementGroupIntelligent = true;
	public static boolean debugGroup = false;

	public boolean displayMapGrid = false;

	public static boolean showUpdateLogicInterval = false;


	// debugging tools
	long timeSteps = 0;
	public int nbGameTurn = 0;

	public int round = 0;
	public int roundDebug = 0;

	public int idPaquetSend = 0;
	public int nbPaquetReceived = 0;
	public int idPaquetReceived = 0;
	public int idPaquetTreated = 0;


	//Increment de game
	public static float ratio = 60f/((float)Main.framerate);

	public static int nbRoundInit = 3*Main.framerate;

	public int idChar = 0;
	public int idBullet = 0;

	// Font 
	public UnicodeFont font;

	// Music and sounds
	public Options options;
	public Images images;
	public Sounds sounds;
	public Musics musics;
	public Taunts taunts;
	public Music musicPlaying;

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


	////////////////////////
	/// PLAYERS && TEAMS ///
	////////////////////////

	// Number of teams
	public int nTeams =2;
	// Number of players
	public int nPlayers;
	// teams and players
	public Vector<Player> players = new Vector<Player>();
	public Player currentPlayer;
	public Vector<GameTeam> teams = new Vector<GameTeam>();


	/////////////////////////////
	/// NETWORK & MULTIPLAYING///
	/////////////////////////////

	//Clock
	public Clock clock;
	public boolean inMultiplayer;
	public boolean host = false;
	public long startTime;
	// Host and client
	public InetAddress addressHost;
	public InetAddress addressBroadcast;
	public InetAddress addressLocal;
	// port
	public int port = 2302;
	// depots for senders
	DatagramSocket server;
	DatagramSocket client;
	int nbReception, tempsReception;
	// depots for receivers
	public Vector<String> receivedConnexion = new Vector<String>();
	public Vector<String> receivedValidation = new Vector<String>();
	public Vector<String> receivedInputs = new Vector<String>();
	public Vector<String> receivedPing = new Vector<String>();
	public Vector<String> receivedResynchro = new Vector<String>();
	public Vector<Checksum> receivedChecksum = new Vector<Checksum>();
	public Vector<String> receivedChat = new Vector<String>();
	// Sender
	// public MultiSender sender;
	// Receiver
	// public MultiReceiver receiver;
	// Chat
	public ChatHandler chatHandler;
	// Handling multiplaying
	public InputHandler inputsHandler;
	public String toParse= null;
	public boolean processSynchro;
	public Vector<Checksum> checksum = new Vector<Checksum>();
	private boolean sendParse;
	public Lock mutexChecksum = new ReentrantLock();
	// antidrop
	public boolean toDrawDrop = false;
	public boolean toDrawAntiDrop = false;
	public int roundDelay;

	public String toSendThisTurn = "";


	/////////////
	/// MENUS ///
	/////////////

	public Menu menuPause;
	public MenuIntro menuIntro;
	public MenuOptions menuOptions;
	public MenuMulti menuMulti;
	public MenuMapChoice menuMapChoice;
	public Credits credits;
	public Menu menuCurrent = null;
	public boolean isInMenu = false;
	public int idInput;
	public float ratioResolution;

	//editor
	public boolean inEditor;
	public MapEditor editor;

	//////////////////////////
	/// VICTORY AND DEFEAT ///
	//////////////////////////

	public boolean endGame = false;
	public boolean victory = false;
	int victoryTime = 120;
	boolean hasAlreadyPlay = false;

	public boolean antidrop;
	int nombreDrop = 0;
	int nombrePlayed = 0;
	int delaySleep = 0;
	static int delaySleepAntiDrop = 9;

	public boolean pingPeak=false;


	//////////////////////////
	///       REPLAYS     ///
	////////////////////////

	public Replay replay = null;



	//////////////////////////////
	// INITIALIZING GAME ENGINE //
	//////////////////////////////

	public static int nbLoadedThing = 0;
	public boolean thingsLoaded = false;
	public boolean plateauLoaded = false;
	public boolean allLoaded = false;
	private boolean special = false;
	private boolean rate = false;
	private int gillesPasse = -1;
	public Image loadingSpearman;
	public Image loadingGilles;
	public Image loadingTitle;
	public Image loadingBackground;
	public float toGoTitle = -0f;
	public int animationLoadingSpearman=0;
	private String lastThing;
	private boolean waitLoading;
	private DeferredResource nextResource;
	private Vector<DisplayRessources> displayRessources = new Vector<DisplayRessources>();

	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
		//app.setClearEachFrame(true);
	}
	public void setMenu(Menu m){
		// handle change of menu
		// including the change of music
		this.menuCurrent = m;
		this.isInMenu = true;

		if((m instanceof MenuMulti || m instanceof MenuMapChoice) && this.musicPlaying!=this.musics.get("themeMulti")){
			this.musicPlaying.stop();
			this.musicPlaying=this.musics.get("themeMulti");
			this.musicPlaying.setVolume(this.options.musicVolume);
			this.musicPlaying.play();
		}
		if(m instanceof MenuIntro && this.musicPlaying!=this.musics.get("themeMenu")){
			this.musicPlaying.stop();
			this.musicPlaying=this.musics.get("themeMenu");
			this.musicPlaying.setVolume(this.options.musicVolume);
			this.musicPlaying.play();
		}
		if(m instanceof Credits){
			this.musicPlaying.stop();
			this.musicPlaying=this.musics.get("themeMapEditor");
			this.musicPlaying.setVolume(this.options.musicVolume);
			this.musicPlaying.play();
		}
		if(m instanceof MenuMapChoice){
			((MenuMapChoice)m).initialize();
		}

	}

	public void addPlayer(String name, InetAddress address,int resX,int resY){
		this.players.addElement(new Player(this.plateau,players.size(),name,teams.get(1),resX,resY));
		this.players.lastElement().address = address;
		nPlayers+=1;

		// adding components in plateau
		this.plateau.selection.addElement(new Vector<ActionObjet>());
		this.plateau.toAddSelection.addElement(new Vector<ActionObjet>());
		this.plateau.toRemoveSelection.addElement(new Vector<ActionObjet>());

		this.plateau.rectangleSelection.addElement(null);
		this.plateau.recX.addElement(0f);
		this.plateau.recY.addElement(0f);
		this.plateau.inRectangle.addElement(new Vector<ActionObjet>());

	}

	public void removePlayer(int indice){
		if(indice==0 || indice>players.size())
			return;
		players.remove(indice);
		nPlayers -= 1;

		// deleting component from plateau
		this.plateau.selection.remove(indice);
		this.plateau.toAddSelection.remove(indice);
		this.plateau.toRemoveSelection.remove(indice);
		this.plateau.rectangleSelection.remove(indice);
		this.plateau.recX.remove(indice);
		this.plateau.recY.remove(indice);
		this.plateau.inRectangle.remove(indice);
	}
	// functions that handle buffers

	public void clearPlayer(){
		/**
		 * function that remove all players but the nature (player 0)
		 */
		while(players.size()>1){
			removePlayer(players.size()-1);
		}
	}

	public void initializePlayers(){
		//UPDATING GAME
		this.teams.clear();
		this.players.clear();
		this.teams.addElement(new GameTeam(players,this.plateau,0,0));
		this.teams.addElement(new GameTeam(players,this.plateau,1,0));
		this.teams.addElement(new GameTeam(players,this.plateau,2,0));
		this.players = new Vector<Player>();
		this.players.add(new Player(this.plateau,0,"Nature",teams.get(0),2,2));
		this.players.add(new Player(this.plateau,1,this.options.nickname,teams.get(1),(int) this.resX, (int) this.resY));
		//		this.players.add(new IAFlo(this.plateau,2,"IA random",teams.get(2),2,2));
		this.currentPlayer = players.get(1);
		this.nPlayers = players.size();
		this.plateau.initializePlateau(this);
	}

	//////////////////////////////////////////////////////

	//////////////
	/// RENDER ///
	//////////////

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		if(!thingsLoaded){
			g.setColor(Color.black);
			g.fillRect(0, 0, resX, resY);
			float toGoTitle2 = Math.max(0f,toGoTitle);
			if(lastThing!=null && toGoTitle2==0f){
				int startBarX = (int) (resX/10);
				int startBarY = (int) (18*resY/20);
				int sizeBarX = (int) (resX - 2*startBarX);
				int sizeBarY = (int)(resY/40);
				g.setColor(Color.white);
				g.fillRect(startBarX-2, startBarY-2,sizeBarX+4, sizeBarY+4);
				g.setColor(Color.black);
				g.fillRect(startBarX, startBarY,sizeBarX, sizeBarY);
				float x = 1f*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing;
				g.setColor(new Color(1f-x,0f,x));
				g.fillRect(startBarX, startBarY,sizeBarX*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing, sizeBarY);
				if(LoadingList.get().getRemainingResources() > 0){
					g.setColor(Color.white);
					g.drawString(""+lastThing, startBarX+20f, startBarY+sizeBarY/2-font.getHeight("H")/2);
				}
				int xanimation = startBarX + sizeBarX*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing;
				if(special){
					int height = this.loadingSpearman.getHeight();
					int width = this.loadingSpearman.getWidth();
					g.drawImage(this.loadingSpearman,xanimation-width/2,startBarY-sizeBarY-height);					
				} else {
					this.animationLoadingSpearman++;
					this.animationLoadingSpearman=this.animationLoadingSpearman%(Main.framerate/2);
					int height = this.loadingSpearman.getHeight()/4;
					int width = this.loadingSpearman.getWidth()/5;
					int w = animationLoadingSpearman*8/Main.framerate+1;
					g.drawImage(this.loadingSpearman.getSubImage(w*width,height,width,height),xanimation-width/2,startBarY-sizeBarY-height);
				}				
				//g.drawImage(this.loadingSpearman.getSubImage(((w+2)%4)*width,height,width,height),startBarX/2-width/2,startBarY+sizeBarY/2-height/2);
				//				g.setColor(Color.white);
				//				String s = "Chargement...";
				//				g.drawString(s, 7*resX/8,16*resY/20+height/2-font.getHeight(s)/2);
			}
			Image temp = this.loadingBackground;
			temp.setAlpha(toGoTitle2);
			g.drawImage(temp, 0,0,resX,resY,0,0,temp.getWidth(),temp.getHeight()-60f);
			temp = this.loadingTitle.getScaledCopy(0.5f+toGoTitle2/2f);
			float xTitle = (this.resX/2-temp.getWidth()/2) ;
			float yTitle = toGoTitle2*(10f) + (1f-toGoTitle2)*(resY/3);
			g.drawImage(temp, xTitle, yTitle);
			return;
		}
		g.setFont(this.font);
		if(isInMenu){
			if(hasAlreadyPlay){
				g.translate(+plateau.Xcam,+ plateau.Ycam);
			}
			g.translate(-plateau.Xcam,- plateau.Ycam);
			this.menuCurrent.draw(g);
			if(inMultiplayer && menuCurrent instanceof MenuMapChoice){
				this.chatHandler.draw(g);
			}
		} else if (inEditor){
			this.editor.draw(g);
		} else if (endGame){

			g.setColor(Color.black);
			g.fillRect(0, 0, this.resX, this.resY);
			g.setColor(Color.white);
			//PRint victory
			if(this.victory){
				g.drawString("Vous Avez Gagné !", this.resX/3f, this.resY/3f);
			}
			else{
				g.drawString("Vous Avez Perdu...", this.resX/3f, this.resY/3f);
			}
		} else {
			// g reprï¿½sente le pinceau
			//g.setColor(Color.black);
			g.translate(-plateau.Xcam,- plateau.Ycam);
			//Draw background
			g.drawImage(this.images.get("seaBackground"), -this.plateau.maxX, -this.plateau.maxY,
					2*this.plateau.maxX, 2*this.plateau.maxY, 0, 0, this.images.get("seaBackground").getWidth(),this.images.get("seaBackground").getHeight());


			g.drawImage(this.images.get("islandTexture"),0, 0, this.plateau.maxX, this.plateau.maxY,
					0, 0, this.images.get("islandTexture").getWidth(),  this.images.get("islandTexture").getHeight());

			// Draw the selection of your team 
			for(ActionObjet o: plateau.selection.get(currentPlayer.id)){
				if(o.target!=null && o instanceof Checkpoint){
					Checkpoint c = (Checkpoint) o.target;
					c.toDraw = true;
				}
				o.drawIsSelected(g);
				if(Game.debugGroup){
					if(o instanceof Character && ((Character) o).group!=null){
						for(Character c : ((Character)o).group){
							g.setColor(Color.white);
							g.fillRect(c.getX()-50f, c.getY()-50f, 100f, 100f);
						}
					}
				}
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

			//Draw bonuses
			for(Bonus o : plateau.bonus){
				//o.draw(g);
				o.draw(g);
			}
			// Draw the natural Objets

			for(NaturalObjet o : this.plateau.naturalObjets){
				if(o.visibleByCurrentPlayer)
					toDrawAfter.add(o);
				else
					toDraw.add(o);
			}
			// Draw the buildings
			for(Building e : this.plateau.buildings){
				if(e.visibleByCurrentPlayer)
					toDrawAfter.add(e);
				else
					toDraw.add(e);
			}
			for(SpellEffect e : this.plateau.spells){
				if(e.visibleByCurrentPlayer)
					toDrawAfter.add(e);
				else
					toDraw.add(e);
			}
			for(Bullet b : this.plateau.bullets){
				if(b.visibleByCurrentPlayer)
					toDrawAfter.add(b);
				else
					toDraw.add(b);
			}
			for(Checkpoint c : this.plateau.checkpoints){
				toDrawAfter.add(c);
			}
			for(Checkpoint c : this.plateau.markersBuilding){
				toDraw.add(c);
			}
			Utils.triY(toDraw);
			Utils.triY(toDrawAfter);
			// determine visible objets
			for(Objet o: toDraw)
				o.draw(g);
			// draw fog of war
			plateau.drawFogOfWar(g);
			for(Objet o: toDrawAfter)
				o.draw(g);

			// Draw the selection :
			if(plateau.cosmetic.selection!=null){
				g.setColor(Colors.selection);
				this.plateau.cosmetic.draw(g);
			}

			// Draw and handle display ressources
			Vector<DisplayRessources> toRemove = new Vector<DisplayRessources>();
			for(DisplayRessources dr : this.displayRessources ){
				dr.update();
				if(dr.isDead())
					toRemove.add(dr);
				else
					dr.draw(g);
			}
			this.displayRessources.removeAll(toRemove);
			toRemove.clear();

			// Draw bottom bar
			g.translate(plateau.Xcam, plateau.Ycam);

			if(this.round<nbRoundInit){
				g.setColor(new Color(0f,0f,0f,1f-0.3f*round/nbRoundInit));
				g.fillRect(0, 0, resX, resY);
//				int sec = (int)((nbRoundInit-round)/Main.framerate);
//				if(sec<=2 ){
//					g.setColor(Color.white);
//					String s = ""+(sec+1);
//					g.drawString(s,this.resX/5-font.getWidth(s)/2,resY/2-font.getHeight(s)/2);
//					g.drawString(s,4*this.resX/5-font.getWidth(s)/2,resY/2-font.getHeight(s)/2);
//				}
				g.drawImage(this.menuIntro.title, this.resX/2-this.menuIntro.title.getWidth()/2, this.resY/2-this.menuIntro.title.getHeight()/2);
			}
			if(this.currentPlayer.bottomBar.topBar!=null)
				this.currentPlayer.bottomBar.topBar.draw(g);
			if(this.currentPlayer.bottomBar!=null)
				this.currentPlayer.bottomBar.draw(g);

		}
		if(Game.debugDisplayDebug){
			if(processSynchro){
				g.setColor(Color.green);
				g.drawString("Resynchro", 30f, 90f);
				g.fillRect(10f,90f,15f,15f);
			}
			if(toDrawDrop){
				g.setColor(Color.red);
				g.drawString("Round Drop", 30f,  90f);
				g.fillRect(10f,90f,15f,15f);
			}
			if(toDrawAntiDrop){
				g.setColor(Color.blue);
				g.drawString("AntiDrop done", 30f,  90f);
				g.fillRect(10f,90f,15f,15f);
			}
			if(!inEditor){
				g.setColor(Color.white);
				this.drawPing(g);

			}
		}
		this.chatHandler.draw(g);
		//		if(debugTimeSteps)
		//			System.out.println("fin du render : "+(System.currentTimeMillis()-timeSteps));
		//		Runtime runtime = Runtime.getRuntime();
		//
		//		NumberFormat format = NumberFormat.getInstance();
		//
		//		StringBuilder sb = new StringBuilder();
		//		long maxMemory = runtime.maxMemory();
		//		long allocatedMemory = runtime.totalMemory();
		//		long freeMemory = runtime.freeMemory();
		//
		//		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		//		sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
		//		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
		//		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");	


	}


	//////////////
	/// UPDATE ///
	//////////////

	@Override
	public void update(GameContainer gc, int t) throws SlickException{
		// Initializing engine
		if(!thingsLoaded){
			this.initializeEngine(gc);
			return;
		}


		// Handling multiReceiver
		this.handleMultiReceiver();

		//		Thread[] tarray = new Thread[Thread.activeCount()];
		//		Thread.enumerate(tarray);
		//		System.out.println("threads prï¿½sents : "+tarray.length);
		//		for(int i=0; i<tarray.length; i++){
		//			System.out.println(tarray[i].getName());
		//		}
		//		System.out.println();
		//		if(t!=16)
		//			System.out.println("Le round "+round+" a dure "+t);
		Vector<InputObject> ims = new Vector<InputObject>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game	
		this.taunts.update();
		if(isInMenu){
			Input in = gc.getInput();
			InputObject im = new InputObject(this,currentPlayer,in,true);
			if(inMultiplayer && menuCurrent instanceof MenuMapChoice){
				this.chatHandler.action(in,im);
			}
			this.menuCurrent.update(im);
			//this.send();
		} else if(inEditor) {
			// Map Editor
			Input in = gc.getInput();
			InputObject im = new InputObject(this,currentPlayer,in,!processSynchro);
			this.editor.update(im,in);
		} else if(!endGame) {

			// gérer le début de partie

			//Update of current round
			this.clock.setRoundFromTime();
			// getting inputs
			Input in = gc.getInput();
			//			if(in.isKeyPressed(Input.KEY_RALT)){
			//				this.displayMapGrid = !this.displayMapGrid;
			//			}
			InputObject im = new InputObject(this,currentPlayer,in,true);
			this.chatHandler.action(in,im);
			if(this.chatHandler.typingMessage){
				im.eraseLetter();
			} else {
				//this.manuelAntidrop(in,gc);
			}
			//			if(replay==null){
			//				replay = new Replay(2,"apocalypse",0,this);
			//			}
			if(inMultiplayer){

				////////////////////
				/// MULTI PLAYER ///
				////////////////////

				//toSendThisTurn = "";
				this.toDrawAntiDrop = false;
				this.toDrawDrop = false;
				this.toSendThisTurn+="1"+im.toString()+"%";
				this.inputsHandler.addToInputs(im);
				this.handleChecksum();
				this.handlePing();
				this.handleSendingResynchroParse();
				this.handleResynchro();
				this.send(toSendThisTurn);
				if(!chatHandler.typingMessage){
					this.plateau.handleView(im, this.currentPlayer.id);
				}
				ims = this.inputsHandler.getInputsForRound(this.round);
				if(debugInputs )
					System.out.println(round+ " : " + ims.size());
				this.handleAntidrop(gc);
				if(tests) Test.testRoundCorrect(this, ims);

				if(ims.size()>0){
					if(this.round>=Game.nbRoundInit){
						this.plateau.update(ims);
					} else {
						this.updateInit();
					}
					this.plateau.updatePlateauState();
				}
				this.plateau.updateCosmetic(im);

			} else {

				/////////////////////
				/// SINGLE PLAYER ///
				/////////////////////

				ims.add(im);
				if(!chatHandler.typingMessage){
					this.plateau.handleView(im, this.currentPlayer.id);
				}
				// solo mode, update du joueur courant
				if(this.round>=Game.nbRoundInit){
					this.plateau.update(ims);
				} else {
					this.updateInit();
				}
				this.plateau.updateCosmetic(im);
				//Update des ordres de l'IA
				this.plateau.updateIAOrders();
				//Update replay
				if(replay!=null){
					replay.addInReplay(ims);
				}
				// Maintenant l'update effectif du plateau est sÃ©parÃ© ..
				this.plateau.updatePlateauState();
				//Update IA orders
				if(debugTimeSteps)
					System.out.println("update du plateau single player: "+(System.currentTimeMillis()-timeSteps));
			}
		} else if(endGame){
			// handle victory / defeat screen
			if(victory && this.musicPlaying!=this.musics.get("themeVictory")){
				this.musicPlaying.stop();
				this.musicPlaying = this.musics.get("themeVictory");
				this.musicPlaying.play(1f, this.options.musicVolume);
			} else if(!victory && this.musicPlaying!=this.musics.get("themeDefeat")) {
				this.musicPlaying.stop();
				this.musicPlaying = this.musics.get("themeDefeat");
				this.musicPlaying.play(1f, this.options.musicVolume);
			} 
			//Print victory !!
			victoryTime --;
			if(victoryTime <0){
				this.musicPlaying.stop();
				victoryTime = 120;
				this.isInMenu = true;
				this.hasAlreadyPlay = true;
				this.endGame = false;
				try {
					this.server.setBroadcast(true);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				this.setMenu(this.menuIntro);
			}
		}

		if(debugPaquet){
			System.out.println("tour de jeu: " + round);
			System.out.println("nb paquets envoyï¿½s: " + idPaquetSend);
			System.out.println("nb paquets reï¿½us: " + nbPaquetReceived);
			System.out.println("-- difference: " + (idPaquetSend - idPaquetReceived));
			System.out.println("nb paquets traitï¿½s: " + idPaquetTreated);
		}

	}

	///////////////////////////////////////////////////////

	// INITIALIZING ENGINE
	private void initializeEngine(GameContainer gc) {
		if (LoadingList.get().getRemainingResources() > 0) { 
			if(waitLoading == false){
				waitLoading = true;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
			nextResource = LoadingList.get().getNext(); 
			try {
				//				System.out.println(nextResource.getDescription());
				nextResource.load();
				lastThing = nextResource.getDescription();
				if(nextResource.getDescription().contains("gilles") ){
					gillesPasse+=3;
				}
				if(gillesPasse>0)
					gillesPasse--;
				if(gc.getInput().isKeyPressed(Input.KEY_SPACE) && !this.rate){
					if(gillesPasse>0 ){
						this.loadingSpearman = this.loadingGilles;
						this.special = true;
					} else {
						this.rate = true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			waitLoading = false;
			return;
		} else if (!plateauLoaded){
			this.handleEndLoading();
			plateauLoaded=true;
			return;
		} else if(toGoTitle<1f) {
			if(toGoTitle>0)
				toGoTitle+=0.05f;
			else
				toGoTitle+=0.005f;
			return;
		} else if(!thingsLoaded){
			app.setMinimumLogicUpdateInterval(1000/Main.framerate);
			app.setMaximumLogicUpdateInterval(1000/Main.framerate);
			app.setTargetFrameRate(Main.framerate);
			this.musicPlaying = this.musics.get("themeMenu");
			this.setMenu(menuIntro);
			g.thingsLoaded = true;
			return;
		}
	}

	private void updateInit() {
		this.startTime = System.currentTimeMillis();

	}

	// FONCTIONS AUXILIAIRES RECEIVER
	private void handleMultiReceiver() throws SlickException {
		int gilles = 0;
		while(true){
			byte[] message = new byte[10000];
			DatagramPacket packet = new DatagramPacket(message, message.length);
			try {
				server.setBroadcast(this.isInMenu);
				server.setSoTimeout(1);
				server.receive(packet);
				if(!Game.g.isInMenu){
					nbReception+=1;
					if(nbReception==1){
						tempsReception = (int) System.currentTimeMillis();
					}else{
						tempsReception = (int) (System.currentTimeMillis()-tempsReception);
					}
					if(debugReceiver)
						System.out.println("reception du message: "+ tempsReception);
					tempsReception = (int) System.currentTimeMillis();
				}
				String msg = new String(packet.getData());
				//				if(Game.debugReceiver) 
				//					System.out.println(msg.substring(0, 200));
				//Split submessages
				String[] tab = msg.split("\\%");
				String temp;

				// TODO : check if input in message
				if(Game.tests && !isInMenu){
					Test.testIfInputInMessage(msg);
					int round = getRoundFromMessage(msg);
					Test.testOrderedMessages(round);
					Test.testNombreMessagesRecus(round);
					//System.out.println("reception du message: "+ round+" on est au round " +Game.g.round);
				}
				//System.out.println(packet.getAddress().getHostAddress());
				gilles++;
				for(int i =0; i<tab.length;i++){
					temp = tab[i];
					nbPaquetReceived++;
					if(temp.length()>0 && !packet.getAddress().equals(InetAddress.getLocalHost())){
						//if(Game.debugReceiver) System.out.println("port : " + port + " message received: " + temp);
						switch(temp.substring(0,1)){
						case "0":this.actionConnexion(temp.substring(1), packet); break;
						case "1":this.actionInput(temp.substring(1)); break;
						case "2":this.actionValidation(temp.substring(1)); break;
						case "3":this.actionResynchro(temp.substring(1)); break;
						case "4":this.actionPing(temp.substring(1)); break;
						case "5":this.actionChecksum(temp.substring(1)); break;
						case "6":this.actionChat(temp.substring(1)); break;
						default:
						}
					}
				}
			} catch (SocketTimeoutException e) {
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		//System.out.println(gilles + " messages reçus ce tour");

	}
	public int getRoundFromMessage(String msg){
		String[] tab = msg.split("\\%");
		String temp;
		int ordre = 0;
		for(int i =0; i<tab.length;i++){
			temp = tab[i];
			if(temp.length()>0 && temp.substring(0,1).equals("1")){
				ordre = Integer.parseInt(temp.substring(1).split(",")[1].substring(4));
				break;
			}
		}
		return ordre;
	}
	public void actionConnexion(String message, DatagramPacket packet){
		if(!host){
			addressHost = packet.getAddress();
		}
		//HashMap<String, String> map = Objet.preParse(msg.substring(1));
		receivedConnexion.add(message);		
	}
	public void actionInput(String message) throws SlickException{
		//System.out.println(msg);
		InputObject io = new InputObject(message,g);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 63 input received at round "+ round);
		}
		//Send the validation for other players if the round is still ok
		if(round<io.round+Main.nDelay){
			toSendThisTurn+="2"+io.getMessageValidationToSend(g)+"%";
			inputsHandler.addToInputs(io);
			io.validate();
		}else if(round>Game.nbRoundInit){
			System.out.println("Game line 941 : Message reçu trop tard "+(round-io.round+Main.nDelay));
		}
	}
	public void actionValidation(String msg){
		//Get the corresponding round and player
		String rawInput = msg;
		String[] valMessage = rawInput.split("\\|");
		int round = Integer.parseInt(valMessage[0]);
		int idPlayer = Integer.parseInt(valMessage[1]);
		int idValidator = Integer.parseInt(valMessage[2]);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 69 validation received for round "+ round);	
		}
		// Ressources partagï¿½ le vecteur d'inputs de la mailbox..
		inputsHandler.validate(round, idPlayer,idValidator);
	}
	public void actionResynchro(String msg){
		//		System.out.println("Receive resynchro message");
		processSynchro = true;
		toParse= msg;
	}
	public void actionPing(String msg){
		String[] valMessage = msg.split("\\|");
		int id = Integer.parseInt(valMessage[1]);

		if(id==g.currentPlayer.id) {
			long time =Long.parseLong(valMessage[0]);
			clock.updatePing(time);
		}else if(g.host){
			if(id<g.players.size()){
				g.toSendThisTurn+="4"+(msg)+"%";
			}
		}
	}
	public void actionChecksum(String msg){
		if(host){
			mutexChecksum.lock();
			receivedChecksum.addElement(new Checksum(msg));
			mutexChecksum.unlock();
		}
		// HANDLE ANTI-DROP 
		String[] u = msg.split("\\|");
		int round = Integer.parseInt(u[0]); 

		if(host){
			long ping =Long.parseLong( u[u.length-2]);
			clock.ping = ping;
		}
		//		System.out.println("Je regarde le checksum du round  "+round+ " au round " +g.round );
		//		System.out.println("Et le ping .. " +g.clock.ping);
		//Calcul du delta
		int delta =(int) (clock.ping*Main.framerate/(2e9));
		int deltaMesure = round - round ;
		if((deltaMesure-delta)>1){
			g.antidrop = true;
		}
	}
	public void actionChat(String message){
		ChatMessage cm = new ChatMessage(message);
		chatHandler.messages.add(cm);
		if(cm.message.charAt(0)=='/'){
			taunts.playTaunt(cm.message.substring(1).toLowerCase());
		}
	}


	public void addDisplayRessources(DisplayRessources dr){
		this.displayRessources.addElement(dr);
	}

	private void drawPing(Graphics g) {
		float y = this.relativeHeightBottomBar*resY/2f-this.font.getHeight("Hg")/2f;
		g.drawString("Ping : "+Integer.toString((int)(this.clock.getPing()/1000000f)), 20f, y);
	}

	public void launchGame(){

		this.musicPlaying = this.musics.get("themeImperial");
		this.musicPlaying.loop();
		this.musicPlaying.setVolume(options.musicVolume);
		try {
			this.server.setBroadcast(false);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		//this.game.newGame();
		this.quitMenu();
		this.plateau.Xcam =(int)( this.currentPlayer.getGameTeam().hq.getX()-this.resX/2);
		this.plateau.Ycam = (int)(this.currentPlayer.getGameTeam().hq.getY()-this.resY/2);
		this.startTime = System.currentTimeMillis();
		this.nbPaquetReceived = 0;
		this.idPaquetSend = 0;
		this.idPaquetTreated = 0;
		this.round = 0;
	}


	public Player getPlayerById(int id){
		return this.players.get(id);

	}




	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc) throws SlickException {	
		Image cursor = new Image("ressources/images/cursor.png");
		java.awt.Font fe = new java.awt.Font("Candara",java.awt.Font.PLAIN,25);
		this.font = new UnicodeFont(fe,25,false,false);
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		this.font.addAsciiGlyphs();
		//this.font.addNeheGlyphs();
		//this.font.addGlyphs(0, 256);
		this.font.loadGlyphs();
		if(gc!=null)
			gc.setMouseCursor(cursor.getSubImage(0, 0, 24, 64),5,16);

		Plateau.fog = new Image((int) (resX), (int) (resY));
		Plateau.gf = Plateau.fog.getGraphics();

		double rdm = Math.random();
		if(rdm<0.25){
			this.loadingSpearman = new Image("ressources/images/unit/spearmanBlue.png");			
		} else if (rdm<0.5){
			this.loadingSpearman = new Image("ressources/images/unit/crossbowmanBlue.png");			
		} else if (rdm<0.75){
			this.loadingSpearman = new Image("ressources/images/unit/knightBlue.png");			
		} else if (rdm<0.99){
			this.loadingSpearman = new Image("ressources/images/unit/inquisitorBlue.png");			
		} else {
			this.loadingSpearman = new Image("ressources/images/danger/gilles.png");					
			this.special = true;
		}
		this.loadingGilles = new Image("ressources/images/danger/gilles.png");
		this.loadingTitle = new Image("ressources/images/menu/menuTitle01.png").getScaledCopy(0.35f*this.resY/650);
		this.loadingBackground = new Image("ressources/images/backgroundMenu.png").getScaledCopy(0.35f*this.resY/650);

		LoadingList.setDeferredLoading(true);

		g.sounds = new Sounds();
		g.options = new Options();
		g.images = new Images();
		g.musics = new Musics();
		g.taunts = new Taunts(g);

		g.menuIntro = new MenuIntro(g);
		g.menuOptions = new MenuOptions(g);
		g.menuMulti = new MenuMulti(g);
		g.menuMapChoice = new MenuMapChoice(g);
		g.credits = new Credits(g);
		g.editor = new MapEditor(g);


		nbLoadedThing = LoadingList.get().getRemainingResources();

	}

	public void handleEndLoading(){
		Map.initializePlateau(g, 1f, 1f);
		app.setMinimumLogicUpdateInterval(1000/Main.framerate);
		app.setMaximumLogicUpdateInterval(1000/Main.framerate);

		//FLO INPUTS
		g.inputsHandler = new InputHandler(g);
		//System.out.println(g.plateau.mapGrid);
		//			Map.createMapEmpty(g);
		// Instantiate BottomBars for all players:
		g.selection = null;
		try {
			g.addressLocal = InetAddress.getLocalHost();
			String address = g.addressLocal.getHostAddress();
			String[] tab = address.split("\\.");
			address = tab[0]+"."+tab[1]+"."+tab[2]+".255";
			addressBroadcast = InetAddress.getByName(address);
			client = new DatagramSocket();
			server = new DatagramSocket(port);
			server.setSoTimeout(1);
			server.setBroadcast(true);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		g.clock = new Clock(g);
		//		g.clock.start();
		g.chatHandler = new ChatHandler(g);

		//TODO : to change to false
		LoadingList.setDeferredLoading(true);
		//g.setMenu(g.menuIntro);
	}



	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;
		this.ratioResolution = this.resX/2800f;
		Game.g = this;
	}


	// AUXILIARY FUNCTIONS FOR MULTIPLAYER
	// DANGER
	public void send(String message) throws FatalGillesError{
		//si on est sur le point de commencer à jouer, on n'envoit plus de requête de ping
		if(this.isInMenu){
			// on gère les connexions de menumapchoice

			if(host){
				this.send(new MultiMessage("0"+message,this.addressBroadcast));
				for(InetAddress ia : this.menuMapChoice.addressesInvites){
					this.send(new MultiMessage(toSendThisTurn+"0"+message+"%",ia));
				}
			} else {
				this.send(new MultiMessage(toSendThisTurn+"0"+message+"%",this.addressHost));
			}
		}else if(toSendThisTurn.length()>0) {
			// on est inGame et on gère les communications habituelles
			for(int i=1; i<this.nPlayers; i++){
				if(i!=currentPlayer.id){
					MultiMessage multimessage = new MultiMessage(toSendThisTurn,this.players.get(i).address);
					this.send(multimessage);
				}
			}
		}
		toSendThisTurn="";
	}

	//private static long timeToSend;

	private void send(MultiMessage m) throws FatalGillesError{
		//		if(!isInMenu){
		//			if(timeToSend!=0L)
		//				System.out.println("dernier message envoyé il y a: "+(System.nanoTime()-timeToSend)/1000);
		//			//			if((System.nanoTime()-timeToSend)/1000<3000)
		//			System.out.println("   = >  "+m.message);
		//			timeToSend= System.nanoTime();
		//		}
		if( Game.tests){
			Test.testSendEmptyMessages(m.message);
		}
		idPaquetSend++;
		InetAddress address = m.address;
		byte[] message = (m.message).getBytes();
		DatagramPacket packet = new DatagramPacket(message, message.length, address, this.port);
		packet.setData(message);
		try {
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(Game.debugSender)
			System.out.println("port : " + port + " address: "+m.address.getHostAddress()+" message sent: " + m.message);
	}


	//	private void manuelAntidrop(Input in,GameContainer gc) {
	//		delaySleep = 0 ;
	//		if(in.isKeyPressed(Input.KEY_P)){
	//			this.round+=1;
	//			this.roundDelay++;
	//		}
	//		if(in.isKeyPressed(Input.KEY_M)){
	//			this.round-=1;
	//			this.roundDelay--;
	//		}
	//		if(in.isKeyPressed(Input.KEY_O)){
	//			this.delaySleep=8;
	//		}
	//		if(in.isKeyPressed(Input.KEY_L)){
	//			this.delaySleep = -8;
	//		}
	//		//UPDATE ROUND DURATION
	//		gc.setMinimumLogicUpdateInterval((1000/Main.framerate)+delaySleep);
	//		gc.setMaximumLogicUpdateInterval((1000/Main.framerate)+delaySleep);
	//	}
	private void handleChecksum() {
		// If host and client send checksum
		if(!processSynchro && this.round>=30 && this.round%20==0){
			//Compute checksum
			String checksum = this.round+"|C";
			int i = 0;
			while(i<this.plateau.characters.size()){
				checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).x))%10);
				checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).y))%10);
				checksum+=Integer.toString(((int)(this.plateau.characters.get(i).lifePoints))%10);
				checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).state))%10);
				checksum+=Integer.toString(this.plateau.characters.get(i).id%10)+"-";
				i++;
			}
			i=0;
			checksum+="B";
			while(i<this.plateau.buildings.size()){
				checksum+=this.plateau.buildings.get(i).name;
				checksum+=Integer.toString(((int)(10f*this.plateau.buildings.get(i).charge))%10);
				checksum+=Integer.toString(((int)(10f*this.plateau.buildings.get(i).constructionPoints))%10);
				if(this.plateau.buildings.get(i) instanceof BuildingProduction){
					BuildingProduction p =(BuildingProduction) this.plateau.buildings.get(i);
					if(p.queue!=null && p.queue.size()>0){

						checksum+=Integer.toString(p.queue.size());
					}

				}
				else if(this.plateau.buildings.get(i) instanceof BuildingTech){
					BuildingTech p =(BuildingTech) this.plateau.buildings.get(i);
					checksum+="bt";
					if(p.queue!=null){
						checksum+="q";
					}
				}
				checksum+="-";
				i++;
			}
			checksum+="|";
			checksum+=this.clock.getPing()+"| ";
			if(host){
				this.checksum.addElement(new Checksum(checksum));
			} else {
				// si client on envoie checksum
				toSendThisTurn+="5"+checksum+"%";				
			}
		}
		// handling checksum comparison
		if(this.host && !this.processSynchro){
			boolean [] tab;
			this.mutexChecksum.lock();
			Vector<Checksum> toRemove = new Vector<Checksum>();
			for(Checksum c: this.receivedChecksum){
				for(Checksum c1 : this.checksum){
					tab = c.comparison(c1);
					if(tab[0]){
						toRemove.add(c);
						if(tab[1]){
							System.out.println(c.checksum+"\n"+c1.checksum);
							this.processSynchro = true;
							this.sendParse = true;					
						}
					}
				}
			}
			this.receivedChecksum.removeAll(toRemove);
			if(this.checksum.size()>2)
				this.checksum.remove(0);
			if(this.receivedChecksum.size()>2){
				this.receivedChecksum.remove(0);
			}
			this.mutexChecksum.unlock();
		}
	}
	private void handlePing() {
		toSendThisTurn+="4"+this.clock.getCurrentTime()+"|"+this.currentPlayer.id+"|%";
	}
	private void handleSendingResynchroParse() {
		if(this.host && this.processSynchro && this.sendParse){
			this.toParse = this.plateau.toStringArray();
			//			System.out.println("Game line 698: Sent synchro message");
			this.sendParse = false;
			this.toSendThisTurn+="3"+this.toParse+"%";
		}
	}
	private void handleAntidrop(GameContainer gc) {
		if(antidrop){
			//UPDATE ROUND DURATION
			gc.setMinimumLogicUpdateInterval((1000/Main.framerate)+delaySleepAntiDrop);
			gc.setMaximumLogicUpdateInterval((1000/Main.framerate)+delaySleepAntiDrop);	
			//			System.out.println("antidrop !!! round: "+this.round);
			this.toDrawAntiDrop = true;
		}else{
			gc.setMinimumLogicUpdateInterval((1000/Main.framerate));
			gc.setMaximumLogicUpdateInterval((1000/Main.framerate));	
		}
		antidrop = false;
	}
	private void handleResynchro() {
		if( processSynchro){
			//Si round+nDelay
			if(toParse==null){
				this.processSynchro = false;
				return;
			}
			String[] u = this.toParse.split("!");
			//Je resynchronise au tour n+nDelay
			if(Integer.parseInt(u[0])==(this.round-Main.nDelay)){
				//				System.out.println("Play resynchronisation round at round " + this.round);
				this.plateau.parse(this.toParse);
				this.toParse = null;
				this.processSynchro = false;

			}
			else if( Integer.parseInt(u[0])<(this.round-Main.nDelay)){
				this.processSynchro = false;
				this.toParse = null;
			}
		}
	}
	public void pingRequest() {
		this.toSendThisTurn+="4"+this.clock.getCurrentTime()+"|"+this.currentPlayer.id+"|"+"%";
	}
	public void sendMessage(ChatMessage m){
		if(m.idPlayer==currentPlayer.id){
			this.toSendThisTurn+="6"+m.toString()+"%";
		}
		this.chatHandler.messages.addElement(m);
		if(m.message.charAt(0)=='/'){
			this.taunts.playTaunt(m.message.substring(1).toLowerCase());
		}
	}


	// GILLES DE BOUARD MODE
	public void activateGdBMode(){
		this.images.activateGdBMode();
	}


}