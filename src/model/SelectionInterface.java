package model;

import java.awt.Graphics;

public class SelectionInterface extends Bar {

	BottomBar parent;
	
	public SelectionInterface(BottomBar parent){
		this.parent = parent;
	}
	
	public Graphics draw(Graphics g){
		// Draw the selection of current player
		return g;
	}
}
