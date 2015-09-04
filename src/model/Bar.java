package model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Bar {
		
		float sizeX;
		float sizeY;
		float x;
		float y;
		Player player;
		Plateau p;
		Image background;
		
	public Graphics draw(Graphics g){
		return g;
	}
}
