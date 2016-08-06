package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import model.Game;
import model.Plateau;
import utils.Utils;

public class TopBar extends Bar {


	Image imageGold ;
	Image imageFood;
	Image imageSpecial;
	Image imagePop;
	Image imageTimer;

	private int gold, food;

	public float ratioSizeGoldX = 1/13f;
	public float ratioSizeTimerX = 1/12f;
	public float ratioSizeGoldY = 1/22f;
	public float ratioSizeTimerY = 1/16f;
	
	private float debutC = Game.nbRoundInit/4;
	private float debut1 = 3*Game.nbRoundInit/8;
	private float debut2 = Game.nbRoundInit/2;
	private float dureeDescente = Game.nbRoundInit/8;

	public TopBar(Plateau p , int resX, int resY){

		this.p = p ;
		this.update(resX, resY);
		this.imageGold = Game.g.images.get("imagegolddisplayressources");
		this.imageFood = Game.g.images.get("imagefooddisplayressources");
		this.imageSpecial = Game.g.images.get("imageSpecial");
		this.imagePop = Game.g.images.get("imagePop");
	}

	public void update(int resX, int resY){

		this.sizeX = resX;
		this.sizeY = Game.g.relativeHeightTopBar*resY;
		this.x = 0f;
		this.y = 0f;
	}

	public Graphics draw(Graphics g){
		String s;
		float rX = Game.g.resX;
		float rY = Game.g.resY;
		float offset = ratioSizeTimerY*rY;
		float yCentral = Math.max(-offset-10,Math.min(0, offset*(Game.g.round-debutC-dureeDescente)/dureeDescente));
		offset = ratioSizeGoldY*rY;
		float y1 = Math.max(-offset-10,Math.min(0, offset*(Game.g.round-debut1-dureeDescente)/dureeDescente));
		float y2 = Math.max(-offset-10,Math.min(0, offset*(Game.g.round-debut2-dureeDescente)/dureeDescente));
		
		if(gold != Game.g.currentPlayer.getGameTeam().gold)
			gold += (Game.g.currentPlayer.getGameTeam().gold-gold)/5+Math.signum(Game.g.currentPlayer.getGameTeam().gold-gold);
		if(food != Game.g.currentPlayer.getGameTeam().food)
			food += (Game.g.currentPlayer.getGameTeam().food-food)/5+Math.signum(Game.g.currentPlayer.getGameTeam().food-food);
		

		// pop
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX,y2,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+Game.g.currentPlayer.getGameTeam().pop + "/" + Game.g.currentPlayer.getGameTeam().maxPop;
		if(Game.g.currentPlayer.getGameTeam().pop==Game.g.currentPlayer.getGameTeam().maxPop){
			g.setColor(Color.red);
		}else{
			g.setColor(Color.white);
		}
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y2+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imagePop, (1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX+10, y2+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// food
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+food;
		g.setColor(Color.white);
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-10f-Game.g.font.getWidth(s), y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageFood, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// faith
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-4,y2,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+Game.g.currentPlayer.getGameTeam().special;
		g.setColor(Color.white);
		g.drawString(s, (1+ratioSizeTimerX)*rX/2+2*ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y2+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageSpecial, (1+ratioSizeTimerX)*rX/2+10+ratioSizeGoldX*rX, y2+ratioSizeGoldY*rY/2f-3-this.imageGold.getHeight()/2);

		// gold
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1+ratioSizeTimerX)*rX/2-4,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+gold;
		g.setColor(Color.white);
		g.drawString(s, (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageGold, (1+ratioSizeTimerX)*rX/2+10, y1+ratioSizeGoldY*rY/2f-3-this.imageGold.getHeight()/2);

		// timer
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2,yCentral,ratioSizeTimerX*rX,ratioSizeTimerY*rY);

		s = ""+Utils.gameTime(Game.g.startTime);
		g.setColor(Color.white);
		g.drawString(s, rX/2-Game.g.font.getWidth(s)/2, yCentral+ratioSizeTimerY*rY/2f-20);


		//		g.drawImage(this.imageTimer, 3*Game.g.resX/7, 0);
		//		g.drawImage(this.imageFood, 4*Game.g.resX/7, 0);
		//		g.setColor(Color.white);
		//		s = ""+Game.g.currentPlayer.getGameTeam().food;
		//		g.drawString(s, 4.8f*Game.g.resX/7-Game.g.font.getWidth(s), this.imageGold.getHeight()/2f-Game.g.font.getHeight(s)/2f);
		//		s = model.Utils.gameTime(Game.g.startTime);
		//		g.drawString(s, Game.g.resX/2-Game.g.font.getWidth(s)/2, this.imageTimer.getHeight()/2f-Game.g.font.getHeight(s)/2f);
		return g;
	}

