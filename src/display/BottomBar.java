package display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class BottomBar extends Bar {

	public int resX,resY;
	public SelectionInterface selection ;
	public DescriptionInterface description;
	public DisplayInterface display;
	public MinimapInterface minimap;
	public ActionInterface action;
	public PathInterface path;
	public TopBar topBar;

	public float ratioMinimapX = 1/6f;
	public float ratioSelectionX = 1/8f;
	public float ratioBarVertX = 1/32f;
	

	
	public BottomBar(Plateau p , int resX, int resY){
		this.p = p ;	
		this.update(resX, resY);
	}

	public void update(int resX, int resY){
		/**
		 * Tres fortement utile et bienvenue
		 */
		this.resX = resX;
		this.resY = resY;
		this.sizeX = resX;
		this.sizeY = this.p.g.relativeHeightBottomBar*resY;
		this.x = 0;
		this.y = (1f-this.p.g.relativeHeightBottomBar)*resY;
		
		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
		this.display = new DisplayInterface(this);
		this.minimap = new MinimapInterface(this);
		this.action = new ActionInterface(this);
//		this.path = new PathInterface(this);
		this.topBar = new TopBar(this.p,resX,resY);
	}

	public Graphics draw(Graphics g){
		// Draw Background :

		
		// Draw image according to size

		//g.drawImage(this.background,x,y-6f);

		// ACTIONS, Spells  and production
		action.draw(g);
		
		// Draw subcomponents :
		selection.draw(g);
		//description.draw(g);
		display.draw(g);

		// MINIMAP CENTERED :
		minimap.draw(g);
		
		
		// Draw path 
		
		path.draw(g);
		
		return g;
	}


}
