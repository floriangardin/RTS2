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
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;

import spells.SpellEffect;
import units.Character;
import buildings.Building;
import bullets.Bullet;
import display.Message;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
public class Game extends BasicGame 
{	
	// DEBUG
	public static boolean debugTimeSteps = false;
	public static boolean debugPaquet = false;
	public static boolean debugValidation = false;
	public static boolean debugReceiver = false;
	public static boolean debugSender = false;
	public static boolean debugTourEnCours = false;

	//MUtex
	public Lock mutexChecksum = new ReentrantLock();

	// debugging tools
	long timeSteps = 0;
	public int nbGameTurn = 0;

	public int round = 0;
	public int roundDebug = 0;

	public int idPaquetSend = 0;
	public int nbPaquetReceived = 0;
	public int idPaquetReceived = 0;
	public int idPaquetTreated = 0;

	public int deltaTime;

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


	// Network and multiplaying
	//Clock
	public Clock clock;
	public boolean inMultiplayer;
	public boolean host = false;
	public long startTime;
	public int portConnexion = 6113;
	public int portInput = 8888;
	public int portOutput = 8889;
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
	public float ratioResolution;

	public Vector<String> checksum = new Vector<String>();
	public Vector<String> clockSynchro= new Vector<String>();
	public boolean processSynchro=false;
	public boolean sendParse = false;

	public Vector<Integer> dropped = new Vector<Integer>();
	public boolean updateDropped= false;
	public boolean clockResynchro = false;
	public boolean restartProcess = false;


	public long delta;
	public long ping;

	//VAut + ou - 1 (pour resynchro à la main)
	public int sleep;
	public int sleepTime;
	public int roundDelay;

	public boolean endGame = false;
	public boolean victory = false;
	int victoryTime = 200;
	

	boolean hasAlreadyPlay = false;
	
	//RESYNCHRO ROUND
	public boolean resynchroRound = false;
	public float nDrop = 0f;
	public float nRound = 0f;
	public int multi = 1;
	public int roundToTest = 0;

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
			return;
		} 


		if(endGame){
			
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

			return;

		}
		
	
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

		//DEBUG

		g.drawString(Integer.toString(deltaTime), 60f,10f);
		if(processSynchro){
			g.setColor(Color.green);
			g.fillRect(10f,10f,10f,10f);
		}
		if(restartProcess){
			g.setColor(Color.red);
			g.fillRect(20f,10f,10f,10f);
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

		g.drawString(Float.toString((float )(this.ping/1000000)), 20f, 60f);
		g.drawString(Integer.toString(this.sleep), 80f, 60f);
		g.drawString(Integer.toString(this.roundDelay), 120f, 60f);

	}
	// Do our logic 
	@Override
	public void update(GameContainer gc, int t) throws SlickException {	
		this.deltaTime = t;
		Vector<InputObject> ims = new Vector<InputObject>();
		// If not in multiplayer mode, dealing with the common input
		// updating the game	
		if(isInMenu){
			InputObject im = new InputObject(this,plateau.currentPlayer,gc.getInput());
			this.menuCurrent.update(im);
		} 
		else if(!endGame) {
			//Update of current round
			this.clock.setRoundFromTime();
			if(t!=16){
				System.out.println("Round a trop dure :"+t);
			}

			Input in = gc.getInput();
			InputObject im = new InputObject(this,plateau.currentPlayer,in);
			//Handle manual resynchro
			if(in.isKeyPressed(Input.KEY_O)){
				this.sleepTime+=1;
				this.sleep++;
			}
			if(in.isKeyPressed(Input.KEY_L)){
				this.sleepTime-=1;
				this.sleep--;
			}
			if(in.isKeyPressed(Input.KEY_P)){
				this.round+=1;
				this.roundDelay++;
			}
			if(in.isKeyPressed(Input.KEY_M)){
				this.round-=1;
				this.roundDelay--;
			}

			if(inMultiplayer){

				//CHECKSUm
				if(this.round>=30 && this.round%30==0){

					//Compute checksum
					String checksum = "3C|"+this.round+"|0";
					int i = 0;
					//Checksum to send of plateau to test synchro
					while(i<this.plateau.characters.size()){
						checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).x))%10);
						checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).y))%10);
						checksum+=Integer.toString(((int)(this.plateau.characters.get(i).lifePoints))%10);
						checksum+=Integer.toString(((int)(10f*this.plateau.characters.get(i).state))%10);
						checksum+=Integer.toString(this.plateau.characters.get(i).id)+"-";
						i++;
					}
					checksum+="|";

					if(!this.host){
						this.sendInputToAllPlayer(checksum);
						//Je l'envoie seulement si je suis client
					}
					//Je l'ajoute dans mes checksum si je suis host
					else{
						this.mutexChecksum.lock();
						this.checksum.addElement(checksum);
						this.mutexChecksum.unlock();
					}

					//J'enleve le premier �lement si probl�me tous les 5 checksum re�u
					if(this.checksum.size()>5){
						this.mutexChecksum.lock();
						this.checksum.remove(0);
						this.mutexChecksum.unlock();
					}
				}

				//PING REQUEST
				if(round%30 == 0){
					this.sendInputToAllPlayer("3M|"+this.clock.getCurrentTime()+"|"+this.plateau.currentPlayer.id+"|");
				}

				//Update framerate
				gc.setMinimumLogicUpdateInterval((1000/Main.framerate) +sleepTime);
				gc.setMaximumLogicUpdateInterval((1000/Main.framerate) +sleepTime);
				sleepTime = 0;

				//RESYNCH
				if(this.host && this.processSynchro && this.sendParse){
					this.toParse = this.plateau.toStringArray();
					System.out.println("Sent synchro message");
					System.out.println(toParse);
					this.sendParse = false;
					this.sendInputToAllPlayer(this.toParse);
				}

				if(processSynchro && this.toParse!=null){
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
				
				
				//RESYNCHRO ROUND
				if(host && (this.round%30)==0){
					this.resynchroRound=true;
				}
				
				if(nRound>10){
					resynchroRound = false;
					float ratio = nDrop/nRound;
					nRound = 0f;
					nDrop = 0f;
					if(ratio>0.8){
						if(multi==-1){
							multi=1;
							roundToTest++;
						}
						if(multi==1){
							roundToTest++;
							multi=-1;
						}
						this.round+=multi*roundToTest;
					}
				}
				
				//UPDATE IF NOT RESYNCH
				else{
					// On envoie l'input du tour courant
					this.sendInputToAllPlayer(im.toString());
					this.inputsHandler.addToInputs(im);
					this.plateau.handleView(im, this.plateau.currentPlayer.id);
					ims = this.inputsHandler.getInputsForRound(this.round);
					if(resynchroRound){
						if(ims.size()==0){
							nDrop++;
						}
						nRound++;
					}
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

		else if(endGame){
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
				this.setMenu(this.menuIntro);
				//TODO FERMER LES SOCKETS
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
		java.awt.Font fe = new java.awt.Font("Candara",java.awt.Font.PLAIN,(int)(14*this.resX/1920));
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
		this.connexionReceiver.start();
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
		this.ratioResolution = this.resX/2800f;
		connexionReceiver = new MultiReceiver(this,portConnexion);
		connexionSender = new MultiSender(null, portConnexion, this.toSendConnexions,this);

	}

}
