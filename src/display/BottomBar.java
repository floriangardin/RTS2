package display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class BottomBar extends Bar {

	public SelectionInterface selection ;
	public DescriptionInterface description;
	public DisplayInterface display;
	public MinimapInterface minimap;
	public ActionInterface action;
	public PathInterface path;

	public BottomBar(Plateau p ,Player player, int resX, int resY){
		this.p = p ;
		this.player = player;
		this.player.bottomBar = this;

		try {
			this.background = new Image("pics/menu/bottombar.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.update(resX, resY);


	}

	public void update(int resX, int resY){
		this.sizeX = resX;
		this.sizeY = this.p.g.relativeHeightBottomBar*resY;
		this.x = 0;
		this.y = (1f-this.p.g.relativeHeightBottomBar)*resY;

		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
		this.display = new DisplayInterface(this);
		this.minimap = new MinimapInterface(this);
		this.action = new ActionInterface(this);
		this.path = new PathInterface(this);
	}

	public Graphics draw(Graphics g){
		// Draw Background :

		
		// Draw image according to size

		//g.drawImage(this.background,x,y-6f);

		
		// Draw subcomponents :
		selection.draw(g);
		//description.draw(g);
		display.draw(g);

		// MINIMAP CENTERED :
		minimap.draw(g);
		
		// ACTIONS, Spells  and production
		action.draw(g);
		
		// Draw path 
		
		path.draw(g);
		
		return g;
	}


}
