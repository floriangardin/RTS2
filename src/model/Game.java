
package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

import buildings.Bonus;
import buildings.Building;
import bullets.Bullet;
import control.InputHandler;
import control.InputObject;
import control.KeyMapper;
import control.KeyMapper.KeyEnum;
import control.Selection;
import data.Attributs;
import data.Data;
import display.BottomBar;
import display.DisplayRessources;
import events.EventQueue;
import main.Main;
import mapeditor.MapEditor;
import menu.Credits;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuNewUser;
import menu.MenuOptions;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import multiplaying.Checksum;
import multiplaying.Clock;
import multiplaying.MultiMessage;
import nature.Tree;
import pathfinding.Case;
import ressources.Images;
import ressources.Map;
import ressources.Musics;
import ressources.Sounds;
import ressources.Taunts;
import spells.SpellEffect;
import tests.FatalGillesError;
import tests.Test;
import units.Character;
import utils.ObjetsList;
import utils.Utils;
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
	public static boolean debugReceiver = true;
	public static boolean debugSender = false;
	public static boolean debugTourEnCours = false;
	public static boolean debugThread = false;
	public static boolean debugDisplayDebug = true;
	public static boolean debugMemory = false;

	public static boolean deplacementGroupIntelligent = false;
	public static boolean debugGroup = false;

	public boolean displayMapGrid = false;

	public static boolean showUpdateLogicInterval = false;


	///////////////////////
	/// BONUS DISABLING ///
	///////////////////////

	public static boolean gillesBombeEnable = true;
	public static boolean gillesEspaceEnable = true;
	public static boolean gillesSurCentEnable = true;
	public static boolean gillesModeEnable = true;
	public static boolean conseilChargementEnable = true;



	// debugging tools
	long timeSteps = 0;
	public int nbGameTurn = 0;

	public int round = 0;
	public int roundDebug = 0;

	public int idPaquetSend = 0;
	public int nbPaquetReceived = 0;
	public int idPaquetReceived = 0;
	public int idPaquetTreated = 0;

	// Controls
	public KeyMapper keymapper;

	//Increment de game
	public static float ratio = 60f/((float)Main.framerate);

	public static int nbRoundInit = 3*Main.framerate;
	public int secondsGong;

	public int idChar = 0;
	public int idBullet = 0;

	// Cursors
	Image attackCursor ; 
	Image cursor;
	private Image currentCursor;

	// Font 
	public UnicodeFont font;

	// Music and sounds
	public Options options;
	public Images images;
	public Sounds sounds;
	public Musics musics;
	public Taunts taunts;
	public Music musicPlaying;
	public Data data;

	/////////////////////////
	/// RENDER ATTRIBUTES ///
	/////////////////////////
	public static Image fog;
	public static Image transparence;
	public static Graphics gf;
	public static Graphics gt;
	public Cosmetic cosmetic;

	// colors
	public static Color couleurJoueur0 = new Color(25,25,25);
	public static Color couleurJoueur1 = new Color(0,0,205);
	public static Color couleurJoueur2 = new Color(255,0,0);


	// Spell with click handling
	public boolean spellOk = true;
	public Character spellLauncher;
	public Objet spellTarget;
	public float spellX,spellY;
	public ObjetsList spellCurrent = null;

	// Timer
	public Timer timer ;

	// Bars
	public float relativeHeightBottomBar = 1f/6f;
	public float relativeHeightTopBar = 1f/20f;
	public BottomBar bottomBar;
	// Selection

	public boolean new_selection;
	public Vector<Objet> objets_selection= new Vector<Objet>();

	// Resolution : 
	public float resX;
	public float resY;

	// Plateau
	public Plateau plateau ;
	public AppGameContainer app;

	// Handling selection
	public InputHandler inputsHandler;

	// Handling events
	public EventQueue events = new EventQueue();

	// Attack click
	public boolean attackClick = false;

	////////////////////////
	/// PLAYERS && TEAMS ///
	////////////////////////

	// Camera
	public int Xcam;
	public int Ycam;
	public boolean slidingCam = false;
	public Point objectiveCam = new Point(0,0);
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
	public Vector<String> receivedPing = new Vector<String>();
	public Vector<Checksum> receivedChecksum = new Vector<Checksum>();
	public Vector<String> receivedChat = new Vector<String>();
	// Sender
	// public MultiSender sender;
	// Receiver
	// public MultiReceiver receiver;
	// Chat
	public ChatHandler chatHandler;
	private Plateau toParse;



	public boolean processSynchro;
	public Vector<Checksum> checksum = new Vector<Checksum>();
	private boolean sendParse;
	public Lock mutexChecksum = new ReentrantLock();
	// antidrop
	public boolean toDrawDrop = false;
	public boolean toDrawAntiDrop = false;
	public int roundDelay;

	public MultiMessage toSendThisTurn;


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


	/////////////////////////
	///       REPLAYS     ///
	/////////////////////////

	public Replay replay = null;



	//////////////////////////////
	// INITIALIZING GAME ENGINE //
	//////////////////////////////

	public static int nbLoadedThing = 0;
	public boolean thingsLoaded = false;
	public boolean plateauLoaded = false;
	public boolean allLoaded = false;
	private boolean special = false;
	private boolean gilles = false;
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
	String adviceToDisplay;






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
		int durationFade = 1000;
		if((m instanceof MenuMulti || m instanceof MenuMapChoice) && this.musicPlaying!=this.musics.get("themeMulti")){
			this.musicPlaying.fade(durationFade,0,true);
			this.musicPlaying=this.musics.get("themeMulti");
			this.musicPlaying.play();
			this.musicPlaying.setVolume(0);
			this.musicPlaying.fade(durationFade,options.musicVolume,false);
		}
		if(m instanceof MenuIntro && this.musicPlaying!=this.musics.get("themeMenu")){
			this.musicPlaying.fade(durationFade,0,true);
			this.musicPlaying=this.musics.get("themeMenu");
			this.musicPlaying.play();
			this.musicPlaying.setVolume(0);
			this.musicPlaying.fade(durationFade,options.musicVolume,false);
		}
		if(m instanceof Credits){
			this.musicPlaying.fade(durationFade,0,true);
			this.musicPlaying=this.musics.get("themeMapEditor");
			this.musicPlaying.play();
			this.musicPlaying.setVolume(0);
			this.musicPlaying.fade(durationFade,options.musicVolume,false);
		}
		if(m instanceof MenuMapChoice){
			((MenuMapChoice)m).initialize();
		}

	}

	public void addPlayer(String name, InetAddress address,int resX,int resY){
		this.players.addElement(new Player(players.size(),name,teams.get(1)));



		this.players.lastElement().address = address;
		nPlayers+=1;



	}

	public void removePlayer(int indice){
		if(indice==0 || indice>players.size())
			return;
		players.remove(indice);
		nPlayers -= 1;

		// deleting component from plateau


		//Pourquoi ???

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
		this.players.add(new Player(0,"Nature",teams.get(0)));
		this.players.add(new Player(1,this.options.nickname,teams.get(1)));
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
		g.setFont(this.font);
		if(!thingsLoaded){
			this.renderIntro(g);
			return;
		}
		if(isInMenu){
			if(hasAlreadyPlay){
				g.translate(+Game.g.Xcam,+ Game.g.Ycam);
			}
			g.translate(-Game.g.Xcam,- Game.g.Ycam);
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
			g.translate(-Game.g.Xcam,- Game.g.Ycam);
			//Draw background
			g.drawImage(this.images.get("seaBackground"), -this.plateau.maxX, -this.plateau.maxY,
					2*this.plateau.maxX, 2*this.plateau.maxY, 0, 0, this.images.get("seaBackground").getWidth(),this.images.get("seaBackground").getHeight());


			g.drawImage(this.images.get("islandTexture"),0, 0, this.plateau.maxX, this.plateau.maxY,
					0, 0, this.images.get("islandTexture").getWidth(),  this.images.get("islandTexture").getHeight());

			// Draw ground effects
			for(SpellEffect e : this.plateau.spells){
				if(e.visibleByCurrentTeam && e.toDrawOnGround)
					e.draw(g);
			}
			// Draw the selection of your team
			for(Objet o: this.inputsHandler.getSelection(currentPlayer.id).selection){

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
			//Draw spells cosmetic
			if(this.spellCurrent!=null){
				int i = spellLauncher.spells.indexOf(spellCurrent);
				boolean ok = spellLauncher.spellsState.get(i)>=Game.g.data.spells.get(spellCurrent).getAttribut(Attributs.chargeTime);
				this.currentPlayer.getGameTeam().data.spells.get(this.spellCurrent).draw(g,this.spellTarget,this.spellX,this.spellY,this.spellLauncher, ok);
			}

			//Creation of the drawing Vector
			Vector<Objet> toDraw = new Vector<Objet>();
			Vector<Objet> toDrawAfter = new Vector<Objet>();
			// Draw the Action Objets
			for(Character o : plateau.characters){
				//o.draw(g);
				if(o.visibleByCurrentTeam)
					toDrawAfter.add(o);

			}

			//

			//Draw bonuses
			for(Bonus o : plateau.bonus){
				//o.draw(g);
				o.draw(g);
			}
			// Draw the natural Objets

			for(NaturalObjet o : this.plateau.naturalObjets){
				if(o.visibleByCurrentTeam)
					toDrawAfter.add(o);
				else
					toDraw.add(o);
			}
			// Draw the buildings
			for(Building e : this.plateau.buildings){
				if(e.visibleByCurrentTeam)
					toDrawAfter.add(e);
				else
					toDraw.add(e);
			}
			for(SpellEffect e : this.plateau.spells){
				if(!e.toDrawOnGround){
					if(e.visibleByCurrentTeam)
						toDrawAfter.add(e);
					else
						toDraw.add(e);
				}
			}
			for(Bullet b : this.plateau.bullets){
				if(b.visibleByCurrentTeam)
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
			drawFogOfWar(g);
			for(Objet o: toDrawAfter)
				o.draw(g);
			handleDrawUnderBuilding(g);

			// Draw the selection :
			if(cosmetic.selection!=null){
				g.setColor(Colors.selection);
				cosmetic.draw(g);
			}
			// Render Graphics Events
			events.render(g);

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
			g.translate(Game.g.Xcam, Game.g.Ycam);

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
			if(this.bottomBar.topBar!=null)
				this.bottomBar.topBar.draw(g);
			if(this.bottomBar!=null)
				this.bottomBar.draw(g);

			//bonus
			if(gillesBombe){
				for(Gilles gi : gillesPics){
					gi.draw(g);
				}
			}
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

	// drawing ecran chargement
	public void renderIntro(Graphics g){
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
			if(Game.conseilChargementEnable)
				g.drawString(adviceToDisplay, resX/2-font.getWidth(adviceToDisplay)/2, resY-20-font.getHeight(adviceToDisplay));
			g.setColor(Color.black);
			g.fillRect(startBarX, startBarY,sizeBarX, sizeBarY);
			float x = 1f*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing;
			g.setColor(new Color(1f-x,0f,x));
			g.fillRect(startBarX, startBarY,sizeBarX*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing, sizeBarY);
			if(LoadingList.get().getRemainingResources() > 0){
				g.setColor(Color.white);
				g.drawString(""+lastThing, startBarX+20f, startBarY+sizeBarY/2-font.getHeight("Hg")/2);
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
			if(this.rate){
				g.setColor(Color.white);
				String s;
				if(this.gilles){
					s = "Trop tard";
				} else {
					s = "Trop tôt";
				}
				g.drawString(s, resX-10-font.getWidth(s), resY-20-font.getHeight(s));
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
	// drawing fog of war method
	public void drawFogOfWar(Graphics g) {
		Vector<Objet> visibleObjet = new Vector<Objet>();
		visibleObjet = this.plateau.getInCamObjets(Game.g.currentPlayer.getTeam());
		gf.setColor(new Color(255, 255, 255));
		gf.fillRect(-this.plateau.maxX, -this.plateau.maxY, this.plateau.maxX + resX, this.plateau.maxY + resX);
		gf.setColor(new Color(50, 50, 50));
		float xmin = Math.max(-this.plateau.maxX, -this.plateau.maxX - Game.g.Xcam);
		float ymin = Math.max(-this.plateau.maxY, -this.plateau.maxY - Game.g.Ycam);
		float xmax = Math.min(resX + this.plateau.maxX, 2 * this.plateau.maxX - Game.g.Xcam);
		float ymax = Math.min(resY + this.plateau.maxY, 2 * this.plateau.maxY - Game.g.Ycam);
		gf.fillRect(xmin, ymin, xmax - xmin, ymax - ymin);
		gf.setColor(Color.white);
		for (Objet o : visibleObjet) {
			float sight = o.getAttribut(Attributs.sight);
			gf.fillOval(o.x - Xcam - sight, o.y - Ycam - sight, sight * 2f, sight * 2f);
		}
		gf.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.drawImage(fog, Xcam, Ycam);
		g.setDrawMode(Graphics.MODE_NORMAL);
	}

	// getting color by id team
	public static Color getColorByTeam(int team){
		switch(team){
		case 1 : return couleurJoueur1;
		case 2 : return couleurJoueur2;
		default : return couleurJoueur0;
		}
	}
	// handle transparency
	public void handleDrawUnderBuilding(Graphics g){
		Vector<Objet> v = new Vector<Objet>();
		Image im;
		Vector<Objet> vb = new Vector<Objet>();
		// buildings
		v.addAll(plateau.buildings);
		v.addAll(plateau.naturalObjets);
		for(Objet b : v){
			if(b.visibleByCamera){
				vb.add(b);
			}
		}
		
		for(Objet b : vb){
			v.clear();
			Case ca = plateau.mapGrid.getCase(b.x, b.y);
			while(ca.y+ca.sizeY>b.y-b.visibleHeight){
//				Game.g.app.getGraphics().setColor(Color.red);
//				Game.g.app.getGraphics().drawRect(ca.x,ca.y,ca.sizeX,ca.sizeY);
				for(Character c : ca.surroundingChars){
					if(c.visibleByCamera && 
							c.x>b.x-b.getAttribut(Attributs.sizeX)/2f &&
							c.x<b.x+b.getAttribut(Attributs.sizeX)/2f &&
							c.y>b.y-b.getVisibleSize()-b.getAttribut(Attributs.sight)-b.visibleHeight 
							&& !v.contains(c)){
						v.add(c);
					}
				}
				if(ca.j==0){
					break;
				} else {
					ca = plateau.mapGrid.grid.get(ca.i).get(ca.j-1);
				}
			}
			if(v.size()>0){
				System.out.println(b);
				gt.translate(-Xcam, -Ycam);
				gt.clear();
				gt.setDrawMode(Graphics.MODE_NORMAL);
				b.drawBasicImage(gt);
				for(Objet o : v){
					if(o instanceof Character){
						Character c = (Character) o;
						int direction = (c.orientation/2-1);
						// inverser gauche et droite
						if(direction==1 || direction==2){
							direction = ((direction-1)*(-1)+2);
						}
						im = Game.g.images.getUnit(c.name, direction, c.animation, c.getGameTeam().id, c.isAttacking);

						gt.drawImage(im,c.x-im.getWidth()/2,c.y-3*im.getHeight()/4, Color.blue);
					}
				}
				gt.clearAlphaMap();
				gt.setDrawMode(Graphics.MODE_ALPHA_MAP);
				b.drawBasicImage(gt);
				gt.flush();
				g.setDrawMode(Graphics.MODE_NORMAL);
				transparence.setAlpha(0.8f);
				g.drawImage(transparence, Xcam, Ycam);
				g.setDrawMode(Graphics.MODE_NORMAL);
			}
		}
	}

	//Handling cosmetic for current player in lan game




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





		Vector<InputObject> ims = new Vector<InputObject>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game	
		this.taunts.update();
		if(isInMenu){
			Input in = gc.getInput();
			int id = -1;
			if(currentPlayer != null){
				id = currentPlayer.id;
			}
			InputObject im = new InputObject(id,in,true, this.keymapper);
			if(inMultiplayer && (menuCurrent instanceof MenuMapChoice || menuCurrent instanceof MenuMulti)){
				this.chatHandler.action(in,im);
			}
			this.menuCurrent.update(im);
			//this.send();
		} else if(inEditor) {
			// Map Editor
			Input in = gc.getInput();
			InputObject im = new InputObject(1,in,!processSynchro, keymapper);
			this.editor.update(im,in);
		} else if(!endGame) {

			// gérer le début de partie

			//Update of current round
			this.clock.setRoundFromTime();

			// on lance la musique
			if(this.round==Game.nbRoundInit){
				this.musicPlaying = musics.get("destruction");
				this.musicPlaying.setVolume(options.musicVolume);
				this.musicPlaying.play();
				this.musicPlaying.loop();
				this.musicPlaying.setVolume(options.musicVolume*0.5f);
			}
			if(secondsGong>=0 && (Game.nbRoundInit-this.round)/Main.framerate<=secondsGong){
				secondsGong--;
				this.sounds.get("menuItemSelected").play(1f,options.soundVolume);
			}
			// getting inputs
			Input in = gc.getInput();
			//			if(in.isKeyPressed(Input.KEY_RALT)){
			//				this.displayMapGrid = !this.displayMapGrid;
			//			}
			InputObject im = new InputObject(currentPlayer.id,in,true, keymapper);
			this.chatHandler.action(in,im);
			if(this.chatHandler.typingMessage){
				im.eraseLetter();
			} else {
				//this.manuelAntidrop(in,gc);
			}
			//			if(replay==null){
			//				replay = new Replay(2,"apocalypse",0,this);
			//			}

			// handling spell cosmetic
			this.handleSpellWithClick(im);

			if(inMultiplayer){

				////////////////////
				/// MULTI PLAYER ///
				////////////////////

				this.toDrawAntiDrop = false;
				this.toDrawDrop = false;
				this.toSendThisTurn.input.addElement(im);
				this.inputsHandler.addToInputs(im);
				this.handleChecksum();
				this.handlePing();
				this.handleSendingResynchroParse();
				this.handleResynchro();
				this.sendFromGame(toSendThisTurn);
				toSendThisTurn = new MultiMessage(null);
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

				}else{
					System.out.println("Game 839 : round drop "+round);
				}
				this.plateau.updatePlateauState();
				this.cosmetic.update(im);
				if(this.gillesBombe){
					this.handleGillesBombe();
				}
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
				this.cosmetic.update(im);
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

	// spell with click handling
	public void handleSpellWithClick(InputObject im){
		Selection selection = inputsHandler.getSelection(currentPlayer.id);
		if(selection.selection.size() > 0 && selection.selection.get(0) instanceof Character){
			Character c = (Character) selection.selection.get(0);
			for(int i=0; i<c.spells.size(); i++){
				if (im.isPressed(KeyEnum.valueOf("Prod"+i)) 
						&& c.getSpell(i).getAttribut(Attributs.needToClick)==1){
					this.spellCurrent = c.spells.get(i);
					this.spellLauncher = c;
				}
			}
		}
		if(this.spellCurrent!=null){
			this.spellTarget = plateau.findTarget(im.x, im.y, currentPlayer.id);
			this.spellX = im.x;
			this.spellY = im.y;

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
					if(!rate)
						gilles = true;
					gillesPasse+=3;
				}
				if(gillesPasse>0)
					gillesPasse--;
				if(Game.gillesEspaceEnable && gc.getInput().isKeyPressed(Input.KEY_SPACE) && !this.rate){
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
			if(Main.debugRapid){
				app.setMinimumLogicUpdateInterval(1000/Main.framerate);
				app.setMaximumLogicUpdateInterval(1000/Main.framerate);
				app.setTargetFrameRate(Main.framerate);
				this.musicPlaying = this.musics.get("themeVictory");
				this.setMenu(menuMapChoice);
				this.initializePlayers();
				Map.updateMap(Main.nameMap, this);
				this.launchGame();
				g.thingsLoaded = true;
			}
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
			if(this.options.nickname.equals(""))
				this.setMenu(new MenuNewUser(this));
			else
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
		while(true){
			byte[] message = new byte[100000];
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
				MultiMessage msg = MultiMessage.getMessageFromString(packet.getData());
				for(String s : msg.connexion){
					this.actionConnexion(s, packet);
				}
				for(InputObject io : msg.input){
					this.actionInput(io); 
				}
				for(String s : msg.validation){
					this.actionValidation(s); 
				}
				if(msg.resynchro!=null){
					this.actionResynchro(msg.resynchro); 
				}
				for(String s : msg.ping){
					this.actionPing(s); 
				}
				for(String s : msg.checksum){
					this.actionChecksum(s);
				}
				for(String s : msg.chat){
					this.actionChat(s);
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
	public void actionInput(InputObject io) throws SlickException{
		//System.out.println(msg);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 63 input received at round "+ round);
		}
		//Send the validation for other players if the round is still ok
		if(round<io.round+Main.nDelay){
			toSendThisTurn.validation.add(io.getMessageValidationToSend(g));
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
	public void actionResynchro(Plateau p){
		//		System.out.println("Receive resynchro message");
		processSynchro = true;
		toParse= p;
	}
	public void actionPing(String msg){
		String[] valMessage = msg.split("\\|");
		int id = Integer.parseInt(valMessage[1]);

		if(id==g.currentPlayer.id) {
			long time =Long.parseLong(valMessage[0]);
			clock.updatePing(time);
		}else if(g.host){
			if(id<g.players.size()){
				g.toSendThisTurn.ping.add(msg);
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
		if(Game.gillesBombeEnable && cm.message.equals("/gillesBombe")){
			this.gillesBombe = true;
			return;
		}
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

		this.inputsHandler.initSelection();
		this.musicPlaying.stop();
		this.musicPlaying = this.musics.get("themeImperial");
		try {
			this.server.setBroadcast(false);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		//this.game.newGame();
		this.quitMenu();
		Game.g.Xcam =(int)( this.currentPlayer.getGameTeam().hq.getX()-this.resX/2);
		Game.g.Ycam = (int)(this.currentPlayer.getGameTeam().hq.getY()-this.resY/2);
		this.startTime = System.currentTimeMillis();
		this.nbPaquetReceived = 0;
		this.idPaquetSend = 0;
		this.idPaquetTreated = 0;
		this.round = 0;
		this.secondsGong = 3;
	}


	public Player getPlayerById(int id){
		return this.players.get(id);

	}


	//////////////////////////
	/// GRAPHISM AND SOUND ///
	//////////////////////////



	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc) throws SlickException {	
		cursor = new Image("ressources/images/cursor.png").getSubImage(0, 0, 24, 64);
		attackCursor = new Image("ressources/images/swordCursor.png");
		java.awt.Font fe = new java.awt.Font("Candara",java.awt.Font.PLAIN,(int)(25*resX/1920));
		this.font = new UnicodeFont(fe,(int)(25*resX/1920),false,false);
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		this.font.addAsciiGlyphs();
		//this.font.addNeheGlyphs();
		//this.font.addGlyphs(0, 256);
		this.font.loadGlyphs();
		this.currentCursor = cursor;
		if(gc!=null)
			gc.setMouseCursor(currentCursor,5,16);

		fog = new Image((int) (resX), (int) (resY));
		gf = fog.getGraphics();
		transparence = new Image((int) (resX), (int) (resY));
		gt = transparence.getGraphics();

		double rdm = Math.random();
		if(rdm<0.20){
			this.loadingSpearman = new Image("ressources/images/unit/spearmanBlue.png");			
		} else if (rdm<0.40){
			this.loadingSpearman = new Image("ressources/images/unit/crossbowmanBlue.png");			
		} else if (rdm<0.60){
			this.loadingSpearman = new Image("ressources/images/unit/knightBlue.png");			
		} else if (rdm<0.80 ){
			this.loadingSpearman = new Image("ressources/images/unit/inquisitorBlue.png");			
		} else if (rdm<0.99 || !Game.gillesSurCentEnable){
			this.loadingSpearman = new Image("ressources/images/unit/priestBlue.png");			
		} else {
			this.loadingSpearman = new Image("ressources/images/danger/gilles.png");					
			this.special = true;
		}
		if(Game.conseilChargementEnable){
			String fichier ="././ressources/conseils.txt";
			//lecture du fichier texte	
			try{
				InputStream ips=new FileInputStream(fichier); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				Vector<String> lignes = new Vector<String>();
				while ((ligne=br.readLine())!=null){
					lignes.add(ligne);
				}
				br.close(); 
				this.adviceToDisplay = lignes.get((int)(Math.random()*lignes.size()));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		this.loadingGilles = new Image("ressources/images/danger/gilles.png");
		this.loadingTitle = new Image("ressources/images/menu/menuTitle01.png").getScaledCopy(0.35f*this.resY/650);
		this.loadingBackground = new Image("ressources/images/backgroundMenu.png").getScaledCopy(0.35f*this.resY/650);

		LoadingList.setDeferredLoading(true);

		g.data = new Data(0,"dualists");
		g.sounds = new Sounds();
		g.options = new Options();
		g.images = new Images();
		g.musics = new Musics();
		g.taunts = new Taunts(g);
		g.keymapper = new KeyMapper();

		g.menuIntro = new MenuIntro(g);
		g.menuOptions = new MenuOptions(g);
		g.menuMulti = new MenuMulti(g);
		g.menuMapChoice = new MenuMapChoice(g);
		g.credits = new Credits(g);
		g.editor = new MapEditor(g);


		nbLoadedThing = LoadingList.get().getRemainingResources();

		this.toSendThisTurn = new MultiMessage(null);
	}

	public void handleEndLoading(){
		Map.initializePlateau(g, 1f, 1f);
		//COSMETIC
		this.cosmetic = new Cosmetic();
		app.setMinimumLogicUpdateInterval(1000/Main.framerate);
		app.setMaximumLogicUpdateInterval(1000/Main.framerate);

		//FLO INPUTS
		g.inputsHandler = new InputHandler();
		//System.out.println(g.plateau.mapGrid);
		//			Map.createMapEmpty(g);
		// Instantiate BottomBars for all players:

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
	public void sendFromMenu(String message) throws FatalGillesError{
		//si on est sur le point de commencer à jouer, on n'envoit plus de requête de ping
		if(this.isInMenu){
			// on gère les connexions de menumapchoice

			if(host){
				MultiMessage m = new MultiMessage(this.addressBroadcast);
				m.connexion.add(message);
				this.send(m);
				for(InetAddress ia : this.menuMapChoice.addressesInvites){
					MultiMessage m1 = new MultiMessage(ia);
					m.connexion.add(message);
					this.send(m1);
				}
			} else {
				MultiMessage m = new MultiMessage(this.addressHost);
				m.connexion.add(message);
				this.send(m);
			}
		}
	}

	//private static long timeToSend;

	public void sendFromGame(MultiMessage m) throws FatalGillesError{
		for(int i=1; i<this.nPlayers; i++){
			if(i!=currentPlayer.id){
				m.address = this.players.get(i).address;
				this.send(m);
			}
		}
	}

	private void send(MultiMessage m) throws FatalGillesError{
		//		if(!isInMenu){
		//			if(timeToSend!=0L)
		//				System.out.println("dernier message envoyé il y a: "+(System.nanoTime()-timeToSend)/1000);
		//			//			if((System.nanoTime()-timeToSend)/1000<3000)
		//			System.out.println("   = >  "+m.message);
		//			timeToSend= System.nanoTime();
		//		}
		idPaquetSend++;
		InetAddress address = m.address;
		byte[] message = Serializer.serialize(m);
		DatagramPacket packet = new DatagramPacket(message, message.length, address, this.port);
		packet.setData(message);
		try {
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(Game.debugSender)
			System.out.println("port : " + port + " address: "+m.address.getHostAddress()+" message sent: " + m.toString());
	}

	private void handleChecksum() {
		// If host and client send checksum
		if(!processSynchro && this.round>=30 && this.round%1==0){
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
				if(this.plateau.buildings.get(i) instanceof Building){
					Building p =(Building) this.plateau.buildings.get(i);
					if(p.queue!=null && p.queue.size()>0){

						checksum+=Integer.toString(p.queue.size());
					}
					checksum+="bt";
					if(p.queueTechnology!=null){
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
				toSendThisTurn.checksum.add(checksum);				
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
							System.out.println("resynchro : " +round+" " +"\n"+c.checksum+"\n"+c1.checksum);
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
		toSendThisTurn.ping.add(this.clock.getCurrentTime()+"|"+this.currentPlayer.id+"|");
	}
	private void handleSendingResynchroParse() {
		if(this.host && this.processSynchro && this.sendParse){
			//this.toParse = this.plateau.toStringArray();
			//			System.out.println("Game line 698: Sent synchro message");
			this.sendParse = false;
			this.plateau.roundToSynchro = this.round+Main.nDelay;
			this.toSendThisTurn.resynchro=this.plateau;
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
			//Je resynchronise au tour n+nDelay
			if(toParse.roundToSynchro==(this.round-Main.nDelay)){
				//				System.out.println("Play resynchronisation round at round " + this.round);
				this.plateau = toParse;
				this.toParse = null;
				this.processSynchro = false;

			}
			else if(toParse.roundToSynchro<(this.round-Main.nDelay)){
				this.processSynchro = false;
				this.toParse = null;
			}
		}
	}
	public void pingRequest() {
		this.toSendThisTurn.ping.addElement(this.clock.getCurrentTime()+"|"+this.currentPlayer.id+"|");
	}
	public void sendMessage(ChatMessage m){
		if(m.idPlayer==currentPlayer.id){
			this.toSendThisTurn.chat.addElement(m.toString());
		}
		//if(!m.message.equals("/gillesBombe"))
		this.chatHandler.messages.addElement(m);
		if(m.message.charAt(0)=='/'){
			this.taunts.playTaunt(m.message.substring(1).toLowerCase());
		}
	}


	// GILLES DE BOUARD MODE
	public boolean GdB;
	public void activateGdBMode(){
		if(Game.gillesModeEnable){
			this.GdB = !this.GdB;
			if(GdB)
				this.images.activateGdBMode();
			else
				this.images.deactivateGdBMode();
		}
	}
	private class Gilles{
		float x,y,vx,vy;
		float angle;
		public Gilles(){
			double proba = Math.random();
			if(proba<0.25){
				//depuis le haut
				y = 0;
				x =  (float) (Math.random()*resX);
				vx =  (float) (5f*(2.0*Math.random()-1.0));
				vy =  (float) (5f*Math.random());
			} else if(proba<0.5){
				// depuis le bas
				y = (float) resY;
				x = (float) (Math.random()*resX);
				vx = (float) (5f*(2.0*Math.random()-1.0));
				vy = (float) -(5f*Math.random());
			} else if(proba<0.75){
				// depuis la gauche
				y = (float) (Math.random()*resY);
				x = 0;
				vx = (float) (5f*Math.random());
				vy = (float) (5f*(2.0*Math.random()-1.0));
			} else {
				// depuis la gauche
				y = (float) (Math.random()*resY);
				x = (float) resX;
				vx = (float) -(5f*Math.random());
				vy = (float) (5f*(2.0*Math.random()-1.0));
			} 
			this.angle = (float) (Math.atan(this.vy/(this.vx+0.00001f))*180/Math.PI);
			if(this.vx<0)
				this.angle+=180;
			if(this.angle<0)
				this.angle+=360;
		}
		public void draw(Graphics g){
			images.get("gilles").rotate(angle);
			g.drawImage(images.get("gilles"), x, y);
			images.get("gilles").rotate(-angle);
		}
		public void update(){
			x += vx;
			y += vy;
		}
	}

	public boolean gillesBombe = false;
	public int timeGilles = 0;
	private Vector<Gilles> gillesPics = new Vector<Gilles>();

	public void handleGillesBombe(){
		if(timeGilles==0){
			this.musicPlaying = musics.get("themeVerdi");
			this.musicPlaying.play();
		}
		timeGilles++;
		if(timeGilles<6*Main.framerate){
			gillesPics.add(new Gilles());
			for(Gilles g : gillesPics){
				g.update();
			}
			Vector<Gilles> toRemove = new Vector<Gilles>();
			for(Gilles g : gillesPics){
				if(g.x<10 || g.x>resX+10 || g.y<0 || g.y>resY+10)
					toRemove.add(g);
			}
			gillesPics.removeAll(toRemove);
		} else {
			this.musicPlaying = musics.get("themeImperial");
			this.musicPlaying.play();
			gillesPics.clear();
			timeGilles = 0;
			gillesBombe = false;
		}
	}


}