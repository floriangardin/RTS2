package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import buildings.Building;
import model.Game;
import model.NaturalObjet;
import pathfinding.Case;
import units.Character;

public class MinimapInterface extends Bar {
	// Minimap caract
	public float startX;
	public float startY;
	public float w;
	public float h;
	public float rw;
	public float rh;
	public Game game;
	public boolean toDraw;

	
	// Extra params __ Debug
	public float x1,y1,x2,y2;
	public boolean isVisibleLine, isPossibleLine;
	Vector<Case> cases;

	public MinimapInterface(BottomBar parent){
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
	
	public void update(Game game){
		this.game = game;
		this.p = game.plateau;
		rw = w/this.p.maxX;
		rh = h/this.p.maxY;
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
		// Draw units on camera 

		for(Character c : this.p.characters){		
			if(c.getTeam()==2){
				if(this.p.isVisibleByPlayer(this.p.currentPlayer.getTeam(), c)){
					g.setColor(Color.red);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startX+rw*c.x-rw*r, startY+rh*c.y-rh*r, 2f*rw*r, 2f*rh*r);
				}
			}
			else if(c.getTeam()==1){
				if(this.p.isVisibleByPlayer(this.p.currentPlayer.getTeam(), c)){
					g.setColor(Color.blue);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startX+rw*c.x-rw*r, startY+rh*c.y-rh*r, 2f*rw*r, 2f*rh*r);
				}
			}
		}
		for(Building c : this.p.buildings){
			if(c.getTeam()==0){
				g.setColor(Color.gray);

			}
			if(c.getTeam()==2){
				if(this.p.isVisibleByPlayer(this.p.currentPlayer.getTeam(), c)){
					g.setColor(Color.red);
				} else {
					g.setColor(Color.gray);

				}
			}
			else if(c.getTeam()==1){
				if(this.p.isVisibleByPlayer(this.p.currentPlayer.getTeam(), c)){
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.gray);

				}
			}
			g.fillRect(startX+rw*c.x-rw*c.sizeX/2f, startY+rh*c.y-rh*c.sizeY/2f, rw*c.sizeX, rh*c.sizeY);
			
			if(c.constructionPoints<c.maxLifePoints && this.p.isVisibleByPlayer(this.p.currentPlayer.getTeam(), c)){
				float ratio = c.constructionPoints/c.maxLifePoints;
				if(c.potentialTeam==1){
					g.setColor(Color.blue);
				}
				else if(c.potentialTeam==2){
					g.setColor(Color.red);
				}
				g.fillRect(startX+rw*c.x-rw*c.sizeX/2f, startY+rh*c.y-rh*c.sizeY/2f, ratio*(rw*c.sizeX), rh*c.sizeY);
			}
		}
//		g.setColor(Color.black);
//		for(float f : this.game.plateau.mapGrid.Xcoord){
//			g.drawLine(startX+rw*f, startY, startX+rw*f, startY+h);
//		}
//		for(float f : this.game.plateau.mapGrid.Ycoord){
//			g.drawLine(startX, startY+rh*f, startX+w, startY+rh*f);
//		}
//		if(this.p.currentPlayer.selection!=null && this.p.currentPlayer.selection.size()>0){
//			if(this.p.currentPlayer.selection.get(0) instanceof Character){
//				
//				Character roger = (Character) this.p.currentPlayer.selection.get(0);
//				g.setColor(Color.red);
//				g.drawString(this.p.mapGrid.maxX+" "+this.p.mapGrid.maxY, 0, 20);
//				g.setColor(Color.cyan);
//				g.drawRect(roger.c.x*rw+startX, roger.c.y*rh+startY, roger.c.sizeX*rw, roger.c.sizeY*rh);
//			}
//		}
//		if(isVisibleLine){
//			if(isPossibleLine)
//				g.setColor(Color.green);
//			else
//				g.setColor(Color.red);
//			g.drawLine(startX+rw*x1, startY+rh*y1, startX+rw*x2, startY+rh*y2);
//			for(Case c: cases){
//				g.drawRect(startX+rw*c.x, startY+rh*c.y, rw*c.sizeX, rh*c.sizeY);
//			}
//		}
		// Draw rect of camera 
		g.setColor(Color.white);

		g.drawRect(hlx,hly,brx-hlx,bry-hly );
		return g;
	}

//	public void createRandomLine(){
//		x1 = (float)(Math.random()*game.plateau.maxX);
//		x2 = (float)(Math.random()*game.plateau.maxX);
//		y1 = (float)(Math.random()*game.plateau.maxY);
//		y2 = (float)(Math.random()*game.plateau.maxY);
//		cases = game.plateau.mapGrid.isLineOk(x1, y1, x2, y2);
//		isPossibleLine = (cases.size()>0);
//		isVisibleLine = true;
//	}
}
