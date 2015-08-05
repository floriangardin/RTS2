package model;

import org.newdawn.slick.Graphics;

public abstract class Bar {
		
		float sizeX;
		float sizeY;
		float x;
		float y;
		Player player;
		Plateau p;
		
	public Graphics draw(Graphics g){
		return g;
	}
}
