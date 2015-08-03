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
	// Then we declare the plateau
	private Plateau plateau ;
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		// g représente le pinceau
		g.setColor(Color.black);
		g.fillRect(0,0,gc.getScreenWidth(),gc.getScreenHeight());

		// Draw the Action Objets
		for(Character o : plateau.characters){
			if(this.displayUnit)
				o.draw(g);
		}
		for(ActionObjet o : plateau.equipments){
			o.draw(g);
		}
		for(Bullet o : plateau.bullets){
			o.draw(g);
		}
		// Draw the natural Objets

		for(NaturalObjet o : this.plateau.naturalObjets){
			o.draw(g);
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

		// Draw the selection :
		if(this.selection !=null){
			g.setColor(Color.green);
			g.draw(this.selection);

		}
		g.setColor(Color.black);
		g.fill(new Rectangle(0,0,90,30));
		// Draw the selection of your team 
		for(Character o: plateau.selection.get(team)){
			o.drawIsSelected(g);

		}
	}
	// Do our logic 
	@Override
	public void update(GameContainer gc, int t) throws SlickException 
	{	
		// Get the input from the usr
		Input i = gc.getInput();

		// Update the selection rectangle :
		// Test if new selection :
		if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			this.plateau.clearSelection(team);
		}
		
		if(i.isKeyPressed(org.newdawn.slick.Input.KEY_0)){
			
			this.changeDisplayUnit();
		}

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
			plateau.updateSelection(selection, this.team);
			this.selection = null;
		}
		else{
			// We update selection when left click is released
			selection = null;
		}
		// Action for player k
		if(i.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			this.plateau.updateTarget(i.getMouseX(),i.getMouseY(),this.team);
		}
		// Perform the collision, the creation and destruction of elements.

		plateau.update();
		// Perform the actions;
		plateau.action();
		
	}
	// Init our Game objects
	@Override
	public void init(GameContainer gc) throws SlickException 
	{	
		plateau = new Plateau(this.constants,this.resX,this.resY,2,this);

		for(int i=0;i<5;i++){
			for(int j=0;j<2;j++){
				new Character(plateau,0,100+10*i,100+10*j);
				new Character(plateau,1,750+10*i,100+10*j);
			}

		}
		for(int i=0;i<plateau.toAddCharacters.size();i++){
			plateau.toAddCharacters.get(i).collectArmor(new LightArmor(0f, 0f, plateau, plateau.toAddCharacters.get(i)));
			plateau.toAddCharacters.get(i).collectWeapon(new Bow(plateau,plateau.toAddCharacters.get(i)));
		}
		//Rock r = new Rock(200,200,plateau);
		//Water w = new Water(200,250,plateau);
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
	}
}
