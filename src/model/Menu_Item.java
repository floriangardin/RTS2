package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Menu_Item {
	
	public float sizeX;
	public float sizeY;
	public float x;
	public float y;
	
	public String name;

	public Menu_Item(float sizeX, float sizeY, float x, float y, String name) {
		super();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public boolean isClicked(Input i){
		float xMouse = i.getAbsoluteMouseX();
		float yMouse = i.getAbsoluteMouseY();
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}
	
	public void draw(Graphics g){
		g.setColor(Color.black);
		g.fillRect(x, y, sizeX, sizeY);
		g.setColor(Color.white);
		g.drawString(name, x+sizeX/3f, y+sizeY/3f);
	}
	

}
