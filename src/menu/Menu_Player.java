package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import model.Game;
import model.Player;

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
	boolean isReady = false;

	Game game;

	public Menu_Player(Player p, float x, float y, Game game){
		this.p = p;
		this.x = x;
		this.y = y;
		this.game = game;
		this.sizeX = 600f;
		this.sizeY = 50f;
		this.startXcolor = x + 300f;
		this.startYcolor = y;
		this.sizeXcolor = 90f;
		this.sizeYcolor = 40f;
		this.startXciv = x+450f;
		this.startYciv = y;
		this.sizeXciv = 200f;
		this.sizeYciv = 60f;
		this.startXready = x+650f;
		this.startYready = y;
	}

	public void update(Input i){
		if(game.plateau.currentPlayer.id==p.id){
			float xMouse = i.getAbsoluteMouseX();
			float yMouse = i.getAbsoluteMouseY();
			//Testing the click
			if(xMouse>startXcolor && yMouse>startYcolor && xMouse<startXcolor+sizeXcolor && yMouse<startYcolor+sizeYcolor){
				isOverColor = true;
				
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
			int newTeam = p.team + 1;
			if(newTeam>this.game.plateau.teams.size())
				newTeam = 1;
			p.team = newTeam;
			p.gameteam = this.game.plateau.teams.get(newTeam);
			System.out.println("changing team: "+newTeam+" "+p.team+" "+p.gameteam.id);
		} 
		if(xMouse>startXciv && yMouse>startYciv && xMouse<startXciv+sizeXciv && yMouse<startYciv+sizeYciv){


		} 
	}

	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.drawString("Player "+p.id+" : "+this.p.nickname, x, y);
		if(isOverColor)
			g.setColor(Color.gray);
		else	
			g.setColor(Color.black);
		g.fillRect(startXcolor-2f, startYcolor-2f, sizeXcolor+4f,sizeYcolor+4f);
		switch(p.team){
		case 1 : g.setColor(Color.blue);break;
		case 2 : g.setColor(Color.red);break;
		default : g.setColor(Color.black);
		}
		g.fillRect(startXcolor+2f, startYcolor+2f, sizeXcolor-4f,sizeYcolor-4f);
		if(isOverCiv)
			g.setColor(Color.gray);
		else
			g.setColor(Color.black);
		g.drawString(p.gameteam.civName, startXciv, startYciv);
		g.setColor(Color.black);
		if(game.inMultiplayer)
			g.drawString(this.isReady ? "Ready":"Not Ready" ,startXready , startYready);
	}
}