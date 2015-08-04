package model;

import org.newdawn.slick.Graphics;

public class DescriptionInterface extends Bar {

	BottomBar parent;
	float x, y;
	
	public DescriptionInterface(BottomBar parent){
		this.parent = parent;
		this.x = parent.x;
		this.y = parent.y;
	}
	
	public Graphics draw(Graphics g){
		
		return g;
	}
}
