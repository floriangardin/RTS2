package model;
import java.net.InetAddress;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;

import buildings.Building;
import bullets.Bullet;
import display.Message;
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
import multiplaying.MultiReceiverChat;
import multiplaying.MultiReceiverChecksum;
import multiplaying.MultiReceiverChecksum.Checksum;
import multiplaying.MultiReceiverConnexion;
import multiplaying.MultiReceiverInput;
import multiplaying.MultiReceiverPing;
import multiplaying.MultiReceiverResynchro;
import multiplaying.MultiReceiverValidation;
import multiplaying.MultiSender;
import spells.SpellEffect;
import units.Character;
public class Game extends BasicGame 
{	
	// DEBUG
	public static boolean debugTimeSteps = false;
	public static boolean debugPaquet = false;
	public static boolean debugValidation = false;
	public static boolean debugReceiver = false;
	public static boolean debugSender = false;
	public static boolean debugTourEnCours = false;
	public static boolean debugThread = false;

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

	// ports
	public int portConnexion = 8887;
	public int portValidation = 8888;
	public int portInput = 8889;
	public int portPing = 8890;
	public int portResynchro = 8891;
	public int portChecksum = 8892;
	public int portChat = 8893;
	// depots for senders
	public Vector<String> toSendInput = new Vector<String>();
	public Vector<String> toSendConnexion = new Vector<String>();
	public Vector<String> toSendValidation = new Vector<String>();
	public Vector<String> toSendPing = new Vector<String>();
	public Vector<String> toSendResynchro = new Vector<String>();
	public Vector<String> toSendChecksum = new Vector<String>();
	public Vector<String> toSendChat = new Vector<String>();
	// depots for receivers
	public Vector<String> receivedConnexion = new Vector<String>();
	public Vector<String> receivedValidation = new Vector<String>();
	public Vector<String> receivedInputs = new Vector<String>();
	public Vector<String> receivedPing = new Vector<String>();
	public Vector<String> receivedResynchro = new Vector<String>();
	public Vector<Checksum> receivedChecksum = new Vector<Checksum>();
	public Vector<String> receivedChat = new Vector<String>();
	// Senders
	public MultiSender connexionSender;
	public MultiSender validationSender;
	public MultiSender inputSender;
	public MultiSender pingSender;
	public MultiSender resynchroSender;
	public MultiSender checksumSender;
	public MultiSender chatSender;
	// Receivers
	public MultiReceiverConnexion connexionReceiver;
	public MultiReceiverValidation validationReceiver;
	public MultiReceiverInput inputReceiver;
	public MultiReceiverPing pingReceiver;
	public MultiReceiverResynchro resynchroReceiver;
	public MultiReceiverChecksum checksumReceiver;
	public MultiReceiverChat chatReceiver;


	// Handling multiplaying
	public InputHandler inputsHandler;
	public String toParse= null;
	public boolean processSynchro;
	public Vector<Checksum> checksum = new Vector<Checksum>();
	private boolean sendParse;
	// Mutex
	public Lock mutexChecksum = new ReentrantLock();
	// antidrop
	public boolean antidropProcess = false;
	public float nDrop = 0f;
	public float nRound = 0f;
	public int multi = 1;
	public int roundToTest = 0;


	// Menus
	public Menu menuPause;
	public MenuIntro menuIntro;
	public MenuOptions menuOptions;
	public MenuMulti menuMulti;
	public MenuMapChoice menuMapChoice;
	public Menu menuCurrent = null;
	public boolean isInMenu = false;
	public int idInput;
	public float ratioResolution;


	//VAut + ou - 1 (pour resynchro Ã  la main)
	public int sleep;
	public int roundDelay;

	// victory
	public boolean endGame = false;
	public boolean victory = false;
	int victoryTime = 200;


