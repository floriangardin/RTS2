package display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Game;
import model.Plateau;
import model.Player;

public class BottomBar extends Bar {

	public int resX,resY;
	public SelectionInterface selection ;
	public DisplayInterface display;
	public MinimapInterface minimap;
	public ActionInterface action;
	public PathInterface path;
	public TopBar topBar;
	public SpellInterface spell;

	public float ratioMinimapX = 1/6f;
	public float ratioSelectionX = 1/8f;
	public float ratioSpellX = 1/12f;
	public float ratioBarVertX = 1/32f;
	

	
	public BottomBar(){
		
		this.update(resX, resY);
	}

	public void update(int resX, int resY){
		/**
		 * Tres fortement utile et bienvenue
		 */
		this.resX = (int) Game.g.resX;
		this.resY = (int) Game.g.resY;
		this.sizeX = resX;
		this.sizeY = Game.g.relativeHeightBottomBar*resY;
		this.x = 0;
		this.y = (1f-Game.g.relativeHeightBottomBar)*resY;
		
		this.selection = new SelectionInterface(this);
		this.display = new DisplayInterface(this);
		this.minimap = new MinimapInterface(this);
		this.action = new ActionInterface(this);
		this.spell = new SpellInterface(this);
//		this.path = new PathInterface(this);
		this.topBar = new TopBar();
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
		
		spell.draw(g);
		
		
		
		return g;
	}


}
