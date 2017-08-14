package menuutils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import model.Game;

public class Menu_Map extends Menu_Item {

	public String name;
	public boolean isSelected;

	public Menu_Map(String name, float x, float y, float sizeX, float sizeY){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.name = name;
	}

	public void draw(Graphics g){
		g.setColor(Color.white);
		if(isSelected)
			g.drawRect(x-5f, y-5f, sizeX+10f, sizeY+10f);
		if(mouseOver)
			g.setColor(Color.gray);
		g.drawString(name, x, y);
	}
	
	public boolean isMouseOver(InputObject im){
		float xMouse = im.x;
		float yMouse = im.y;
		boolean b = (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
		if(b && im.isPressed(KeyEnum.LeftClick)){
			this.isSelected = true;
		}
		return b;
	}

}
