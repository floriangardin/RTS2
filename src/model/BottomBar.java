package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class BottomBar extends Bar {

	SelectionInterface selection ;
	DescriptionInterface description;
	DisplayInterface display;
	
	
	// Minimap caract
	float startX;
	float startY;
	float w;
	float h;
	float rw;
	float rh;
	
	// Production Bar
	Building buildingToShow;
	float prodX;
	float prodY;
	float prodW;
	float prodH;
	int prodIconNb = 4;
	float icoSizeX;
	float icoSizeY;
	
	public BottomBar(Plateau p ,Player player, int resX, int resY){
		this.p = p ;
		this.player = player;
		this.player.bottomBar = this;

		try {
			this.background = new Image("pics/bottombar.jpg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.buildingToShow = null;
		this.update(resX, resY);
	}

	public void update(int resX, int resY){
		this.sizeX = resX;
		this.sizeY = this.p.g.relativeHeightBottomBar*resY;
		this.x = 0;
		this.y = (1f-this.p.g.relativeHeightBottomBar)*resY;
		w = this.sizeX/6f;
		h = this.sizeY-2f;
		rw = w/this.p.maxX;
		rh = h/this.p.maxY;
		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
		this.display = new DisplayInterface(this);
		
		this.prodX = 2.0f*this.sizeX/3f-1f;
		this.prodY = this.y+1f;
		this.prodW = 0.5f*this.sizeX;
		this.prodH = this.sizeY-2f ; 
		this.icoSizeX = this.prodW/5f;
		this.icoSizeY = this.prodH/3f;
		
		this.startX = 2.5f*this.sizeX/3f-1f;
		this.startY = this.y+1f;
	}
	
	
	public Graphics draw(Graphics g){
		// Draw Background :

		float xt =x ;
		float yt = y;
		g.setColor(Color.black);
		g.fillRect(xt, yt,sizeX, sizeY);
		// Draw image according to size
		float u = x;
		float v = y;
		while(u<sizeX){
			g.drawImage(this.background,u,y);
			u+=680f;
		}

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
		
		
		// Draw the minimap 

		// Find the high left corner
		float hlx = Math.max(startX,startX+rw*this.p.Xcam);
		float hly = Math.max(startY,startY+rh*this.p.Ycam);
		float brx = Math.min(startX+w,startX+rw*(this.p.Xcam+this.p.g.resX));
		float bry = Math.min(startY+h,startY+rh*(this.p.Ycam+this.p.g.resY));
		// Find the bottom right corner

		// Draw background 
		g.setColor(Color.white);
		g.fillRect(startX, startY, w, h);
		// Draw units on camera :
		
		for(Character c : this.p.characters){		
			if(c.team==2){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.red);
					g.fillRect(startX+rw*c.x, startY+rh*c.y, 3f, 3f);
				}
			}
			else if(c.team==1){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.blue);
					g.fillRect(startX+rw*c.x, startY+rh*c.y, 3f, 3f);
				}
			}
		}
		for(Building c : this.p.buildings){		
			if(c.team==2){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.red);
					g.fillOval(startX+rw*c.x, startY+rh*c.y, 4f, 4f);
				}
			}
			else if(c.team==1){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.blue);
					g.fillOval(startX+rw*c.x, startY+rh*c.y, 4f, 4f);
				}
			}
		}

		// Draw rect of camera 
		g.setColor(Color.green);

		g.drawRect(hlx,hly,brx-hlx,bry-hly );
		
		
		// Draw Production/Effect Bar
		if(this.player.selection.size()>0 && this.player.selection.get(0) instanceof BuildingProduction){
			BuildingProduction b =(BuildingProduction) this.player.selection.get(0);
			//Print building capacities
			Vector<UnitsList> ul = b.productionList;
			Font f = g.getFont();
			float ratio =1f/prodIconNb;
			for(int i=0; i<Math.min(4, ul.size());i++){ 
				g.drawImage(this.p.images.getIconByName(ul.get(i).name), prodX+2f, prodY+2f + ratio*i*prodH, prodX-2f+ratio*prodH, prodY-2f+ratio*(i+1)*prodH, 0, 0, 512,512);
				g.setColor(Color.white);
				g.drawRect(prodX, prodY + ratio*i*sizeY, prodW, ratio*prodH);
				g.setColor(Color.black);
				g.drawString(ul.get(i).name, prodX + ratio*prodH+10f, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
			}
		}

		return g;
	}


}
