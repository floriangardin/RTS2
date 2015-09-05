package model;

import java.util.Vector;

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
		float startX = this.x+10f;
		@SuppressWarnings("unused")
		float startY = this.y+20f;
		g.setColor(Color.red);
		// Draw 4 separations
		g.setColor(Color.white);
		for(int i=0;i<5;i++){
			g.fillRect(this.x+this.sizeX*i/5f, this.y, 1f, this.sizeY);
			if(this.parent.player.groupSelection==i){
				g.setColor(Color.pink);
				g.fillRect(this.x+this.sizeX*i/5f, this.y,this.sizeX/5f, this.sizeY);
				g.setColor(Color.white);
			}
		}
		int compteur = 0;

		// Draw building state
		if(this.player.selection.get(0) instanceof ProductionBuilding){
			ProductionBuilding p = (ProductionBuilding) this.player.selection.get(0)  ;
			
		}
			

		
		return g;
	}
}
