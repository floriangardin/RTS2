package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import buildings.BuildingProduction;
import buildings.BuildingTech;
import model.Game;
import model.Utils;


public class SelectionInterface extends Bar {

	float startX, startY;
	
	public BottomBar parent;
	public Game game;
	public SelectionInterface(BottomBar parent){
		this.game = parent.p.g;
		this.parent = parent;
		this.startX = 0;
		this.startY = parent.p.g.resY-parent.p.g.resX*parent.ratioSelectionX;
		this.sizeX = parent.p.g.resX*parent.ratioSelectionX;
		this.sizeY = sizeX;
		this.x = 0;
		this.y = 0;
	}

	public Graphics draw(Graphics g){

		// Draw the selection of current player
		g.setColor(Color.red);
		// Draw 4 separations
		g.setColor(Color.white);
		
		// variable de travail sizeVerticalBar
		float sVB = parent.ratioBarVertX*parent.p.g.resX;
		Utils.drawNiceRect(g, startX+sizeX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			// TODO: faire des carrés gris
		}
		Utils.drawNiceRect(g, startX-4, startY, sizeX+4, sizeY+4);

		// Draw building state
		if(this.parent.p.g.currentPlayer.selection.size()>0 && this.parent.p.g.currentPlayer.selection.get(0) instanceof BuildingProduction ){

			BuildingProduction b = (BuildingProduction) this.parent.p.g.currentPlayer.selection.get(0);
			int compteur = 0;
			if(b.queue.size()>0){
				for(int q : b.queue){

					Image icone = this.parent.p.g.images.getIconByName(b.productionList.get(q).name);
					if(compteur ==0){
						//Show icons

						//Show production bar
						g.drawImage(icone,startX+this.sizeX/4, startY+this.sizeY/4,startX+sizeX-5, startY + sizeY-5,0,0,512,512);
						g.setColor(Color.white);
						String s = b.productionList.get(q).name;
						g.drawString(s, startX+sizeX/2-parent.p.g.font.getWidth(s)/2f, startY+sizeY/8f-parent.p.g.font.getHeight(s)/2f);
						g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
						g.setColor(Color.gray);
						g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
						g.setColor(parent.p.g.currentPlayer.getGameTeam().color);
						g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4+10f+b.charge*3*sizeY/(4*b.productionList.get(q).time), sizeX/8f,3*sizeY/4-20f-b.charge*3*sizeY/(4*b.productionList.get(q).time));
					}
					else{
						g.drawImage(icone,this.x+this.sizeX+2+(sVB)*(compteur-1), parent.p.g.resY-sVB+3f, this.x+this.sizeX+(sVB)*(compteur), parent.p.g.resY-1,0f,0f,512f,512f);
					}
					compteur ++;

				}
			}




		}
		if(this.parent.p.g.currentPlayer.selection.size()>0 && this.parent.p.g.currentPlayer.selection.get(0) instanceof BuildingTech ){

			BuildingTech b = (BuildingTech) this.parent.p.g.currentPlayer.selection.get(0);
			if(b.queue!=null){

				Image icone = b.queue.icon;

				//Show icons

				//Show production bar
				g.drawImage(icone,this.x+this.sizeX/4, this.y+this.sizeY/2 - 50f,this.x+this.sizeX/4+24f, this.y+this.sizeY/2 - 50f+24,0,0,512,512);
				g.setColor(Color.red);
				g.fillRect(this.x+this.sizeX/3, this.y+this.sizeY/2 - 50f, 100f,20f);
				g.setColor(Color.green);
				g.fillRect(this.x+this.sizeX/3, this.y+this.sizeY/2 - 50f,b.charge*100f/b.queue.tech.prodTime,20f);



			}




		}




		return g;
	}
}
