package model;

import org.newdawn.slick.Color;
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
	
	public BottomBar(Plateau p ,Player player, int resX, int resY){
		this.p = p ;
		this.player = player;
		this.player.bottomBar = this;
		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
		this.display = new DisplayInterface(this);
		try {
			this.background = new Image("pics/bottombar.jpg");
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
		w = this.sizeX/6f;
		h = this.sizeY-2f;
		rw = w/this.p.maxX;
		rh = h/this.p.maxY;
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
			
			if(c.team!=this.player.team){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){

					g.setColor(Color.red);
					g.fillRect(startX+rw*c.x, startY+rh*c.y, 3f, 3f);
				}
			}
			else{
				g.setColor(Color.blue);
				g.fillRect(startX+rw*c.x, startY+rh*c.y, 3f, 3f);
			}

		}

		// Draw rect of camera 
		g.setColor(Color.green);

		g.drawRect(hlx,hly,brx-hlx,bry-hly );


		return g;
	}


}
