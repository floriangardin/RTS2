package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Menu_MapChoice extends Menu_Item {
	
	String name;
	public boolean isSelected;

	public Menu_MapChoice(String name, float x, float y, float sizeX, float sizeY){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.name = name;
	}
	
	public void draw(Graphics g){
		if(isSelected)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.white);
		g.drawString(name, x, y);
	}
	
}
