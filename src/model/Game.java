package model;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.tiled.*;

public class Game extends BasicGame 
{	
	int team = 0;
	int step = 0;
	boolean displayUnit= true;
	public void changeDisplayUnit(){
		displayUnit = !displayUnit;
	}
	// Ennemy generator 
	EnemyGenerator gen ;
	//Music 
	Music mainMusic ;
	Music musicMenu;
	Music musicStartGame;
	//Sounds ;
	Sounds sounds;
	//Images ;
	Images images;
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
	// Point where you clicked the rectangle the first time : 
	float recX ;
	float recY ;
	// framerate
	int framerate ;
	// Resolution : 
	float resX;
	float resY;
	// We keep the reference of the plateau in the game :
	// Only the game knows the reference for raw vector of objects !
	private Vector<ActionObjet> actionObjets = new Vector<ActionObjet>();
	protected Vector<Player> players = new Vector<Player>();
	// Then we declare the plateau
	private Plateau plateau ;

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
		if(this.selection !=null){
			g.setColor(Color.green);
			g.draw(this.selection);

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
	public void update(GameContainer gc, int t) throws SlickException 
	{	

		// Get the input from the usr
		Input i = gc.getInput();
		// Update the selection rectangle :
		// Test if new selection :
		if(isInMenu){
			if(!this.musicMenu.playing()){
				this.musicMenu.play();
			}
			this.menuCurrent.update(i);
			return;
		}
		
		if(!isInMenu && i.isKeyPressed(org.newdawn.slick.Input.KEY_ESCAPE)){
			if(!this.musicMenu.playing()){
				this.musicMenu.play();
				this.musicMenu.setVolume(1.5f);
			}
			this.setMenu(menuPause);
			return;
		}

		
		if(this.musicMenu.playing() && !isInMenu){
			this.musicMenu.fade(300,0f,true);
			if(!this.musicStartGame.playing()){
				this.musicStartGame.play();
				
			}
		}

		
		if(!isInMenu && !this.musicStartGame.playing() && !this.mainMusic.playing()){
			this.mainMusic.loop();
		}
			
		// GAME PART 

		if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			this.plateau.clearSelection(team);
		}
		
		//TODO: Handling the groups
		int touche;
		for(int to=0; to<10; to++){
			touche = to + 2;
			if(i.isKeyPressed(touche)){
				this.players.get(this.currentPlayer).groupSelection = touche-2;
				if(i.isKeyDown(org.newdawn.slick.Input.KEY_LCONTROL) || i.isKeyDown(org.newdawn.slick.Input.KEY_RCONTROL)){
					// Creating a new group made of the selection
					this.players.get(currentPlayer).groups.get(to).clear();
					for(Character c: this.plateau.selection.get(currentPlayer))
						this.players.get(currentPlayer).groups.get(to).add(c);
				} else if(i.isKeyDown(org.newdawn.slick.Input.KEY_LSHIFT) || i.isKeyDown(org.newdawn.slick.Input.KEY_RSHIFT)){
					// Adding the current selection to the group
					for(Character c: this.plateau.selection.get(currentPlayer))
						this.players.get(currentPlayer).groups.get(to).add(c);
				} else {
					this.plateau.selection.get(currentPlayer).clear();
					for(Character c: this.players.get(currentPlayer).groups.get(to))
						this.plateau.selection.get(currentPlayer).add(c);
				}
			}
		}
		if(i.isKeyPressed(org.newdawn.slick.Input.KEY_A)){
			currentPlayer+=1;
			if(currentPlayer==2)
				currentPlayer = 0;
		}
		if(i.isKeyPressed(org.newdawn.slick.Input.KEY_Z))
			Utils.printCurrentState(plateau);

		// Update the rectangle
		if(i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			if(selection==null){
				recX = i.getAbsoluteMouseX();
				recY = i.getAbsoluteMouseY();
				selection = new Rectangle(recX,recY,0.1f,0.1f);

			}
			selection.setBounds( (float)Math.min(recX,i.getAbsoluteMouseX()), (float)Math.min(recY, i.getAbsoluteMouseY()),
					(float)Math.abs(i.getAbsoluteMouseX()-recX)+0.1f, (float)Math.abs(i.getAbsoluteMouseY()-recY)+0.1f);
		}
		else if(!i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && this.selection!=null){
			plateau.updateSelection(selection, this.currentPlayer);
			this.selection = null;
		}
		else{
			// We update selection when left click is released
			selection = null;
		}
		// Action for player k
		if(i.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			if(i.isKeyDown(org.newdawn.slick.Input.KEY_LSHIFT) || i.isKeyDown(org.newdawn.slick.Input.KEY_RSHIFT)){
				this.plateau.updateSecondaryTarget(i.getMouseX(),i.getMouseY(),currentPlayer);
			} else {				
				this.plateau.updateTarget(i.getMouseX(),i.getMouseY(),currentPlayer);
			}
		}
		// Perform the collision, the creation and destruction of elements.

		plateau.update();
		// Perform the actions;
		plateau.action();

		for(int pl =0; pl <this.players.size(); pl++){
			this.players.get(currentPlayer).selection.clear();
			for(Character c: this.plateau.selection.get(currentPlayer))
				this.players.get(currentPlayer).selection.addElement(c);
		}
	}

