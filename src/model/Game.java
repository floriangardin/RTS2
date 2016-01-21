
package model;
import java.net.InetAddress;
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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;

import battleIA.IAFlo;
import buildings.Bonus;
import buildings.Building;
import buildings.BuildingProduction;
import buildings.BuildingTech;
import bullets.Bullet;
import main.Main;
import mapeditor.MapEditor;
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
import multiplaying.MultiSender;
import spells.SpellEffect;
import units.Character;
public class Game extends BasicGame 
{
	/////////////
	/// DEBUG ///
	/////////////

	public static boolean debugTimeSteps = false;
	public static boolean debugPaquet = false;
	public static boolean debugValidation = false;
	public static boolean debugReceiver = false;
	public static boolean debugSender = false;
	public static boolean debugTourEnCours = false;
	public static boolean debugThread = false;
	public static boolean debugDisplayDebug = false;

	public static boolean deplacementGroupIntelligent = true;
	public static boolean debugGroup = false;

	public boolean displayMapGrid = false;

	public static boolean showUpdateLogicInterval = true;


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

	public static float ratioSpace = 0.8f;

	public int idChar = 0;
	public int idBullet = 0;

	// Font 
	public UnicodeFont font;

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
	public int port = 2301;
	// depots for senders
	public Vector<MultiMessage> toSend = new Vector<MultiMessage>();
	// depots for receivers
	public Vector<String> receivedConnexion = new Vector<String>();
	public Vector<String> receivedValidation = new Vector<String>();
	public Vector<String> receivedInputs = new Vector<String>();
	public Vector<String> receivedPing = new Vector<String>();
	public Vector<String> receivedResynchro = new Vector<String>();
	public Vector<Checksum> receivedChecksum = new Vector<Checksum>();
	public Vector<String> receivedChat = new Vector<String>();
	// Sender
	public MultiSender sender;
	// Receiver
	public MultiReceiver receiver;
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
	int victoryTime = 200;
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

	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
		//app.setClearEachFrame(true);
		this.plateau.Xcam = (int) (this.plateau.maxX/2 - this.resX/2);
		this.plateau.Ycam = (int) (this.plateau.maxY/2 -this.resY/2);
	}
	public void setMenu(Menu m){
		this.menuCurrent = m;
		this.isInMenu = true;
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

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{

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
				g.drawString("Vous Avez Gagn� !", this.resX/3f, this.resY/3f);
			}
			else{
				g.drawString("Vous Avez Perdu...", this.resX/3f, this.resY/3f);
			}
		} else {
			// g repr�sente le pinceau
			//g.setColor(Color.black);
			g.translate(-plateau.Xcam,- plateau.Ycam);
			//Draw background
			g.drawImage(this.images.seaBackground, -this.plateau.maxX, -this.plateau.maxY,
					2*this.plateau.maxX, 2*this.plateau.maxY, 0, 0, this.images.seaBackground.getWidth(),this.images.seaBackground.getHeight());


			g.drawImage(this.images.grassTexture,0, 0, this.plateau.maxX, this.plateau.maxY,
					0, 0, this.images.grassTexture.getWidth(),  this.images.grassTexture.getHeight());

			//		int i = 0;
			//		int j = 0;
			//		while(i<this.plateau.maxX+this.images.grassTexture.getWidth()){
			//			while(j<this.plateau.maxY+this.images.grassTexture.getHeight()){
			//				g.drawImage(this.images.grassTexture, i,j);
			//				j+=this.images.grassTexture.getHeight();
			//			}
			//			i+=this.images.grassTexture.getWidth();
			//			j= 0;
			//		}

			//		g.setColor(Color.black);
			//		g.fillRect(this.plateau.maxX, 0, 2*this.plateau.maxX, 2*this.plateau.maxY);
			//		g.fillRect(0, this.plateau.maxY, 2*this.plateau.maxX, 2*this.plateau.maxY);

			//		g.drawImage(this.images.background,this.plateau.maxX, 0);
			//		g.drawImage(this.images.background,0, this.plateau.maxY);

			//			MapGrid mapGrid = this.plateau.mapGrid;
			//			g.setColor(Color.black);
			//			for(Float x : mapGrid.Xcoord){
			//				if(x>this.plateau.Xcam && x<this.plateau.Xcam+this.resX){
			//					g.drawLine(x, plateau.Ycam, x, plateau.Ycam+resY);
			//				}
			//			}
			//			for(Float y : mapGrid.Ycoord){
			//				if(y>this.plateau.Ycam && y<this.plateau.Ycam+this.resY){
			//					g.drawLine(plateau.Xcam, y,plateau.Xcam+resX, y);
			//				}
			//			}

			// Draw the selection of your team 
			for(ActionObjet o: plateau.selection.get(currentPlayer.id)){
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
			// Draw bottom bar
			g.translate(plateau.Xcam, plateau.Ycam);


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
		}
		this.chatHandler.draw(g);
		//		if(debugTimeSteps)
		//			System.out.println("fin du render : "+(System.currentTimeMillis()-timeSteps));
		if(!inEditor){
			//g.setColor(Color.white);
			//this.drawPing(g);
		}
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
		//		


	}
	// Do our logic 
	@Override
	public void update(GameContainer gc, int t) throws SlickException {	
		//		Thread[] tarray = new Thread[Thread.activeCount()];
		//		Thread.enumerate(tarray);
		//		System.out.println("threads pr�sents : "+tarray.length);
		//		for(int i=0; i<tarray.length; i++){
		//			System.out.println(tarray[i].getName());
		//		}
		//		System.out.println();
		//		if(t!=16)
		//			System.out.println("Le round "+round+" a dure "+t);
		Vector<InputObject> ims = new Vector<InputObject>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game	
		if(isInMenu){
			Input in = gc.getInput();
			InputObject im = new InputObject(this,currentPlayer,in,true);
			if(inMultiplayer && menuCurrent instanceof MenuMapChoice){
				this.chatHandler.action(in,im);
			}
			this.menuCurrent.update(im);
			this.send();
		} else if(inEditor) {
			// Map Editor
			Input in = gc.getInput();
			InputObject im = new InputObject(this,currentPlayer,in,!processSynchro);
			this.editor.update(im,in);

		} else if(!endGame) {

			//Update of current round
			this.clock.setRoundFromTime();
			// getting inputs
			Input in = gc.getInput();
			if(in.isKeyPressed(Input.KEY_RALT)){
				this.displayMapGrid = !this.displayMapGrid;
			}
			InputObject im = new InputObject(this,currentPlayer,in,true);
			this.chatHandler.action(in,im);
			if(this.chatHandler.typingMessage){
				im.eraseLetter();
			} else {
				//this.manuelAntidrop(in,gc);
			}
			//Handle manual resynchro
			if(replay==null){
				replay = new Replay(2,"apocalypse",0,this);
			}
			if(inMultiplayer){

				this.toDrawAntiDrop = false;
				this.toDrawDrop = false;
				this.toSendThisTurn+="1"+im.toString()+"%";
				this.inputsHandler.addToInputs(im);
				this.handleChecksum();
				this.handlePing();
				this.handleSendingResynchroParse();
				this.handleResynchro();
				this.send();
				if(!chatHandler.typingMessage){
					this.plateau.handleView(im, this.currentPlayer.id);
				}
				ims = this.inputsHandler.getInputsForRound(this.round);
				this.handleAntidrop(gc);
				if(ims.size()>0){
					this.plateau.update(ims);
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
				this.plateau.update(ims);
				this.plateau.updateCosmetic(im);
				//Update des ordres de l'IA
				this.plateau.updateIAOrders();
				//Update replay
				if(replay!=null){
					replay.addInReplay(ims);
				}
				// Maintenant l'update effectif du plateau est séparé ..
				this.plateau.updatePlateauState();
				//Update IA orders
				if(debugTimeSteps)
					System.out.println("update du plateau single player: "+(System.currentTimeMillis()-timeSteps));
			}
		} else if(endGame){
			
			//Write replay
			replay.write("test");
			if(this.musics.imperial.playing()){
				this.musics.imperial.stop();
			}
			if(victory){
				if(!this.sounds.soundVictory.playing())
					this.sounds.soundVictory.play(1f, this.options.musicVolume);
			}
			else{
				if(!this.sounds.soundDefeat.playing())
					this.sounds.soundDefeat.play(1f, this.options.musicVolume);
			}
			//Print victory !!
			victoryTime --;
			if(victoryTime <0){
				if(this.sounds.soundVictory.playing()){
					this.sounds.soundVictory.stop();
				}
				else{
					this.sounds.soundDefeat.stop();
				}
				victoryTime = 100;
				this.isInMenu = true;
				this.hasAlreadyPlay = true;
				this.endGame = false;
				if(inMultiplayer){
					this.receiver.shutdown();
					this.sender.shutdown();
				}
				this.setMenu(this.menuIntro);

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

	private void drawPing(Graphics g) {
		float y = this.relativeHeightBottomBar*resY/2f-this.font.getHeight("Hg")/2f;
		g.drawString("Ping : "+Integer.toString((int)(this.clock.getPing()/1000000f)), 20f, y);
		g.drawString("delay : "+Integer.toString(this.roundDelay), 110f, y);
	}

	public void launchGame(){

		this.musics.imperial.loop();
		this.musics.imperial.setVolume(options.musicVolume);
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
	@Override
	public void init(GameContainer gc) throws SlickException {	
		Image cursor = new Image("pics/cursor.png");
		java.awt.Font fe = new java.awt.Font("Candara",java.awt.Font.PLAIN,25);
		this.font = new UnicodeFont(fe,25,false,false);
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		this.font.addAsciiGlyphs();
		//this.font.addNeheGlyphs();
		//this.font.addGlyphs(0, 256);
		this.font.loadGlyphs();
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

		this.editor = new MapEditor(this);
		this.setMenu(menuIntro);
		Map.initializePlateau(this, 1f, 1f);

		//FLO INPUTS
		this.inputsHandler = new InputHandler(this);
		//System.out.println(this.plateau.mapGrid);
		//			Map.createMapEmpty(this);
		// Instantiate BottomBars for all players:
		selection = null;
		try {
			this.addressLocal = InetAddress.getLocalHost();
			String address = addressLocal.getHostAddress();
			String[] tab = address.split("\\.");
			address = tab[0]+"."+tab[1]+"."+tab[2]+".255";
			this.addressBroadcast = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.clock = new Clock(this);
		this.clock.start();
		chatHandler = new ChatHandler(this);
	}



	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;
		this.ratioResolution = this.resX/2800f;
	}


	// AUXILIARY FUNCTIONS FOR MULTIPLAYER
	// DANGER
	public void sendConnexion(String message){
		if(host){
			this.toSend.add(new MultiMessage("0"+message,this.addressBroadcast));
		} else {
			this.toSend.add(new MultiMessage("0"+message,this.addressHost));
		}
	}
	public void send(){
		for(int i=1; i<this.nPlayers; i++){
			if(i!=currentPlayer.id)
				this.toSend.add(new MultiMessage(toSendThisTurn,this.players.get(i).address));
		}
		toSendThisTurn="";
	}


	private void manuelAntidrop(Input in,GameContainer gc) {
		delaySleep = 0 ;
		if(in.isKeyPressed(Input.KEY_P)){
			this.round+=1;
			this.roundDelay++;
		}
		if(in.isKeyPressed(Input.KEY_M)){
			this.round-=1;
			this.roundDelay--;
		}
		if(in.isKeyPressed(Input.KEY_O)){
			this.delaySleep=8;
		}
		if(in.isKeyPressed(Input.KEY_L)){
			this.delaySleep = -8;
		}
		//UPDATE ROUND DURATION
		gc.setMinimumLogicUpdateInterval((1000/Main.framerate)+delaySleep);
		gc.setMaximumLogicUpdateInterval((1000/Main.framerate)+delaySleep);
	}
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
			// si client on envoie checksum
			toSendThisTurn+="5"+checksum+"%";
			if(host){
				this.checksum.addElement(new Checksum(checksum));
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
							//							System.out.println("Game line 719 : Probleme synchro round "+this.round);
							//							System.out.println(c.checksum);
							//							System.out.println(c1.checksum);
							//
							//							this.processSynchro = true;
							//							this.sendParse = true;					
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
		//TODO
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
			//Si round+2
			if(toParse==null){
				this.processSynchro = false;
				return;
			}
			String[] u = this.toParse.split("!");
			//Je resynchronise au tour n+2
			if(Integer.parseInt(u[0])==(this.round-InputHandler.nDelay)){
				//				System.out.println("Play resynchronisation round at round " + this.round);
				this.plateau.parse(this.toParse);
				this.toParse = null;
				this.processSynchro = false;

			}
			else if(this.toParse==null || Integer.parseInt(u[0])<(this.round-InputHandler.nDelay)){
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
	}


}