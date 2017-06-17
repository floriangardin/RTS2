package menuutils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import model.Colors;
import model.Game;
import model.Player;
import ressources.GraphicElements;
import ressources.Images;

public class Menu_Player extends Menu_Item{

	public int id;
	public int team;
	public boolean isReady;

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

	// constants

	float startY = Game.resY*0.37f;
	float stepY = 0.12f*Game.resY;
	float startXPlayers = 1f*Game.resX/6f;
	float startYPlayers = startY;
	float sizeXPlayers = Game.resX*(2f/3f)-2*startXPlayers;
	float sizeYPlayers = Game.resY*0.80f-startY;


	public Menu_Player(int id, int team, String name){
		this.updatePosition(id);
		this.team = team;
		this.name = name;
	}

	public void updatePosition(int id){
		this.id = id;
		this.x = startXPlayers+ 1f/10f*sizeXPlayers;
		this.y = startYPlayers+1f*(id+1)/6f*sizeYPlayers-GraphicElements.font_main.getHeight("Pg")/2f;
		this.sizeX = 600f;
		this.sizeY = 50f;
		String s1 = "Player  "+id+" :  "+"Gilles de Bouard   ";
		this.startXcolor = x + GraphicElements.font_main.getWidth(s1);
		this.startYcolor = y;
		this.sizeXcolor = 90f*Game.resX/1920f;
		this.sizeYcolor = 40f*Game.resY/1080f;
		this.startXciv = startXcolor+sizeXcolor+GraphicElements.font_main.getWidth("      ");
		this.sizeXciv = GraphicElements.font_main.getWidth("Zinaids    ");
		this.startXready = startXciv+GraphicElements.font_main.getWidth("Zinaids                        ");
		this.startYciv = y;
		this.startYready = y;
	}

	public void update(InputObject im){
		float xMouse = im.x;
		float yMouse = im.y;
		//Testing the click
		if(xMouse>startXcolor && yMouse>startYcolor && xMouse<startXcolor+sizeXcolor && yMouse<startYcolor+sizeYcolor){
			isOverColor = true;
			if(im.isPressed(KeyEnum.LeftClick)){
				this.team = 3-this.team;
			}			
		} else {
			isOverColor = false;
		}
		if(xMouse>startXciv && yMouse>startYcolor && xMouse<startXciv+sizeXciv && yMouse<startYcolor+sizeYcolor){
			isOverCiv = true;
			if(im.isPressed(KeyEnum.LeftClick)){
				//TODO:civ
				//					switch(p.getGameTeam().civ.name){
				//					case "dualists" : p.getGameTeam().civ = new Civilisation("zinaids", p.getGameTeam()); break;
				//					case "zinaids" : p.getGameTeam().civ = new Civilisation("kitanos", p.getGameTeam()); break;
				//					case "kitanos" : p.getGameTeam().civ = new Civilisation("dualists", p.getGameTeam()); break;
				//					}
			}
		} else {
			isOverCiv = false;
		}
	}


	public void draw(Graphics g) {
		g.setColor(Color.white);
		String s = "Joueur "+id+" : "+this.name+"   ";
		g.drawString(s, x, y);
		if(isOverColor)
			g.setColor(Color.gray);
		else	
			g.setColor(Color.white);
		g.fillRect(startXcolor-2f, startYcolor-2f, sizeXcolor+4f,sizeYcolor+4f);
		switch(team){
			case 1 : g.setColor(Colors.team1);break;
			case 2 : g.setColor(Colors.team2);break;
			default : g.setColor(Colors.team0);
		}
		g.fillRect(startXcolor+2f, startYcolor+2f, sizeXcolor-4f,sizeYcolor-4f);
		if(isOverCiv)
			g.setColor(Color.gray);
		else
			g.setColor(Color.white);
		g.drawString("", startXciv, startYciv);
		g.setColor(Color.white);
		if(Game.menuSystem.menuMapChoice.lobby.multiplayer)
			g.drawString(this.isReady ? "Prêt":"" ,startXready , startYready);
//		g.drawImage(Images.get("spell"+p.getGameTeam().civ.uniqueSpell.name).getScaledCopy((int)sizeYcolor, (int)sizeYcolor), startXready - sizeYcolor-18 , startYcolor);


	}

}