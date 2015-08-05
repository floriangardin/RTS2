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
		int compteur = 0;
		for(Vector<Character> group : this.parent.player.groups){
			// Draw each character with 1 of fifth the screen
			if(group.size()>0){
				Character c = group.get(0);
				g.drawImage(c.image.getSubImage(0, 0, c.image.getWidth()/3,c.image.getHeight()/4),startX+this.sizeX*compteur/5f,this.y);
				
			}
			compteur++;
			if(compteur>=5){
				break;
			}
			
			

		}
		return g;
	}
}
