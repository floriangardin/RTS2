package model;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.tiled.*;

import multiplaying.*;
import multiplaying.ConnectionModel.*;

public class Game extends BasicGame 
{	
	int step = 0;
	boolean displayUnit= true;
	public void changeDisplayUnit(){
		displayUnit = !displayUnit;
	}
	public int idChar = 0;
	public int idBullet = 0;
	// Ennemy generator 
	EnemyGenerator gen ;
	//Music 
	float soundVolume;
	// Volume
	float volume;
	Music mainMusic ;
	Music musicStartGame;
	//Sounds ;
	Sounds sounds;
	//Images ;
	Images images;
	//Maps;
	Map map;
	// Bottom bar :
	BottomBar bottomBars;
	// Top bars:
	TopBar topBars;

	Image background ;
	Constants constants;
	// Selection
	Rectangle selection;
	boolean new_selection;
	Vector<Objet> objets_selection=new Vector<Objet>();
	// framerate
	int framerate ;
	// Resolution : 
	float resX;
	float resY;
	boolean playStartMusic = true;
	// We keep the reference of the plateau in the game :
	// Only the game knows the reference for raw vector of objects !
	private Vector<ActionObjet> actionObjets = new Vector<ActionObjet>();
	protected Vector<Player> players = new Vector<Player>();
	// Then we declare the plateau
	public Plateau plateau ;

	// Network and multiplaying
	protected boolean inMultiplayer;
	public long startTime;
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
	public int delay = 2;
	// Sender and Receiver
	public MultiReceiver inputReceiver = new MultiReceiver(this,2345);
	public MultiSender inputSender = new MultiSender(this.app,this,addressHost,2345,this.toSendInputs);
	public MultiReceiver outputReceiver = new MultiReceiver(this,2346);
	public MultiSender outputSender = new MultiSender(this.app,this,addressClient, 2346, this.toSendOutputs);
	public MultiReceiver connexionReceiver = new MultiReceiver(this,2344);
	public MultiSender connexionSender;
	public boolean isHost;
	//Debugging network
	public int toAdd = 0;
	public int toRemove = 0;

	// Current player
	protected int currentPlayer = 0;
	// Menus
	AppGameContainer app;
	protected Menu menuPause;
	protected Menu menuIntro;
	protected Menu menuCurrent = null;
	public boolean isInMenu = false;
	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
		app.setClearEachFrame(true);

