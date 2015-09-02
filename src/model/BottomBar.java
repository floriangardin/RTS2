package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BottomBar extends Bar {
	
	SelectionInterface selection ;
	DescriptionInterface description;
	DisplayInterface display;
	
	public BottomBar(Plateau p ,Player player, Game g){
		this.p = p ;
		this.player = player;
		this.sizeX = g.resX;
		this.sizeY = 1f/5f*g.resY;
		this.x = 0;
		this.y = 4f/5f*g.resY;
		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
		this.display = new DisplayInterface(this);
	}
	
	public Graphics draw(Graphics g,float Xcam, float Ycam){
		// Draw Background :
		g.translate(Xcam, Ycam);
		float xt =x ;
		float yt = y;
		g.setColor(Color.black);
		g.fillRect(xt, yt,sizeX, sizeY);
		g.setColor(Color.white);
		g.fillRect(xt,yt,sizeX,1f);
		// Draw subcomponents :
		selection.draw(g);
		description.draw(g);
		display.draw(g);
		// Draw Separation (1/3 1/3 1/3) : 
		g.setColor(Color.white);
		g.fillRect(this.sizeX/3f-0.5f,this.y,1f,this.sizeY);
		g.fillRect(2f*this.sizeX/3f-0.5f,this.y,1f,this.sizeY);
		return g;
	}
	
	
}
