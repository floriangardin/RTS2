package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import units.Character;
import buildings.Building;
import model.Game;
import model.NaturalObjet;

public class PathInterface extends Bar {
	// Minimap caract
	public float startX;
	public float startY;
	public float w;
	public float h;
	public float rw;
	public float rh;
	public Game game;
	public boolean toDraw;


	public PathInterface(BottomBar parent){
		this.game = parent.p.g;
		this.p = parent.p;
		this.player = parent.player;
		this.startX = this.game.resX/4;
		this.startY = this.game.resY/4;
		this.w = this.game.resX/2;
		this.h = this.game.resY/2;
		rw = w/this.p.maxX;
		rh = h/this.p.maxY;
		this.toDraw = false;
	}

	public Graphics draw(Graphics g){
		// Draw the minimap 
		if(!toDraw){
			return g;
		}
		// Find the high left corner
		float hlx = Math.max(startX,startX+rw*this.p.Xcam);
		float hly = Math.max(startY,startY+rh*this.p.Ycam);
		float brx = Math.min(startX+w,startX+rw*(this.p.Xcam+this.p.g.resX));
		float bry = Math.min(startY+h,startY+rh*(this.p.Ycam+this.p.g.resY));
		// Find the bottom right corner

		// Draw background 
		g.setColor(new Color(0.1f,0.4f,0.1f));
		g.fillRect(startX, startY, w, h);
		// Draw water
		for(NaturalObjet q : p.naturalObjets){
			g.setColor(Color.cyan);
			g.fillRect(startX+rw*q.x-rw*q.sizeX/2f, startY+rh*q.y-rh*q.sizeY/2f,rw*q.sizeX , rh*q.sizeY);
		}
		
		g.setColor(Color.black);
		for(float f : this.game.plateau.mapGrid.Xcoord){
			g.drawLine(startX+rw*f, startY, startX+rw*f, startY+h);
		}
		for(float f : this.game.plateau.mapGrid.Ycoord){
			g.drawLine(startX, startY+rh*f, startX+w, startY+rh*f);
		}
		
		
		for(Building c : this.p.buildings){
			g.setColor(Color.gray);
			
			g.fillRect(startX+rw*c.x-rw*c.sizeX/2f, startY+rh*c.y-rh*c.sizeY/2f, rw*c.sizeX, rh*c.sizeY);
			
		}

		// Draw rect of camera 
		g.setColor(Color.white);

		g.drawRect(hlx,hly,brx-hlx,bry-hly );
		return g;
	}
}
