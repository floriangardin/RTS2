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
	Image imagePop;
	public TopBar(Plateau p , int resX, int resY){
		
		this.p = p ;
		this.update(resX, resY);
		try {
			int taille = 24;
			this.imageGold = new Image("pics/ressources.png").getSubImage(7*taille ,15*taille ,taille, taille);
			this.imageFood = new Image("pics/ressources.png").getSubImage(7*taille, taille, taille, taille);
			this.imageSpecial = new Image("pics/faith.png");
			this.imagePop = new Image("pics/pop.png").getScaledCopy(32, 32);
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
		g.fillRect(0, -3f,sizeX, sizeY+6f);
		g.setColor(Color.black);
		g.fillRect(0, 0, sizeX, sizeY);
		//g.fillRect(3*sizeX/5, sizeY, 2*sizeX/5, -3f);
		

		g.setColor(Color.white);
		g.drawString(model.Utils.gameTime(this.p.g.startTime), this.sizeX/3, (this.sizeY-28)/2);
		// Draw subcomponents :

		String food = ":";
		if(this.p.g.currentPlayer.getGameTeam().food<10)
			food+="0";
		if(this.p.g.currentPlayer.getGameTeam().food<100)
			food+="0";
		if(this.p.g.currentPlayer.getGameTeam().food<1000)
			food+="0";
		String gold = ":";
		if(this.p.g.currentPlayer.getGameTeam().gold<10)
			gold+="0";
		if(this.p.g.currentPlayer.getGameTeam().gold<100)
			gold+="0";
		if(this.p.g.currentPlayer.getGameTeam().gold<1000)
			gold+="0";
		String special = ":";
		if(this.p.g.currentPlayer.getGameTeam().special<10)
			special+="0";
		if(this.p.g.currentPlayer.getGameTeam().special<100)
			special+="0";
		if(this.p.g.currentPlayer.getGameTeam().special<1000)
			special+="0";
		String pop = ":";
		if(this.p.g.currentPlayer.getGameTeam().pop<10)
			pop+="0";
		float sizefood = this.p.g.font.getWidth(food);
		float sizegold = this.p.g.font.getWidth(gold);
		float sizespecial = this.p.g.font.getWidth(special);
		float sizepop = this.p.g.font.getWidth(pop);
		

		// Draw Ressources
		g.drawImage(this.imageFood, 11.2f*this.sizeX/16,(this.sizeY-24)/2);
		g.drawString(":", 11.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+this.p.g.currentPlayer.getGameTeam().food, 11.5f*this.sizeX/16+sizefood,(this.sizeY-28)/2);
		g.drawImage(this.imageGold, 12.2f*this.sizeX/16,(this.sizeY-24)/2);
		g.drawString(":", 12.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+this.p.g.currentPlayer.getGameTeam().gold, 12.5f*this.sizeX/16+sizegold,(this.sizeY-28)/2);
		g.drawImage(this.imageSpecial, 13.2f*this.sizeX/16,(this.sizeY-32)/2);
		g.drawString(":", 13.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+this.p.g.currentPlayer.getGameTeam().special, 13.5f*this.sizeX/16+sizespecial,(this.sizeY-28)/2);
		g.drawImage(this.imagePop, 14.2f*this.sizeX/16,(this.sizeY-32)/2);
		g.drawString(":", 14.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+this.p.g.currentPlayer.getGameTeam().pop+"/"+this.p.g.currentPlayer.getGameTeam().maxPop, 14.5f*this.sizeX/16+sizepop,(this.sizeY-28)/2);
		// Draw separation 

		return g;
	}
	
	
}