	public void newGame(){
		//Clean all variables
		this.plateau = new Plateau(this.constants,this.resX,4f/5f*this.resY,2,this);
		this.players = new Vector<Player>();
		this.players.add(new Player(0));
		this.players.add(new Player(1));

		//Instantiate allies
		Character[] team = new Character[5];
		for(int i=0;i<5;i++){		
			team[i]=new Character(this.plateau,0,500f+10f*i,500f);
			this.players.get(0).groups.get(i).add(team[i]);
		}
		// Give equipement to team 
		// 0 : sword heavy armor, 1: Bow light armor , 2: Horse sword medium armor, 3: Bible no armor, 4:magician no armor
		team[0].collectWeapon(new Sword(plateau,team[0]));
		team[0].collectArmor(new HeavyArmor(team[0].getX(),team[0].getY(),plateau,team[0]));

		team[1].collectWeapon(new Bow(plateau,team[1]));
		team[1].collectArmor(new LightArmor(team[1].getX(),team[1].getY(),plateau,team[1]));

		team[2].collectWeapon(new Sword(plateau,team[2]));
		team[2].collectArmor(new MediumArmor(team[2].getX(),team[2].getY(),plateau,team[1]));
		team[2].collectHorse(new Horse(plateau, team[2]));

		team[3].collectWeapon(new Bible(plateau,team[3]));
		team[4].collectWeapon(new Balista(plateau,team[4]));


		for(int i = 0;i<9; i++){
			new Water(395f+32*i,570f,plateau);
		}
		for(int i = 0;i<3; i++){
			new Tree(368f,490f+32*i,plateau,4);
			new Tree(682f,490f+32*i,plateau,4);
		}
		new Tree(200f,400f,plateau,1);
		// Instantiate enemy generator :
		new EnemyGenerator(plateau,this,520f,100f);

		// Instantiate BottomBars for current player:
		this.bottomBars = new BottomBar(this.plateau,this.players.get(0),this);
		selection = null;
	}
	// Init our Game objects
	@Override
	public void init(GameContainer gc) throws SlickException 
	{	
		Image cursor = new Image("pics/cursor.png");
		
		gc.setMouseCursor(cursor.getSubImage(0, 0, 24, 64),5,16);
		mainMusic = new Music("music/ambiance.ogg");
		//mainMusic.setVolume(0.1f);
		//mainMusic.loop();
		this.musicMenu = new Music("music/intro_verdi.ogg");
		this.musicStartGame = new Music("music/nazi_start.ogg");
		this.players.add(new Player(0));
		this.players.add(new Player(1));
		this.sounds = new Sounds();
		this.images = new Images();
		this.plateau = new Plateau(this.constants,this.resX,4f/5f*this.resY,2,this);
		this.background =  new Image("pics/dirt.png");
		this.menuIntro = new MenuIntro(this);
		this.menuPause = new MenuPause(this);
		this.setMenu(menuIntro);


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
