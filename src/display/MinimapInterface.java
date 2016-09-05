package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import bonus.Bonus;
import data.Attributs;
import model.Building;
import model.Character;
import model.Colors;
import model.Game;
import model.NaturalObjet;
import pathfinding.Case;
import utils.Utils;

public class MinimapInterface extends Bar {
	// Minimap caract
	public float startX, startX2, offsetDrawX;
	public float startY, startY2;
	public float w;
	public float h;
	public float rw;
	public float rh;
	public boolean toDraw;
	
	public static boolean todrawGrid = true;

	private float debutGlissade = Game.nbRoundInit/4;
	private float dureeGlissade = Game.nbRoundInit/4;
	
	// Extra params __ Debug
	public float x1,y1,x2,y2;
	public boolean isVisibleLine, isPossibleLine;
	Vector<Case> cases;

	public MinimapInterface(BottomBar parent){

		

		this.player = parent.player;
		this.startX2 = Game.g.resX*(1-parent.ratioMinimapX)+3;
		this.startY2 = Game.g.resY-parent.ratioMinimapX*Game.g.resX+3;
		this.sizeX = Game.g.resX*parent.ratioMinimapX-6;
		this.sizeY = sizeX;
		if(Game.g.plateau.maxX>Game.g.plateau.maxY){
			this.w = this.sizeX;
			this.h = this.w*Game.g.plateau.maxY/Game.g.plateau.maxX;
			this.startX = this.startX2;
			this.startY = this.startY2 + (this.sizeY-h)/2;
		} else {
			this.h = this.sizeY;			
			this.w = this.h*Game.g.plateau.maxX/Game.g.plateau.maxY;
			this.startX = this.startX2 + (this.sizeX-w)/2;
			this.startY = this.startY2;
		}
		rw = w/Game.g.plateau.maxX;
		rh = h/Game.g.plateau.maxY;
		this.toDraw = false;
	}
	
	
	public void updateRatio(){
		
		if(Game.g.plateau.maxX>Game.g.plateau.maxY){
			this.w = this.sizeX;
			this.h = this.w*Game.g.plateau.maxY/Game.g.plateau.maxX;
			this.startX = this.startX2;
			this.startY = this.startY2 + (this.sizeY-h)/2;
		} else {
			this.h = this.sizeY;			
			this.w = this.h*Game.g.plateau.maxX/Game.g.plateau.maxY;
			this.startX = this.startX2 + (this.sizeX-w)/2;
			this.startY = this.startY2;
		}
		rw = w/Game.g.plateau.maxX;
		rh = h/Game.g.plateau.maxY;
	}

	public Graphics draw(Graphics g){
		this.offsetDrawX = Math.max(0, Math.min(sizeX+10, -sizeX*(Game.g.round-debutGlissade-dureeGlissade)/dureeGlissade));
		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color,startX2+offsetDrawX-3, startY2-3, sizeX+9, sizeY+9);
		g.setColor(Color.black);
		g.fillRect(this.startX2+offsetDrawX, this.startY2, this.sizeX, this.sizeY);
		// Find the high left corner
		float hlx = Math.max(startX,startX+rw*Game.g.Xcam);
		float hly = Math.max(startY,startY+rh*Game.g.Ycam);
		float brx = Math.min(startX+w,startX+rw*(Game.g.Xcam+Game.g.resX));
		float bry = Math.min(startY+h,startY+rh*(Game.g.Ycam+Game.g.resY));
		// Find the bottom right corner

		// Draw background
		g.setColor(new Color(0.1f,0.4f,0.1f));
		g.drawImage(Game.g.images.get("islandTexture"),startX+offsetDrawX, startY, startX+offsetDrawX+w, startY+h,0,0,Game.g.images.get("islandTexture").getWidth(),Game.g.images.get("islandTexture").getHeight());
		for(NaturalObjet q : Game.g.plateau.naturalObjets){
			g.setColor(Color.green);
			g.fillRect(startX+offsetDrawX+rw*q.x-rw*q.sizeX/2f, startY+rh*q.y-rh*q.sizeY/2f,rw*q.sizeX , rh*q.sizeY);
		}
		// Draw units on camera 
		g.setAntiAlias(true);
		for(Character c : Game.g.plateau.characters){		
			if(c.getTeam()==2){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team2);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startX+offsetDrawX+rw*c.x-rw*r, startY+rh*c.y-rh*r, 2f*rw*r, 2f*rh*r);
				}
			}
			else if(c.getTeam()==1){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team1);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startX+offsetDrawX+rw*c.x-rw*r, startY+rh*c.y-rh*r, 2f*rw*r, 2f*rh*r);
				}
			}
		}
		
		
		for(Bonus c : Game.g.plateau.bonus){
			if(c.getTeam()==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam()==2){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam()==1){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillOval(startX+offsetDrawX+rw*(c.x-c.getAttribut(Attributs.size)/2f), 
					startY+rh*(c.y-c.getAttribut(Attributs.size)/2f), 
					rw*c.getAttribut(Attributs.size), 
					rh*c.getAttribut(Attributs.size));
		}
		g.setAntiAlias(false);
		for(Building c : Game.g.plateau.buildings){
			if(c.getTeam()==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam()==2){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c) || Game.debugFog){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam()==1){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c) || Game.debugFog){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillRect(startX+offsetDrawX+rw*c.x-rw*c.getAttribut(Attributs.sizeX)/2f, startY+rh*c.y-rh*c.getAttribut(Attributs.sizeY)/2f, rw*c.getAttribut(Attributs.sizeX), rh*c.getAttribut(Attributs.sizeY));
			
			if(c.constructionPoints<c.getAttribut(Attributs.maxLifepoints) && (Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c) || Game.debugFog)){
				float ratio = c.constructionPoints/c.getAttribut(Attributs.maxLifepoints);
				if(c.potentialTeam==1){
					g.setColor(Colors.team1);
				}
				else if(c.potentialTeam==2){
					g.setColor(Colors.team2);
				}
				g.fillRect(startX+offsetDrawX+rw*c.x-rw*c.getAttribut(Attributs.sizeX)/2f, startY+rh*c.y-rh*c.getAttribut(Attributs.sizeY)/2f, ratio*(rw*c.getAttribut(Attributs.sizeX)), rh*c.getAttribut(Attributs.sizeY));
			}
		}
		
		// Draw rect of camera 
		g.setColor(Color.white);
		g.drawRect(hlx+offsetDrawX,hly,brx-hlx,bry-hly );
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
