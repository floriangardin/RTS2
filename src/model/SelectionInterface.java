package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


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


		// Draw building state
		if(this.parent.player.selection.size()>0 && this.parent.player.selection.get(0) instanceof ProductionBuilding ){

			ProductionBuilding b = (ProductionBuilding) this.parent.player.selection.get(0);
			int compteur = 0;
			if(b.isProducing){
				for(int q : b.queue){

					Image icone = this.parent.p.images.getIconByName(b.productionList.get(q).name);
					if(compteur ==0){
						//Show icons
						
						//Show production bar
						g.drawImage(icone,this.x+this.sizeX/4, this.y+this.sizeY/2 - 50f,this.x+this.sizeX/4+24f, this.y+this.sizeY/2 - 50f+24,0,0,512,512);
						g.setColor(Color.red);
						g.fillRect(this.x+this.sizeX/3, this.y+this.sizeY/2 - 50f, 100f,20f);
						g.setColor(Color.green);
						g.fillRect(this.x+this.sizeX/3, this.y+this.sizeY/2 - 50f,b.charge*100f/b.productionTime.get(q),20f);
					}
					else{
						g.drawImage(icone,this.x+this.sizeX/4+25f*(compteur-1), this.y+2*this.sizeY/3 - 50f,this.x+this.sizeX/4+25f*(compteur-1)+24f, this.y+2*this.sizeY/3 - 50f+24f,0f,0f,512f,512f);
					}
					compteur ++;
		
				}
			}



		}



		return g;
	}
}