		if(!this.musicStartGame.playing()){
			this.musicStartGame.play();
			this.musicStartGame.setVolume(this.volume);

		}
	}
	public void setMenu(Menu m){
		this.menuCurrent = m;
		this.isInMenu = true;

		app.setClearEachFrame(false);
	}


	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//Utils.triY1(this.plateau.characters);

		// g repr�sente le pinceau
		//g.setColor(Color.black);
		if(isInMenu){
			this.menuCurrent.draw(g);
			return;
		}
		for(int i=0;i<3;i++){
			for(int j=0;j<2;j++){
				g.drawImage(this.background, i*512, j*512);
			}
		}


		//g.fillRect(0,0,gc.getScreenWidth(),gc.getScreenHeight());



		// Draw the selection of your team 
		for(Character o: plateau.selection.get(currentPlayer)){
			o.drawIsSelected(g);
		}
		//Creation of the drawing Vector
		Vector<Objet> toDraw = new Vector<Objet>();
		// Draw the Action Objets
		for(Character o : plateau.characters){
			//o.draw(g);
			if(this.displayUnit)
				toDraw.add(o);
		}
		for(ActionObjet o : plateau.equipments){
			//o.draw(g);
			toDraw.add(o);
		}
		// Draw the natural Objets

		for(NaturalObjet o : this.plateau.naturalObjets){
			//o.draw(g);
			toDraw.add(o);
		}
		// Draw the enemy generators
		for(EnemyGenerator e : this.plateau.enemyGens){
			toDraw.add(e);
		}
		Utils.triY(toDraw);
		for(Objet o: toDraw)
			o.draw(g);

		for(Bullet o : plateau.bullets){
			o.draw(g);
			//toDraw.add(o);
		}
		// Draw the selection :
		if(this.plateau.rectangleSelection.get(currentPlayer) !=null){
			g.setColor(Color.green);
			g.draw(this.plateau.rectangleSelection.get(currentPlayer));

		}

		int gentils=0,mechants=0;
		for(Character c: plateau.characters){
			if(c.team==0)
				gentils+=1;
			else
				mechants+=1;
		}
		g.setColor(Color.blue);
		g.drawString(String.valueOf(gentils), 10, 30);

		g.setColor(Color.red);
		g.drawString(String.valueOf(mechants), 10, 70);

		if(this.app.isShowingFPS()){
			g.setColor(Color.black);
			g.fill(new Rectangle(0,0,90,30));
		}

		// Draw bottom bar
		if(this.bottomBars!=null)
			this.bottomBars.draw(g);


	}
	// Do our logic 
	@Override
	public synchronized void update(GameContainer gc, int t) throws SlickException 
	{	
		InputModel im=null;
		Vector<InputModel> ims = new Vector<InputModel>();
		//System.out.println(this.plateau.characters);
		if(inMultiplayer){

			//Utils.printCurrentState(this.plateau);
			// The game is in multriplaying mode
			if(isHost){
				/* Multiplaying Host Pipeline
				 * 1 - take the input of t-5 client and host
				 * 2 - perform action() and update()
				 * 3 - send the output of the action and update step
				 */

				// Defining the clock
				timeValue = (int)(System.currentTimeMillis() - startTime)/framerate;

				// 1 - take the input of t-5 client and host
				int indice;
				InputModel imm;
				for(int player = 0; player<players.size(); player++){
					if(player!=currentPlayer){
						indice = 0;
						imm=null;
						im=null;
						while(indice<this.inputs.size()){
							imm = this.inputs.get(indice);
							if(imm.team==player && imm.timeValue<=timeValue){
								if(imm.timeValue<timeValue-5){
									this.toRemoveInputs.addElement(imm);
									// If the input is for a past time we erase it
								} else {
									if(im!=null){
										im.mix(imm);
									} else {
										im = imm;
									}
									this.toRemoveInputs.addElement(imm);
								}
							}
							indice+=1;
						}
						for(InputModel im2 : this.toRemoveInputs){
							this.inputs.remove(im2);
							this.toRemove++;
						}
						this.toRemoveInputs.clear();
						ims.add(im);
					} else {
						im = new InputModel(timeValue,currentPlayer,gc.getInput());
						ims.add(im);
					}
				}

				// 2 - perform action() and update()
				OutputModel om = null;
				if(isInMenu){
					this.menuCurrent.update(ims.get(0));
				} else {
					om = this.plateau.update(ims);
				}

				// 3 - send the output of the action and update step
				om.timeValue = timeValue+delay;
				this.toSendOutputs.addElement(om.toString());

			} else if(!isHost){
				/* Multiplaying Client Pipeline
				 * 1 - send the input to host
				 * 2 - take the output from t-5
				 * 3 - update from the output file
				 */

				// Defining the clock
				timeValue = (int)(System.currentTimeMillis() - startTime)/framerate;

				// 1 - send the input to host
				im = new InputModel(timeValue+delay,currentPlayer,gc.getInput());
				this.toSendInputs.addElement(im.toString());

				// 2 - take the output from t-5
				int indice = 0;
				OutputModel om = null,omm;
				while(indice<this.outputs.size()){
					omm = this.outputs.get(indice);
					if(omm.timeValue<=timeValue){
						if(omm.timeValue<timeValue-5){
							this.toRemoveOutputs.addElement(omm);
							// If the input is for a past time we erase it
						} else {
							om = omm;
							this.toRemoveOutputs.addElement(omm);
						}
					}
					indice+=1;
				}
				for(OutputModel om2 : this.toRemoveOutputs){
					this.outputs.remove(om2);
				}
				this.toRemoveOutputs.clear();

				// 3 - update from the output file
				this.plateau.updateFromOutput(om, im);

			}
		} else if (!inMultiplayer){
			// If not in multiplayer mode, dealing with the common input
			ims.add(new InputModel(0,1,gc.getInput()));
			// updating the game
			if(isInMenu){
				this.menuCurrent.update(ims.get(0));
			} else {
				this.plateau.update(ims);
			}

		}

	}

	public void newGame(){
		//Clean all variables
		this.plateau = new Plateau(this.constants,this.resX,4f/5f*this.resY,2,this);
		this.players = new Vector<Player>();
		this.players.add(new Player(0));
		this.players.add(new Player(1));

		this.map.createMapVersus(plateau);
		// Instantiate BottomBars for current player:
		this.bottomBars = new BottomBar(this.plateau,this.players.get(0),this);
		selection = null;
	}
	public void newGame(ConnectionModel cm){
		//Clean all variables
		this.plateau = new Plateau(this.constants,this.resX,4f/5f*this.resY,2,this);
		this.players = new Vector<Player>();
		this.players.add(new Player(0));
		this.players.add(new Player(1));

		this.addressHost = cm.ia;
		for( ConnectionObjet co : cm.naturalObjets){
			if(co instanceof ConnectionTree){
				new Tree(co.x,co.y,this.plateau,((ConnectionTree) co).type);
			} else if(co instanceof ConnectionWater){
				new Water(co.x,co.y,((ConnectionWater)co).sizeX,((ConnectionWater)co).sizeY,this.plateau);
			}
		}

		// Instantiate BottomBars for current player:
		this.bottomBars = new BottomBar(this.plateau,this.players.get(1),this);
		selection = null;
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
		
		this.musicStartGame = new Music("music/nazi_start.ogg");
		this.players.add(new Player(0));
		this.players.add(new Player(1));
		this.sounds = new Sounds();
		this.images = new Images();
		this.plateau = new Plateau(this.constants,this.resX,4f/5f*this.resY,3,this);
		this.background =  new Image("pics/dirt.png");
		this.menuIntro = new MenuIntro(this);
		this.menuPause = new MenuPause(this);
		this.map = new Map();
		this.setMenu(menuIntro);
		this.startTime = System.currentTimeMillis();
		//		Thread t1 = new Thread(new InputListener(this, this.app, 0));
		//		t1.start();
		try{
			Thread.sleep(10);
		} catch(InterruptedException e) {}
	}
	public Game ()
	{
		super("Ultra Mythe RTS 3.0");
	}
	public void setParams(Constants constants,float resX,float resY){
		this.constants = constants;
		this.framerate = constants.FRAMERATE ;
		this.resX = resX;
		this.resY = resY ;
		//
	}
}
