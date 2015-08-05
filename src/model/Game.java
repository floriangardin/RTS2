package model;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.tiled.*;
import org.newdawn.slick.Input;

public class Game extends BasicGame 
{	
	int team = 0;
	int step = 0;
	boolean displayUnit= true;
	public void changeDisplayUnit(){
		displayUnit = !displayUnit;
	}
	//Music 
	Music mainMusic ;
	//Sounds ;
	Sounds sounds;
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
	protected Menu menuPause;
	protected Menu menuMain;
	protected Menu menuCurrent = null;
	public boolean isInMenu = false;
	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
	}
	public void setMenu(Menu m){
		this.menuCurrent = m;
		this.isInMenu = true;
		m.printDebug();
	}


	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//Utils.triY1(this.plateau.characters);

		// g représente le pinceau
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


		g.setColor(Color.black);
		g.fill(new Rectangle(0,0,90,30));

		// Draw bottom bar
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
			if(menuCurrent==menuPause && i.isKeyPressed(org.newdawn.slick.Input.KEY_ESCAPE)){
				this.quitMenu();
				return;
			}
			if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				menuCurrent.callItems(i);			
			return;
		}
		
		if(!isInMenu && i.isKeyPressed(org.newdawn.slick.Input.KEY_ESCAPE)){
			this.setMenu(menuPause);
			return;
		}
		
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
	// Init our Game objects
	@Override
	public void init(GameContainer gc) throws SlickException 
	{	
		//mainMusic = new Music("music/background.ogg");
		//mainMusic.setVolume(0.1f);
		//mainMusic.loop();
		this.players.add(new Player(0));
		this.players.add(new Player(1));
		this.sounds = new Sounds();
		plateau = new Plateau(this.constants,this.resX,4f/5f*this.resY,2,this);
		this.background =  new Image("pics/dirt.png");
		//Instantiate allies
		for(int i=0;i<5;i++){		
			new Character(this.plateau,0,500f+10f*i,500f);
		}
		for(int i = 0;i<9; i++){
			new Water(395f+32*i,570f,plateau);
		}
		for(int i = 0;i<3; i++){
			new Tree(368f,490f+32*i,plateau,4);
			new Tree(682f,490f+32*i,plateau,4);
		}

		// Instantiate BottomBars for current player:
		this.bottomBars = new BottomBar(this.plateau,this.players.get(0),this);
		selection = null;
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
		this.menuPause = new MenuPause(this);
	}
}