	boolean hasAlreadyPlay = false;


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
		} else if(endGame){

			g.setColor(Color.black);
			g.fillRect(0, 0, this.resX, this.resY);
			g.setColor(Color.white);
			//PRint victory
			if(this.victory){
				g.drawString("Alors, on se sent un peu comme Gilles ?", this.resX/3f, this.resY/3f);
			}
			else{
				g.drawString("T'as perdu Roger ...", this.resX/3f, this.resY/3f);
			}
		} else {


			// g reprï¿½sente le pinceau
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
		}
		if(processSynchro){
			g.setColor(Color.green);
			g.fillRect(10f,10f,10f,10f);
		}
		if(debugTimeSteps)
			System.out.println("fin du render : "+(System.currentTimeMillis()-timeSteps));

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
		//		g.drawString(sb.toString(), 20f, 40f);

	}
	// Do our logic 
	@Override
	public void update(GameContainer gc, int t) throws SlickException {	
		Vector<InputObject> ims = new Vector<InputObject>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game	
		if(isInMenu){
			InputObject im = new InputObject(this,plateau.currentPlayer,gc.getInput(),!antidropProcess && !processSynchro);
			this.menuCurrent.update(im);
		} else if(!endGame) {
			//Update of current round
			this.clock.setRoundFromTime();
			// getting inputs
			Input in = gc.getInput();
			InputObject im = new InputObject(this,plateau.currentPlayer,in,!antidropProcess);
			//Handle manual resynchro
			this.manuelAntidrop(in);

			if(inMultiplayer){

				////////////////////
				/// MULTIPLAYING ///
				////////////////////

				// Checksum
				this.handleChecksum();

				// Ping
				this.handlePing();

				// Send Resynchro
				this.handleSendingResynchroParse();

				// Antidrop
				this.handleAntidrop();

				if(processSynchro && this.toParse!=null){
					// Resynchro
					this.handleResynchro();
				}else{
					//UPDATE NORMAL
					// On envoie l'input du tour courant
					this.sendInputToAllPlayer(im.toString());
					this.inputsHandler.addToInputs(im);
					this.plateau.handleView(im, this.plateau.currentPlayer.id);
					ims = this.inputsHandler.getInputsForRound(this.round);
					if(ims.size()==0 && !processSynchro){
						nDrop++;
					}
					boolean toPlay = true;
					for(InputObject o : ims){
						if(!o.toPlay){
							System.out.println("Anti drop : "+round);
							toPlay = false;
						}
					}
					if(toPlay){
						this.plateau.update(ims);
						this.plateau.updatePlateauState();
					}
				}

				if(debugTimeSteps)
					System.out.println("update du plateau serveur: "+(System.currentTimeMillis()-timeSteps));

			} else {

				/////////////////////
				/// SINGLE PLAYER ///
				/////////////////////

				ims.add(im);
				this.plateau.handleView(im, this.plateau.currentPlayer.id);
				// solo mode, update du joueur courant
				this.plateau.update(ims);
				//Update des ordres de l'IA
				this.plateau.updateIAOrders();

				// Maintenant l'update effectif du plateau est sÃ©parÃ© ..
				this.plateau.updatePlateauState();
				//Update IA orders
				if(debugTimeSteps)
					System.out.println("update du plateau single player: "+(System.currentTimeMillis()-timeSteps));
			}
		} else if(endGame){
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
					this.menuMapChoice.shutdownNetwork();
					this.connexionReceiver.shutdown();
					this.connexionSender.shutdown();
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
		java.awt.Font fe = new java.awt.Font("Candara",java.awt.Font.PLAIN,(int)(28*this.resX/1920));
		this.font = new UnicodeFont(fe,(int)(28*this.resX/1920),false,false);
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		this.font.addAsciiGlyphs();
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
		this.setMenu(menuIntro);
		Map.initializePlateau(this, 1f, 1f);

		//FLO INPUTS
		this.inputsHandler = new InputHandler(this);
		//System.out.println(this.plateau.mapGrid);
		//			Map.createMapEmpty(this);
		// Instantiate BottomBars for all players:
		selection = null;
		this.clock = new Clock(this);
		this.clock.start();
	}


	public void sendInputToAllPlayer(String s){
		this.toSendInput.add(s);
	}

	public Game (float resX,float resY){
		super("Ultra Mythe RTS 3.0");
		this.resX = resX;
		this.resY = resY;
		this.ratioResolution = this.resX/2800f;
	}


	// AUXILIARY FUNCTIONS FOR MULTIPLAYER
	// DANGER

	private void manuelAntidrop(Input in) {
		if(in.isKeyPressed(Input.KEY_P)){
			this.round+=1;
			this.roundDelay++;
		}
		if(in.isKeyPressed(Input.KEY_M)){
			this.round-=1;
			this.roundDelay--;
		}
	}
	private void handleChecksum() {
		// If host and client send checksum
		if(!processSynchro && this.round>=30 && this.round%20==0){
			//Compute checksum
			String checksum = this.round+"|0";
			int i = 0;
			while(i<this.plateau.characters.size()){
				checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).x))%10);
				checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).y))%10);
				checksum+=Integer.toString(((int)(this.plateau.characters.get(i).lifePoints))%10);
				checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).state))%10);
				checksum+=Integer.toString(this.plateau.characters.get(i).id%10)+"-";
				i++;
			}
			checksum+="|";
			if(!this.host){
				// si client on envoie checksum
				this.toSendChecksum.add(checksum);
			} else {
				// on l'ajoute dans mes checksum si je suis host
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
		if(!host && round%40 == 0){
			this.toSendPing.add(this.clock.getCurrentTime()+"|"+this.plateau.currentPlayer.id+"|");
		}
	}
	private void handleSendingResynchroParse() {
		if(this.host && this.processSynchro && this.sendParse){
			this.toParse = this.plateau.toStringArray();
			System.out.println("Game line 698: Sent synchro message");
			this.sendParse = false;
			this.sendInputToAllPlayer(this.toParse);
		}
	}
	private void handleAntidrop() {
		if(host && (this.round%7)==0){
			nDrop = 0f;
			if(nDrop>=2){
				if(antidropProcess==false){
					this.antidropProcess=true;					
				}
				// on tente une nouvelle valeur pour le décalage
				if(multi==-1){
					multi=1;
					roundToTest++;
				}
				else if(multi==1){
					roundToTest++;
					multi=-1;
				}
				this.round+=multi*roundToTest;
				this.roundDelay+=multi*roundToTest;
				if(roundToTest>=8){
					// si on a été trop loin on revient à zéro
					this.round-=this.roundDelay;
					roundToTest = 0;
					multi = 1;
					this.roundDelay=0;
				}
			} else {
				antidropProcess = false;
			}
		}
	}
	private void handleResynchro() {
		//Si round+2
		String[] u = this.toParse.split("!");
		//Je resynchronise au tour n+2
		if(Integer.parseInt(u[1])==(this.round-InputHandler.nDelay)){
			System.out.println("Play resynchronisation round at round " + this.round);
			this.plateau.parse(this.toParse);
			this.toParse = null;
			this.processSynchro = false;
			System.out.println("Resynchronisation ....");
		}
	}
	public void pingRequest() {
		this.toSendPing.addElement(this.clock.getCurrentTime()+"|"+this.plateau.currentPlayer.id+"|");
	}



}
