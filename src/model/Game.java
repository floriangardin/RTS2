package model;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.tiled.*;

public class Game extends BasicGame 
{	
	int team = 0;
	Constants constants ;
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
		g.setColor(Color.white);
		g.fillRect(0,0,gc.getScreenWidth(),gc.getScreenHeight());

		// Draw the plateau
		for(ActionObjet o : actionObjets){
			o.draw(g);
			if (this.plateau.isSelected(o)){
				g.setColor(Color.green);
				g.draw(new Circle(o.getX(),o.getY(),o.collisionBox.getBoundingCircleRadius()));
			}
		}

		// Draw the selection :
		if(this.selection !=null){
			g.setColor(Color.green);
			g.draw(this.selection);

		}
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
		plateau = new Plateau(this.constants,this.resX,this.resY,2);
		Character c = new Character(plateau,0,100,100);
		plateau.addActionsObjets(c);
		this.actionObjets = plateau.actionsObjets;
		//new Ennemi(plateau,100f,100f,framerate);
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