	public Graphics draw2(Graphics g){
		// Draw Background :
		g.setColor(Color.white);
		g.fillRect(0, -3f,sizeX, sizeY+6f);
		g.setColor(Color.black);
		g.fillRect(0, 0, sizeX, sizeY);
		//g.fillRect(3*sizeX/5, sizeY, 2*sizeX/5, -3f);


		g.setColor(Color.white);
		g.drawString(Utils.gameTime(Game.g.startTime), this.sizeX/3, (this.sizeY-28)/2);
		// Draw subcomponents :

		String food = ":";
		if(Game.g.currentPlayer.getGameTeam().food<10)
			food+="0";
		if(Game.g.currentPlayer.getGameTeam().food<100)
			food+="0";
		if(Game.g.currentPlayer.getGameTeam().food<1000)
			food+="0";
		String gold = ":";
		if(Game.g.currentPlayer.getGameTeam().gold<10)
			gold+="0";
		if(Game.g.currentPlayer.getGameTeam().gold<100)
			gold+="0";
		if(Game.g.currentPlayer.getGameTeam().gold<1000)
			gold+="0";
		String special = ":";
		if(Game.g.currentPlayer.getGameTeam().special<10)
			special+="0";
		if(Game.g.currentPlayer.getGameTeam().special<100)
			special+="0";
		if(Game.g.currentPlayer.getGameTeam().special<1000)
			special+="0";
		String pop = ":";
		if(Game.g.currentPlayer.getGameTeam().pop<10)
			pop+="0";
		float sizefood = Game.g.font.getWidth(food);
		float sizegold = Game.g.font.getWidth(gold);
		float sizespecial = Game.g.font.getWidth(special);
		float sizepop = Game.g.font.getWidth(pop);


		// Draw Ressources
		g.drawImage(this.imageFood, 11.2f*this.sizeX/16,(this.sizeY-24)/2);
		g.drawString(":", 11.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+Game.g.currentPlayer.getGameTeam().food, 11.5f*this.sizeX/16+sizefood,(this.sizeY-28)/2);
		g.drawImage(this.imageGold, 12.2f*this.sizeX/16,(this.sizeY-24)/2);
		g.drawString(":", 12.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+Game.g.currentPlayer.getGameTeam().gold, 12.5f*this.sizeX/16+sizegold,(this.sizeY-28)/2);
		g.drawImage(this.imageSpecial, 13.2f*this.sizeX/16,(this.sizeY-32)/2);
		g.drawString(":", 13.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+Game.g.currentPlayer.getGameTeam().special, 13.5f*this.sizeX/16+sizespecial,(this.sizeY-28)/2);
		g.drawImage(this.imagePop, 14.2f*this.sizeX/16,(this.sizeY-32)/2);
		g.drawString(":", 14.5f*this.sizeX/16,(this.sizeY-28)/2);
		g.drawString(""+Game.g.currentPlayer.getGameTeam().pop+"/"+Game.g.currentPlayer.getGameTeam().maxPop, 14.5f*this.sizeX/16+sizepop,(this.sizeY-28)/2);
		// Draw separation 

		return g;
	}


}
