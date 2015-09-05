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
		

		// Draw building state
		if(this.player.selection.get(0) instanceof ProductionBuilding ){
			
			ProductionBuilding b = (ProductionBuilding) this.player.selection.get(0)  ;
			if(b.isProducing){
				g.setColor(Color.red);
				g.drawRect(startX+20f, startY+20f, 100f,20f);
				g.setColor(Color.green);
				g.drawRect(startX+20f, startY+20f,b.charge*100f/b.productionTime.get(b.queue.get(0)),20f);
					
			}
			
		}
			

		
		return g;
	}
}
