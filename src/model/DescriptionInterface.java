package model;

import org.newdawn.slick.Graphics;

public class DescriptionInterface extends Bar {

	BottomBar parent;
	
	public DescriptionInterface(BottomBar parent){
		this.parent = parent;
	}
	
	public Graphics draw(Graphics g){
		// print 
		return g;
	}
}
