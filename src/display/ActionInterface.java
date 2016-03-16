package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import buildings.Building;
import buildings.BuildingProduction;
import buildings.BuildingTech;
import javafx.scene.Parent;
import model.Game;
import model.Utils;
import spells.Spell;
import technologies.Technologie;
import units.Character;
import units.UnitsList;

public class ActionInterface extends Bar {

	// Minimap caract
	public float startX;
	public float startY;
	public float sizeX;
	public float sizeY;
	public Game game;
	public Image imageGold ;
	public Image imageFood;
	public Image imageSpecial;
	public int prodIconNb = 5;
	public boolean[] toDrawDescription = new boolean[prodIconNb];
	public float icoSizeX;
	public float icoSizeY;
	// Production Bar
	public Building buildingToShow;


	public ActionInterface(BottomBar parent){
		this.game = parent.p.g;
		float ratio =1f/prodIconNb;
		this.p = parent.p;
		this.player = parent.player;
		this.sizeX = this.game.resX*parent.ratioBarVertX;
		this.sizeY = 5*sizeX;
		this.icoSizeX = ratio*this.sizeY+2f;
		this.icoSizeY = ratio*this.sizeY+2f;
		this.x = 0;
		this.y = this.game.resY - this.sizeY - parent.ratioSelectionX*parent.p.g.resX;

		toDrawDescription[0] = false;
		toDrawDescription[1] = false;
		toDrawDescription[2] = false;
		toDrawDescription[3] = false;
		toDrawDescription[4] = false;

		this.buildingToShow = null;
		try {
			int taille = 24;
			this.imageGold = new Image("pics/ressources.png").getSubImage(7*taille ,15*taille ,taille, taille);
			this.imageFood = new Image("pics/ressources.png").getSubImage(7*taille, taille, taille, taille);
			this.imageSpecial = new Image("pics/arrow.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Graphics draw(Graphics g){
		// Draw the potential actions
		// Draw Separation (1/3 1/3 1/3) : 

		float ratio =1f/prodIconNb;
		Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, x-4, y-5, sizeX+4, sizeY+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(this.x+2f, this.y+2f + i*this.sizeX, -7f+this.sizeX, -7f+this.sizeX);
		}
		g.setColor(Color.white);


		// Draw Production/Effect Bar
		if(this.p.g.currentPlayer.selection.size()>0 && this.p.g.currentPlayer.selection.get(0) instanceof BuildingProduction){
			BuildingProduction b =(BuildingProduction) this.p.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<UnitsList> ul = b.productionList;
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(this.p.g.images.getIconByName(ul.get(i).name), this.x+2f, this.y+2f + i*this.sizeX, this.x-5f+this.sizeX, this.y+2f+(i+1)*(sizeX-5), 0, 0, 512,512);
				g.setColor(Color.white);
				g.setColor(this.p.g.currentPlayer.getGameTeam().color);
				g.drawRect(this.x+1f, this.y+1f + i*this.sizeX, -6f+this.sizeX, -6f+this.sizeX);
				if(ul.size()>i && this.toDrawDescription[i]){
					g.drawString(ul.get(i).name, this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageFood,this.x + 3.6f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).foodPrice,this.x + 3.95f*this.sizeX/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageGold,this.x + 4.8f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).goldPrice,this.x + 5.15f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString("T: ",this.x + 6f*this.sizeX/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(Integer.toString(((int)ul.get(i).time)),this.x + 6.35f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
				}
			}
		}
		if(this.p.g.currentPlayer.selection.size()>0 && this.p.g.currentPlayer.selection.get(0) instanceof BuildingTech){
			BuildingTech b =(BuildingTech) this.p.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<Technologie> ul = b.productionList;
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(ul.get(i).icon, this.x+2f, this.y+2f + ratio*i*this.sizeY, this.x-5f+this.sizeX, this.y-5f+ratio*i*sizeY+this.sizeX, 0, 0, 512,512);
				// CHANGE PUT PRICES
				g.setColor(this.p.g.currentPlayer.getGameTeam().color);
				g.drawRect(this.x+1f, this.y+1f + i*this.sizeX, -6f+this.sizeX, -6f+this.sizeX);
				if(ul.size()>i && this.toDrawDescription[i]){
					g.setColor(Color.white);
					g.drawString(ul.get(i).name, this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageFood,this.x + 3.6f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).tech.foodPrice,this.x + 3.95f*this.sizeX/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageGold,this.x + 4.8f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).tech.goldPrice,this.x + 5.15f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString("T: ",this.x + 6f*this.sizeX/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(Integer.toString(((int)ul.get(i).tech.prodTime)),this.x + 6.35f*this.sizeX/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
				}
			}
		}
		if(this.p.g.currentPlayer.selection.size()>0 && this.p.g.currentPlayer.selection.get(0) instanceof Character){
			Character b =(Character) this.p.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<Spell> ul = b.spells;
			int limit = Math.min(5, ul.size());
			Vector<Float> state = b.spellsState;
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				if(state.get(i)==ul.get(i).chargeTime){
					g.setColor(Color.white);
				} else {
					g.setColor(this.p.g.currentPlayer.getGameTeam().color);
				}
				g.drawRect(this.x+1f, this.y+1f + i*this.sizeX, -6f+this.sizeX, -6f+this.sizeX);
				g.drawImage(ul.get(i).icon, this.x+2f, this.y+2f + ratio*i*this.sizeY, this.x-5f+this.sizeX, this.y-5f+ratio*i*sizeY+this.sizeX, 0, 0, 512,512);
				Color c = this.p.g.currentPlayer.getGameTeam().color;
				c.a = 0.8f;
				g.setColor(c);
				if(state.get(i)>10){
					float diffY = (int)((-5f+this.sizeX)*(state.get(i))/ul.get(i).chargeTime);
					g.fillRect(this.x+2f, this.y+2f + ratio*i*this.sizeY+diffY, this.x-5f+this.sizeX, -5f+this.sizeX-diffY);
				}
				g.setColor(Color.white);

				if(ul.size()>i && this.toDrawDescription[i]){
					g.setColor(Color.white);
					if(ul.get(i).chargeTime>0)
						if(state.get(i)>=ul.get(i).chargeTime)
							g.drawString(ul.get(i).name, this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
						else
							g.drawString(ul.get(i).name+" - "+(int)(100*state.get(i)/ul.get(i).chargeTime)+"%", this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					else
						g.drawString(ul.get(i).name, this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
				}

			}
		}

		return g;
	}
}
