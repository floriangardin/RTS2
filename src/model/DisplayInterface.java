package model;

import org.newdawn.slick.Graphics;

public class DisplayInterface extends Bar{
	BottomBar parent;
	float x, y, sizeX, sizeY;
	Character c0;
	
	public DisplayInterface(BottomBar parent){
		this.parent = parent;
		this.x = parent.x+2f*parent.sizeX/3f;
		this.y = parent.y;
		this.c0 = null;
		this.sizeX = parent.sizeX/3f;
		this.sizeY = parent.sizeY;
	}
	
	public Graphics draw(Graphics g){

		return g;
	}
	
}
