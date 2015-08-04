package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BottomBar extends Bar {
	
	SelectionInterface selection ;
	DescriptionInterface description;
	
	public BottomBar(Plateau p ,Player player, Game g){
		this.p = p ;
		this.player = player;
		this.sizeX = g.resX;
		this.sizeY = 1f/5f*g.resY;
		this.x = 0;
		this.y = 4f/5f*g.resY;
		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
	}
	
	
	public Graphics draw(Graphics g){
		// Draw Background : 
		g.setColor(Color.black);
		g.fillRect(x, y,sizeX, sizeY);
		// Draw subcomponents :
		selection.draw(g);
		description.draw(g);
		// Draw Separation (1/3 1/3 1/3) : 
		g.setColor(Color.white);
		g.fillRect(this.sizeX/3f-5f,this.y,10f,this.sizeY);
		g.fillRect(2f*this.sizeX/3f-5f,this.y,10f,this.sizeY);
		
		return g;
	}
	
	
}
