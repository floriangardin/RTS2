package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class TopBar extends Bar {
	
	
	Image imageGold ;
	Image imageFood;
	Image imageSpecial;
	public TopBar(Plateau p ,Player player, int resX, int resY){
		
		this.p = p ;
		this.player = player;
		this.player.topBar = this;
		this.update(resX, resY);
		try {
			int taille = 24;
			this.imageGold = new Image("pics/ressources.png").getSubImage(7*taille ,15*taille ,taille, taille);
			this.imageFood = new Image("pics/ressources.png").getSubImage(7*taille, taille, taille, taille);
			this.imageSpecial = new Image("pics/arrow.png");
			//this.background = new Image("pics/menu/bottombar.png").getSubImage(0,626-(int)sizeY,1680,(int) sizeY);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void update(int resX, int resY){

		this.sizeX = resX;
		this.sizeY = this.p.g.relativeHeightTopBar*resY;
		this.x = 0f;
		this.y = 0f;
	}
	
	public Graphics draw(Graphics g){
		// Draw Background :
		//g.translate(Xcam, Ycam);
		float xt =x ;
		float yt = y;

		// Draw image according to size
		float u = x;
		float v = y;

		g.setColor(Color.white);
		g.drawString(model.Utils.gameTime(this.p.g.startTime), this.sizeX/6, (this.sizeY-28)/2);

		// Draw subcomponents :

		// Draw Ressources
		g.drawImage(this.imageFood, 5*this.sizeX/8,(this.sizeY-24)/2);
		g.drawString(": "+this.player.food, 5.2f*this.sizeX/8,(this.sizeY-28)/2);
		g.drawImage(this.imageGold, 6*this.sizeX/8,(this.sizeY-24)/2);
		g.drawString(": "+this.player.gold, 6.2f*this.sizeX/8,(this.sizeY-28)/2);
		g.drawImage(this.imageSpecial, 7*this.sizeX/8,(this.sizeY-32)/2);
		g.drawString(": "+this.player.special, 7.2f*this.sizeX/8,(this.sizeY-28)/2);
		// Draw separation 

		return g;
	}
	
	
}
