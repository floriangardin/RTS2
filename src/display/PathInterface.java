package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
	public boolean toDraw;


	public PathInterface(BottomBar parent){
		this.p = parent.p;
		this.player = parent.player;
		this.startX = Game.g.resX/4;
		this.startY = Game.g.resY/4;
		this.w = Game.g.resX/2;
		this.h = Game.g.resY/2;
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
		float hlx = Math.max(startX,startX+rw*Game.g.Xcam);
		float hly = Math.max(startY,startY+rh*Game.g.Ycam);
		float brx = Math.min(startX+w,startX+rw*(Game.g.Xcam+Game.g.resX));
		float bry = Math.min(startY+h,startY+rh*(Game.g.Ycam+Game.g.resY));
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
		for(float f : Game.g.plateau.mapGrid.Xcoord){
			g.drawLine(startX+rw*f, startY, startX+rw*f, startY+h);
		}
		for(float f : Game.g.plateau.mapGrid.Ycoord){
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
