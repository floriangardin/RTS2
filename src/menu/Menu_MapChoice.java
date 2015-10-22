package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Menu_MapChoice extends Menu_Item {

	String name;
	public boolean isSelected;
	public boolean isOver;

	public Menu_MapChoice(String name, float x, float y, float sizeX, float sizeY){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.name = name;
	}

	public void draw(Graphics g){
		g.setColor(Color.black);
		if(isSelected)
			g.drawRect(x-5f, y-5f, sizeX, sizeY+10f);
		if(isOver)
			g.setColor(Color.gray);
		g.drawString(name, x, y);
	}

	public void update(Input i){
		if(this.isClicked(i)){
			if(!isOver){
				isOver = true;
			}
		} else {
			if(isOver)
				isOver = false;
		}
	}
	
	

}
