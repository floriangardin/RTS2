package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import model.Game;
import multiplaying.InputObject;

public class Menu_MapChoice extends Menu_Item {

	String name;
	public boolean isSelected;

	public Menu_MapChoice(Game game, String name, float x, float y, float sizeX, float sizeY){
		this.game = game;
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.name = name;
	}

	public void draw(Graphics g){
		g.setColor(Color.white);
		if(isSelected)
			g.drawRect(x-5f, y-5f, sizeX, sizeY);
		if(mouseOver)
			g.setColor(Color.gray);
		g.drawString(name, x, y);
	}
	
	public boolean isMouseOver(InputObject im){
		float xMouse = im.xMouse;
		float yMouse = im.yMouse;
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}

}
