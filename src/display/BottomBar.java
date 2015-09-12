package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import buildings.*;
import model.NaturalObjet;
import model.Plateau;
import model.Player;
import spells.Spell;
import technologies.Technologie;
import units.Character;
import units.UnitsList;

public class BottomBar extends Bar {

	public SelectionInterface selection ;
	public DescriptionInterface description;
	public DisplayInterface display;
	Image imageGold ;
	Image imageFood;
	Image imageSpecial;

	// Minimap caract
	public float startX;
	public float startY;
	public float w;
	public float h;
	public float rw;
	public float rh;

	// Production Bar
	public Building buildingToShow;
	public float prodX;
	public float prodY;
	public float prodW;
	public float prodH;
	public int prodIconNb = 4;
	public float icoSizeX;
	public float icoSizeY;

	public BottomBar(Plateau p ,Player player, int resX, int resY){
		this.p = p ;
		this.player = player;
		this.player.bottomBar = this;

		try {
			this.background = new Image("pics/bottombar.jpg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.buildingToShow = null;
		this.update(resX, resY);
		try {
			int taille = 24;
			this.imageGold = new Image("pics/ressources.png").getSubImage(7*taille ,15*taille ,taille, taille);
			this.imageFood = new Image("pics/ressources.png").getSubImage(7*taille, taille, taille, taille);
			this.imageSpecial = new Image("pics/arrow.png");
			this.background = new Image("pics/bottombar.jpg").getSubImage(0,0,683,(int) sizeY);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void update(int resX, int resY){
		this.sizeX = resX;
		this.sizeY = this.p.g.relativeHeightBottomBar*resY;
		this.x = 0;
		this.y = (1f-this.p.g.relativeHeightBottomBar)*resY;
		w = this.sizeX/6f;
		h = this.sizeY-2f;
		rw = w/this.p.maxX;
		rh = h/this.p.maxY;
		this.selection = new SelectionInterface(this);
		this.description = new DescriptionInterface(this);
		this.display = new DisplayInterface(this);

		this.prodX = this.selection.x+this.selection.sizeX;
		this.prodY = this.y+1f;
		this.prodW = this.sizeX/4f;
		this.prodH = this.sizeY-2f ; 
		this.icoSizeX = this.prodW/5f;
		this.icoSizeY = this.prodH/3f;

		this.startX = this.prodX+this.prodW;
		this.startY = this.y+1f;
	}


	public Graphics draw(Graphics g){
		// Draw Background :

		float xt =x ;
		float yt = y;
		g.setColor(Color.black);
		g.fillRect(xt, yt,sizeX, sizeY);
		// Draw image according to size
		float u = x;
		float v = y;
		while(u<sizeX){
			g.drawImage(this.background,u,y);
			u+=680f;
		}

		g.setColor(Color.white);
		g.fillRect(xt,yt,sizeX,1f);
		// Draw subcomponents :
		selection.draw(g);
		description.draw(g);
		display.draw(g);
		// Draw Separation (1/3 1/3 1/3) : 
		g.setColor(Color.white);



		// Draw the minimap 

		// Find the high left corner
		float hlx = Math.max(startX,startX+rw*this.p.Xcam);
		float hly = Math.max(startY,startY+rh*this.p.Ycam);
		float brx = Math.min(startX+w,startX+rw*(this.p.Xcam+this.p.g.resX));
		float bry = Math.min(startY+h,startY+rh*(this.p.Ycam+this.p.g.resY));
		// Find the bottom right corner

		// Draw background 
		g.setColor(new Color(0.1f,0.4f,0.1f));
		g.fillRect(startX, startY, w, h);
		// Draw water
		for(NaturalObjet q : p.naturalObjets){
			g.setColor(Color.cyan);
			g.fillRect(startX+rw*q.x-rw*q.sizeX/2f, startY+rh*q.y-rh*q.sizeY/2f,rw*q.sizeX , rh*q.sizeY);
		}
		// Draw units on camera 

		for(Character c : this.p.characters){		
			if(c.team==2){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.red);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startX+rw*c.x-rw*r, startY+rh*c.y-rh*r, 2f*rw*r, 2f*rh*r);
				}
			}
			else if(c.team==1){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.blue);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startX+rw*c.x-rw*r, startY+rh*c.y-rh*r, 2f*rw*r, 2f*rh*r);
				}
			}
		}
		for(Building c : this.p.buildings){
			if(c.team==0){
				g.setColor(Color.gray);
			}
			if(c.team==2){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.red);
				} else {
					g.setColor(Color.gray);
					
				}
			}
			else if(c.team==1){
				if(this.p.isVisibleByPlayerMinimap(this.player.team, c)){
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.gray);
					
				}
			}
			g.fillRect(startX+rw*c.x-rw*c.sizeX/2f, startY+rh*c.y-rh*c.sizeY/2f, rw*c.sizeX, rh*c.sizeY);
		}

		// Draw rect of camera 
		g.setColor(Color.white);

		g.drawRect(hlx,hly,brx-hlx,bry-hly );


		// Draw Production/Effect Bar
		if(this.player.selection.size()>0 && this.player.selection.get(0) instanceof BuildingProduction){
			BuildingProduction b =(BuildingProduction) this.player.selection.get(0);
			//Print building capacities
			Vector<UnitsList> ul = b.productionList;
			Font f = g.getFont();
			float ratio =1f/prodIconNb;
			for(int i=0; i<Math.min(4, ul.size());i++){ 
				g.drawImage(this.p.images.getIconByName(ul.get(i).name), prodX+2f, prodY+2f + ratio*i*prodH, prodX-2f+ratio*prodH, prodY-2f+ratio*(i+1)*prodH, 0, 0, 512,512);
				g.setColor(Color.white);
				g.drawRect(prodX, prodY + ratio*i*sizeY, prodW, ratio*prodH);
				g.setColor(Color.black);
				g.drawString(ul.get(i).name, prodX + ratio*prodH+10f, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawImage(this.imageFood,prodX + 3.5f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString(": "+(int)ul.get(i).foodPrice,prodX + 3.85f*this.prodW/7, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawImage(this.imageGold,prodX + 4.7f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString(": "+(int)ul.get(i).goldPrice,prodX + 5.05f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString("T: ",prodX + 5.9f*this.prodW/7, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString(Integer.toString(((int)ul.get(i).time)),prodX + 6.25f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
			}
		}
		if(this.player.selection.size()>0 && this.player.selection.get(0) instanceof BuildingTech){
			BuildingTech b =(BuildingTech) this.player.selection.get(0);
			//Print building capacities
			Vector<Technologie> ul = b.productionList;

			Font f = g.getFont();
			float ratio =1f/prodIconNb;
			for(int i=0; i<Math.min(4, ul.size());i++){ 
				g.drawImage(ul.get(i).icon, prodX+2f, prodY+2f + ratio*i*prodH, prodX-2f+ratio*prodH, prodY-2f+ratio*(i+1)*prodH, 0, 0, 512,512);
				g.setColor(Color.white);
				g.drawRect(prodX, prodY + ratio*i*sizeY, prodW, ratio*prodH);
				// CHANGE PUT PRICES

				g.setColor(Color.black);
				g.drawString(ul.get(i).name, prodX + ratio*prodH+10f, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawImage(this.imageFood,prodX + 3.5f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString(": "+(int)ul.get(i).tech.foodPrice,prodX + 3.85f*this.prodW/7, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawImage(this.imageGold,prodX + 4.7f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString(": "+(int)ul.get(i).tech.goldPrice,prodX + 5.05f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString("T: ",prodX + 5.9f*this.prodW/7, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
				g.drawString(Integer.toString(((int)ul.get(i).tech.prodTime)),prodX + 6.25f*this.prodW/7 , prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
			}
		}
		if(this.player.selection.size()>0 && this.player.selection.get(0) instanceof Character){
			Character b =(Character) this.player.selection.get(0);
			//Print building capacities
			Vector<Spell> ul = b.spells;
			Vector<Float> state = b.spellsState;
			Font f = g.getFont();
			float ratio =1f/prodIconNb;
			for(int i=0; i<Math.min(4, ul.size());i++){ 
				g.drawImage(ul.get(i).icon, prodX+2f, prodY+2f + ratio*i*prodH, prodX-2f+ratio*prodH, prodY-2f+ratio*(i+1)*prodH, 0, 0, 512,512);
				Color c = new Color(0,0,0,180);
				g.setColor(c);
				float diffY = (-4f + ratio*prodH)*state.get(i)/ul.get(i).chargeTime;
				g.fillRect(prodX+2f, prodY+2f + ratio*i*prodH+diffY, -4f+ratio*prodH, (-2f + ratio*prodH)-diffY);
				g.setColor(Color.white);
				g.drawRect(prodX, prodY + ratio*i*sizeY, prodW, ratio*prodH);
				if(this.p.isCastingSpell.get(player.team) && this.p.castingSpell.get(player.team)==i){
					g.setColor(Color.green);
					g.drawRect(prodX+1f, prodY +1f+ ratio*i*sizeY, prodW-2f, ratio*prodH-2f);
				}
				g.setColor(Color.black);
				g.drawString(ul.get(i).name, prodX + ratio*prodH+10f, prodY + ratio*i*prodH + ratio/2f*prodH - f.getHeight(ul.get(i).name)/2f);
			}
		}
		return g;
	}


}
