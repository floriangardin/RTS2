package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TopBar extends Bar {
	
	
	Image imageGold ;
	Image imageFood;
	
	public TopBar(Plateau p ,Player player, int resX, int resY){
		
		this.p = p ;
		this.player = player;
		this.player.topBar = this;
		this.sizeX = resX;
		this.sizeY = 1f/20f*resY;
		this.x = 0f;
		this.y = 0f;
		try {
			int taille = 24;
			this.imageGold = new Image("pics/ressources.png").getSubImage(7*taille ,15*taille ,taille, taille);
			this.imageFood = new Image("pics/ressources.png").getSubImage(7*taille, taille, taille, taille);
			this.background = new Image("pics/bottombar.jpg").getSubImage(0,0,683,(int) sizeY);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Graphics draw(Graphics g){
		// Draw Background :
		//g.translate(Xcam, Ycam);
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

		// Draw Ressources
		g.drawImage(this.imageFood, 3*this.sizeX/5,(this.sizeY-24)/2);
		// Draw number of ressources
		g.drawString(": "+this.player.food, 3.1f*this.sizeX/5,(this.sizeY-24)/2);
		g.drawImage(this.imageGold, 4*this.sizeX/5,(this.sizeY-24)/2);
		g.drawString(": "+this.player.gold, 4.1f*this.sizeX/5,(this.sizeY-24)/2);
		// Draw separation 
		g.setColor(Color.white);
		g.fillRect(xt,this.sizeY,sizeX,1f);
		return g;
	}
	
	
}
