package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class SelectionInterface extends Bar {

	BottomBar parent;
	
	public SelectionInterface(BottomBar parent){
		this.parent = parent;
		this.sizeX = parent.sizeX/3f;
		this.sizeY = parent.sizeY;
		this.x = parent.x+parent.sizeX/3f;
		this.y = parent.y;
	}
	
	public Graphics draw(Graphics g){
		
		// Draw the selection of current player
		int compteurX = 0;
		int compteurY = 0;
		float startX = this.x;
		float startY = this.y;
		g.setColor(Color.red);
		for(Character c : this.parent.player.selection){
			g.drawString("C",startX+10f*compteurX,startY+10f*compteurY);
			compteurX++;
			if(compteurX>2){
				compteurY++;
			}
		}
		return g;
	}
}
