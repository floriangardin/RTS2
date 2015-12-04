package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import model.Game;
import model.Player;
import multiplaying.InputObject;

public class Menu_Player extends Menu_Item{

	Player p;

	// about the team
	float startXcolor;
	float startYcolor;
	float sizeXcolor;
	float sizeYcolor;

	// about the civ
	float startXciv;
	float startYciv;
	float sizeXciv;
	float sizeYciv;

	// about the civ
	float startXready;
	float startYready;

	boolean isOverColor;
	boolean isOverCiv;

	public int messageDropped = 0;
	public boolean hasBeenUpdated = false;

	Game game;

	public Menu_Player(Player p, float x, float y, Game game){
		this.p = p;
		this.game = game;
		this.updatePosition(x, y);
	}

	public void updatePosition(float x, float y){
		this.x = x;
		this.y = y;
		this.sizeX = 600f;
		this.sizeY = 50f;
		String s1 = "Player "+p.id+" : "+"WWWWWWWW"+" ";
		this.startXcolor = x + this.game.font.getWidth(s1);
		this.startYcolor = y;
		this.sizeXcolor = 90f*this.game.resX/1920f;
		this.sizeYcolor = 40f*this.game.resY/1080f;
		this.startXciv = startXcolor+sizeXcolor+this.game.font.getWidth(" ");
		this.startXready = startXciv+this.game.font.getWidth("Zinaids ");
		this.startYciv = y;
		this.startXready = x+650f;
		this.startYready = y;
	}

	public void update(InputObject im){
		if(game.plateau.currentPlayer.id==p.id){
			float xMouse = im.xMouse;
			float yMouse = im.yMouse;
			//Testing the click
			if(xMouse>startXcolor && yMouse>startYcolor && xMouse<startXcolor+sizeXcolor && yMouse<startYcolor+sizeYcolor){
				isOverColor = true;
				if(im.pressedLeftClick){
					this.p.setTeam(this.p.getTeam()%(this.game.plateau.nTeams)+1);
				}			
			} else {
				isOverColor = false;
			}
			if(xMouse>startXciv && yMouse>startYciv && xMouse<startXciv+sizeXciv && yMouse<startYciv+sizeYciv){
				isOverCiv = true;
			} else {
				isOverCiv = false;
			}
		}
	}

	public void callItem(Input i){
		float xMouse = i.getAbsoluteMouseX();
		float yMouse = i.getAbsoluteMouseY();
		if(xMouse>startXcolor && yMouse>startYcolor && xMouse<startXcolor+sizeXcolor && yMouse<startYcolor+sizeYcolor){
			int newTeam = p.getTeam() + 1;
			if(newTeam>=this.game.plateau.teams.size())
				newTeam = 1;
			p.setTeam(newTeam);
		} 
		if(xMouse>startXciv && yMouse>startYciv && xMouse<startXciv+sizeXciv && yMouse<startYciv+sizeYciv){


		} 
	}

	public void draw(Graphics g) {
		g.setColor(Color.white);
		String s = "Player "+p.id+" : "+this.p.nickname+"   ";
		g.drawString(s, x, y);
		if(isOverColor)
			g.setColor(Color.gray);
		else	
			g.setColor(Color.white);
		g.fillRect(startXcolor-2f, startYcolor-2f, sizeXcolor+4f,sizeYcolor+4f);
		switch(p.getGameTeam().id){
		case 1 : g.setColor(Color.blue);break;
		case 2 : g.setColor(Color.red);break;
		default : g.setColor(Color.black);
		}
		g.fillRect(startXcolor+2f, startYcolor+2f, sizeXcolor-4f,sizeYcolor-4f);
		if(isOverCiv)
			g.setColor(Color.gray);
		else
			g.setColor(Color.white);
		g.drawString(p.getGameTeam().civName, startXciv, startYciv);
		g.setColor(Color.white);
		if(game.inMultiplayer)
			g.drawString(this.p.isReady ? "Ready":"Not Ready" ,startXready , startYready);
	}

}