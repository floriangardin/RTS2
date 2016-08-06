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
import spells.Spell;
import technologies.Technologie;
import units.Character;
import utils.UnitsList;
import utils.Utils;

public class ActionInterface extends Bar {

	// Minimap caract
	public float startY;
	public float startY2;
	public float sizeX;
	public float sizeY;
	public Image imageGold ;
	public Image imageFood;
	public Image imageSpecial;
	public int prodIconNb = 5;
	public boolean[] toDrawDescription = new boolean[prodIconNb];
	public boolean mouseOnIt;
	public float icoSizeX;
	public float icoSizeY;
	private float debut = Game.nbRoundInit/4, duree = debut;
	private float offset;
	// Production Bar
	public Building buildingToShow;


	public ActionInterface(BottomBar parent){
		float ratio =1f/prodIconNb;
		this.p = parent.p;
		this.player = parent.player;
		this.sizeX = Game.g.resX*parent.ratioBarVertX;
		this.offset = Game.g.resX*parent.ratioSelectionX;
		this.sizeY = 5*sizeX;
		this.icoSizeX = ratio*this.sizeY+2f;
		this.icoSizeY = ratio*this.sizeY+2f;
		this.x = 0;
		this.startY = Game.g.resY - this.sizeY - parent.ratioSelectionX*Game.g.resX;
		this.startY2 = Game.g.resY - parent.ratioSelectionX*Game.g.resX;
		this.y = startY2;

		toDrawDescription[0] = false;
		toDrawDescription[1] = false;
		toDrawDescription[2] = false;
		toDrawDescription[3] = false;
		toDrawDescription[4] = false;

		this.buildingToShow = null;
		this.imageGold = Game.g.images.get("imagegolddisplayressources");
		this.imageFood = Game.g.images.get("imagefooddisplayressources");
		this.imageSpecial = Game.g.images.get("imageSpecial");

	}
	public Graphics draw(Graphics g){
		// Draw the potential actions
		// Draw Separation (1/3 1/3 1/3) : 

		if(Game.g.round<Game.nbRoundInit)
			this.x = Math.max(-offset-10, Math.min(0, offset*(Game.g.round-debut-duree)/duree));
		else
			this.x = 0;
		g.setLineWidth(1f);
		if(mouseOnIt && y>startY)
			y = startY+(y-startY)/5;
		if(!mouseOnIt && y<startY2)
			y = startY2+(y-startY2)/5;
		
		float ratio =1f/prodIconNb;
		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, x-4, y-5, sizeX+4, sizeY+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(this.x+2f, this.y+2f + i*this.sizeX, -7f+this.sizeX, -7f+this.sizeX);
		}
		g.setColor(Color.white);


		// Draw Production/Effect Bar
		if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof BuildingProduction){
			mouseOnIt = true;
			BuildingProduction b =(BuildingProduction) Game.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<UnitsList> ul = b.productionList;
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(Game.g.images.get("icon"+ul.get(i).name), this.x+2f, this.y+2f + i*this.sizeX, this.x-5f+this.sizeX, this.y+2f+(i+1)*(sizeX-5), 0, 0, 512,512);
				g.setColor(Color.white);
				g.setColor(Game.g.currentPlayer.getGameTeam().color);
				g.drawRect(this.x+1f, this.y+1f + i*this.sizeX, -6f+this.sizeX, -6f+this.sizeX);
				if(ul.size()>i && this.toDrawDescription[i]){
					g.setColor(Color.white);
					g.drawString(ul.get(i).name, this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageFood,this.x + 3.6f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).foodPrice,this.x + 3.95f*(this.sizeX+400f)/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageGold,this.x + 4.8f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).goldPrice,this.x + 5.15f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString("T: ",this.x + 6f*(this.sizeX+400f)/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(Integer.toString(((int)ul.get(i).time)),this.x + 6.35f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
				}
			}
		}else if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof BuildingTech){
			mouseOnIt = true;
			BuildingTech b =(BuildingTech) Game.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<Technologie> ul = b.productionList;
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(ul.get(i).icon, this.x+2f, this.y+2f + ratio*i*this.sizeY, this.x-5f+this.sizeX, this.y-5f+ratio*i*sizeY+this.sizeX, 0, 0, 512,512);
				// CHANGE PUT PRICES
				g.setColor(Game.g.currentPlayer.getGameTeam().color);
				g.drawRect(this.x+1f, this.y+1f + i*this.sizeX, -6f+this.sizeX, -6f+this.sizeX);
				if(ul.size()>i && this.toDrawDescription[i]){
					g.setColor(Color.white);
					g.drawString(ul.get(i).name, this.x + ratio*this.sizeY+10f, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageFood,this.x + 3.6f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).tech.foodPrice,this.x + 3.95f*(this.sizeX+400f)/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawImage(this.imageGold,this.x + 4.8f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(": "+(int)ul.get(i).tech.goldPrice,this.x + 5.15f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString("T: ",this.x + 6f*(this.sizeX+400f)/7, this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
					g.drawString(Integer.toString(((int)ul.get(i).tech.prodTime)),this.x + 6.35f*(this.sizeX+400f)/7 , this.y + ratio*i*this.sizeY + ratio/2f*this.sizeY - f.getHeight(ul.get(i).name)/2f);
				}
			}
		} else if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof Character){
			mouseOnIt = true;
			Character b =(Character) Game.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<Spell> ul = b.spells;
			int limit = Math.min(5, ul.size());
			Vector<Float> state = b.spellsState;
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				if(state.get(i)==ul.get(i).chargeTime){
					g.setColor(Color.white);
				} else {
					g.setColor(Game.g.currentPlayer.getGameTeam().color);
				}
				g.drawRect(this.x+1f, this.y+1f + i*this.sizeX, -6f+this.sizeX, -6f+this.sizeX);
				g.drawImage(ul.get(i).icon, this.x+2f, this.y+2f + ratio*i*this.sizeY, this.x-5f+this.sizeX, this.y-5f+ratio*i*sizeY+this.sizeX, 0, 0, 512,512);
				Color c = Game.g.currentPlayer.getGameTeam().color;
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
		} else {
			mouseOnIt = false;
		}

		return g;
	}
}
